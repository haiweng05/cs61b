public class LinkedListDequeTest {
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

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

        boolean passed = checkEmpty(true, lld1.isEmpty());

        lld1.addFirst("front");

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

        boolean pass = true;

        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        for (int i = 0; i < 1000; ++i) {
            lld.addLast(i);
        }

        lld.removeFirst();

        pass = (lld.get(0).equals(1)) && pass;
        pass = (lld.size() == 999) && pass;

        if (pass) {
            System.out.println("addRemoveTest1 passed!");
        }

        for (int i = 0; i < 1000; ++i) {
            lld.addFirst(i);
        }

        lld.removeLast();

        pass = (lld.get(1997).equals(998)) && pass;
        pass = (lld.get(0).equals(999)) && pass;

        if (pass) {
            System.out.println("addRemoveTest2 passed!");
        }

        LinkedListDeque<Integer> lst = new LinkedListDeque<>();

        lst.addLast(0);
        lst.removeFirst();
        pass = (lst.isEmpty()) && pass;
        lst.addLast(3);
        pass = (lst.removeFirst() == 3) && pass;

        if (pass) {
            System.out.println("addRemoveTest3 passed!");
        }
    }

    public static void getTest() {
        System.out.println("Running get test.");

        LinkedListDeque<String> lst = new LinkedListDeque<>();
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

        pass = (lst.getRecursive(2).equals("2")) && pass;
        pass = (lst.getRecursive(1).equals("1")) && pass;
        if (pass) {
            System.out.println("GetRecursive method test passed!");
        }
    }
    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();
        addRemoveTest();
        getTest();
    }
}
