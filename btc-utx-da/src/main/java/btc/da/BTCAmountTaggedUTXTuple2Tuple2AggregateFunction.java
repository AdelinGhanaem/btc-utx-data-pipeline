package btc.da;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BTCAmountTaggedUTXTuple2Tuple2AggregateFunction implements
        AggregateFunction<BTCAmountTaggedUTX,
                Tuple2<String, Long>,
                Tuple2<String, Long>> {

    @Override
    public Tuple2<String, Long> createAccumulator() {
        return new Tuple2<>("",0L);
    }

    @Override
    public Tuple2<String, Long> add(BTCAmountTaggedUTX value, Tuple2<String, Long> accumulator) {
        System.out.println("tag "+value.getTag()+" f0 "+accumulator.f0 +" f1 "+ accumulator.f1);

        accumulator.f1 = accumulator.f1 + 1L;
        accumulator.f0 = String.valueOf(value.getTag());

        System.out.println("result: "+accumulator.f0 +" "+ accumulator.f1);

        return accumulator;
    }

    @Override
    public Tuple2<String, Long> getResult(Tuple2<String, Long> accumulator) {
        return accumulator;
    }

    @Override
    public Tuple2<String, Long> merge(Tuple2<String, Long> a, Tuple2<String, Long> b) {
        a.f1 = a.f1 + b.f1;
        return a;
    }





}
