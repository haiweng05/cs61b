public class ArrayDeque<T> {
    private T[] val;
    private int size;
    private int max_size;
    private int begin;


    ArrayDeque() {
        max_size = 8;
        size = 0;
        begin = 0;
        val = (T[]) new Object[max_size];
    }

    private int indexBefore(int index) {
        if (index == 0) {
            return max_size - 1;
        }

        return index - 1;
    }

    private int indexAfter(int index) {
        if (index == max_size - 1) {
            return 0;
        }
        return index + 1;
    }

    private int indexBack() {
        return (begin + size - 1) % max_size;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        for (int i = begin, cnt = 0; cnt < size; ++cnt, i = indexAfter(i)) {
            System.out.print(val[i]);
            System.out.print(' ');
        }
    }

    public void resize(int newSize) {
        T[] new_array = (T[]) new Object[newSize];
        for (int i = begin, cnt = 0; cnt < size; ++cnt, i = indexAfter(i)) {
            new_array[cnt] = val[i];
            val[i] = null;
        }
        max_size = newSize;
        val = new_array;
        begin = 0;
    }

    public void addLast(T item) {
        if (size == max_size) {
            resize(max_size * 2);
        }

        val[indexAfter(indexBack())] = item;
        size++;
    }

    public void addFirst(T item) {
        if (size == max_size) {
            resize(max_size * 2);
        }

        if (val[begin] == null) {
            val[begin] = item;
        }
        else {
            begin = indexBefore(begin);
            val[begin] = item;
        }
        size++;
    }

    public T removeFirst() {
        T v = val[begin];
        val[begin] = null;
        begin = indexAfter(begin);
        size--;

        double useage = 1.0d * size / max_size;
        if(useage <= 0.25d && max_size >= 16) {
            resize(max_size / 2);
        }
        return v;
    }

    public T removeLast() {
        T v = val[indexBack()];
        val[indexBack()] = null;
        size--;

        double useage = 1.0d * size / max_size;
        if (useage <= 0.25d && max_size >= 16) {
            resize(max_size / 2);
        }
        return v;
    }

    public T get(int index) {
        return val[(begin + index) % max_size];
    }
}
