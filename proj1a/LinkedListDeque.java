public class LinkedListDeque<T> {
    public static class Node<T>{
        private T _val;
        private Node<T> prev;
        private Node<T> next;

        Node(T val) {
            _val = val;
        }

        Node() {}
    }

    private Node<T> sentinal;
    private int size;
    private Node<T> left;
    private Node<T> right;

    LinkedListDeque() {
        sentinal = new Node<T>();
        size = 0;
        sentinal.next = sentinal;
        sentinal.prev = sentinal;
        left = sentinal;
        right = sentinal;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addLast(T item) {
        Node<T> cur = sentinal;
        while (cur.next != left) {
            cur = cur.next;
        }
        cur.next = new Node<T>(item);
        cur.next.prev = cur;
        cur.next.next = left;
        left.prev = cur.next;
        right = right.next;
        size++;
    }

    public void addFirst(T item) {
        Node<T> cur = sentinal;
        while (cur.prev != right) {
            cur = cur.prev;
        }
        cur.prev = new Node<T>(item);
        cur.prev.next = cur;
        cur.prev.prev = right;
        right.next = cur.prev;
        left = left.prev;
        size++;
    }

    public void printDeque() {
        Node<T> iter = left;
        while (iter.next != left) {
            if (iter._val != null) {
                System.out.print(iter._val);
                System.out.print(' ');
            }
            iter = iter.next;
        }
        if (iter._val != null) {
            System.out.print(iter._val);
            System.out.print(' ');
        }
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T val = right._val;
        Node<T> before = right.prev;
        before.next = left;
        left.prev = before;
        size--;
        return val;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T val = left._val;
        Node<T> after = left.next;
        after.prev = right;
        right.next = after;
        size--;
        return val;
    }

    public T get(int index) {
        Node<T> iter = left;
        for (int i = 0; i < index; ++i) {
            if(iter._val == null) {
                i--;
            }
            iter = iter.next;
        }
        if (iter._val == null) {
            iter = iter.next;
        }
        return iter._val;
    }

    public T getRecursive(int index) {
        return getRecursiveHelper(index, left);
    }

    public T getRecursiveHelper(int index, Node<T> cur) {
        if(cur._val == null) {
            return getRecursiveHelper(index, cur.next);
        }
        if(index == 0) {
            return cur._val;
        }
        return getRecursiveHelper(index - 1, cur.next);
    }
}
