# PathFinding Analyzer

PathFinding Analyzer is a visualization tool which is focused on shortest path algorithms in Graph. The application has different algorithms which are designed for a 2D Grid, where the cost between the two consecutive cells is 1.

<p align="center">
  <img src="https://github.com/ashishgopalhattimare/PathFinderAnalyzer/blob/master/videos/INFO_GIF.gif" width="30%">
</p>
<p align="center"> <i>Fig 1.0 Tutorial to the PathFinding Analyzer</i> </p>

## Shortest Path Algorithms
1. **Breadth -First Search (BFS)**

	[BFS](https://www.hackerearth.com/practice/algorithms/graphs/breadth-first-search/tutorial/) is a unweighted graph traversal algorithm that starts the traversal from the source node and explores all the neighbouring nodes. It follows the principle of level order traversal, where it selects the nearest node and explore all the unexplored nodes.
	
	It is a great algorithm which is simple to implement using **Queue**. It also guarantees the shortest path from source to destination, if exists.

	The Space Complexity of the BFS algorithm is b^d (branching factor raised to the depth of the graph).

<p align="center">
  <img src="https://github.com/ashishgopalhattimare/PathFinderAnalyzer/blob/master/videos/BFS_GIF.gif" width="50%">
</p>
<p align="center"> <i>Fig 1.1 Demo of the Breadth-First Search (BFS) Algorithm</i> </p>

2. **Depth-First Search (DFS)**

	[DFS](https://www.hackerearth.com/practice/algorithms/graphs/depth-first-search/tutorial/) is an algorithm for traversing or searching tree or graph data structures. THe algorithm starts at the source node and explores as far as possible along each branch before it bracktracks and follows the remaining edges of a node.

	It is very inefficient algorithm for path-finding and it does not guarantee shortest path. It includes **exhaustive** searches of all the nodes by going ahead, if possible, else by backtracking. It uses **Stack** for its implementation. It can get stuck in an infinite loop, which is why it is not **"Complete"**.

	The Space Complexity of the DFS algorithm is O(log(d)) where is 'd' is the depth of the graph.

<p align="center">
  <img src="https://github.com/ashishgopalhattimare/PathFinderAnalyzer/blob/master/videos/DFS_GIF.gif" width="50%">
</p>
<p align="center"> <i>Fig 1.2 Demo of the Depth-First Search (DFS) Algorithm</i> </p>

4. **A\* Search**

	A* (pronounced as "A star") is a computer algorithm that is widely used in pathfinding and traversal. The algorithm efficiently plots a walkable path between multiple nodes, or points, on the graph.
On a map with many obstacles, pathfinding from points  AA  to  BB  can be difficult. A robot, for instance, without getting much other direction, will continue until it encounters an obstacle, as in the path-finding example to the left below.

	However, the A* algorithm introduces a [heuristic](https://brilliant.org/wiki/heuristic/ "heuristic") into a regular graph-searching algorithm, essetially planning ahead at each step so a more optimal decision is made. With A*, a robot would instead find a path in a way similar to the diagram on the right below.

	A* is an extension of  [Dijkstra's algorithm](https://brilliant.org/wiki/dijkstras-short-path-finder/ "Dijkstra's algorithm")  with some characteristics of  [Breadth-First Search (BFS)](https://brilliant.org/wiki/breadth-first-search-bfs/ "breadth-first search (BFS)").

	To know more about the A\* Algorithm, follow this [*link*](https://brilliant.org/wiki/a-star-search/).
  
<p align="center">
  <img src="https://github.com/ashishgopalhattimare/PathFinderAnalyzer/blob/master/videos/Astar_GIF.gif" width="50%">
</p>
<p align="center"> <i>Fig 1.3 Demo of the A* Search Algorithm</i> </p>

5 **Greedy Best-First Search**

	It is a suboptimal best-first search algorithm which works on the principle of A* Algorithm and always priorities the node with the lowest heuristic value without any consideration of the cost to get to that node. While this greedy GBFS algorithm can be effective in practice, it can be misled by an arbitrary amount if th heuristic is wrong. Hence, it does not gurantee shortest path.
	
	To know more about the GBFS Algorithm, follow this [*link*](https://pdfs.semanticscholar.org/96b1/4928aa2cdcb60e29ac68be39e8a91069c875.pdf).
	
<p align="center">
  <img src="https://github.com/ashishgopalhattimare/PathFinderAnalyzer/blob/master/videos/Greedy_GIF.gif" width="50%">
</p>
<p align="center"> <i>Fig 1.3 Demo of the Greedy Best-First Search (GBFS) Algorithm</i> </p>

