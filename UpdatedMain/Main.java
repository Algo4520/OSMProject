
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

public class Main {

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
		/****
		 * for (int i = 0; i < vertices.length; i++) { // double[] coords =
		 * osm_data.getVertices()[i].getCartesian_coord(); //
		 * System.out.println("Location of vertex [Cartesian Coord]: " + coords[0] + ","
		 * // + coords[1]); }
		 ****/

		// Used to test if the threshold was too high
		/***
		 * System.out.println("Last vertex: " + vertices[vertices.length -
		 * 1].getLatitude() + "," + vertices[vertices.length - 1].getLongitude());
		 * System.out.println("Source: " + edges[5634].getSource());
		 * System.out.println("Destination: " + edges[5634].getDestination());
		 ***/

		double THRESHOLD = .0010997;
		// gets inputs for the start and end points
		Scanner sc = new Scanner(System.in);

		// start point
		// some test points (32.075874,-81.094446) (32.055884,-81.073379), (32.077202,-81.091510) and (32.059971,-81.093227)
		System.out.println("Please enter your starting latitude"); 
		double startLat = sc.nextDouble();
		
		System.out.println("Please enter your starting longitude"); // I was using -81.094446 (should get -81.0935174)
		double startLong = sc.nextDouble();

		// double startLat = 32.075874;
		// double startLong = -81.094446;
		int startVertex = findEndpoint(startLong, startLat, vertices, THRESHOLD);
		

		// Destination

		System.out.println("Please enter your destination latitude"); 
		double destLat = sc.nextDouble();
		System.out.println("Please enter your destination longitude"); 
																		// -81.0904602)
		double destLong = sc.nextDouble();

		int destVertex = findEndpoint(destLong, destLat, vertices, THRESHOLD);

		Graph graph = new Graph();
		graph.makeGraph(edges, vertices);
		Dijkstra(graph, startVertex, destVertex, vertices);

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
				+ endVertex.getLongitude() + " Index is: " + index);
		System.out.println();
		return index;
	}

	public static int minDist(double dist[], Boolean isMin[], Graph g) {
		double min = Double.POSITIVE_INFINITY;
		int minIndex = -1;

		for (int i = 0; i < g.graph.size(); i++) {
			if (isMin[i] == false && dist[i] <= min) {
				min = dist[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	public static void Dijkstra(Graph g, int start, int end, OsmVertex[] vertices) {
		int numEdges = g.graph.size();
		// Create distance array.
		double[] dist = new double[numEdges + 1];
		// Create previous array.
		int[] prev = new int[numEdges + 1];

		Boolean[] isMin = new Boolean[numEdges + 1];

		// Set all distances to infinity
		for (int i = 0; i < numEdges; i++) {
			dist[i] = Double.POSITIVE_INFINITY;
			prev[i] = -1;
			isMin[i] = false;
		}

		dist[start] = 0;

		for (int i = 0; i < numEdges - 1; i++) {
			int min = minDist(dist, isMin, g);
			isMin[min] = true;

			for (Graph.Node l : g.graph.get(min)) {
				if (dist[min] + l.weight < dist[l.num] && !isMin[l.num] && dist[min] != Double.POSITIVE_INFINITY) {
					dist[l.num] = dist[min] + l.weight;
					prev[l.num] = min;
				}
			}
		}
		System.out.println("The minimum distance is " + dist[end]);
		findPath(prev, start, end, vertices);
	}

	public static void findPath(int prev[], int start, int end, OsmVertex[] vertices) {
		System.out.println("The path is: ");
		Stack<Integer> stack = new Stack<Integer>();
		int curr = end;
		while (curr != start) {
			stack.push(curr);
			curr = prev[curr];

		}

		stack.push(start);

		while (!stack.isEmpty()) {
			if (stack.peek() == end) {
				System.out.print("(" + vertices[stack.peek()].getLatitude() + ", " + vertices[stack.peek()].getLongitude() + ")(Index: " + stack.peek() + ")");
				stack.pop();
				
			} else {
				System.out.println("(" + vertices[stack.peek()].getLatitude() + ", " + vertices[stack.peek()].getLongitude() + ")(Index: " + stack.peek() + ")" + " -> ");
				stack.pop();
			}

		}

	}
}
