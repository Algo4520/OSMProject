
import java.util.*;


import bridges.data_src_dependent.OsmEdge;
import bridges.data_src_dependent.OsmVertex;

public class Graph{
	Map<Integer, List<Node>> graph;
	
	Graph(){
		this.graph = new HashMap<>();
	}
	
	public class Node {
		double weight;
		int num;

		public Node(int num, double weight) {
			this.weight = weight;
			this.num = num;
		}
		public Node() {
			this.weight = 0;
			this.num = 0;
		}

	}
	
	// makes an adjacency list using a Hashmap that maps the index of the vertex in
	// the edge array to a list of its neighbors
	// for example 0: <2, 120, 3264>
	public void makeGraph(OsmEdge[] edges, OsmVertex[] vertices) {
		for (int i = 0; i < vertices.length; i++) {
			graph.put(i, new ArrayList<>());
		}

		for (int i = 0; i < edges.length; i++) {
			Node NextVert = new Node();
			NextVert.num = edges[i].getDestination();
			NextVert.weight = edges[i].getDistance();
			graph.get(edges[i].getSource()).add(NextVert);

			Node CurrVert = new Node();
			CurrVert.num = edges[i].getSource();
			CurrVert.weight = edges[i].getDistance();
			graph.get(edges[i].getDestination()).add(CurrVert);
		}
	}
}