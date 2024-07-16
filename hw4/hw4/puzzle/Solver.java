package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solver {

    private int moves;
    private SearchNode s;
    private Map<WorldState, Integer> distTo;
    private Map<WorldState, Integer> estiDist;
    int enqueue;


    private class SearchNode implements Comparable<SearchNode> {

        private WorldState worldState;
        private int move;
        private SearchNode prev;

        public SearchNode(WorldState w, int m, SearchNode p) {
            worldState = w;
            move = m;
            prev = p;
        }

        public int estimatedDistanceToGoal() {
            if (!estiDist.containsKey(worldState)) {
                estiDist.put(worldState, worldState.estimatedDistanceToGoal());
            }
            return estiDist.get(worldState);
        }

        public Iterable<WorldState> neighbors() {
            return worldState.neighbors();
        }

        @Override
        public int compareTo(SearchNode s) {
            int priority1 = this.move + this.estimatedDistanceToGoal();
            int priority2 = s.move + s.estimatedDistanceToGoal();
            return Integer.compare(priority1, priority2);
        }

    }
    public Solver(WorldState initial) {
        MinPQ<SearchNode> queue = new MinPQ<>();
        distTo = new HashMap<WorldState, Integer>();
        estiDist = new HashMap<WorldState, Integer>();
        SearchNode n = new SearchNode(initial, 0, null);
        queue.insert(n);
        enqueue += 1;
        distTo.put(n.worldState, 0);
        while (!queue.isEmpty()) {
            SearchNode cur = queue.delMin();
            if (cur.estimatedDistanceToGoal() == 0) {
                moves = cur.move;
                s = cur;
                return;
            }
            if (cur.move > distTo.get(cur.worldState)) {
                continue;
            }
            for (WorldState neighbor : cur.neighbors()) {
                if (!distTo.containsKey(neighbor) || distTo.get(neighbor) > cur.move + 1) {
                    distTo.put(neighbor, cur.move + 1);
                    SearchNode searchNode = new SearchNode(neighbor, cur.move + 1, cur);
                    queue.insert(searchNode);
                    enqueue += 1;
                }
            }
        }
    }
    public int moves() {
        return moves;
    }
    public Iterable<WorldState> solution() {
        List<WorldState> list = new ArrayList<>();
        SearchNode cur = s;
        while (cur != null) {
            list.add(0, cur.worldState);
            cur = cur.prev;
        }
        return list;
    }
}
