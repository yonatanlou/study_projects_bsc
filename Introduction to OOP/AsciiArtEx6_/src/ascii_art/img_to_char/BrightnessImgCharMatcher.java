package ascii_art.img_to_char;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import image.Image;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class is maintaining all the calculations for converting the image for chars.
 */
public class BrightnessImgCharMatcher {
    private float[] brightLevels;
    private char[] charArray;
    private final HashMap<Image, Double> cache = new HashMap<>();
    private Image imgToConv;
    private String font;

    public BrightnessImgCharMatcher(Image image, String fontName) {
        font = fontName;
        imgToConv = image;
    }

    // Count the number of white pixels in a given character
    private int countWhite(char c) {
        int sum = 0;
        for (boolean[] row : CharRenderer.getImg(c, 16, font))
            for (boolean value : row)
                if (value)
                    sum++;
        return sum;
    }

    /**
     * Set max values for min and max, iterate through all  characters and count the number of
     * white pixels that exist in the image of each character using countWhite(). Then, using the predefined
     * min and max values, normalize.
     *
     * @param charSet
     * @return charBrigtness
     */
    private float[] brightnessLevels(Character[] charSet) {
        int[] whiteN = new int[charSet.length];
        int minBrightness = 256;
        int maxBrightness = 0;
        for (int i = 0; i < charSet.length; i++) {
            int whiteScale = countWhite(charSet[i]);
            if (whiteScale > maxBrightness)
                maxBrightness = whiteScale;

            if (whiteScale < minBrightness)
                minBrightness = whiteScale;

            whiteN[i] = whiteScale;
        }
        float[] charBrightn = new float[charSet.length];
        for (int i = 0; i < charSet.length; i++) {
            if (maxBrightness == minBrightness)
                charBrightn[i] = 0.5f;
            else
                charBrightn[i] = (float) (whiteN[i] - minBrightness) / (maxBrightness - minBrightness);
        }
        return charBrightn;
    }

    // for each char, setting the char array and the brightness level for each one of the characters.
    private void charsSetup(Character[] charSet) {
        charArray = new char[charSet.length];
        for (int i = 0; i < charSet.length; i++)
            charArray[i] = charSet[i].charValue();
        brightLevels = brightnessLevels(charSet);
    }

    // Find the character in the picture whose brightness is the closest to a particular level.
    private char matchBrightness(float brightness) {
        float distance = 1;
        char c = ' ';
        for (int i = 0; i < charArray.length; i++) {
            float d = Math.abs(brightness - brightLevels[i]);
            if (d < distance) {
                distance = d;
                c = charArray[i];
            }
        }
        return c;
    }

    /**
     * Calculates an image's average brightness by converting each pixel to greyscale and then averaging over all
     * pixels in the image.
     * In contrast to ex4_1, this method now uses a cache to save the images that have
     * previously been computed as keys and values. After one instantiation of the class, the method will use
     * the saved data from earlier runs instead of calculating from scratch the next time it is used.
     *
     * @param image
     * @return double
     */
    private double calcImgBrightness(Image image) {
        if (cache.containsKey(image)) {
            return cache.get(image);
        }
        int counter = 0;
        double sum = 0;

        for (Color pixel : image.pixels()) {
            double greyPixel = (pixel.getRed() * 0.2126 + pixel.getGreen() * 0.7152 + pixel.getBlue() * 0.0722) / 255;
            sum += greyPixel;
            counter++;
        }

        sum /= counter;
        if (counter > 0) {
            cache.put(image, sum);
            return sum;
        } else
            return 0.5;
    }

    /**
     * The variable pixels represent the size of the individual subimages, which are averaged to produce the brightness levels for the
     * characters in the output.
     * The variable lineLength is the actual length of the output line, which may differ due to the arithmetic of the integer numCharsInRow
     * Using the specified API of squareSubImagesOfSize(), you subdivide the image to convert it into smaller subimages. Then
     * calcAverageBrightness to calculate how bright a character should be, and use matchBrightness to find a matching character
     * character in the given set.
     *
     * @param numCharsInRow
     * @return char[][] which represent the image
     */
    private char[][] convertImageToAscii(int numCharsInRow) {
        int pix = imgToConv.getWidth() / numCharsInRow;
        int i = 0;
        int j = 0;
        int lineLength = imgToConv.getWidth() / pix;
        char[][] imageRepresentation = new char[imgToConv.getHeight() / pix][imgToConv.getWidth() / pix];
        for (Image subImage : imgToConv.squareSubImagesOfSize(pix)) {
            double b = calcImgBrightness(subImage);
            imageRepresentation[i][j] = matchBrightness((float) b);
            if (++j == lineLength) {
                ++i;
                j = 0;
            }
        }
        return imageRepresentation;
    }

    /**
     * The method is called externally and is given a specific character set to use in the
     * statement. It also receives a numCharsInRow to specify how many characters to display in each row.
     * The method calls charsSetup to set the two variables charArray
     * and brightLevels (the brightness for each character in the array).
     *
     * @param numCharsInRow
     * @param charSet
     * @return char[][] which represent the image
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        charsSetup(charSet);
        var chars = convertImageToAscii(numCharsInRow);
        return chars;
    }
}
