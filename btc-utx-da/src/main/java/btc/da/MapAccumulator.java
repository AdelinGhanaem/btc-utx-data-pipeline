package btc.da;

import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapAccumulator implements AggregateFunction<BTCAmountTaggedUTX, Map<String, Integer>, Map<String, Integer>> {


    @Override
    public Map<String, Integer> createAccumulator() {
        return new ConcurrentHashMap<>();
    }

    @Override
    public Map<String, Integer> add(BTCAmountTaggedUTX value, Map<String, Integer> accumulator) {
        accumulator.computeIfPresent(String.valueOf(value.getTag()), (k, v) -> ++v);
        accumulator.putIfAbsent(String.valueOf(value.getTag()), 1);
        return accumulator;
    }

    @Override
    public Map<String, Integer> getResult(Map<String, Integer> accumulator) {
        return accumulator;
    }

    @Override
    public Map<String, Integer> merge(Map<String, Integer> a, Map<String, Integer> b) {
        if (a.size() > b.size()) {
            b.forEach((k, v) -> a.merge(k, v, Integer::sum));
            return a;
        } else {
            a.forEach((k, v) -> b.merge(k, v, Integer::sum));
            return b;
        }
    }
}
