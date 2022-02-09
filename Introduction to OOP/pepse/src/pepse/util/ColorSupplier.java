package pepse.util;

import java.awt.*;
import java.util.Random;

/**
 * Provides procedurally-generated colors around a pivot.
 * @author Dan Nirel
 */
public final class ColorSupplier {
    private static final int DEFAULT_COLOR_DELTA = 10;
    private final static Random random = new Random();

    /**
     * Returns a color similar to baseColor, with a default delta
     */
    public static Color approximateColor(Color baseColor) {
        return approximateColor(baseColor, DEFAULT_COLOR_DELTA);
    }

    /**
     * Returns a color similar to baseColor, with a difference of at most colorDelta
     */
    public static Color approximateColor(Color baseColor, int colorDelta) {

        return new Color(
                randomChannelInRange(baseColor.getRed()-colorDelta, baseColor.getRed()+colorDelta),
                randomChannelInRange(baseColor.getGreen()-colorDelta, baseColor.getGreen()+colorDelta),
                randomChannelInRange(baseColor.getBlue()-colorDelta, baseColor.getBlue()+colorDelta));
    }

    private static int randomChannelInRange(int min, int max) {
        int channel = random.nextInt(max-min+1) + min;
        return Math.min(255, Math.max(channel, 0));
    }
}
