package btc.da.model;

public class BTCAmmountTags {

    public static int tag(double btcAmmount) {

        if (btcAmmount > 1)
            return 1;

        if (btcAmmount < 1d && btcAmmount > 0.5d)
            return 2;

        if (btcAmmount < 0.5d && btcAmmount > 0.1d)
            return 3;

        if (btcAmmount < 0.1d && btcAmmount > 0.05d)
            return 4;

        if (btcAmmount < 0.05d && btcAmmount > 0.01d)
            return 5;

        if (btcAmmount < 0.01d && btcAmmount > 0.001d)
            return 6;

        if (btcAmmount < 0001d)
            return 7;

        return 8;
    }
}
