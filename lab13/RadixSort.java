/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // Implement LSD Sort
        String[] sorted = new String[asciis.length];
        int maxLen = 0;
        for (int i = 0; i < asciis.length; ++i) {
            sorted[i] = asciis[i];
            maxLen = Math.max(maxLen, asciis[i].length());
        }
        sortHelperLSD(sorted, maxLen - 1);
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        if (index == -1) {
            return;
        }

        int[] start = new int[256];
        int[] diff = new int[256];
        for (int i = 0; i < asciis.length; ++i) {
            int ascii = 0;
            if (asciis[i].length() - 1 >= index) {
                ascii = (int) asciis[i].charAt(index);
            }
            diff[ascii] += 1;
        }
        for (int i = 1; i < 256; ++i) {
            start[i] = start[i - 1] + diff[i - 1];
        }
        String[] sorted = new String[asciis.length];
        for (int i = 0; i < asciis.length; ++i) {
            int ascii = 0;
            if (asciis[i].length() - 1 >= index) {
                ascii = (int) asciis[i].charAt(index);
            }
            sorted[start[ascii]] = asciis[i];
            start[ascii] += 1;
        }
        for (int i = 0; i < asciis.length; ++i) {
            asciis[i] = sorted[i];
        }
        sortHelperLSD(asciis, index - 1);
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
