package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

//TODO: refactoring +rewrite docu

/**
 * The class is used to create an application for our ascii image converter. There are several commands defined
 * in this class, all of which are named currState in their variables. The class allows the user to perform some basic
 * operations on the output image and select whether to save it as an HTML file or output it to the console.

 */


public class Shell {
    Image image;
    private static final String currState_EXIT = "exit";
    private static final String currState_CHARS = "chars";
    private static final String currState_ADD = "add";
    private static final String currState_REMOVE = "remove";
    private static final String currState_RES = "res";
    private static final String currState_RENDER = "render";
    private static final String currState_CONSOLE = "console";
    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";

    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private int charsInRow;
    private Set<Character> charSet = new HashSet<>();

    private BrightnessImgCharMatcher charMatcher;
    private AsciiOutput output;
    private static final String INITIAL_CHARS_RANGE = "0-9";

    /**
     * The constructor receives an image, initialize a charSet, and calculating different sizes
     * needed to create photos.
     * @param img
     */
    public Shell(Image img) {
        image = img;
        Collections.addAll(charSet, 'a','b','c','d');
        minCharsInRow = Math.max(1, img.getWidth()/img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);
        charMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);
        output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
        addChars(INITIAL_CHARS_RANGE);
    }

    /**
     *
     * The main application's method. The options are: show chars, add chars, remove chars, res up,
     * res down, render, and print to console. All of this options can be used by the user.
     * The switch case syntax is used to construct the method by the desire command.
     */

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(">>> ");
        String currState = scanner.next().trim();
        //exit for getting out of the program
        while (!currState.toLowerCase().equals(currState_EXIT)) {
            switch (currState.toLowerCase()) {
                case currState_CHARS:
                    showChars();
                    break;
                case currState_ADD:
                    addChars(scanner.next().trim());
                    break;
                case currState_REMOVE:
                    removeChars(scanner.next().trim());
                    break;
                case currState_RES:
                    resChange(scanner.next().trim());
                    break;
                case currState_RENDER:
                    render();
                    break;
                case currState_CONSOLE:
                    console();
                    break;
                default:
                    System.out.printf("%s is an unknown command, try again \n", currState);
            }
            var param = scanner.nextLine().trim();
            System.out.print(">>> ");
            currState = scanner.next();
        }
    }

    /**
     * The parsing of the string given by the user takes place here, which is used by addChars().
     * If the user enters one character, it will be added to the char set as required. The range between two
     * characters will be added if the user enters two characters. If the user presses the spacebar, space will
     * be added. When the user types 'all,' all characters are added.

     * @param param string
     * @return char[]
     */
    private static char[] parseCharRange(String param) {
        if (param.length() == 1)
            return new char[]{param.charAt(0), param.charAt(0)};

        if (param.equals("all"))
            return new char[]{' ', '~'};

        if (param.equals("space"))
            return new char[]{' ', ' '};

        if (param.length() == 3 && param.charAt(1) == '-') {
            if (param.charAt(0) > param.charAt(2))
                return new char[]{param.charAt(2), param.charAt(0)};

            else
                return new char[]{param.charAt(0), param.charAt(2)};

        }
        return null;
    }

    /**
     * Displaying which characters are in use for the ascii converter.
     */
    private void showChars() {
        charSet.stream().sorted().forEach(c-> System.out.print(c + " "));
        System.out.println();
    }

    private void addChars(String s) {
        char[] range = parseCharRange(s);
        if (range != null)
            Stream.iterate(range[0], c -> c <= range[1], c -> (char)((int)c+1)).forEach(charSet::add);
    }

    private void removeChars(String s) {
        char[] range = parseCharRange(s);
        if (range != null)
            Stream.iterate(range[0], c -> c <= range[1], c -> (char)((int)c+1)).forEach(charSet::remove);
    }

    /**
     * This method is enabling the user to change the resolution of the image by changing the maximal/minimal number of
     * chars in a row, and therefore making the resolution better or worst by factor of 2.
     * After checking minimal/maximal number of chars in row is not exceeded, enlarges/shrinks the resolution by
     * multiplying in 2.
     * @param s string
     */
    private void resChange(String s) {
        if (s.equals("up")) {
            if (charsInRow * 2 <= maxCharsInRow)
                charsInRow = charsInRow * 2;
            else
                System.out.println("You're using the maximal resolution");
        }
        else if (s.equals("down")) {
            if (charsInRow / 2 >= minCharsInRow)
                charsInRow = charsInRow / 2;
            else
                System.out.println("You're using the minimal resolution");
        }
        else
            System.out.println("Wrong input, please try again");
        System.out.printf("Width set to %d\n", charsInRow);
    }
    /**
     * rendering the converted image to a html file.
     */
    private void render() {
        HtmlAsciiOutput asciiOutput = new HtmlAsciiOutput("out1.html", "Courier New");
        char[][] chars = charMatcher.chooseChars(charsInRow, charSet.toArray(new Character[charSet.size()]));
        asciiOutput.output(chars);
    }

    /**
     * rendering the converted image to an output in the console.
     */
    private void console() {
        ConsoleAsciiOutput consoleOutput = new ConsoleAsciiOutput();
        char[][] chars = charMatcher.chooseChars(charsInRow, charSet.toArray(new Character[charSet.size()]));
        consoleOutput.output(chars);
    }
}
