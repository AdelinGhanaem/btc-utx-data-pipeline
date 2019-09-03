package btc.da;

import btc.da.model.BTCAmountTaggedUTX;
import btc.da.model.UTX;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static btc.da.BTCSatoshi.toBtc;
import static btc.da.model.BTCAmmountTags.tag;

public class UTXBTCAmountTaggedUTXProcessFunction extends ProcessFunction<UTX, BTCAmountTaggedUTX> {

    static final Logger LOGGER = LoggerFactory.getLogger(UTXBTCAmountTaggedUTXProcessFunction.class.getName());
    boolean debug;

    public UTXBTCAmountTaggedUTXProcessFunction(boolean debug) {
        this.debug = debug;
    }

    @Override
    public void processElement(UTX value, Context ctx, Collector<BTCAmountTaggedUTX> out) {
        if (value.getX() != null && value.getX().getOut() != null && value.getX().getOut().size() > 0) {
            int tagInt = tag(toBtc(value.getX().getOut().get(0).getValue()));
            BTCAmountTaggedUTX btcAmountTaggedUTX = new BTCAmountTaggedUTX();
            btcAmountTaggedUTX.setTag(tagInt);
            btcAmountTaggedUTX.setUtx(value);
            if (debug)
                LOGGER.info("Collecting {}", btcAmountTaggedUTX);
            out.collect(btcAmountTaggedUTX);
        }
    }
}
