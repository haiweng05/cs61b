import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testStudentDeque() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> correct = new ArrayDequeSolution<>();

        int countAdd = 0;
        int countRemove = 0;
        String message = new String();

        for (int i = 0; i < 10000; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            int integer = StdRandom.uniform(-10, 10);

            if (numberBetweenZeroAndOne < 0.25) {
                countAdd++;
                message += "addLast(" + integer + ")\n";

                student.addLast(integer);
                correct.addLast(integer);

                int checkPosition = StdRandom.uniform(0, countAdd - countRemove);
                assertEquals(message, student.get(checkPosition), correct.get(checkPosition));

            } else if (numberBetweenZeroAndOne > 0.25 && numberBetweenZeroAndOne < 0.5) {
                countAdd++;
                message += "addFirst(" + integer + ")\n";

                student.addFirst(integer);
                correct.addFirst(integer);

                int checkPosition = StdRandom.uniform(0, countAdd - countRemove);
                assertEquals(message, student.get(checkPosition), correct.get(checkPosition));

            } else if (numberBetweenZeroAndOne > 0.5 && numberBetweenZeroAndOne < 0.75 && countAdd > countRemove) {
                countRemove++;
                message += "removeLast()\n";

                assertEquals(message, student.removeLast(), correct.removeLast());

            } else if (numberBetweenZeroAndOne > 0.75 && countAdd > countRemove) {
                countRemove++;
                message += "removeFirst()\n";

                assertEquals(message, student.removeFirst(), correct.removeFirst());
            }

        }
    }
}
