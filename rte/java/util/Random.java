package java.util;

import kernel.Timer;

public class Random {
    private long seed;
    private static final long serialVersionUID = 3905348978240129619L;


    public Random() {
        this(Timer.getUptimeReal());
    }


    public Random(long seed) {
        setSeed(seed);
    }


    public void setSeed(long seed) {
        this.seed = (seed ^ 0x5DEECE66DL) & ((1L << 48) - 1);
    }


    protected int next(int bits) {
        seed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        return (int) (seed >>> (48 - bits));
    }


    public void nextBytes(byte[] bytes) {
        int random;
        // Do a little bit unrolling of the above algorithm.
        int max = bytes.length & ~0x3;
        for (int i = 0; i < max; i += 4) {
            random = next(32);
            bytes[i] = (byte) random;
            bytes[i + 1] = (byte) (random >> 8);
            bytes[i + 2] = (byte) (random >> 16);
            bytes[i + 3] = (byte) (random >> 24);
        }
        if (max < bytes.length) {
            random = next(32);
            for (int j = max; j < bytes.length; j++) {
                bytes[j] = (byte) random;
                random >>= 8;
            }
        }
    }


    public int nextInt() {
        return next(32);
    }


    public int nextInt(int n) {
        if (n <= 0)
            //throw new Exception("n must be positive");
        if ((n & -n) == n) // i.e., n is a power of 2
            return (int) ((n * (long) next(31)) >> 31);
        int bits, val;
        do {
            bits = next(31);
            val = bits % n;
        }
        while (bits - val + (n - 1) < 0);
        return val;
    }


    public long nextLong() {
        return ((long) next(32) << 32) + next(32);
    }


    public boolean nextBoolean() {
        return next(1) != 0;
    }


    public float nextFloat() {
        return next(24) / (float) (1 << 24);
    }


    public double nextDouble() {
        return (((long) next(26) << 27) + next(27)) / (double) (1L << 53);
    }
}