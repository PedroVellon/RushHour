import java.util.LinkedList;

public class Node implements Comparable<Node>{

    int id;
    Node parentNode;
    String action;
    Level state;
    int cost;
    int depth;
    int heuristic;
    int value;
    
    /* Node Constructor (action, state, cost)
     * * - What it does:
     * Partially initializes a node object (not all attributes are given values).
     * * - Why it was implemented:
     * To partially initialize a node in the makeMove method. This node will
     * be used to print the successors output or to provide values to the rest of its
     * attributes in the solver.
     */
    public Node (String action, Level state, int cost) {
        this.action = action;
        this.state = state;
        this.cost = cost;
    }

    /* Node Constructor (state)
     * * - What it does:
     * Initializes a node object containing only a state.
     * * - Why it was implemented:
     * To initialize a node in the Level class move method and perform
     * the first move.
     */
    public Node (Level state) {
        this.state = state;
    }

    /* Node Constructor (id, parentNode, action, state, cost, depth, heuristic, value)
     * * - What it does:
     * Initializes a full node object.
     * * - Why it was implemented:
     * Used to initialize the first node of the search in the solver method.
     */
    public Node (int id, Node parentNode, String action, Level state, int cost, int depth, int heuristic, int value) {
        this.id = id;
        this.parentNode = parentNode;
        this.action = action;
        this.state = state;
        this.cost = cost;
        this.depth = depth;
        this.heuristic = heuristic;
        this.value = value;
    }
    
    /* Method path
     * * - What it does:
     * Returns the path of visited nodes to reach this node.
     * * - Why it was implemented:
     * To obtain the ordered sequence of visited nodes to reach
     * the solution.
     */
    public LinkedList<Node> path () {

        LinkedList<Node> pathNodes = new LinkedList<>();
        Node current = this;

        while (current != null) {
            pathNodes.addFirst(current); // Added at the beginning to leave the path ordered from the root
            current = current.parentNode;
        }

        return pathNodes;
    }

    /* Method toString
     * * - What it does:
     * Returns the node information as a String.
     * * - Why it was implemented:
     * To return the node information as a String when needed.
     */
    public String toString() {

        String msg = "";

        if (parentNode == null) 
            msg = "[" + id + ",none," + action + "," + state.toString() + ","
         + cost + "," + depth + "," + heuristic + "," + value + "]";
        

        else 
            msg = "[" + id + "," + parentNode.id + "," + action + "," + state.toString() + ","
         + cost + "," + depth + "," + heuristic + "," + value + "]";
        
        return msg;
    }

    /* Method compareTo
     *
     * - What it does:
     * Compares this Node with another and determines the order based on their value.
     * If values are equal, their ids are compared.
     * * - Why it was implemented:
     * Used to sort the PriorityQueue in the solver method.
     */
    public int compareTo(Node nodeAux) {

        int result;

        if (this.value != nodeAux.value) {
            result =  Integer.compare(this.value, nodeAux.value);
        } else {
            result = Integer.compare(this.id, nodeAux.id);
        }

        return result;
    }
    
}