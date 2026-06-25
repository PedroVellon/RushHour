import java.util.LinkedList;

public class RushHour {

    public static void main(String[] args) {
        
        if (args.length >= 3) {

            Level level;
            
            String boardString = "";
            String action = "";
            String question = "";
            String questionArg = "";
            String strategy = "";
            int maxDepth = -1; // default -1 (infinite)
            boolean stats = false;
            int heuristic = -1;

            int i = 0;
            
            try {

                try {

                    while (i < args.length) {

                        switch (args[i]) {

                            case "-s": 
                                boardString = args[i+1];
                            break;

                            case "verify":
                            case "question":
                            case "successors":
                            case "solver":
                                action = args[i];
                            break;
                            
                            case "--howmany":
                            case "--goal":
                                question = args[i];
                            break;

                            case "--what":
                            case "--size":
                            case "--move": 
                            case "--whereis":
                                question = args[i];
                                questionArg = args[i+1];
                            break;

                            case "--strategy":
                                strategy = args[i+1];
                            break;

                            case "--depth":
                                maxDepth = Integer.parseInt(args[i+1]);
                            break; 

                            case "--stats":
                                stats = true;
                            break;

                            case "--heuristic":
                                heuristic = Integer.parseInt(args[i+1]);

                        }
                        i++;
                    }

                    if (!boardString.equals(""))  {
                        level = new Level(boardString);
                        performAction(level, action, question, questionArg, strategy, maxDepth, stats, heuristic);
                    }
                    
                    else 
                        System.out.println("No board detected");

                } catch (NumberFormatException n) {
                    System.out.println("Incorrect argument syntax "  + n.getMessage());
                }
                
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Incorrect argument syntax "  + e.getMessage());
            }
        }
        else System.out.println("Not enough arguments");
    }

    /* Method performAction
    * * - What it does:
    * Redirects the program flow according to the provided arguments.
    * * - Why it was implemented:
    * It could be done in the main method, but it was extracted to modularize the code.
    */
    public static void performAction (Level level, String action, String question, 
        String questionArg, String strategy, int maxDepth, boolean stats,
        int heuristic) {

        int result;
        String msg = "";

            result = level.verify();

            if (result != 0) System.out.println(result);

            else {

                switch (action) {

                    case "verify" : 
                        System.out.println(result);
                    break;
                    
                    
                    case "question" :

                        switch (question) {

                            case "--howmany" : 
                                msg = level.howmany();
                            break;

                            case "--what" : 
                               msg = level.what(questionArg);
                            break;

                            case "--whereis" : 
                                msg = level.whereis(questionArg.toCharArray()[0]);
                            break;

                            case "--size" : 
                                msg = level.size(questionArg.toCharArray()[0]);
                            break;

                            case "--goal" :
                                msg = String.valueOf(level.goal());
                                msg = msg.toUpperCase();
                            break;

                            case "--move" :
                                msg = level.move(questionArg.split(","));
                            break;

                            default : 
                                msg = "Question syntax error";
                        }

                        System.out.println(msg);
                        
                    break;
                    
                    case "successors" :

                        LinkedList<Node> successors = level.successors();

                        if (!successors.isEmpty()) 
                            for (Node successor : successors) System.out.println
                            ("[" + successor.action + ", " + successor.state.toString() + ", " + successor.cost + "]");
                        
                        else System.out.println("The level has no successors");

                    break;
                    
                    
                    case "solver" :
                        

                        // If strategy is not DFS or max depth is greater than 0
                        if (!strategy.equals("DFS") || maxDepth > 0) {


                        // If strategy is neither GBF nor AStar, or the heuristic is not known
                            if ((!strategy.equals("GBF") && !strategy.equals("AStar")) || (heuristic >= 0 && heuristic < 3)) {

                                if (strategy.equals("DFS") || strategy.equals("BFS") || strategy.equals("UCS")
                                    || strategy.equals("GBF") || strategy.equals("AStar")) {
                                        
                                        SearchResult searchResult;

                                        if (strategy.equals("BFS") || strategy.equals("UCS")) 
                                            searchResult = level.solver(strategy, -1, heuristic);
                                        
                                        else if (strategy.equals("DFS")) 
                                            searchResult = level.solver(strategy, maxDepth, heuristic);
                                        
                                        else 
                                            searchResult = level.solver(strategy, -1, heuristic);

                                        if (!searchResult.path.isEmpty()) {

                                            for (Node auxNode : searchResult.path) 
                                                System.out.println(auxNode.toString());

                                            if (stats == true) {
                                                System.out.println("\nET: " + searchResult.totalExecutionTime);
                                                System.out.println("TN: " + searchResult.totalNodes);
                                                System.out.println("EN: " + searchResult.expandedNodes);
                                                System.out.println("CN: " + searchResult.cutNodes);
                                                System.out.println("DF: " + searchResult.maxDepthReached);
                                            }
                                        }
                                
                                    else System.out.println("No solution found");
                                }
                                else System.out.println("Invalid search strategy: " + strategy);
                            }
                            else System.out.println("Invalid heuristic for " + strategy + ": " + heuristic);
                        }
                        else System.out.println("Invalid maximum depth for DFS: " + maxDepth);     

                    break;
      
                    default : System.out.println("Option syntax error");
                
            }
        }
    }
}