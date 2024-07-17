package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private Maze maze;
    private boolean findCycle = false;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
    }

    @Override
    public void solve() {
        for (int i = 0; i < maze.N() * maze.N(); ++i) {
            if (!marked[i]) {
                distTo[i] = 0;
                edgeTo[i] = i;
                dfs(i);
            }
        }
    }

    // Helper methods go here
    private void dfs(int v) {
        marked[v] = true;
        announce();

        for (int to : maze.adj(v)) {
            if (marked[to] && to != edgeTo[v]) {
                findCycle = true;
                edgeTo[to] = v;
                announce();
                return;
            }
            if (!marked[to]) {
                marked[to] = true;
                edgeTo[to] = v;
                distTo[to] = distTo[v] + 1;
                announce();
                dfs(to);
                if (findCycle) {
                    return;
                }
            }
        }
    }
}

