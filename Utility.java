import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
*
*   The class is Utility class used by Approx, BnB
*   It writes .sol and .trace
*
* */

public class Utility {

    public void writeSol(FileWriter solWriter, int bestSolutionNumber, List<Integer> solutionVertexList) {
        try {
            solWriter.write(bestSolutionNumber + "\n");
            String vertexString = solutionVertexList.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            solWriter.write(vertexString);

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("open Writer error");
        }

    }

    public void writeTrace(FileWriter traceWriter, double usedTimeSec, int bestSolutionNumber) throws IOException {
        try {
            traceWriter.write(String.format("%.4f",usedTimeSec)  + "," + bestSolutionNumber + "\n");

        } catch (Exception e) {
            System.out.println("open Writer error ============");
            e.printStackTrace();
        }
    }

}