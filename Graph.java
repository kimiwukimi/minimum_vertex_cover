import java.util.LinkedList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/*
*
*   The class is Graph class used by Approx, BnB, and LS1
*
* */

public class Graph {
    public int V;
    public int E;
    public Set[] adj;
    public int[] degrees;
    String NEWLINE = "\n";

    public Graph(int V) {
        this.V = V;   // might need to -1 latter, because of 0 index vertex
        degrees = new int[V];
        adj = new HashSet[V];
        for (int v = 0; v < V; v++)
            adj[v] = new HashSet();
    }

    public void removeVertice(int v){
        Set<Integer> adj_v = this.adj[v];
        this.E -= adj_v.size();

        for (Integer w : adj_v){
            this.adj[w].remove(v);
            this.degrees[w]--;
        }
        this.adj[v].removeAll(this.adj[v]);
        this.degrees[v] = 0;

//        System.out.println("remove Vertices: " + v);
    }

    public void addBackVertice(int v, Set<Integer> adj_v){
        this.E += adj_v.size();

        for (Integer w: adj_v){
            this.adj[w].add(v);
            this.degrees[w]++;
        }
        this.adj[v] = new HashSet<Integer>(adj_v);
        this.degrees[v] = adj_v.size();
//        System.out.println("add back Vertices: "+ v);
    }

    public void addEdge(int v, int w) {
        if (!adj[v].contains(w)) {
            degrees[v] += 1;
            degrees[w] += 1;
            E++;
        }
        adj[v].add(w);
        adj[w].add(v);
    }

    public void removeEdge(int v, int w) {
        adj[v].remove(w);
        adj[w].remove(v);
        degrees[v]--;
        degrees[w]--;
        E--;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public int degree(int v) {
//        validateVertex(v);
        return adj[v].size();
    }

    /**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * {@code for (Edge e : G.edges())}.
     *
     * @return all edges in this edge-weighted graph, as an iterable
     */
//    public Iterable<Edge> edges() {
//        LinkedList<Edge> list = new LinkedList<Edge>();
//        for (int v = 0; v < V; v++) {
//            int selfLoops = 0;
//            for (Edge e : adj(v)) {
//                if (e.other(v) > v) {
//                    list.add(e);
//                }
//                // only add one copy of each self loop (self loops will be consecutive)
//                else if (e.other(v) == v) {
//                    if (selfLoops % 2 == 0) list.add(e);
//                    selfLoops++;
//                }
//            }
//        }
//        return list;
//    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
//    private void validateVertex(int v) {
//        if (v < 0 || v >= V)
//            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
//    }

    public Iterator<Integer> adj(int v) {
        return adj[v].iterator();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            Set adj_list = adj[v];
            Iterator it = adj_list.iterator();
            while (it.hasNext()) {
                s.append(it.next()+ "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}
