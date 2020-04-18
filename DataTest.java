import java.io.IOException;
import java.util.*;
import java.lang.Math;

import bridges.connect.Bridges;
import bridges.connect.DataSource;
import bridges.data_src_dependent.OsmData;
import bridges.data_src_dependent.OsmVertex;
import bridges.validation.RateLimitException;
import bridges.data_src_dependent.OsmEdge;

/*
 * What I found out about the data structures given:
 * 1) To access the vertices from the edges you have .getSource() from an edges and plug that into the vertex array(same for destination but you will use .getDestination())
 * Ex) The vertex at vertices[edges[i].getSource()] will be the source of edges[i].
 * 
 * 2) The weight of the edges can be found using edges[i]. 
 * 
 * 
 * What to use for algorithm:
 * 1) Use the startVertex for the starting point. It is the position of the vertex in the array, so you can pass it to the map to get the List of nodes which contain
 *    weight of the key(source of edge) to the num attribute of the node(index of destination node)
 * 2) Pass graph to the algorithm.
 * 3) You can call the destVertex at the end of the algorithm to go back through the shortest path so you can print it out.
 */

public class DataTest {

	public static void main(String[] args) throws Exception {

		// create the Bridges object
		Bridges bridges = new Bridges(1, "AlgoProject", "978939733888");// go to bridges->profile to find your username
																		// and api key

		// opens data source for use
		DataSource ds = bridges.getDataSource();

		// picks a city from the data set
		OsmData osm_data = ds.getOsmData("Savannah, Georgia" + "");

		// gets the edges and vertices from the data set
		OsmVertex[] vertices = osm_data.getVertices();
		OsmEdge[] edges = osm_data.getEdges();

		System.out.println("Number of Vertices [Savannah]:" + vertices.length);
		System.out.println("Number of Edges [Savannah]:" + edges.length);

		// Used to see examples of the vertices
		// Delete later
		/**** for (int i = 0; i < vertices.length; i++) {
		// double[] coords = osm_data.getVertices()[i].getCartesian_coord();
		// System.out.println("Location of vertex [Cartesian Coord]: " + coords[0] + ","
		// + coords[1]);
		 } 
		 ****/
		
		//Used to test if the threshold was too high
		/***System.out.println("Last vertex: " + vertices[vertices.length - 1].getLatitude() + ","
				+ vertices[vertices.length - 1].getLongitude());
		System.out.println("Source: " + edges[5634].getSource());
		System.out.println("Destination: " + edges[5634].getDestination());
		***/
		
		double THRESHOLD = .0010997;
		// gets inputs for the start and end points
		Scanner sc = new Scanner(System.in);

		// start point
		System.out.println("Please enter your starting latitude"); // I was using 32.075874 (should get 32.0747743)
		double startLat = sc.nextDouble();
		System.out.println("Please enter your starting longitude"); // I was using -81.094446 (should get -81.0935174)
		double startLong = sc.nextDouble();
		int startVertex = findEndpoint(startLong, startLat, vertices, THRESHOLD);

		// Destination
		System.out.println("Please enter your destination latitude"); // I was using 32.077202 (should get 32.0763728)
		double destLat = sc.nextDouble();
		System.out.println("Please enter your destination longitude"); // I was using -81.091510 (should get -81.0904602)
		double destLong = sc.nextDouble();
		int destVertex = findEndpoint(destLong, destLat, vertices, THRESHOLD);

		Graph graph = new Graph();
		graph.makeGraph(edges, vertices);
	}

	// Takes the inputs from the user and determines the closest point in the vertex
	// list to the given point
	// Returns the position of the vertex in the vertices array.
	public static Integer findEndpoint(double endLong, double endLat, OsmVertex[] vertices, double THRESHOLD) {

		OsmVertex endVertex = null;
		int index = -1;
		for (int i = 0; i < vertices.length; i++) {

			if (Math.abs(endLong - vertices[i].getLongitude()) <= THRESHOLD
					&& Math.abs(endLat - vertices[i].getLatitude()) <= THRESHOLD) {
				endVertex = vertices[i];
				index = i;
			}
		}
		System.out.println("Location of vertex [Latitude, Longitude]: " + endVertex.getLatitude() + ","
				+ endVertex.getLongitude());
		System.out.println();
		return index;
	}

	
	
}
