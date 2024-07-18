import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        double ullat = params.get("ullat");
        double ullon = params.get("ullon");
        double lrlat = params.get("lrlat");
        double lrlon = params.get("lrlon");
        double w = params.get("w");
        double h = params.get("h");
        if (w < 0 || h < 0 || !check(ullon, ullat, lrlon, lrlat)) {
            results.put("query_success", false);
            return results;
        }
        lrlon = Math.min(lrlon, MapServer.ROOT_LRLON);
        lrlat = Math.max(lrlat, MapServer.ROOT_LRLAT);
        ullon = Math.max(ullon, MapServer.ROOT_ULLON);
        ullat = Math.min(ullat, MapServer.ROOT_ULLAT);

        /* Consider the lonDpp and latDpp of the tile */
        double basicLonDpp = -(MapServer.ROOT_ULLON - MapServer.ROOT_LRLON) / MapServer.TILE_SIZE;
        double basicLatDpp = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / MapServer.TILE_SIZE;

        double queryLonDpp = -(ullon - lrlon) / w;
        double queryLatDpp = (ullat - lrlat) / h;

        int times = 64;
        int depth = 7;


        while (basicLonDpp / times <= queryLonDpp && basicLatDpp / times <= queryLatDpp && times >= 1) {
            depth -= 1;
            times /= 2;
        }
        times *= 2;
        results.put("depth", depth);

        double curLonDpp = basicLonDpp / times;
        double curLatDpp = basicLatDpp / times;

        double numPixelX = (ullon - MapServer.ROOT_ULLON) / curLonDpp;
        int startX = (int) Math.floor(numPixelX / MapServer.TILE_SIZE);
        double numPixelY = -(ullat - MapServer.ROOT_ULLAT) / curLatDpp;
        int startY = (int) Math.floor(numPixelY / MapServer.TILE_SIZE);

        double newUllon = MapServer.ROOT_ULLON + startX * MapServer.TILE_SIZE * curLonDpp;
        results.put("raster_ul_lon", newUllon);

        double newUllat = MapServer.ROOT_ULLAT + (-1) * startY * MapServer.TILE_SIZE * curLatDpp;
        results.put("raster_ul_lat", newUllat);

//        int nX = (int) w / MapServer.TILE_SIZE;
        int nX = (int) Math.ceil((lrlon - MapServer.ROOT_ULLON) / (MapServer.TILE_SIZE * curLonDpp) - startX);
//        while (MapServer.ROOT_ULLON + (startX + nX) * MapServer.TILE_SIZE * curLonDpp <= lrlon) {
//            nX += 1;
//        }
        double newLrlon = MapServer.ROOT_ULLON + (startX + nX) * MapServer.TILE_SIZE * curLonDpp;
        results.put("raster_lr_lon", newLrlon);

//        int nY = (int) h / MapServer.TILE_SIZE;
        int nY = (int) Math.ceil((-lrlat + MapServer.ROOT_ULLAT) / (MapServer.TILE_SIZE * curLatDpp) - startY);
//        while (MapServer.ROOT_ULLAT + (-1) * (startY + nY) * MapServer.TILE_SIZE * curLatDpp > lrlat) {
//            nY += 1;
//        }
        double newLrlat = MapServer.ROOT_ULLAT + (-1) * (startY + nY) * MapServer.TILE_SIZE * curLatDpp;
        results.put("raster_lr_lat", newLrlat);

        String[][] render = new String[nY][nX];
        for (int row = 0; row < nY; ++row) {
            for (int col = 0; col < nX; ++col) {
                render[row][col] = String.format("d%d_x%d_y%d.png", depth, startX + col, startY + row);
            }
        }
        results.put("render_grid", render);
        results.put("query_success", true);
        return results;
    }

    private boolean in(double lon, double lat) {
        if (lon > MapServer.ROOT_LRLON || lon < MapServer.ROOT_ULLON) {
            return false;
        }
        if (lat < MapServer.ROOT_LRLAT || lat > MapServer.ROOT_ULLAT) {
            return false;
        }
        return true;
    }
    private boolean check(double ullon, double ullat, double lrlon, double lrlat) {
        if (!in(ullon, ullat) && !in(ullon, lrlat) && !in(lrlon, ullat) && !in(lrlon, lrlat)) {
            return false;
        }
        if (ullon > lrlon || ullat < lrlat) {
            return false;
        }
        return true;
    }
}
