import java.util.Comparator;

public class MyComparator implements Comparator<String> {
    public int compare(String a, String b) {
        int len1 = a.length();
        int len2 = b.length();
        if (len1 > len2) {
            return 1;
        } else if (len1 < len2) {
            return -1;
        } else {
            for (int i = 0; i < len1; ++i) {
                if (a.charAt(i) < b.charAt(i)) {
                    return 1;
                }
                if (a.charAt(i) > b.charAt(i)) {
                    return -1;
                }
            }
            return 0;
        }
    }
}
