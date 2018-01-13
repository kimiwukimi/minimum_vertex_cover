/*
*
*   The class is Edge class used by Approx, BnB, and LS1
*
* */

public class Edge {
    public int v, w;

    public Edge(int v, int w) {
        this.v = v;
        this.w = w;
    }

    public int either() {
        return v;
    }

    public int other(int vertex) {
        if (vertex == v) return w;
        else return v;
    }

    public String toString() {
        return String.format("%d %d", v, w);
    }
}
