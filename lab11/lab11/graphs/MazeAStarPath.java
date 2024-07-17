package lab11.graphs;

import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    private class Edge implements Comparable<Edge> {
        private int to;
        private int priority;
        public Edge(int to, int p) {
            this.to = to;
            priority = p;
        }
        @Override
        public int compareTo(Edge o) {
            if (o == null || o.getClass() != this.getClass()) {
                throw(new NullPointerException());
            }
            if (this.priority < o.priority) {
                return 1;
            } else if (this.priority == o.priority) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(new Edge(s, h(s) + edgeTo[s]));
        marked[s] = true;
        announce();

        while (!priorityQueue.isEmpty()) {
            Edge e = priorityQueue.remove();

            if (e.to == t) {
                targetFound = true;
                return;
            }
            for (int to : maze.adj(e.to)) {
                if (!marked[to]) {
                    marked[to] = true;
                    edgeTo[to] = e.to;
                    distTo[to] = distTo[e.to] + 1;
                    priorityQueue.add(new Edge(to, h(to) + edgeTo[to]));
                    announce();
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

