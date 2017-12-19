import java.util.*;

public class State implements Comparable<State>{
   // private int[][] board;
    private double XWinRate = 0;
    private double OWinRate = 0;
    private HashSet<Integer> nextStates = new HashSet<>(8);
    private HashSet<Integer> previousStates = new HashSet<>(8);
    private int hashCode;
    private int moves = 0;
    private int count = 1;

    public State(int[][] board) {
        if (board.length != 9 || board[0].length != 9) {
            System.err.println("Game size invalid");
        }
        /*
        this.board = new int[9][9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.board[i][j] = board[i][j];
                if(this.board[i][j]!=0) this.moves++;
            }
        }
        */
       // System.out.println("Copied board:");
        //printBoard();
        hashCode=State.computeHashCode(board);

    }

    public static void printBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 2) System.out.print("O ");
                else if (board[i][j] == 1) System.out.print("X ");
                else System.out.print("* ");
                if ((j + 1) % 3 == 0 && j != 8) System.out.print("| ");
            }
            System.out.println();
            if ((i + 1) % 3 == 0 && i != 8) System.out.println("---------------------");

        }
    }

    public void addPreviousState(State input)
    {
        if (!previousStates.contains(input.getGoodHashCode()))
        {
            previousStates.add(input.getGoodHashCode());
        }
        /*
        if(previousStates.size()>1)
        {
            printPreviousStates();
        }
        */
    }

    public void addNextState(State input)
    {
        if (!nextStates.contains(input.getGoodHashCode()))
        {
            /*
            System.out.println("Adding:");
            input.printBoard();
            System.out.println("To:");
            printBoard();
            System.out.println();
            */
            nextStates.add(input.getGoodHashCode());
        }
       // System.out.println(nextStates.size());
    }

    public static int computeHashCode(int[][] board)
    {
        String concat="";
        int numX=0;
        int numY=0;
        for(int i=0;i<board.length;i++ )
        {
            for(int j=0; j<board[i].length; j++)
            {
                if(board[i][j]==1) numX++;
                else if(board[i][j]==1) numY++;
                concat=concat+Integer.toString(board[i][j]);
            }
        }
        //return Arrays.deepHashCode(board);
        //printBoard();
        //System.out.println(concat);
        //System.out.println(concat.hashCode());
        return concat.hashCode()/((numX*numY)+1);
        //return (concat.hashCode()/((numX*numY)+1))*Arrays.deepHashCode(board);
    }

   /* public int[][] getBoard() {
        return board;
    }
    */

    public double getXWinRate() {
        return XWinRate;
    }

    public double getOWinRate() {
        return OWinRate;
    }

    public void setXWinRate(double xwinRate) {
        XWinRate = xwinRate;
    }

    public void setOWinRate(double OWinrate) {
        this.OWinRate = OWinrate;
    }

    public HashSet<Integer> getNextStates() {
        return nextStates;
    }

    public HashSet<Integer> getPreviousStates() {
        return previousStates;
    }

    public int getGoodHashCode() {
        return hashCode;
    }

    public int getCount()
    {
        return count;
    }
    public void incrementCount()
    {
        count++;
    }

    public void printNextStates()
    {
        Iterator<Integer> it = nextStates.iterator();
        System.out.println("Next States: " +nextStates.size());
        while(it.hasNext())
        {
            Integer next = it.next();
            //State.printBoard(Tree.getNodes().get(next).getBoard());
            //System.out.println("Times: " + next.getValue().getCount());
            System.out.println("OWinRate: " + Tree.getNodes().get(next).getOWinRate());
            System.out.println("XWinRate: " + Tree.getNodes().get(next).getXWinRate());
            System.out.println();
        }
    }
    public void printPreviousStates()
    {
        System.out.println("Current state:");
        //State.printBoard(this.getBoard());
        Iterator<Integer> it = previousStates.iterator();
        System.out.println("Previous States: " + previousStates.size());
        while(it.hasNext())
        {
            Integer next = it.next();
            //Tree.getNodes().get(next).printBoard();
            //State.printBoard(Tree.getNodes().get(next).getBoard());
            System.out.println("Times: " + Tree.getNodes().get(next).getCount());
            System.out.println();
        }
    }
/*
    public void setBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }
*/
    public int getMoves() {
        return moves;
    }

    @Override
    public int compareTo(State o) {
        if(o.getMoves()>this.moves) return 1;
        else if(o.getMoves()<this.moves) return -1;
        else return 0;
    }
}
