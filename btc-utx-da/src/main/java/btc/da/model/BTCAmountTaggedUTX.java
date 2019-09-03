package btc.da.model;

public class BTCAmountTaggedUTX extends UTX {


    private int tagValueAmount;
    private UTX utx;
    private long time;


    public void setUtx(UTX utx) {
        this.utx = utx;
    }


    public void setTag(int tag) {
        this.tagValueAmount = tag;
    }

    public Integer getTag() {

        return tagValueAmount;
    }


    public void setTime(long l) {
        time = l;
    }

    public long getTime() {
        return time;
    }
}
