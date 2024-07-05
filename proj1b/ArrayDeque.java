public class ArrayDeque<T> implements Deque<T> {
    private T[] val;
    private int size;
    private int maxSize;
    private int begin;


    public ArrayDeque() {
        maxSize = 8;
        size = 0;
        begin = 0;
        val = (T[]) new Object[maxSize];
    }

    private int indexBefore(int index) {
        if (index == 0) {
            return maxSize - 1;
        }

        return index - 1;
    }

    private int indexAfter(int index) {
        if (index == maxSize - 1) {
            return 0;
        }
        return index + 1;
    }

    private int indexBack() {
        return (begin + size - 1) % maxSize;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void printDeque() {
        for (int i = begin, cnt = 0; cnt < size; ++cnt, i = indexAfter(i)) {
            System.out.print(val[i]);
            System.out.print(' ');
        }
    }

    private void resize(int newSize) {
        T[] newArray = (T[]) new Object[newSize];
        for (int i = begin, cnt = 0; cnt < size; ++cnt, i = indexAfter(i)) {
            newArray[cnt] = val[i];
            val[i] = null;
        }
        maxSize = newSize;
        val = newArray;
        begin = 0;
    }

    @Override
    public void addLast(T item) {
        if (size == maxSize) {
            resize(maxSize * 2);
        }

        val[indexAfter(indexBack())] = item;
        size++;
    }

    @Override
    public void addFirst(T item) {
        if (size == maxSize) {
            resize(maxSize * 2);
        }

        if (val[begin] == null) {
            val[begin] = item;
        } else {
            begin = indexBefore(begin);
            val[begin] = item;
        }
        size++;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        T v = val[begin];
        val[begin] = null;
        begin = indexAfter(begin);
        size--;

        double useage = 1.0d * size / maxSize;
        if (useage <= 0.25d && maxSize >= 16) {
            resize(maxSize / 2);
        }
        return v;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        T v = val[indexBack()];
        val[indexBack()] = null;
        size--;

        double useage = 1.0d * size / maxSize;
        if (useage <= 0.25d && maxSize >= 16) {
            resize(maxSize / 2);
        }
        return v;
    }

    @Override
    public T get(int index) {
        return val[(begin + index) % maxSize];
    }
}
