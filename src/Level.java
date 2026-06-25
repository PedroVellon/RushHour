import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;

public class Level {

    public char[] board;
    Vehicle [] vehiclesOnBoard;
    
    /* Level Constructor (levelString)
     * * - What it does:
     * Creates a Level object from a String.
     * * - Why it was implemented:
     * Used when initializing a Level for the first time.
     */
    public Level (String levelString) {
        board = levelString.toCharArray();
    }

    /* Level Constructor (board, vehiclesOnBoard)
     * * - What it does:
     * Creates a Level object from a board array and a vehicles array.
     * * - Why it was implemented:
     * Used in the makeMove method of the Vehicle class to create a copy 
     * of the board without having to reinitialize the vehicles.
     * */
    public Level (char [] board, Vehicle[] vehiclesOnBoard) {
        this.board = board;
        this.vehiclesOnBoard = vehiclesOnBoard;
    }

    /* Method verify
     * * - What it does:
     * Verifies that this level's board meets the following 
     * requirements and returns a corresponding code:
     * * - (0) If everything is correct.
     * * - (1) If the string length does not correspond to a 6x6 level.
     * * - (2) If the string contains invalid characters (not ('A'-'Z' or 'o')).
     * * - (3) If the red vehicle ('A') is not in the string.
     * * - (4) If the red vehicle is not in the exit row.
     * * - (5) If the red vehicle is not positioned horizontally.
     * * - (6) Vehicle with a length greater than 3 or less than 2.
     * * - (7) If there are duplicated vehicles with the same letter.
     * * - Why it was implemented:
     * It is one of the main functionalities of the program and is
     * necessary to ensure the input board is valid.
     */
    public int verify () {

        int result = 0;

        // If the board has an incorrect length (1)
        if (board.length != 36) result = 1;
        
        else {

            // Initialize the set of vehicles where we will add all
            // found vehicles. Used to detect the duplicated vehicles issue (6) 
            // and to detect if there is a car A on the board (3)
            Set<Character> hashVehicles = new HashSet<>();
            
            LinkedList<Vehicle> listVehicles = new LinkedList<>();

            // Initialize "boardAux" and copy the board content into it.
            // We need this auxiliary board since we are going to modify its content.
            char[] boardAux = new char[36];
            System.arraycopy(board, 0, boardAux, 0, 36);
            
            int i = 0;

            while (i < 36) {
                
                if ((boardAux[i] < 'A' || boardAux[i] > 'Z') 
                    && boardAux[i] != 'o') {
                    result = 2;
                    break;
                }
                
                // Check that car A is in the exit row = 2 (4)
                if (boardAux[i] == 'A' && (i < 12 || i > 17)) {
                    result = 4;
                    break;
                }

                // Check that the element is a vehicle
                if (boardAux[i] >= 'A' && boardAux[i] <= 'Z') {
                      
                    char vehicle = boardAux[i];

                    // If we cannot add the vehicle to the set, it is because 
                    // it is duplicated (7)
                    if (hashVehicles.add(vehicle) == false) {
                        result = 7;
                        break;
                    }

                    // We will use these two flags to determine the vehicle's orientation
                    boolean rightFlag = true;
                    boolean downFlag = true;
                        
                    // Record the vehicle size to control that it is not less than 2 or greater than 3 (6)
                    int vehicleSize = 1;

                    // We will use these two variables to iterate over the vehicle
                    int horizontal_i = i;
                    int vertical_i = i;
                    
                    // Create a vehicle object
                    Vehicle vehicleAux;

                    // Create an array for the vehicle's positions
                    LinkedList<Integer> positionsList = new LinkedList<>();
                    int [] positionsArray;

                    positionsList.add(i);
                    boardAux[i] = 'o';

                    char orientation = ' ';

                    while (rightFlag == true || downFlag == true) {

                        // Check that the right flag is true and that horizontal_i 
                        // can be incremented without going out of bounds
                        if (rightFlag == true && ++horizontal_i < 36
                        && i / 6 == horizontal_i / 6) {

                            // If the next position to the right matches the vehicle
                            if (boardAux[horizontal_i] == vehicle) {

                                // Update vehicle size
                                vehicleSize++;
                                    
                                // Replace the cell with a space to avoid visiting it again
                                boardAux[horizontal_i] = 'o';
                                    
                                // The vehicle is placed horizontally, so we don't check vertically
                                downFlag = false;

                                orientation = 'h';

                                // Assign the vehicle position
                                positionsList.add(horizontal_i);
                                
                            }

                            // Otherwise, the vehicle is not placed horizontally.
                            else rightFlag = false;
                        }
                            
                        // Check that the down flag is true and that vertical_i 
                        // can be incremented without going out of bounds
                        else if (downFlag == true && (vertical_i += 6) < 36) {

                            
                            // If the next position downwards matches the vehicle
                            if (boardAux[vertical_i] == vehicle) {

                                // Update vehicle size
                                vehicleSize++;

                                // Replace the cell with a space to avoid visiting it again
                                boardAux[vertical_i] = 'o';

                                // The vehicle is placed vertically, so we don't check horizontally
                                rightFlag = false;

                                // If it is car A, it cannot be placed vertically (5)
                                if (vehicle == 'A') result = 5;

                                orientation = 'v';
                                
                                // Assign the vehicle position
                                positionsList.add(vertical_i);
                                
                            }
                            // Otherwise, the vehicle is not placed vertically.
                            else downFlag = false;
                        }
                            
                        // If we cannot check either right or down, set both flags to false.
                        else {
                            rightFlag = false;
                            downFlag = false;
                        }
                    }

                    // Check that the vehicle size is not less than 2 or greater than 3 (6)
                    if (vehicleSize < 2 || vehicleSize > 3) {
                        result = 6;
                        break;
                    }
                        
                        
                    // If car A has a length other than 2
                    if (vehicle == 'A' && vehicleSize != 2) {
                        result = 3;
                        break;
                    }  

                    // Pour the positions from the list to the array
                    positionsArray = new int [positionsList.size()];
                    for (int j = 0; j < positionsList.size(); j++) {
                        positionsArray[j] = positionsList.get(j);
                    } 

                    vehicleAux = new Vehicle(vehicle, positionsArray, orientation);

                    listVehicles.add(vehicleAux);
                }

                // Loop increment
                i++;
                
            }

            // Check that the board contains car A (3)
            if (result == 0 && hashVehicles.contains('A') == false) result = 3;    

            if (result == 0) {
                
                vehiclesOnBoard = new Vehicle[listVehicles.size()];
                
                Collections.sort(listVehicles);

                for (int j = 0; j < listVehicles.size(); j++) {
                    vehiclesOnBoard[j] = listVehicles.get(j);
                    vehiclesOnBoard[j].positionInArray = j;
                }
            }
        }

        return result;
    }

    /* Method successors
     *
     * - What it does:
     * Returns a list of successors for the current board state.
     * To do this, it uses the getSuccessors method of each vehicle 
     * on the board.
     * * - Why it was implemented:
     * It is one of the queries the program can receive.
     */
    public LinkedList<Node> successors() {
        
        LinkedList<Node> successors = new LinkedList<>();

        for (Vehicle vehicleAux : vehiclesOnBoard) {
            
            // Add all the vehicle's successors to the successors list.
            successors.addAll(vehicleAux.getSuccessors(this));
        }

        return successors;
    }

    /* Method whereis
     *
     * - What it does:
     * Receives a vehicle identifier and returns a message with its 
     * positions on the board.
     * * - Why it was implemented:
     * It is one of the queries the program can receive. Implemented 
     * inside this class since the question is about the board.
     */
    public String whereis (char vehicle) {

        String result = "";

        for (Vehicle vehicleAux : vehiclesOnBoard) {
            if (vehicleAux.id == vehicle) {

                for (int position : vehicleAux.positions) {

                    result = result.concat("("+ position/6 +","+ position%6 +")");
                }
            }
        }

        if (result.isEmpty()) result = "Vehicle not found " + vehicle;

        return result;
    }

    /* Method what
     *
     * - What it does: Returns a String with the content of a cell.
     * * - Why it was implemented: 
     * It is one of the queries the program can receive.
     */
    public String what (String cell) {

        String result = "";

        String arrayCells[] = cell.split(",");

        int row = Integer.parseInt(arrayCells[0]);
        int column = Integer.parseInt(arrayCells[1]);

        if (row < 0 || row >= 6 || column < 0 || column >= 6) {
            result = "Invalid cells entered " + cell;
        }
        else {
            result = "" + board[(row * 6)+ column];
        }

        return result;
    }
    
    /* Method size
     *
     * - What it does: 
     * Returns a message with the size of a vehicle. If the vehicle 
     * is not found, returns a different message.
     * * - Why it was implemented:
     * It is one of the queries the program can receive.
     */
    public String size (char vehicle) {
    
        int vehicleSize = 0;
        String result = "";

        for (Vehicle auxVehicle : vehiclesOnBoard) {
            if (auxVehicle.id == vehicle) {
                vehicleSize = auxVehicle.positions.length;
            }
        }
        
        // If its size is 0, it is not on the board
        if (vehicleSize == 0) result = "Vehicle not found";
        
        else result = result.concat("" + vehicleSize);
  
        return result;
    }  

    /* Method howmany
     *
     * - What it does:
     * Returns a message with the number of vehicles on the board. 
     * * - Why it was implemented:
     * It is one of the queries the program can receive.
     */ 
    public String howmany() {
        return "" + vehiclesOnBoard.length;
    }

    /* Method goal
     *
     * - What it does:
     * Determines if the current position of vehicle A on the board matches 
     * the solution (3,5) and returns a boolean response.
     * * - Why it was implemented:
     * It is one of the queries the program can receive.
     */
    public boolean goal() {
        
        boolean result = false;

        // If the last position of vehicle A (the first in the array) is at index 17
        if (vehiclesOnBoard[0].positions[vehiclesOnBoard[0].positions.length - 1] == 17) {
            result = true;
        }

        return result;
    }

    /* Method move
     *
     * - What it does:
     * Receives an array of Strings formatted as "VehicleId, Direction, Value"
     * and performs the resulting moves on the board. 
     * * - Why it was implemented:
     * It is one of the queries the program can receive.
     */
    public String move (String [] moves) {

        char vehicleId;
        char direction;
        int move;

        String result = "";

        // Initialize an empty node and assign this level as state
        Node nodeAux = new Node(this);

        int i = 0;

        while (i < moves.length) {

            Vehicle vehicleAux = null;
            String moveString = moves[i];

            // Decode the move
            vehicleId = moveString.toCharArray()[0];
            direction = moveString.toCharArray()[1];
            move = Integer.parseInt(moveString.substring(2));

            // Search for the vehicle in the vehicle list
            int j = 0;
            while (j < nodeAux.state.vehiclesOnBoard.length) {
                if (nodeAux.state.vehiclesOnBoard[j].id == vehicleId) {
                    vehicleAux = new Vehicle(nodeAux.state.vehiclesOnBoard[j]);
                    break;
                }
                j++;
            }

            if (vehicleAux != null) {

                if (direction == '+' || direction == '-') {

                    // If the direction is negative and the orientation is horizontal or if the
                    // direction is positive and the orientation is vertical, then the move
                    // will have a negative value.
                    if ((direction == '-' && vehicleAux.orientation == 'h')
                        || (direction == '+' && vehicleAux.orientation == 'v'))  {
                        move = 0 - move;
                    }

                    // If the vehicle can move on the board
                    if (vehicleAux.canMove(nodeAux.state, move)) {

                        // Perform the move
                        nodeAux = vehicleAux.makeMove(nodeAux.state, move);
                    }

                    // If one of the moves cannot be performed, execution stops
                    else {
                        result = "Could not perform the following move: " + moveString;
                        break;
                    }
                }

                else {
                    result = "Invalid direction: " + moveString.toCharArray()[1];
                    break;
                }

            i++;
            
            }
            else {
            result = "Vehicle " + vehicleId + " not found on the board";
            break;
            }
        }

        // If all moves were performed successfully, return the final board state.
        if (i == moves.length) {
            result = nodeAux.state.toString();
        }
        

        return result;
    }

    /* Method solver
     *
     * - What it does:
     * Implements a graph search algorithm following a strategy
     * (BFS, DFS, UCS, GBF, AStar) and returns a SearchResult containing the 
     * solution path (if found) and the statistics generated during the search
     * (execution time, total nodes, expanded nodes, pruned nodes, and 
     * max depth reached).
     * * - Why it was implemented:
     * This method contains the core functionality of milestone 3.
     */
    public SearchResult solver (String strategy, int maxDepth, int heuristic) {

        LinkedList<Node> path = new LinkedList<>();
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        HashMap<String, Integer> closedList = new HashMap<>();

        long t0 = 0;
        long t1 = 0;
        
        int totalNodes = 0;
        int cutNodes = 0;
        int expandedNodes = 0;
        int maxDepthReached = 0;
        long totalExecutionTime = 0;

        int nodeId = 0;

        // Create and add the first node to the frontier (root)
        if (strategy.equals("BFS") || strategy.equals("DFS") || strategy.equals("UCS")) {
        frontier.add(new Node(nodeId++, null, "___", this, 0, 0, 0, 0));
        }

        // If it's an informed search strategy, calculate its heuristic
        else {
        int initialNodeHeuristic = this.calculateHeuristic(heuristic);
        frontier.add(new Node(nodeId++, null, "___", this, 0, 0, initialNodeHeuristic, initialNodeHeuristic));
        }

        totalNodes++;

        t0 = System.nanoTime();
        while (true) { 
            
            // If the frontier is empty, stop execution
            if (frontier.isEmpty()) {
                break;
            }

            // Poll the first node from the frontier
            Node currentNode = frontier.poll();

            // If the node is the goal, stop execution
            if (currentNode.state.goal() == true) {
                t1 = System.nanoTime();
                path = currentNode.path();
                break;
            }

            // If the node's depth is greater than the allowed depth, do not expand it
            if (currentNode.depth >= maxDepth && maxDepth != -1) {

                // Increment total pruned nodes
                cutNodes++;
                continue; 
            }

            boolean cut = false;
            
            if (closedList.containsKey(currentNode.state.toString())) {

                int storedValue = closedList.get(currentNode.state.toString());

                // If the strategy is DFS, the value is negative
                if (strategy.equals("DFS") && storedValue >= currentNode.value) {

                    cutNodes++;
                    cut = true;

                } else if (!strategy.equals("DFS") && storedValue <= currentNode.value) {

                    cutNodes++;
                    cut = true;
                }
            }
            
            // If the node was not pruned
            if (cut == false) {

                // Expand it
                closedList.put(currentNode.state.toString(), currentNode.value);

                // Increment total expanded nodes
                expandedNodes++;

                // Create the successors list and successor nodes
                LinkedList<Node> successors = currentNode.state.successors();

                for (Node successorAux : successors) {

                    successorAux.id = nodeId++;
                    successorAux.heuristic = 0;
                    successorAux.parentNode = currentNode;
                    successorAux.depth = currentNode.depth+1;
                    successorAux.cost += currentNode.cost;

                    switch (strategy) {

                        
                        case "BFS" : // For BFS, the value equals depth
                            
                            successorAux.value = currentNode.depth+1;

                        break;

                        case "DFS" : // For DFS, the value equals negative depth

                            successorAux.value = -(successorAux.depth);

                        break;

                        case "UCS" : // For UCS, the value equals cost

                            successorAux.value = successorAux.cost;

                        break;

                        case "GBF": // Value equals heuristic
                            successorAux.heuristic = successorAux.state.calculateHeuristic(heuristic);
                            successorAux.value = successorAux.heuristic;
                        break;

                        case "AStar": // Value equals heuristic + cost
                            successorAux.heuristic = successorAux.state.calculateHeuristic(heuristic);
                            successorAux.value = successorAux.cost + successorAux.heuristic;
                        break;

                    }
                    
                    // Add the successor sorted into the frontier
                    frontier.add(successorAux);

                    // Increment the number of total nodes
                    totalNodes++;   
                }

                // If the current depth is greater than the max reached depth
                if (!successors.isEmpty() && currentNode.depth + 1 >= maxDepthReached) {
                    maxDepthReached = currentNode.depth + 1;
                }
            
            }

        }

        totalExecutionTime = (t1 - t0) / 1000; 

        SearchResult searchResult = new SearchResult(path, totalNodes, cutNodes, 
        expandedNodes, maxDepthReached, totalExecutionTime);

        return searchResult;
    }

    /* Method calculateHeuristic
     * * - What it does:
     * Calculates a heuristic for the board state based on an integer.    
     * * - Why it was implemented:
     * Necessary to calculate state heuristics for nodes in 
     * informed search strategies (GBF, AStar).
     */
    public int calculateHeuristic (int heuristic) {

        int result = 0;

        // Car A is always the first one and has length 2
        int i = vehiclesOnBoard[0].positions[1];

        switch (heuristic) {
            
            case 0:
                result = 17 - i;
            break;

            case 1:
            // Check horizontal cars
                i++;
                char previousCar = 0;
                while (i <= 17) {
                    if (board[i] != 'o' && board[i] != previousCar) {
                        result++;
                        previousCar = board[i];    
                    }
                    i++;
                }
            break;

            case 2:
                result = calculateHeuristic(0) + calculateHeuristic(1);
            break;
        }

        return result;
    }

    /* Method toString
     *
     * - What it does:
     * Returns the board state as a String.
     * * - Why it was implemented:
     * To output the board state as a String when needed.
     */
    @Override
    public String toString () {
        
        return new String(board);
    }
}