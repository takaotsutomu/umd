package graphs;

import java.util.*;

/**
 * Implements a graph. We use two maps: one map for adjacency properties 
 * (adjancencyMap) and one map (dataMap) to keep track of the data associated 
 * with a vertex. 
 * 
 * @author cmsc132
 * 
 * @param <E>
 */
public class Graph<E> {
	class Heap<T extends Comparable<T>> { 
		private Comparable< T >[] heap;
	
		public Heap( Collection<Comparable<T>> elements ) {
			heap = elements.toArray(new Comparable[0]);
			int start = (heap.length - 2) / 2;
			while (start > -1) {
				heapDown(start--, heap.length - 1);
			}
		}
		
		private void heapDown( int index, int end ) {
			int leftChildIndex = index * 2 + 1;
			if (leftChildIndex + 1 <= end && heap[index].compareTo((T)heap[leftChildIndex + 1]) < 0) {
				if (heap[leftChildIndex + 1].compareTo((T)heap[leftChildIndex]) > 0) {
					swap(index, leftChildIndex + 1);
					heapDown(leftChildIndex + 1, end);
				} else {
					swap(index, leftChildIndex);
					heapDown(leftChildIndex, end);
				}
			} else if (leftChildIndex <= end && heap[index].compareTo((T)heap[leftChildIndex]) < 0) {
				swap(index, leftChildIndex);
				heapDown(leftChildIndex, end);
			} 
		}
		
		private void swap(int index, int leftChildIndex) {
			Comparable<T> temp = heap[index];
			heap[index] = heap[leftChildIndex];
			heap[leftChildIndex] = temp;
		}
	
		public ArrayList<T> sort() {
			int end = heap.length - 1;
			while (end > 0) {
				Comparable<T> temp = heap[0];
				heap[0] = heap[end];
				heap[end] = temp;
				heapDown(0, --end);
			}
			ArrayList<T> list = new ArrayList<T>();
			for (int i = 0; i < heap.length; i++) {
				list.add((T)heap[i]);
			}
			return list;
		}
	}
	/* You must use the following maps in your implementation */
	private HashMap<String, HashMap<String, Integer>> adjacencyMap;
	private HashMap<String, E> dataMap;
	/**
	 * Initializes the adjacency and data maps.
	 */
	public Graph() {
		adjacencyMap = new HashMap<String, HashMap<String, Integer>>();
		dataMap = new HashMap<String, E>();
	}
	/**
	 * Adds a vertex to the graph by adding to the adjacency map 
	 * an entry for the vertex. This entry will be an empty map. 
	 * An entry in the dataMap will store the provided data.
	 * @param vertexName vertex's name
	 * @param data data associated with the vertex
	 * @throws IllegalArgumentException if the vertex already exists in the graph
	 */
	public void addVertex(String vertexName,
            E data) {
		if (adjacencyMap.containsKey(vertexName)) {
			throw new IllegalArgumentException("Error.");
		}
		adjacencyMap.put(vertexName, new HashMap<String, Integer>());
		dataMap.put(vertexName, data);
	}
	/**
	 * Adds or updates a directed edge with the specified cost.
	 * @param startVertexName
	 * @param endVertexName
	 * @param cost
	 * @throws IllegalArgumentException if any of the vertices are not part of the graph
	 */
	public void addDirectedEdge(String startVertexName, 
			String endVertexName,
            int cost) {
		if (!adjacencyMap.containsKey(startVertexName) ||
				!adjacencyMap.containsKey(endVertexName)) {
			throw new IllegalArgumentException("Error.");
		}
		adjacencyMap.get(startVertexName).put(endVertexName, cost);
	}
	/**
	 * Returns a string with information about the Graph. 
	 * @return a string with information about the Graph
	 */
	public java.lang.String toString() {
		StringBuilder sb = new StringBuilder("Vertices: ");
		ArrayList<String> verticesList = new ArrayList<String>();
		Set<String> verticesSet = adjacencyMap.keySet();
		for (String vertex : verticesSet) {
			verticesList.add(vertex);
		}
		Heap<String> heap = new Heap(verticesList);
		verticesList = heap.sort();
		sb.append(verticesList.toString() + "\nEdges:\n");
		for (String vertex : verticesList) {
			sb.append("Vertex(" + vertex + ")--->{");
			if (!adjacencyMap.get(vertex).isEmpty()) {
				ArrayList<String> adjacentVerticesList = new ArrayList<String>();
				Set<String> adjacentVerticesSet = adjacencyMap.get(vertex).keySet();
				for (String adjacentVertex : adjacentVerticesSet) {
					adjacentVerticesList.add(adjacentVertex);
				}
				heap = new Heap(adjacentVerticesList);
				adjacentVerticesList = heap.sort();
				for (int i = 0; i < adjacentVerticesList.size() - 1; i++) {
					sb.append(adjacentVerticesList.get(i) + 
							"=" + adjacencyMap.get(vertex).get(adjacentVerticesList.get(i)) + ", ");
				}
				sb.append(adjacentVerticesList.get(adjacentVerticesList.size() - 1) + 
						"=" + adjacencyMap.get(vertex).get(adjacentVerticesList.get(adjacentVerticesList.size() - 1)) +
						"}\n");
			} else {
				sb.append("}\n");
			}
		}
		return sb.toString();
	}
	/**
	 * Returns a map with information about vertices adjacent to vertexName. 
	 * If the vertex has no adjacents, an empty map is returned.
	 * @param vertexName
	 * @return a map with information about vertices adjacent to vertexName 
	 */
	public Map<String, Integer> getAdjacentVertices(String vertexName) {
		return adjacencyMap.get(vertexName);
	}
	/**
	 * Returns the cost associated with the specified edge.
	 * @param startVertexName
	 * @param endVertexName
	 * @throws IllegalArgumentException if any of the vertices are not part of the graph
	 * @return the cost associated with the specified edge 
	 */
	public int getCost(String startVertexName,
			String endVertexName) {
		if (!adjacencyMap.containsKey(startVertexName) ||
				!adjacencyMap.containsKey(endVertexName)) {
			throw new IllegalArgumentException("Error.");
		}
		return adjacencyMap.get(startVertexName).get(endVertexName);
	}
	/**
	 * Returns a Set with all the graph vertices.
	 * @return a Set with all the graph vertices 
	 */
	public Set<String> getVertices() {
		return adjacencyMap.keySet();
	}
	/**
	 * Returns the data component associated with the specified vertex.
	 * @param vertex
	 * @throws IllegalArgumentException if the vertex is not part of the graph
	 * @return the data component associated with the specified vertex 
	 */
	public E getData(String vertex) {
		if (!adjacencyMap.containsKey(vertex)) {
			throw new IllegalArgumentException("Error.");
		}
		return dataMap.get(vertex);
	}
	/**
	 * Computes Depth-First Search of the specified graph.
	 * @param startVertexName
	 * @param callback represents the processing to apply to each vertex
	 * @throws IllegalArgumentException if the vertex is not part of the graph
	 */
	public void doDepthFirstSearch(String startVertexName,
            CallBack<E> callback) {
		if (!adjacencyMap.containsKey(startVertexName)) {
			throw new IllegalArgumentException("Error.");
		}
		HashSet<String> visited = new HashSet<String>();
		Stack<String> stack = new Stack<String>();
		stack.push(startVertexName);
		while (!stack.isEmpty()) {
			String vertex = stack.pop();
			if (!visited.contains(vertex)) {
				callback.processVertex(vertex, dataMap.get(vertex));
				visited.add(vertex);
				ArrayList<String> adjacentVerticesList = new ArrayList<String>();
				Set<String> adjacentVerticesSet = adjacencyMap.get(vertex).keySet();
				for (String adjacentVertex : adjacentVerticesSet) {
					adjacentVerticesList.add(adjacentVertex);
				}
				Heap heap = new Heap(adjacentVerticesList);
				adjacentVerticesList = heap.sort();
				for (int i = 0; i < adjacentVerticesList.size(); i++) {
					if (!visited.contains(adjacentVerticesList.get(i))) {
						stack.push(adjacentVerticesList.get(i));
					}
				}
			}
		}
	}
	/**
	 * Computes Breadth-First Search of the specified graph.
	 * @param startVertexName
	 * @param callback represents the processing to apply to each vertex
	 * @throws IllegalArgumentException if the vertex is not part of the graph
	 */
	public void doBreadthFirstSearch(String startVertexName,
            CallBack<E> callback) {
		if (!adjacencyMap.containsKey(startVertexName)) {
			throw new IllegalArgumentException("Error.");
		}
		HashSet<String> visited = new HashSet<String>();
		PriorityQueue<String> queue = new PriorityQueue<String>();
		queue.add(startVertexName);
		while (!queue.isEmpty()) {
			String vertex = queue.poll();
			if (!visited.contains(vertex)) {
				callback.processVertex(vertex, dataMap.get(vertex));
				visited.add(vertex);
				ArrayList<String> adjacentVerticesList = new ArrayList<String>();
				Set<String> adjacentVerticesSet = adjacencyMap.get(vertex).keySet();
				for (String adjacentVertex : adjacentVerticesSet) {
					adjacentVerticesList.add(adjacentVertex);
				}
				Heap heap = new Heap(adjacentVerticesList);
				adjacentVerticesList = heap.sort();
				for (int i = 0; i < adjacentVerticesList.size(); i++) {
					if (!visited.contains(adjacentVerticesList.get(i))) {
						queue.add(adjacentVerticesList.get(i));
					}
				}
			}
		}
	}
	/**
	 * Computes the shortest path and shortest path cost using Dijkstras's 
	 * algorithm. It initializes shortestPath with the names of the vertices 
	 * corresponding to the shortest path. If there is no shortest path, shortestPath will be have entry "None".
	 * @param startVertexName
	 * @param endVertexName
	 * @param shortestPath initialized by the method with the shortest path or "None"
	 * @throws IllegalArgumentException if any of the vertices are not part of the graph
	 * @return Shortest path cost or -1 if no path exist
	 */
	public int doDijkstras(String startVertexName,
            String endVertexName,
            ArrayList<String> shortestPath) {
		if (!adjacencyMap.containsKey(startVertexName) ||
				!adjacencyMap.containsKey(endVertexName)) {
			throw new IllegalArgumentException("Error.");
		}
		class VQEntry implements Comparable<VQEntry> {
			String vertexName;
			String predVertex;
			int cost;
			
			public VQEntry(String vertexName, String predVertex, int cost) {
				this.vertexName = vertexName;
				this.predVertex = predVertex;
				this.cost = cost;
			}
			
			@Override
			public int compareTo(VQEntry o) {
				// TODO Auto-generated method stub
				if (this.cost < o.cost) {
					return -1;
				} else if (this.cost == o.cost) {
					return 0;
				} else {
					return 1;
				}
			}
		}
		HashSet<VQEntry> sPath = new HashSet<VQEntry>();
		HashMap<String, VQEntry> VQEntryMap = new HashMap<String, VQEntry>();
		Set<String> verticesSet = getVertices();
		for (String vertex : verticesSet) {
			if (vertex.equals(startVertexName)) {
				VQEntryMap.put(vertex, new VQEntry(vertex, "None", 0));
			} else {
				VQEntryMap.put(vertex, new VQEntry(vertex, "None", Integer.MAX_VALUE));
			}
		}
		PriorityQueue<VQEntry> queue = new PriorityQueue<VQEntry>();
		queue.add(VQEntryMap.get(startVertexName));
		while (!queue.isEmpty()) {
			VQEntry kVertex = queue.poll();
			if (!sPath.contains(kVertex)) {
				sPath.add(kVertex);
				Set<String> adjacentVerticesNameSet = getAdjacentVertices(kVertex.vertexName).keySet();
				for (String adjacentVertexName : adjacentVerticesNameSet) {
					if (kVertex.cost + getCost(kVertex.vertexName, adjacentVertexName) < 
							VQEntryMap.get(adjacentVertexName).cost) {
						VQEntryMap.get(adjacentVertexName).cost = kVertex.cost + getCost(kVertex.vertexName, adjacentVertexName);
						VQEntryMap.get(adjacentVertexName).predVertex = kVertex.vertexName;
					}
					if (!sPath.contains(VQEntryMap.get(adjacentVertexName))) {
						queue.add(VQEntryMap.get(adjacentVertexName));
					}
				}
			}
		}
		Stack<String> stack = new Stack<String>();
		stack.add(endVertexName);
		String predVertex = VQEntryMap.get(endVertexName).predVertex;
		while (!predVertex.equals("None")) {
			stack.add(predVertex);
			predVertex = VQEntryMap.get(predVertex).predVertex;
		}
		while (!stack.isEmpty()) {
			shortestPath.add(stack.pop());
		}
		System.out.println(shortestPath);
		if (!shortestPath.get(0).equals(startVertexName)) {
			System.out.println("hey");
			shortestPath.clear();
			shortestPath.add("None");
			return -1;
		}
		return VQEntryMap.get(endVertexName).cost;
	}

}