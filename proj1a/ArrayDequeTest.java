public class ArrayDequeTest {
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");

        ArrayDeque<String> lld1 = new ArrayDeque<String>();

        boolean passed = checkEmpty(true, lld1.isEmpty());

        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        passed = checkSize(1, lld1.size()) && passed;
        passed = checkEmpty(false, lld1.isEmpty()) && passed;

        lld1.addLast("middle");
        passed = checkSize(2, lld1.size()) && passed;

        lld1.addLast("back");
        passed = checkSize(3, lld1.size()) && passed;

        System.out.println("Printing out deque: ");
        lld1.printDeque();

        printTestStatus(passed);

    }

    public static void addRemoveTest() {

        System.out.println("Running add/remove test.");

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        // should be empty
        boolean passed = checkEmpty(true, lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        passed = checkEmpty(false, lld1.isEmpty()) && passed;

        lld1.removeFirst();
        // should be empty
        passed = checkEmpty(true, lld1.isEmpty()) && passed;

        printTestStatus(passed);
    }

    public static void getTest() {
        System.out.println("Running get test.");

        ArrayDeque<String> lst = new ArrayDeque<>();
        lst.addFirst("1");
        lst.addFirst("0");
        lst.addLast("2");
        lst.addLast("3");
        lst.printDeque();
        System.out.print("\n");

        boolean pass = true;
        pass = (lst.get(2).equals("2")) && pass;
        pass = (lst.get(1).equals("1")) && pass;
        if (pass) {
            System.out.println("Get method test passed!");
        }
    }

    public static void resizeTest() {
        ArrayDeque<String> lst = new ArrayDeque<>();
        lst.addLast("0");
        lst.addLast("1");
        lst.addLast("2");
        lst.addLast("3");
        lst.addLast("4");
        lst.addFirst("-1");
        lst.addFirst("-2");
        lst.addFirst("-3");
        lst.addFirst("-4");
        lst.addFirst("-5");

        boolean pass = true;
//        pass = (lst.max_size == 16) && pass;
//        if (pass) {
//            System.out.println("Resize bigger passed!");
//        }

        pass = (lst.get(0).equals("-5")) && pass;
        pass = (lst.get(9).equals("4")) && pass;

        if (pass) {
            System.out.println("Value transport test pass!");
        }

        for (int i = 0; i < 8; ++i) {
            lst.addLast("rubbish");
        }

        for (int i = 0; i < 17; ++i) {
            lst.removeLast();
        }
//
//        pass = (lst.max_size == 8) && pass;
//        if (pass) {
//            System.out.println("Resize smaller pass!");
//        }


        ArrayDeque<String> expected = new ArrayDeque<>();

    }

    public static void speedTest() {
        ArrayDeque<String> lst = new ArrayDeque<>();
        for (int i = 0; i < 100000; ++i) {
            lst.addLast("after");
        }

        for (int i = 0; i < 100000; ++i) {
            lst.addFirst("before");
        }

        boolean pass = true;

//        pass = (lst.max_size == 262144) && pass;
//        if (pass) {
//            System.out.println("Speed and correctness in adding test passed");
//        }

        for (int i = 0; i < 99999; ++i) {
            lst.removeLast();
            lst.removeFirst();
        }

//        pass = (lst.max_size == 8) && pass;
//        if (pass) {
//            System.out.println("Speed and correctness in deleting test passed");
//        }
    }
    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();
        addRemoveTest();
        resizeTest();
        getTest();
        speedTest();
    }
}
