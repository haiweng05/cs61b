import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;


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

        public String getName() {
            return name;
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

        public void addNode(long idx) {
            nodes.add(idx);
        }
        public int size() {
            return nodes.size();
        }
    }

    HashMap<Long, Node> nodes;
    HashMap<Long, List<Long>> adj;
    HashMap<String, List<Long>> name2Idx;


    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        nodes = new HashMap<>();
        adj = new HashMap<>();
        name2Idx = new HashMap<>();
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
        // Your code here.
        List<Long> delete = new ArrayList<>();
        for (Long key : nodes.keySet()) {
            if (adjacent(key) == null) {
                delete.add(key);
            }
        }
        for (Long key : delete) {
            adj.remove(key);
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
            if (adjacent(v) == null) {
                continue;
            }
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
        nodes.put(n.id, n);
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
    }



    public static class Trie {
        private class TrieNode {
            TrieNode[] next;
            Character c;
            String val;

            TrieNode(char a) {
                c = a;
                next = new TrieNode[27];
            }
        }
        TrieNode[] root;

        Trie() {
            root = new TrieNode[27];
        }

        private int getIndex(char a) {
            if (a == ' ') {
                return 0;
            } else {
                return a - 'a' + 1;
            }
        }
        public void add(String str) {
            TrieNode cur = null;
            String ori = str;
            str = GraphDB.cleanString(str);
            for (char ch : str.toCharArray()) {
                if (cur == null) {
                    if (root[getIndex(ch)] == null) {
                        root[getIndex(ch)] = new TrieNode(ch);
                    }
                    cur = root[getIndex(ch)];
                } else {
                    if (cur.next[getIndex(ch)] == null) {
                        cur.next[getIndex(ch)] = new TrieNode(ch);
                    }
                    cur = cur.next[getIndex(ch)];
                }
            }
            if (cur != null) {
                cur.val = ori;
            }
        }

        public List<String> find(String str) {
            TrieNode cur = null;
            str = GraphDB.cleanString(str);
            for (char ch : str.toCharArray()) {
                if (ch != ' ' && !Character.isLowerCase(ch)) {
                    continue;
                }
                if (cur == null) {
                    if (root[getIndex(ch)] == null) {
                        return new ArrayList<>();
                    }
                    cur = root[getIndex(ch)];
                } else {
                    if (cur.next[getIndex(ch)] == null) {
                        return new ArrayList<>();
                    }
                    cur = cur.next[getIndex(ch)];
                }
            }
            Set<String> set = new TreeSet<>();
            set = yieldFrom(cur);

            List<String> lst = new ArrayList<>(set);
            return lst;
        }

        private TreeSet<String> yieldFrom(TrieNode n) {
            TreeSet<String> lst = new TreeSet<>();
            if (n.val != null) {
                lst.add(n.val);
            }
            for (int i = 0; i < 27; ++i) {
                if (n.next[i] != null) {
                    lst.addAll(yieldFrom(n.next[i]));
                }
            }
            return lst;
        }
    }

    private static boolean trieBuilt = false;
    private static Trie trie;
    /**
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        if (!trieBuilt) {
            buildTrie();
            trieBuilt = true;
        }
        return trie.find(prefix);
    }

    private void buildTrie() {
        trie = new Trie();
        for (long l : vertices()) {
            GraphDB.Node n = nodes.get(l);
            String name = n.getName();
            if (name != null) {
                trie.add(name);
            }
        }
    }
    public List<Map<String, Object>> getLocations(String locationName) {
        locationName = GraphDB.cleanString(locationName);
        List<Long> positions = name2Idx.get(locationName);
        if (positions == null) {
            return null;
        }
        List<Map<String, Object>> lst = new ArrayList<>();
        for (long l : positions) {
            Node n = nodes.get(l);
            Map<String, Object> map = new HashMap<>();
            map.put("lat", lat(l));
            map.put("lon", lon(l));
            map.put("name", n.getName());
            map.put("id", l);
            lst.add(map);
        }
        return lst;
    }
}
