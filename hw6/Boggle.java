import org.junit.Test;

import java.sql.Time;
import java.util.*;

public class Boggle {
    private static int M;
    private static int N;
    private static String[] board;
    private static boolean[][] vis;
    private static Trie trie;
    private static PriorityQueue<String> pq;
    private static final int[] dx = {1, -1, 0, 0, 1, 1, -1, -1};
    private static final int[] dy = {0, 0, 1, -1, -1, 1, -1, 1};
    private static int limit;

    public static class Trie {
        public static class TrieNode {
            TrieNode[] next;
            char c;
            String val;
            TrieNode(char a) {
                c = a;
                next = new TrieNode[26];
            }
            public String val() {
                return val;
            }
            public void removeVal() {
                val = null;
            }
        }

        TrieNode[] root;

        Trie() {
            root = new TrieNode[26];
        }

        public void add(String str) {
            String ori = str;
            str = str.toLowerCase();
            TrieNode cur = null;
            for (char chr : str.toCharArray()) {
                if (chr - 'a' >= 26) {
                    continue;
                }
                if (cur == null) {
                    if (root[chr - 'a'] == null) {
                        root[chr - 'a'] = new TrieNode(chr);
                    }
                    cur = root[chr - 'a'];
                } else {
                    if (cur.next[chr - 'a'] == null) {
                        cur.next[chr - 'a'] = new TrieNode(chr);
                    }
                    cur = cur.next[chr - 'a'];
                }
            }
            if (cur != null) {
                cur.val = ori;
            }
        }
    }

    private static class State {
        public int x;
        public int y;
        public Trie.TrieNode node;
        public State(int x, int y, Trie.TrieNode n) {
            this.x = x;
            this.y = y;
            node = n;
        }
    }
    // File path of dictionary file
    static String dictPath = "words.txt";

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        if (k < 0) {
            throw new IllegalArgumentException();
        }
        In boardFile = new In(boardFilePath);
        if (!boardFile.exists()) {
            throw new IllegalArgumentException();
        }
        board = boardFile.readAllLines();
        N = board.length;
        M = board[0].length();
        for (int i = 1; i < N; ++i) {
            if (board[i].length() != M) {
                throw new IllegalArgumentException();
            }
        }

        In dictFile = new In(dictPath);
        trie = new Trie();
        while (!dictFile.isEmpty()) {
            String str = dictFile.readString();
            if (str.contains("'")) {
                continue;
            }
            trie.add(str);
        }
        limit = k;
        Comparator<String> mcp = new MyComparator();
        pq = new PriorityQueue<>(mcp);
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                vis = new boolean[N][M];
                vis[i][j] = true;
                Trie.TrieNode n = trie.root[board[i].charAt(j) - 'a'];
                if (n.val() != null) {
                    pq.add(n.val());
                    n.removeVal();
                    keepSize();
                }
                dfs(i, j, n);
                vis[i][j] = false;
            }
        }
        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.remove());
        }
        return result.reversed();
    }

    private static void dfs(int x, int y, Trie.TrieNode n) {
        for (int i = 0; i < 8; ++i) {
            char c = charAt(x, y, i);
            if (c == ' ') {
                continue;
            }
            if (n.next[c - 'a'] != null) {
                vis[x + dx[i]][y + dy[i]] = true;
                Trie.TrieNode next = n.next[c - 'a'];
                if (next.val() != null) {
                    pq.add(next.val());
                    next.removeVal();
                    keepSize();
                }
                dfs(x + dx[i], y + dy[i], next);
                vis[x + dx[i]][y + dy[i]] = false;
            }
        }
    }

    private static char charAt(int x, int y, int bearing) {
        x = x + dx[bearing];
        y = y + dy[bearing];

        if (x < 0 || x >= N || y < 0 || y >= M || vis[x][y]) {
            return ' ';
        } else {
            return board[x].charAt(y);
        }
    }

    private static void keepSize() {
        if (pq.size() > limit) {
            pq.remove();
        }
    }

    public static void main(String[] arg) {
        long startTime = System.currentTimeMillis();
        System.out.println(solve(Integer.parseInt(arg[0]), arg[1]));
        long endTime = System.currentTimeMillis();
        System.out.println("Total time: %d".formatted(endTime - startTime));
    }
}
