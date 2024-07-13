package lab9;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V>, Iterable<K> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    private int hash(K key, int mod) {
        if (key == null) {
            return 0;
        }

        return Math.floorMod(key.hashCode(), mod);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        ArrayMap<K, V> map = buckets[(hash(key))];
        return map.get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        ArrayMap<K, V> map = buckets[(hash(key))];
        int before = map.size();
        map.put(key, value);
        int after = map.size();
        if (after - before != 0) {
            size += 1;
        }

        if (loadFactor() > MAX_LF) {
            int mod = buckets.length * 2;
            ArrayMap<K, V>[] newBuckets = new ArrayMap[mod];
            for (int i = 0; i < mod; i++) {
                newBuckets[i] = new ArrayMap<K, V>();
            }
            for (int i = 0; i < buckets.length; ++i) {
                ArrayMap<K, V> bucket = buckets[i];
                for (K k: bucket) {
                    newBuckets[hash(k, mod)].put(k, bucket.get(k));
                }
            }
            buckets = newBuckets;
        }
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
        Set<K> set = new HashSet<>();
        for (int i = 0; i < DEFAULT_SIZE; ++i) {
            ArrayMap<K, V> map = buckets[i];
            for (K key : map) {
                set.add(key);
            }
        }
        return set;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        ArrayMap<K, V> bucket = buckets[hash(key)];
        V val = bucket.get(key);
        if (val == null) {
            return null;
        }
        size -= 1;
        return val;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        ArrayMap<K, V> bucket = buckets[hash(key)];
        V val = bucket.get(key);
        if (val == null || !val.equals(value)) {
            return null;
        }
        size -= 1;
        return val;
    }

    private class keyIterator implements Iterator<K> {
        private int currentIndex;
        private Iterator<K> hiddenIterator;
        public keyIterator() {
            currentIndex = 0;
            hiddenIterator = buckets[currentIndex].iterator();
        }
        @Override
        public boolean hasNext() {
            if (currentIndex >= buckets.length) {
                return false;
            }
            while (!hiddenIterator.hasNext()) {
                currentIndex += 1;
                if (currentIndex >= buckets.length) {
                    return false;
                }
                hiddenIterator = buckets[currentIndex].iterator();
            }
            return true;
        }

        @Override
        public K next() {
            if (!hasNext()) {
                return null;
            }
            while (!hiddenIterator.hasNext()) {
                currentIndex += 1;
                hiddenIterator = buckets[currentIndex].iterator();
            }
            return hiddenIterator.next();
        }
    }
    @Override
    public Iterator<K> iterator() {
        return new keyIterator();
    }
}
