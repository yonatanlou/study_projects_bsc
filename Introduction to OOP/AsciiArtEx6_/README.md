## README
A simple program that can take an image and to convert the image to a beautiful ascci art.

<p align="center">
  <img src="https://github.com/yonatanlou/study_projects/blob/main/Introduction%20to%20OOP/AsciiArtEx6_/Screen%20Shot%202022-02-09%20at%2022.32.27.png?raw=true" width="350" title="Screenshot">
</p>

```bash
javac Driver.java
java Driver PATH_TO_IMAGE
```

### Options:

```bash
>>> chars
0 1 2 3 4 5 6 7 8 9 a b c d 
>>> remove all
>>> chars

>>> add a-z
>>> chars
a b c d e f g h i j k l m n o p q r s t u v w x y z 
>>> remove d-z
>>> chars
a b c 
>>> res up
Width set to 128
>>> res down
Width set to 64
>>> console
.......SHIT LOAD OF chars.....
>>> render
will create an html file of the picture with the chars
```

=      File description     =

BrightnessImgCharMatcher.java - maintaining all the calculations for converting the image for chars.
Driver.java - The main function which runs the program (processing the args).
CharRenderer.java - Provided through supplied materials.
Shell - The class is used to create an application for our ascii image converter. Giving various options to handle
the converter.

=          Design           =

To convert a given image to ascii art, the application have to do a variety of calculations and normalizations.
The ascii_art goal is to maintain all the calculations of the convertion from image to ascii.
The Shell is the main interface which from there the user can control the convertion.
The method chooseChars() is called first; I chose to design it using charsSetup to generate the array and then call
convertImageToAscii(), which does calculations and also calling for other methods.
The usage of a hashmap for photo conversion is crucial; rather than converting each time from scratch,
the hashmap saves the data of the pictures converted, and instead of calculating afresh,
it just uses the calculations from the previous time.

