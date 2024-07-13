package lab9;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>, Iterable<K> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
            left = null;
            right = null;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        int result = key.compareTo(p.key);
        if (result < 0) {
            if (p.left != null) {
                return getHelper(key, p.left);
            } else {
                return null;
            }
        } else if (result > 0) {
            if (p.right != null) {
                return getHelper(key, p.right);
            } else {
                return null;
            }
        } else {
            return p.value;
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (root == null) {
            return null;
        }
        return getHelper(key, root);
    }


    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {

        int result = key.compareTo(p.key);
        if (result < 0) {
            if (p.left != null) {
                return putHelper(key, value, p.left);
            } else {
                p.left = new Node(key, value);
                size += 1;
                return p.left;
            }
        } else if (result > 0) {
            if (p.right != null) {
                return putHelper(key, value, p.right);
            } else {
                p.right = new Node(key, value);
                size += 1;
                return p.right;
            }
        } else {
            p.value = value;
            return p;
        }
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new Node(key, value);
            size += 1;
            return;
        }
        putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        if (root == null) {
            return null;
        }
        return keySetHelper(root);
    }

    private Set<K> keySetHelper(Node p) {
        Set<K> set = new TreeSet<K>();
        set.add(p.key);
        if (p.left != null) {
            set.addAll(keySetHelper(p.left));
        }
        if (p.right != null) {
            set.addAll(keySetHelper(p.right));
        }
        return set;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        if (root == null) {
            return null;
        }
        Node dummyRoot = new Node(null, null);
        dummyRoot.left = root;

        Node parent = dummyRoot;
        Node toRemove = root;
        while (toRemove != null && !toRemove.key.equals(key)) {
            parent = toRemove;
            if (key.compareTo(toRemove.key) < 0) {
                toRemove = toRemove.left;
            } else {
                toRemove = toRemove.right;
            }
        }
        if (toRemove == null) {
            return null;
        }

        V val = toRemove.value;
        if (toRemove.left == null && toRemove.right == null) {
            if (toRemove.key.equals(parent.left.key)) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        } else if (toRemove.left != null && toRemove.right == null) {
            if (toRemove.key.equals(parent.left.key)) {
                parent.left = toRemove.left;
            } else {
                parent.right = toRemove.left;
            }
        } else if (toRemove.left == null && toRemove.right != null) {
            if (toRemove.key.equals(parent.left.key)) {
                parent.left = toRemove.right;
            } else {
                parent.right = toRemove.right;
            }
        } else {
            Node leftMax = toRemove.left;
            Node leftMaxParent = toRemove;
            while (leftMax.right != null) {
                leftMaxParent = leftMax;
                leftMax = leftMax.right;
            }

            if (leftMaxParent != toRemove) {
                leftMaxParent.right = leftMax.left;
            }

            leftMax.left = toRemove.left;
            leftMax.right = toRemove.right;

            if (toRemove.key.equals(parent.left.key)) {
                parent.left = leftMax;
            } else {
                parent.right = leftMax;
            }
        }
        size -= 1;
        root = dummyRoot.left;
        return val;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (root == null) {
            return null;
        }
        Node dummyRoot = new Node(null, null);
        dummyRoot.left = root;

        Node parent = dummyRoot;
        Node toRemove = root;
        while (toRemove != null && !toRemove.key.equals(key)) {
            parent = toRemove;
            if (key.compareTo(toRemove.key) < 0) {
                toRemove = toRemove.left;
            } else {
                toRemove = toRemove.right;
            }
        }
        if (toRemove == null) {
            return null;
        }

        V val = toRemove.value;
        if (!val.equals(value)) {
            return null;
        }
        if (toRemove.left == null && toRemove.right == null) {
            if (toRemove.key.equals(parent.left.key)) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        } else if (toRemove.left != null && toRemove.right == null) {
            if (toRemove.key.equals(parent.left.key)) {
                parent.left = toRemove.left;
            } else {
                parent.right = toRemove.left;
            }
        } else if (toRemove.left == null && toRemove.right != null) {
            if (toRemove.key.equals(parent.left.key)) {
                parent.left = toRemove.right;
            } else {
                parent.right = toRemove.right;
            }
        } else {
            Node leftMax = toRemove.left;
            Node leftMaxParent = toRemove;
            while (leftMax.right != null) {
                leftMaxParent = leftMax;
                leftMax = leftMax.right;
            }

            if (leftMaxParent != toRemove) {
                leftMaxParent.right = leftMax.left;
            }

            leftMax.left = toRemove.left;
            leftMax.right = toRemove.right;

            if (toRemove.key.equals(parent.left.key)) {
                parent.left = leftMax;
            } else {
                parent.right = leftMax;
            }
        }
        size -= 1;
        root = dummyRoot.left;
        return val;
    }

    private class keyIterator implements Iterator<K> {
        private Deque<Node> stack;

        public keyIterator(Node root) {
            stack = new ArrayDeque<>();
            pushLeft(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public K next() {
            Node node = stack.pop();
            pushLeft(node.right);
            return node.key;
        }

        private void pushLeft(Node node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }
    }
    @Override
    public Iterator<K> iterator() {
        return new keyIterator(root);
    }
}
