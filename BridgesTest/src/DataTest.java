import bridges.connect.Bridges;
import bridges.connect.DataSource;
import bridges.data_src_dependent.OsmData;
import bridges.data_src_dependent.OsmVertex;
import bridges.data_src_dependent.OsmEdge;

public class DataTest {
	 public static void main(String[] args) throws Exception {

	        //create the Bridges object
	        Bridges bridges = new Bridges(1, "USERNAME", "API_KEY");//go to bridges->profile to find your username and api key

	        //opens data source for use
			DataSource  ds = bridges.getDataSource();
			
			//picks a city from the data set
			OsmData osm_data = ds.getOsmData("Atlanta, Georgia"
					+ "");

			//gets the edges and vertices from the data set
			OsmVertex[] vertices = osm_data.getVertices();
			OsmEdge[] edges = osm_data.getEdges();

			System.out.println("Number of Vertices [Atlanta]:" + vertices.length);
			System.out.println("Number of Edges [Atlanta]:" + edges.length);

			// get cartesian coordinate  location of first vertex(example of what a vertex looks like)
			double[] coords = osm_data.getVertices()[0].getCartesian_coord();
			System.out.println ("Location of first vertex [Cartesian Coord]: " +  coords[0] + ","
							+ coords[1]);
		}
}
