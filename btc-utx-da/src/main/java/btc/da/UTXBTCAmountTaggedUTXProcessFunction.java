package btc.da;

import btc.da.model.UTX;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import static btc.da.BTCAmmountTags.tag;
import static btc.da.BTCSatoshi.toBtc;

public class UTXBTCAmountTaggedUTXProcessFunction extends ProcessFunction<UTX, BTCAmountTaggedUTX> {
    @Override
    public void processElement(UTX value, Context ctx, Collector<BTCAmountTaggedUTX> out) {
        if (value.getX() != null && value.getX().getOut() != null && value.getX().getOut().size() > 0) {
            int tagInt = tag(toBtc(value.getX().getOut().get(0).getValue()));
            BTCAmountTaggedUTX btcAmountTaggedUTX = new BTCAmountTaggedUTX();
            btcAmountTaggedUTX.setTag(tagInt);
            btcAmountTaggedUTX.setUtx(value);
            out.collect(btcAmountTaggedUTX);
        }
    }
}
