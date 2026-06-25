import java.util.LinkedList;

public class Vehicle implements Comparable<Vehicle>{

    public char id;
    public int [] positions;
    public char orientation;
    public int positionInArray;

    /* Vehicle Constructor (vehicleAux)
     *
     * - What it does:
     * Creates a Vehicle object from an auxiliary vehicle.
     * * - Why it was implemented:
     * Used to copy Vehicle objects.
     */
    public Vehicle (Vehicle vehicleAux)  {
        this.id = vehicleAux.id;
        this.positions = new int [vehicleAux.positions.length];
        System.arraycopy(vehicleAux.positions, 0,this.positions, 0, vehicleAux.positions.length);
        this.orientation = vehicleAux.orientation;
        this.positionInArray = vehicleAux.positionInArray;
    }

    /* Vehicle Constructor (id, positions, orientation, positionInArray)
     *
     * - What it does:
     * Creates a Vehicle object with the incoming values.
     * * - Why it was implemented:
     * Used to instantiate Vehicle objects.
     */
    public Vehicle (char id, int [] positions, char orientation) {
        this.id = id;
        this.orientation = orientation;
        this.positions  = positions;
    }
    
    /* Method getSuccessors
     *
     * - What it does:
     * Returns a list of successor nodes from a level.
     * * - Why it was implemented:
     * It is necessary to get the successors of a board to perform searches.
     */
    public LinkedList<Node> getSuccessors (Level level) {

        LinkedList<Node> successors = new LinkedList<>();

        boolean positiveMove = true;
        boolean negativeMove = true;

        int move = 1;

        // While it can move
        while (positiveMove == true) {

                // If the vehicle can move to the right
                if (orientation == 'h' && canMove(level, move) == true) {      
                    successors.add(makeMove(level, move));
                }

                // If the vehicle can move upwards
                else if (orientation == 'v' && canMove(level, -move) == true) {
                    successors.add(makeMove(level, -move));
                }
            
                else positiveMove = false;
            
                move++;
        }

        move = 1;

        while (negativeMove == true) {

                // If the vehicle can move to the left
                if (orientation == 'h' && canMove(level, -move) == true) {      
                    successors.add(makeMove(level, -move));
                }
                
                 // If the vehicle can move downwards
                else if (orientation == 'v' && canMove(level, move) == true) {
                    successors.add(makeMove(level, move));
                }
            
                else negativeMove = false;   

            move++;
        }
        
        return successors;
    }
    
    /* Method canMove
     *
     * - What it does:
     * Determines if a move on a board is valid.
     * * - Why it was implemented:
     * Implemented to control access to the makeMove method,
     * ensuring the move can be performed before executing it.
     */
    public boolean canMove (Level level, int move) {

        boolean result = false;
        int i;

        if (orientation == 'h') {

            // If the car can move right (+)
            if (move >= 0 && positions[positions.length - 1] + move < 36
            && (positions[positions.length - 1] / 6 == (positions[positions.length - 1] + move) / 6)) {

                result = true;
                i = 1;
                 
                while (i <= move) {
                    if (level.board[positions[positions.length-1] + i] != 'o') {
                        result = false;
                        break;
                    }
                    i++;
                }
                
            }  

            // If the car can move left (-)
            else if (move <= 0 && positions[0] + move >= 0
            && (positions[0] / 6 == (positions[0] + move) / 6)) {
                result = true;
                i = -1;
                
                 
                while (i >= move) {
                    if (level.board[positions[0] + i] != 'o') {
                        result = false;
                        break;
                    }
                    i--;
                }
                
            }     
        }

        else if (orientation == 'v') {

            // If the car can move up (-)
            if (move <= 0 && positions[0] + move*6 >= 0) {

                result = true;
                
                i = -1;
                while (i >= move) {
                    if (level.board[positions[0] + i*6] != 'o') {
                        result = false;
                        break;
                    }
                    i--;
                }
                
            }
            
            // If the car can move down (+)
            else if (move >= 0 && positions[positions.length - 1] + move*6 < 36) {
                
                result = true;
                
                i = 1;
                while (i <= move) {
                    if (level.board[positions[positions.length - 1] + i*6] != 'o') {
                        result = false;
                        break;
                    }
                    i++;
                }
                
            }
            
        }
        
        return result;
    }

    /* Method makeMove
     *
     * - What it does:
     * Moves the vehicle a certain number of cells on the level's board and returns
     * a node with the action, its cost, and resulting state.
     * * - Why it was implemented:
     * Necessary to get the result of moving a vehicle on the board.
     */
    public Node makeMove (Level level, int move) {

        String nodeAction;
        Level nodeState;
        int nodeCost;

        // Make a copy of the board
        char [] boardAux = new char [36];
        System.arraycopy(level.board, 0, boardAux, 0, 36);

        // Make a copy of the vehicles list
        Vehicle vehiclesOnBoardAux [] = new Vehicle[level.vehiclesOnBoard.length];

        for (int j = 0; j < level.vehiclesOnBoard.length; j++) {
            vehiclesOnBoardAux[j] = new Vehicle(level.vehiclesOnBoard[j]);
        }

        // Replace the old vehicle position with empty cells
        for (int i = 0; i < positions.length; i++) boardAux[positions[i]] = 'o'; 

        if (orientation == 'h') {
            
            for (int i = 0; i < positions.length; i++) {

                // Perform the move
                boardAux[positions[i] + move] = id;
                
                // Update the positions attribute
                vehiclesOnBoardAux[positionInArray].positions[i] = positions[i] + move;
            }
            nodeAction = id + String.format("%+d", move);
        }
        else {
            
            for (int i = 0; i < positions.length; i++) {

                // Perform the move
                boardAux[positions[i] + move * 6] = id;
                
                // Update the positions attribute
                vehiclesOnBoardAux[positionInArray].positions[i] = positions[i] + move*6;
            }
            // upwards
            if (move < 0) {
                nodeAction = id + "+" + (move * -1);
            }
            else {
                nodeAction = id + "-" + move;
            }
        }
        nodeState = new Level(boardAux, vehiclesOnBoardAux);

        if (move > 0) nodeCost = 6 - move;
        
        else nodeCost = 6 + move;
        
        Node auxNode = new Node(nodeAction, nodeState, nodeCost);

        return auxNode;
    }   

    /* Method compareTo
     *
     * - What it does:
     * Compares this Vehicle with another and determines the alphabetical order.
     * * - Why it was implemented:
     * Used to sort the Level's vehiclesOnBoard list alphabetically.
     */
    @Override
    public int compareTo (Vehicle vehicleAux) {
        return this.id - vehicleAux.id;
    }

}