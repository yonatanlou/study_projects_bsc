package algo_questions;

import java.util.Arrays;

public class Solutions {
    /**
     *
     * Method computing the maximal amount of tasks out of n tasks that can be completed with m time slots.
     * A task can only be completed in a time slot if the length of the time slot is
     * grater than the no. of hours needed to complete the task.

     *
     * @param tasks
     * @param timeSlots
     * @return
     */
    public static int alotStudyTime(int[] tasks, int[] timeSlots) {
        int i, j;
        Arrays.sort(tasks);
        Arrays.sort(timeSlots);
        j = 0;
        for (i = 0; i < tasks.length; i++) {

            while (j < timeSlots.length && timeSlots[j] < tasks[i])
                j++;

            if (j == timeSlots.length)
                break;
            ++j;
        }

        return i;
    }

    /**
     * Method computing the nim amount of leaps a frog needs to jumb across n waterlily leaves,
     * from leaf 1 to leaf n. The leaves vary in size and how stable they are,
     * so some leaves allow larger leaps than others. leapNum[i] is an integer telling you how many leaves
     * ahead you can jump from leaf i. If leapNum[3]=4,
     * the frog can jump from leaf 3, and land on any of the leaves 4, 5, 6 or 7.
     * @param leapNum
     * @return
     */
    public static int minLeap(int[] leapNum) {
        int step;

        int maxPrev = 0, maxCurr = leapNum[0], maxNext;
        for (step = 1; maxCurr < leapNum.length; step++) {
            maxNext = maxCurr;

            for (int i = maxPrev + 1; i <= maxCurr; i++) {
                if (i + leapNum[i] > maxNext)
                    maxNext = i + leapNum[i];
            }

            maxPrev = maxCurr;
            maxCurr = maxNext;
        }

        if (maxPrev >= leapNum.length - 1)
            return step - 1;

        return step;
    }

    /**
     * Method computing the solution to the following problem: A boy is filling the water trough
     * for his father's cows in their village. The trough holds n liters of water. With every trip to the
     * village well, he can return using either the 2 bucket yoke, or simply with a single bucket.
     * A bucket holds 1 liter. In how many different ways can he fill the water trough? n can be assumed to
     * be greater or equal to 0, less than or equal to 48.
     * @param n
     * @return
     */
    public static int bucketWalk(int n) {
        int fibo = 1, prevfibo = 0, nextfibo;
        for (int i = 0; i < n; i++) {
            nextfibo = fibo + prevfibo;
            prevfibo = fibo;

            fibo = nextfibo;
        }

        return fibo;
    }



    /**
     * Method computing the solution to the following problem: Given an integer n, return the number of
     * structurally unique BST's (binary search trees) which has exactly n nodes of unique values from 1 to n.
     * You can assume n is at least 1 and at most 19. (Definition: two trees S and T are structurally distinct if
     * one can not be obtained from the other by renaming of the nodes.) (credit: LeetCode)
     * @param n
     * @return
     */
    public static int numTrees(int n) {
        int[] treesNum = new int[n + 1];

        treesNum[0] = 1;

        for (int i = 1; i <= n; i++) {
            int sum = 0;

            for (int j = 0; j < i; j++)
                sum += treesNum[j] * treesNum[i - j - 1];

            treesNum[i] = sum;
        }
        return treesNum[n];
    }

}
