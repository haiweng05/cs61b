import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BinaryTrie implements Serializable {
    private TrieNode root;
    public class TrieNode implements Serializable {
        private char val;
        private TrieNode[] next;
        private boolean hasVal;
        public TrieNode(char c) {
            val = c;
            next = new TrieNode[2];
        }
        public TrieNode() {
            next = new TrieNode[2];
        }
    }

    public BinaryTrie() {
        root = new TrieNode();
    }

    private class State implements Comparable<State> {
        private int cnt;
        private char val;
        private TrieNode node;
        public State(int c, char v) {
            cnt = c;
            val = v;
            node = new TrieNode();
            node.val = v;
            node.hasVal = true;
        }

        public State(int c) {
            cnt = c;
            node = new TrieNode();
            node.hasVal = false;
        }

        public int getCount() {
            return cnt;
        }

        public char getVal() {
            return val;
        }

        public int compareTo(State s) {
            if (this.cnt < s.cnt) {
                return -1;
            } else if (this.cnt == s.cnt) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        PriorityQueue<State> pq = new PriorityQueue<>();
        for (char key : frequencyTable.keySet()) {
            State s = new State(frequencyTable.get(key), key);
            pq.add(s);
        }
        while (!pq.isEmpty()) {
            if (pq.size() == 1) {
                root = pq.remove().node;
                break;
            } else {
                State left = pq.remove();
                State right = pq.remove();
                int cnt = left.cnt + right.cnt;
                State newState = new State(cnt);
                newState.node = new TrieNode();
                newState.node.next[0] = left.node;
                newState.node.next[1] = right.node;
                pq.add(newState);
            }
        }
    }
    public Match longestPrefixMatch(BitSequence querySequence) {
        TrieNode current = root;
        for (int i = 0; i < querySequence.length(); ++i) {
            int bit = querySequence.bitAt(i);
            current = current.next[bit];
            if (current.hasVal) {
                return new Match(querySequence.firstNBits(i + 1), current.val);
            }
        }
        return null;
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> map = new HashMap<>();
        dfs(root, new BitSequence(), map);
        return map;
    }

    public void dfs(TrieNode n, BitSequence bitSequence, Map<Character, BitSequence> map) {
        for (int i = 0; i < 2; ++i) {
            if (n != null && n.hasVal) {
                map.put(n.val, bitSequence);
                return;
            }
            if (n != null && n.next[i] != null) {
                TrieNode next = n.next[i];
                BitSequence newSequence = bitSequence.appended(i);
                dfs(next, newSequence, map);
            }
        }
    }
}
