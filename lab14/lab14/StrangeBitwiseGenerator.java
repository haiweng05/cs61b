package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int p) {
        period = p;
        state = 0;
    }

    @Override
    public double next() {
        state += 1;
        int weirdState = state & (state >>> 3) % period;
//        weirdState = state & (state >> 3) & (state >> 8) % period;
//        return (weirdState % period) * 2.0d / period - 1;
        return 1.0d * (weirdState % period) / period - 1;
    }
}
