import java.util.LinkedList;

public class SearchResult {
    
    public LinkedList<Node> path;

    // Statistics values
    public int totalNodes;
    public int cutNodes;
    public int expandedNodes;
    public int maxDepthReached;
    public long totalExecutionTime;

    /* SearchResult Constructor (path, totalNodes, cutNodes, expandedNodes,
     * maxDepthReached, totalExecutionTime)
     * * - What it does: 
     * Initializes a SearchResult object with all its attributes.
     * * - Why it was implemented:
     * To effectively return the results of the solver method, containing
     * both the statistics and the solution path within the same object.
     */
    public SearchResult (LinkedList<Node> path, int totalNodes, int cutNodes, 
        int expandedNodes, int maxDepthReached, long totalExecutionTime) {

            this.path = path;
            this.totalNodes = totalNodes;
            this.cutNodes = cutNodes;
            this.expandedNodes = expandedNodes;
            this.maxDepthReached = maxDepthReached;
            this.totalExecutionTime = totalExecutionTime;
    }

}