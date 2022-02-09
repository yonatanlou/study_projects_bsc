package ascii_art;

import java.util.HashMap;
import java.util.HashSet;
public class Algorithms {
    public static int findDuplicate(int[] numList) {
        int sum = 0;
        for (int i = 0; i < numList.length; i++)
            sum += numList[i] - i;

        return sum;
    }

    public static int uniqueMorseRepresentations(String[] words) {
        HashSet<String> morseResult = new HashSet<>();
        final String[] MORSE =
                {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---",
                        ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};
        for (String word : words) {
            String tmpCode = "";
            for (byte c : word.toLowerCase().getBytes())
                tmpCode += MORSE[c - 'a'];
            morseResult.add(tmpCode);
        }
        return morseResult.size();
    }
}
