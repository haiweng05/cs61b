import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    public static class Node {
        private long id;
        private double lat;
        private double lon;
        private String name;
        public Node(long id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
        }

        public void setName(String str) {
            this.name = str;
        }
    }

    public static class Edge {
        private static int startId = 0;
        private long id;
        private long from;
        private long to;
        private double len;
//        private boolean activate;
//        private int[] refs;
        public Edge(long from, long to) {
            this.id = startId;
            startId += 1;
            this.from = from;
            this.to = to;
        }
    }

    public static class Road {
        private List<Long> nodes;
        private long id;
        private String type;
        private String name;
        private String speed;
        public Road(long id) {
            this.id = id;
            nodes = new ArrayList<>();
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getSpeed() {
            return speed;
        }
        public void setSpeed(String speed) {
            this.speed = speed;
        }
        public long getNode(int idx) {
            return nodes.get(idx);
        }

        public void addNode(long id) {
            nodes.add(id);
        }
        public int size() {
            return nodes.size();
        }
    }

    HashMap<Long, Node> nodes;
    HashMap<Long, Edge> edges;
    HashMap<Long, Long> degrees;
    HashMap<Long, List<Long>> adj;


    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        nodes = new HashMap<>();
        edges = new HashMap<>();
        adj = new HashMap<>();
        degrees = new HashMap<>();
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TODO: Your code here.
        List<Long> delete = new ArrayList<>();
        for (Long key : nodes.keySet()) {
            if (degrees.containsKey(key) && degrees.get(key) == 0) {
                delete.add(key);
            }
        }
        for (Long key : delete) {
            nodes.remove(key);
            adj.remove(key);
            degrees.remove(key);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return nodes.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return adj.get(v);
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double min = Double.MAX_VALUE;
        long minIdx = 0;
        for (long v : vertices()) {
            double dis = distance(lon(v), lat(v), lon, lat);
            if (min > dis) {
                min = dis;
                minIdx = v;
            }
        }
        return minIdx;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(v).lat;
    }

    void addNode(Node n) {
        if (!degrees.containsKey(n.id)) {
            degrees.put(n.id, 0L);
        }
        nodes.put(n.id, n);
    }

    void addEdge(Edge e) {

    }

    void addEdge(long from, long to) {
        if (!this.adj.containsKey(from)) {
            this.adj.put(from, new ArrayList<>());
        }
        if (!this.adj.containsKey(to)) {
            this.adj.put(to, new ArrayList<>());
        }
        this.adj.get(from).add(to);
        this.adj.get(to).add(from);

        addDegree(from);
        addDegree(to);
    }

    void addDegree(long v) {
        if (!degrees.containsKey(v)) {
            degrees.put(v, 0L);
        } else {
            long degree = degrees.get(v) + 1L;
            degrees.put(v, degree);
        }
    }
}
