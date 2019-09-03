package btc.da;

import btc.da.model.BTCAmountTaggedUTX;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UTXAccumulator extends ProcessWindowFunction<BTCAmountTaggedUTX, StreamingJob.BTCPerTagInTimeWindow, Integer, TimeWindow> {

    private static Logger logger = LoggerFactory.getLogger(UTXAccumulator.class.getName());
    private boolean debug;

    public UTXAccumulator(boolean debug) {
        this.debug = debug;
    }

    @Override
    public void process(Integer integer,
                        Context context,
                        Iterable<BTCAmountTaggedUTX> elements,
                        Collector<StreamingJob.BTCPerTagInTimeWindow> out) {

        Integer i = 0;
        Long amount = 0L;
        for (BTCAmountTaggedUTX u : elements) {
            if (u.getX() != null && u.getX().getOut() != null && u.getX().getOut().get(0) != null)
                amount = +u.getX().getOut().get(0).getValue();
            i++;
        }
        StreamingJob.BTCPerTagInTimeWindow b = new StreamingJob.BTCPerTagInTimeWindow(context.window().getStart(), context.window().getEnd(), integer, i, amount);
        if (debug)
            logger.info("Pushing to pub/sub {} ", b);
        out.collect(b);
    }
}
