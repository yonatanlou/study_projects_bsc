import java.util.*;
class Arr2d {
    public static void main(String[] args) {
        float[][] pic = {
                {0, 1, 0, 1},
                {1, 0, 1, 0},
                {0, 1, 0, 1}
        };
        String[][] StringPic = new String[3][4];
        float avg = 0;
        for(int row=0; row < pic.length ; row++) {
            for(int col = 0 ; col < pic[row].length ; col++) {
                avg += pic[row][col];
                StringPic[row][col] = String.valueOf(pic[row][col]);
            }
        }
        avg /= (pic.length*pic[0].length);
        System.out.println(avg);
        System.out.println(Arrays.deepToString(pic));

    }
}
