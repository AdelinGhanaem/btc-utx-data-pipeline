package btc.da;

import btc.da.model.UTX;

public class BTCAmountTaggedUTX extends UTX {


    private int tagValueAmount;
    private UTX utx;


    public  void setUtx(UTX utx) {
        this.utx = utx;
    }



    public void setTag(int tag){
        this.tagValueAmount = tag;
    }

    public Integer getTag() {

        return tagValueAmount;
    }
}
