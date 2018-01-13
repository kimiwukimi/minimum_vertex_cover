import java.util.Iterator;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.io.FileWriter;
import java.io.IOException;

/*
*
*   The class will use Branch and Bound Algorithm and output minimum vertex number
*
* */

public class BnB {
    private Graph G;
    private int time_cutoff;
    private int opt;
    private long startTime;
    private long endTime;
    private String fileName;
    private String filePath;
    private Set<Integer> covered;
    private Set<Integer> free;

    private FileWriter traceWriter;
    private FileWriter solWriter;

    public BnB(Graph G, String fileName, String time_cutoff) {
        this.G = G;
        this.time_cutoff = Integer.parseInt(time_cutoff); // cast to int and transfer to milli-second
        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + this.time_cutoff * 1000;
        this.filePath = fileName;
        int end = fileName.indexOf(".graph");
        int start = fileName.lastIndexOf("/");
        this.fileName = fileName.substring(start + 1, end);
        this.opt = Integer.MAX_VALUE;
    }

    public void solve() {

        try {
            // get appr lower bound
            Approx appr = new Approx(G, this.filePath, String.valueOf(this.time_cutoff), false);
            this.opt = appr.solve();
            System.out.println("Approx bound is: " + this.opt);
            System.out.println();
            writeInitialTraceAndSol(appr);

            String baseName = System.getProperty("user.dir") + "/" + this.fileName + "_BnB_" + Integer.toString(this.time_cutoff);
            String solFileName = baseName + ".sol";
            String traceFileName = baseName + ".trace";
            traceWriter = new FileWriter(traceFileName, true); // do not append, rewrite instead

            this.covered = new HashSet();
            this.free = new HashSet();
            solveMVC(G, this.opt);
            traceWriter.close();
            System.out.println("Min MVC is: " + this.opt);
        } catch (StackOverflowError e) {
            System.err.println("StackOverflowError! file = " + this.fileName + ".graph");
        } catch (Exception e) {
            System.out.println("Write sol error.=====");
            e.printStackTrace();
        }
    }

    public void writeInitialTraceAndSol(Approx approx_object) {
        String baseName = System.getProperty("user.dir") + "/" + this.fileName + "_BnB_" + Integer.toString(this.time_cutoff);
        String solFileName = baseName + ".sol";
        String traceFileName = baseName + ".trace";

        Utility u = new Utility();
        try {

            // write solution
            solWriter = new FileWriter(solFileName, false);    // do not append, rewrite instead
            int bestSolutionNumber = this.opt;
            List<Integer> solutionVertexList = approx_object.getCoveredList();
            u.writeSol(solWriter, bestSolutionNumber, solutionVertexList);
            solWriter.close();

            // write trace
            this.traceWriter = new FileWriter(traceFileName, false);
            double usedTimeSec = (System.currentTimeMillis() - this.startTime) / 1000.0;
            u.writeTrace(this.traceWriter, usedTimeSec, bestSolutionNumber);
            this.traceWriter.close();

        } catch (Exception e) {
            System.out.println("Write sol error.=====");
            e.printStackTrace();
        }
    }

    public void solveMVC(Graph G, int depth) {
//        System.out.println("Current opt:            " + this.opt);
        if (depth < 0) {
            return;
        }

        if (covered.size() > this.opt) {
            return;
        }

        if (this.G.E() == 0) {  // all edges are being covered
            if (covered.size() < this.opt) {
                this.opt = covered.size();
                System.out.println("New sol found:          " + this.opt);

                // write trace
                Utility u = new Utility();
                String baseName = System.getProperty("user.dir") + "/" + this.fileName + "_BnB_" + Integer.toString(this.time_cutoff);
                String solFileName = baseName + ".sol";
                String traceFileName = baseName + ".trace";

                try {

                    // write solution
                    solWriter = new FileWriter(solFileName, false);    // do not append, rewrite instead
                    int bestSolutionNumber = this.opt;
                    List<Integer> solutionVertexList = addSolutionVertexToList(this.covered);
                    u.writeSol(solWriter, bestSolutionNumber, solutionVertexList);
                    solWriter.close();  // when sees a better answer, reapeatedly write to solution file

                    // write trace
                    double usedTimeSec = (System.currentTimeMillis() - this.startTime) / 1000.0;
                    u.writeTrace(traceWriter, usedTimeSec, this.opt);
                    traceWriter.flush();


                } catch (Exception e) {
                    System.out.println("Write sol error.=====");
                    e.printStackTrace();
                }

                return;
            }
        }

        if (System.currentTimeMillis() > this.endTime) {
            try {
                traceWriter.close();
            } catch (Exception e) {
                System.out.println("close error.=====");
                e.printStackTrace();
            }

            System.exit(0);
        }


        int v = getHighestDegreeNode(this.G.degrees, this.covered, this.free);
        if (v == -1) {
//            System.out.println("reach leaf, return.");
            return;
        }
//        System.out.println();
//        System.out.println("v is: " + v);


        int w = -1;
        Set<Integer> adj_v = G.adj[v];
        for (Integer i : adj_v) {
            if (this.covered.contains(i)) {
                continue;
            }
            w = i;
            break;
        }
//        System.out.println(v + " " + w);
        if (w == -1) {
            System.out.println("w is -1  EXCEPTION!!!");
            return;
        }


        Set<Integer> copy_adj_v = new HashSet(adj_v);

//        printDegree();
        G.removeVertice(v);
//        printDegree();

        covered.add(v);

        solveMVC(G, depth - 1);      // **************

        covered.remove(v);
        G.addBackVertice(v, copy_adj_v);


        assert (w != -1);
        Set<Integer> adj_w = G.adj[w];
        Set<Integer> copy_adj_w = new HashSet(adj_w);

        // debug
//        printDegree();
        G.removeVertice(w);
//        printDegree();

        covered.add(w);
        solveMVC(G, depth - 1);      // **************
        covered.remove(w);
        G.addBackVertice(w, copy_adj_w);
    }

    private List<Integer> addSolutionVertexToList(Set<Integer> visited) {
        List<Integer> ls = new LinkedList<>();
        for (Integer i : visited)
            ls.add(i);
        return ls;
    }

    public int getTotalDegree() {
        int count = 0;
        for (int i : G.degrees)
            count += i;
        return count;
    }

    public int countTatalDegree(Set<Integer> covered, Set<Integer> free) {
        int count = 0;
        int[] degrees = this.G.degrees;
        for (int i = 1; i < degrees.length; i++) {
            if (covered.contains(i) || free.contains(i))
                continue;
            count += degrees[i];
        }
        return count;
    }

    public int getHighestDegreeNode(int[] degrees, Set<Integer> covered, Set<Integer> free) {
        int maxNode = -1;
        int maxNodeDegree = 0;
        for (int i = 1; i < degrees.length; i++) {

            if (covered.contains(i) || free.contains(i))    // if already covered, it is not highest degree candidate
                continue;
            int degree = degrees[i];
//            System.out.println(i + " node degree = " + degree);
            if (degree > maxNodeDegree) {
//                System.out.println("maxNode = " + i);
                maxNodeDegree = degree;
                maxNode = i;
            }
        }
        return maxNode;
    }

    public void printDegree() {
        for (int i = 1; i < G.degrees.length; i++) {
            System.out.println("V: " + i + " degrees is : " + G.degrees[i]);
        }
//        System.out.println();
    }

}
