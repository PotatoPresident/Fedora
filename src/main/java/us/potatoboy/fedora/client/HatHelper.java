package us.potatoboy.fedora.client;

public class HatHelper {
    private final double[] offsets = new double[3];
    private final float scale;
    private final int headIndex;

    public HatHelper(double height, double forward, double side, float scale) {
        this(height, forward, side, scale, 0);
    }

    public HatHelper(double height, double forward, double side, float scale, int headIndex) {
        offsets[0] = height;
        offsets[1] = forward;
        offsets[2] = side;
        this.scale = scale;
        this.headIndex = headIndex;
    }

    public double getHeightOffset() {
        return offsets[0];
    }

    public double getForwardOffset() {
        return offsets[1];
    }

    public double getSideOffset() {
        return offsets[2];
    }

    public float getScale() {
        return scale;
    }

    public int getHeadIndex() {
        return headIndex;
    }
}
