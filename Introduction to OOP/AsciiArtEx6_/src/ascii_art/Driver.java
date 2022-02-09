package ascii_art;

import image.Image;

import java.util.logging.Logger;
//TODO:  rewrite docu
/**
 * The Driver class contains the main function. It has a default constructor that does not require any parameters.
 * The purpose of the class is to read an image from the file and then execute the run method,
 * which is contained in the Shell class.
 */
public class Driver {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("USAGE: java asciiArt ");
            return;
        }
        Image img = Image.fromFile(args[0]);
        if (img == null) {
            Logger.getGlobal().severe("Failed to open image file " + args[0]);
            return;
        }
        new Shell(img).run();
    }
}