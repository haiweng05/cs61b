package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private double acceleration;
    private int state;

    AcceleratingSawToothGenerator(int f, double a) {
        period = f;
        acceleration = a;
        state = 0;
    }

    @Override
    public double next() {
        state = (state + 1);
        double result = (state % period) * 2.0d / period - 1;
        if (state == period) {
            state = 0;
            period = (int) (period * acceleration);
        }
        return result;
    }
}
