package btc.da;

public class BTCSatoshi {


    private static final double BTCInSatoshi = 0.00000001;

    private static final double currentBTCinUSD = 12000;

    public static Double toBtc(Long satoshi) {
        return satoshi * BTCInSatoshi;
    }


    public static Double toSatoshi(Long btc) {
        return btc / BTCInSatoshi;
    }



}
