# About the MVC Project
For more project information, please see project requirement detailed in [pdf](project_description.pdf).

## Minimum Vertex Cover problem definition
![Minimum Vertex Cover definition](pics/mvc_definition.png?raw=true "Title")

## Data description
![Details of data](pics/graph_data_description.png?raw=true "Title")

## My implementations
**Branch-and-Bound Algorithm** (ranked 8th/49)
> The branch-and-bound (BnB) algorithm is a complete algorithm, which means that it guarantees the we can have the global optimum solution. But the time complexity may increase exponentially as the size of vertices increases. We use **backtracking** to make sure all possibilities are covered . 


	 Basic idea:
	1. We pick an arbitrary edge (u, w) ∈ E. 
	2. Recursively search for a vertex cover in (G-u) , i.e. G remove vertex of u. 
	3. Recursively search for a vertex cover in (G-w) , i.e. G remove vertex of w. 
- Time complexity: 2^|V|
- Space complexity: O(|V|+|E|)

**Approximation Algorithm** (ranked 3th/49) : 
> Approximation algorithms guarantee performance bounds (ex: 2-approximation algorithms guarantees the performance would be only 2 times worse the best result). And it is fast comparing to Branch-and-Bound Algorithm and local search algorithm. 

	Basic idea:
	1. Selecting any unvisited vertex v with some heuristic. 
	2. Look for any unvisited neighbor u of v.
	
- Time complexity: O(|E| + |V|)
- Space complexity: O(1)
