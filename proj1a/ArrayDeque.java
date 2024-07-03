public class ArrayDeque<ValueType> {
    public ValueType[] val;
    private int size;
    public int max_size;
    private int begin;


    ArrayDeque() {
        max_size = 8;
        size = 0;
        begin = 0;
        val = (ValueType[]) new Object[max_size];
    }

    private int IndexBefore(int index) {
        if (index == 0) {
            return max_size - 1;
        }

        return index - 1;
    }

    private int IndexAfter(int index) {
        if(index == max_size - 1) {
            return 0;
        }
        return index + 1;
    }

    private int IndexBack() {
        return (begin + size - 1) % max_size;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        for (int i = begin, cnt = 0; cnt < size; ++ cnt, i = IndexAfter(i)) {
            System.out.print(val[i]);
            System.out.print(' ');
        }
    }

    public void resize(int new_size) {
        ValueType[] new_array = (ValueType[]) new Object[new_size];
        for (int i = begin, cnt = 0; cnt < size; ++ cnt, i = IndexAfter(i)) {
            new_array[cnt] = val[i];
            val[i] = null;
        }
        max_size = new_size;
        val = new_array;
        begin = 0;
    }

    public void addLast(ValueType item) {
        if (size == max_size) {
            resize(max_size * 2);
        }

        val[IndexAfter(IndexBack())] = item;
        size ++;
    }

    public void addFirst(ValueType item) {
        if (size == max_size) {
            resize(max_size * 2);
        }

        if (val[begin] == null) {
            val[begin] = item;
        }
        else {
            begin = IndexBefore(begin);
            val[begin] = item;
        }
        size ++;
    }

    public ValueType removeFirst() {
        ValueType v = val[begin];
        val[begin] = null;
        begin = IndexAfter(begin);
        size --;

        double useage = 1.0d * size / max_size;
        if(useage <= 0.25d && max_size >= 16) {
            resize(max_size / 2);
        }
        return v;
    }

    public ValueType removeLast() {
        ValueType v = val[IndexBack()];
        val[IndexBack()] = null;
        size --;

        double useage = 1.0d * size / max_size;
        if(useage <= 0.25d && max_size >= 16) {
            resize(max_size / 2);
        }
        return v;
    }

    public ValueType get(int index) {
        return val[(begin + index) % max_size];
    }
}
