package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int period;
    private int state;

    public SawToothGenerator(int p) {
        period = p;
        state = 0;
    }

    @Override
    public double next() {
        state += 1;
        return (state % period) * 2.0d / period - 1;
    }
}
