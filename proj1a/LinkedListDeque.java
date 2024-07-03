import com.sun.jdi.Value;

public class LinkedListDeque <ValueType> {
    public static class Node <ValueType>{
        public ValueType _val;
        public Node<ValueType> prev;
        public Node<ValueType> next;

        Node(ValueType val) {
            _val = val;
        }

        Node() {}
    }

    private Node<ValueType> sentinal;
    private int size;
    private Node<ValueType> left;
    private Node<ValueType> right;

    LinkedListDeque() {
        sentinal = new Node<ValueType>();
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

    public void addLast(ValueType item) {
        Node<ValueType> cur = sentinal;
        while (cur.next != left) {
            cur = cur.next;
        }
        cur.next = new Node<ValueType>(item);
        cur.next.prev = cur;
        cur.next.next = left;
        left.prev = cur.next;
        right = right.next;
        size ++;
    }

    public void addFirst(ValueType item) {
        Node<ValueType> cur = sentinal;
        while (cur.prev != right) {
            cur = cur.prev;
        }
        cur.prev = new Node<ValueType>(item);
        cur.prev.next = cur;
        cur.prev.prev = right;
        right.next = cur.prev;
        left = left.prev;
        size ++;
    }

    public void printDeque() {
        Node<ValueType> iter = left;
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

    public ValueType removeLast() {
        if (size == 0) {
            return null;
        }
        ValueType val = right._val;
        Node<ValueType> before = right.prev;
        before.next = left;
        left.prev = before;
        size --;
        return val;
    }

    public ValueType removeFirst() {
        if (size == 0) {
            return null;
        }
        ValueType val = left._val;
        Node<ValueType> after = left.next;
        after.prev = right;
        right.next = after;
        size --;
        return val;
    }

    public ValueType get(int index) {
        Node<ValueType> iter = left;
        for (int i = 0; i < index; ++ i) {
            if(iter._val == null) {
                i --;
            }
            iter = iter.next;
        }
        if (iter._val == null) {
            iter = iter.next;
        }
        return iter._val;
    }

    public ValueType getRecursive(int index) {
        return getRecursiveHelper(index, left);
    }

    public ValueType getRecursiveHelper(int index, Node<ValueType> cur) {
        if(cur._val == null) {
            return getRecursiveHelper(index, cur.next);
        }
        if(index == 0) {
            return cur._val;
        }
        return getRecursiveHelper(index - 1, cur.next);
    }
}
