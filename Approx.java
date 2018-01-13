import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
*
*   The class will use 2-approximation Algorithm and output minimum vertex number
*
* */

public class Approx {
    private Graph G;
    private int time_cutoff;
    private long startTime;
    private long endTime;
    private String fileName;
    private boolean writeSolTrace;
    public boolean[] visited;
    private FileWriter traceWriter;
    private FileWriter solWriter;


    public Approx(Graph G, String fileName, String time_cutoff) {
        this(G, fileName, time_cutoff, true);   // defaut to write .sol and .trace
    }
    public Approx(Graph G, String fileName, String time_cutoff, boolean writeSolTrace) {
        this.G = G;
        this.time_cutoff = Integer.parseInt(time_cutoff); // cast to int and transfer to milli-second
        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + this.time_cutoff * 1000;
        int end  = fileName.indexOf(".graph");
        int start = fileName.lastIndexOf("/");
        this.fileName = fileName.substring(start+1, end);
        this.writeSolTrace = writeSolTrace;
    }

    public int solve() {

        // use array to record if visited
        boolean visited[] = new boolean[G.V];

        Iterator<Integer> iter;

        // check from node 0, but node 0 is actually empty, because node index start from 1
        for (int u = 0; u < G.V(); u++) {

            if (!visited[u]) {
                iter = G.adj[u].iterator();
                while (iter.hasNext()) {
                    int v = iter.next();    // name next vertex to be v
                    if (!visited[v]) {
                        visited[u] = visited[v] = true;
                        break;
                    }
                }
            }
        }

        if (writeSolTrace) {
            String baseName = System.getProperty("user.dir") + "/" + this.fileName + "_Approx_" + Integer.toString(this.time_cutoff);
            String solFileName = baseName + ".sol";
            String traceFileName = baseName + ".trace";

            Utility u = new Utility();
            try {

                // write solution
                solWriter = new FileWriter(solFileName, false);    // do not append, rewrite instead
                int bestSolutionNumber = this.getMVCnumbers(visited);
                List<Integer> solutionVertexList = addSolutionVertexToList(visited);
                u.writeSol(solWriter, bestSolutionNumber, solutionVertexList);
                solWriter.close();

                // write trace
                traceWriter = new FileWriter(traceFileName, false);
                double usedTimeSec = (System.currentTimeMillis() - this.startTime) / 1000.0;
                u.writeTrace(traceWriter, usedTimeSec, bestSolutionNumber);
                traceWriter.close();

            } catch (Exception e) {
                System.out.println("Write sol error.=====");
                e.printStackTrace();
            }
        }


        // Print the vertex cover
//        System.out.println("Appr vertex cover is:");
//        for (int j = 0; j < G.V(); j++)
//            if (visited[j])
//                System.out.print(j + " ");
//        System.out.println();
        int vertexCoverNumber = this.getMVCnumbers(visited);
        System.out.println("Appr of Minimum Vertex Cover Number is: " + vertexCoverNumber);

        long elpasedTime = System.currentTimeMillis() - this.startTime;
        System.out.println("Elapsed Time(ms): " + elpasedTime);
        return vertexCoverNumber;
    }

    private List<Integer> addSolutionVertexToList(boolean[] visited) {
        List<Integer> ls = new LinkedList<>();
        for (int j = 0; j < this.G.V(); j++)
            if (visited[j])
                ls.add(j);
        return ls;
    }

    private int getMVCnumbers(boolean[] visited) {
        int count = 0;
        for (Boolean b : visited) {
            if (b) {
                count++;
            }
        }
        return count;
    }

    public List<Integer> getCoveredList() {

        // use array to record if visited
        boolean visited[] = new boolean[G.V];

        Iterator<Integer> iter;

        // check from node 0, but node 0 is actually empty, because node index start from 1
        for (int u = 0; u < G.V(); u++) {

            if (!visited[u]) {
                iter = G.adj[u].iterator();
                while (iter.hasNext()) {
                    int v = iter.next();    // name next vertex to be v
                    if (!visited[v]) {
                        visited[u] = visited[v] = true;
                        break;
                    }
                }
            }
        }

        List<Integer> solutionVertexList = addSolutionVertexToList(visited);
        return solutionVertexList;
    }

}