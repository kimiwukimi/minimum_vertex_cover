import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/* This is the main driver file. Arguments are parsed in the main function and then the graph is parsed  in ConstructGraph. Data structures are defined in separate file. 
Then, the switch case redirects the control to other files based on what algorithm is to be implemented. */

public class Mvc {

    public static void main(String[] args) throws IOException {
//        exec -inst[0] <filename>[1] -alg[2] [BnB|Approx|LS1|LS2][3] -time[4] <cutoff in seconds>[5] -seed[6] <random seed>[7]

        String fileName = "";
        String alg = "";
        String time_cutoff = "";
        String seed_number = "";

        for (int i = 0; i < args.length - 1; i++) { //parsing arguments
            if (args[i].equals("-inst"))
                fileName = args[i + 1];
            if (args[i].equals("-alg"))
                alg = args[i + 1];
            if (args[i].equals("-time"))
                time_cutoff = args[i + 1];
            if (args[i].equals("-seed"))
                seed_number = args[i + 1];
        }

        if (fileName.equals("") || alg.equals("") || time_cutoff.equals("")) { //check correctness of algorithms
            System.out.println("Incorrect or missing arguments");
            System.exit(0);
        }

        if ((alg.equals("LS1") || alg.equals("LS2")) && seed_number.equals("")) { //ensures BnB can run without seed
            System.out.println("Incorrect or missing arguments");
            System.exit(0);
        }


        System.out.printf("args = %s %s %s %s \n", fileName, alg, time_cutoff, seed_number);


        Graph G;
        switch (alg) { //calls appropriate algorithm
            case "BnB":
                G = constructGraph(fileName);
                BnB bnb = new BnB(G, fileName, time_cutoff);
                bnb.solve();
                break;

            case "Approx":
                G = constructGraph(fileName);
                Approx appr = new Approx(G, fileName, time_cutoff);
                appr.solve();
                break;

            case "LS1":
                G = constructGraph(fileName);
                LS1 hillclimb = new LS1(G, time_cutoff, seed_number, fileName);
                hillclimb.solve();
                break;

            case "LS2":
                Graph1 g = new Graph1();
                long cutOff = Long.parseLong(time_cutoff);
                long seed = Long.parseLong(seed_number);
                long start = System.currentTimeMillis();
                try {
                    g.constructGraph(new File(fileName));
                    SimulatedAnnealing sa = new SimulatedAnnealing(g, seed);
                    sa.run(cutOff, fileName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private static Graph constructGraph(String fileName) {
        String line = null;
        Graph G = null;

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int V, E, directed_flag;

            // read first line
            line = bufferedReader.readLine();
            String[] s = (line.split("\\s+"));
            V = Integer.parseInt(s[0]);
            E = Integer.parseInt(s[1]);


            G = new Graph(V + 1);     // +1 because vertices index start from 1

            int lineNumber = 1;     // records vertex number

            int count = 0;
            while (lineNumber <= V) { //iterating over each line
                line = bufferedReader.readLine();

                if (line == null || line.equals("")) {
                    lineNumber++;
                    continue;            //bug fix ==> do not let .graph end with two empty lines
                }
                s = (line.split("\\s+"));
                

                for (String node_number_in_string : s) {
                    int other_node = Integer.parseInt(node_number_in_string);
                    count++;
                    G.addEdge(lineNumber, other_node);  // lineNumber is current_node
                }

                lineNumber++;
            }

            System.out.println("V = " + V + ", E = " + E);
            System.out.println("G reads edges number: " + G.E());
            bufferedReader.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Cannot open file: " + fileName);
        } catch (IOException ex) {
            System.out.println("Unexpected error when reading file: " + fileName);
        }
        return G;
    }
}