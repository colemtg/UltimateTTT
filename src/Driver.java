import java.lang.management.PlatformLoggingMXBean;
import java.util.*;

public class Driver {
    public static void main (String args[])
    {
        Scanner input = new Scanner(System.in);
        System.out.println("enter number of simulations:");
        Tree tree = runTests(input.nextInt());

        //State startingState = new State(new int[9][9]);

        //State[] states = new State[82];
        //states[0] = new State(new char[9][9]);
        int[][] previous = new int[9][9];
        int[][] current = new int[9][9];

        int row,col;
        int playerX;
        int playerO;
        System.out.println("0 for comp vs comp\n1 for player vs player \n2 for player " +
                "vs comp go first \n3 for player vs comp go second");
        int in= input.nextInt();
        if(in == 0)
        {
            playerX=0;
            playerO=0;
        }
        else if(in == 1)
        {
            playerX=1;
            playerO=1;
        }
        else if(in == 2)
        {
            playerX=0;
            playerO=1;
        }
        else
        {
            playerX=1;
            playerO=0;
        }
        int targetBox=-1;
        int[] doneBoxes = new int[9];
        boolean invalid;
        for (int moves=1; moves<81+1; moves++)
        {
            invalid=true;
            //if (Tree.getNodes().containsKey(states[moves - 1].getGoodHashCode()))

            if (Tree.getNodes().containsKey(State.computeHashCode(previous)))
            {
                double maxX=0;
                Integer codeX=0;
                double maxO=0;
                Integer codeO=0;
                //Tree.getNodes().get(states[moves-1].getGoodHashCode()).printNextStates();
               // if(moves>1)
                //{

                Iterator<Integer> it = Tree.getNodes().get(State.computeHashCode(previous)).getNextStates().iterator();
                while (it.hasNext())
                {
                    Integer next = it.next();
                    if (moves % 2 == 1 && Tree.getNodes().get(next).getXWinRate() > maxX) {
                        maxX = Tree.getNodes().get(next).getXWinRate();
                        codeX = next;
                    }
                    if (moves % 2 == 0 && Tree.getNodes().get(next).getOWinRate() > maxO) {
                        maxO = Tree.getNodes().get(next).getOWinRate();
                        codeO = next;
                    }
                }
                if(moves % 2 == 0) //O
                {
                    System.out.println("best move:");
                    //Tree.getNodes().get(codeO).printBoard();
                    //System.out.println(Tree.getNodes().get(codeO).getGoodHashCode());
                    System.out.println(codeO);
                    System.out.println("O winrate: " + Tree.getNodes().get(codeO).getOWinRate());
                }
                if(moves % 2 == 1) //X
                {
                    System.out.println("best move:");
                    //Tree.getNodes().get(codeX).printBoard();
                    //System.out.println(Tree.getNodes().get(codeX).getGoodHashCode());
                    System.out.println(codeX);
                    System.out.println("X winrate: " + Tree.getNodes().get(codeX).getXWinRate());
                }
                //}
            }
            else
            {
                System.out.println("new state");
            }

            //states[moves-1].printBoard();
                State.printBoard(previous);
            while (invalid) {
                if (moves % 2 == 0) System.out.println("O Player");
                else System.out.println("X Player");
                System.out.println("enter row: (1-9)");
                if((moves % 2 == 0 && playerO == 0) || (moves % 2 == 1 && playerX == 0))
                //row = (int)(Math.random()*9);
                    row = getRow(targetBox);
                else
                row = input.nextInt()-1;
                System.out.println("enter col:(1-9)");
                if((moves % 2 == 0 && playerO == 0) || (moves % 2 == 1 && playerX == 0))
                //col = (int)(Math.random()*9);
                    col=getCol(targetBox);
                else
                col = input.nextInt()-1;
                if (col > 8 || row > 8 || previous[row][col] != 0 ||
                        !inBox(targetBox,row,col) || doneBoxes[whatBox(row,col)]!=0 ) {
                    System.out.println("invalid choice");
                }

                else
                {
                    for(int i=0; i<previous.length; i++)
                    {
                        for(int j=0; j<previous[0].length; j++)
                        {
                            current[i][j]=previous[i][j];
                        }
                    }
                   // char[][] temp = previous;
                    if(moves%2==0) current[row][col]= 2;
                    else current[row][col] = 1;
                    invalid=false;
                    targetBox = nextBox(row,col);
                    doneBoxes[whatBox(row,col)] = boxDone(row,col,current);
                    for(int i=0; i<doneBoxes.length; i++)
                    {
                        System.out.println("Box " + (i+1) + " = " + (doneBoxes[i] == 2 ? "O" : doneBoxes[i] == 1 ? "X" : ""));
                    }

                    int k=0;
                    int[][]checkWinner = new int[3][3];
                    for(int i=0; i<3; i++)
                    {
                        for(int j=0; j<3; j++)
                        {
                            checkWinner[i][j] = doneBoxes[k++];
                        }
                    }
                    if(isWinner(checkWinner)==2)
                    {
                        System.out.println("O wins");
                        State.printBoard(current);
                        //states[moves].printBoard();
                        System.out.println("number of movess: " + moves);
                        moves=82;
                    }
                    else if(isWinner(checkWinner)==1)
                    {
                        System.out.println("X wins");
                        State.printBoard(current);
                        //states[moves].printBoard();
                        System.out.println("number of movess: " + moves);
                        moves=82;
                    }
                    else
                    {
                        if (doneBoxes[targetBox] == 1 || doneBoxes[nextBox(row, col)] != 0) targetBox = -1;
                        System.out.println("Next box: " + (targetBox +1));
                    }
                }
                /*
                try
                {
                    Thread.sleep(1000);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
                */
                for(int i=0; i<previous.length; i++)
                {
                    for(int j=0; j<previous[0].length; j++)
                    {
                        previous[i][j]=current[i][j];
                    }
                }
            }
        }
    }
    public static int boxDone(int row, int col, int[][] state)
    {
        return isWinner(box(whatBox(row,col),state));
    }
    //0 for no, 2 for O, 1 for X
    public static int isWinner(int box[][])
    {
        int status=0;

             if(box[0][0]!=0 && box[0][0]==box[0][1] && box[0][0]==box[0][2]) status=box[0][0];
        else if(box[1][0]!=0 && box[1][0]==box[1][1] && box[1][0]==box[1][2]) status=box[1][0];
        else if(box[2][0]!=0 && box[2][0]==box[2][1] && box[2][0]==box[2][2]) status=box[2][0];

        else if(box[0][0]!=0 && box[0][0]==box[1][0] && box[0][0]==box[2][0]) status=box[0][0];
        else if(box[0][1]!=0 && box[0][1]==box[1][1] && box[0][1]==box[2][1]) status=box[0][1];
        else if(box[0][2]!=0 && box[0][2]==box[1][2] && box[0][2]==box[2][2]) status=box[0][2];

        else if(box[0][0]!=0 && box[0][0]==box[1][1] && box[0][0]==box[2][2]) status=box[0][0];
        else if(box[0][2]!=0 && box[0][2]==box[1][1] && box[0][2]==box[2][0]) status=box[0][2];

        if(status==0)
        {
            int numX=0;
            int numO=0;
            for(int i=0;i<box.length; i++)
            {
                for(int j=0; j<box[i].length; j++)
                {
                    if(box[i][j]==2) numO++;
                    else if(box[i][j]==1) numX++;
                    else return 0;
                }
            }
            if(numX>numO) status=1;
            else status=2;
        }

        return status;
    }

    public static boolean inBox(int target, int row, int col)
    {
        return (target == -1 ||target==whatBox(row,col));
    }
    public static int nextBox(int row, int col)
    {
        int box=0;

        if(row%3==0 && col%3==0) box = 0;
        else if(row%3==0 && col%3==1) box = 1;
        else if(row%3==0 && col%3==2) box = 2;

        if(row%3==1 && col%3==0) box = 3;
        else if(row%3==1 && col%3==1) box = 4;
        else if(row%3==1 && col%3==2) box = 5;

        else if(row%3==2 && col%3==0) box = 6;
        else if(row%3==2 && col%3==1) box = 7;
        else if(row%3==2 && col%3==2) box = 8;

        return box;
    }
    public static int whatBox(int row, int col)
    {
        //System.out.println("row = " + row + " col = " + col);
        int box=0;
        if(row<3 && col <3) box=0;
        else if(row<3 && col <6) box=1;
        else if(row<3 && col <9) box=2;

        else if(row<6 && col <3) box=3;
        else if(row<6 && col <6) box=4;
        else if(row<6 && col <9) box=5;

        else if(row<9 && col <3) box=6;
        else if(row<9 && col <6) box=7;
        else if(row<9 && col <9) box=8;

        return box;
    }
    public static int[][] box(int box,int[][] state)
    {
        int rowS=0,colS=0;
        int[][] output=new int[3][3];
        //System.out.println("box: " + box);

        if(box == 0)
        {
            rowS=0;
            colS=0;
        }
        else if(box == 1)
        {
            rowS=3;
            colS=0;
        }
        else if(box == 2)
        {
            rowS=6;
            colS=0;
        }

        else if(box == 3)
        {
            rowS=0;
            colS=3;
        }
        else if(box == 4)
        {
            rowS=3;
            colS=3;
        }
        else if(box == 5)
        {
            rowS=6;
            colS=3;
        }

        else if(box == 6)
        {
            rowS=0;
            colS=6;
        }
        else if(box == 7)
        {
            rowS=3;
            colS=6;
        }
        else if(box == 8)
        {
            rowS=6;
            colS=6;
        }
        for(int i=colS; i<colS+3; i++)
        {
            for(int j=rowS; j<rowS+3; j++)
            {
                output[i%3][j%3]=state[i][j];
            }
        }
        /*
        System.out.println("col start = " + colS);
        System.out.println("row start = " + rowS);
        for(int i=0; i<output.length; i++)
        {
            for(int j=0; j<output[i].length; j++)
            {
                System.out.print(output[i][j]);
            }
            System.out.println();
        }
        */

        return output;
    }

    public static int getRow(int box)
    {
        //System.out.println("box: " + box);
        int choice;
        if(box == 0 || box == 1 || box == 2) choice=(int)(Math.random()*3);
        else if(box == 3 || box==4 || box==5)choice=(int)(Math.random()*3)+3;
        else if(box == 6 || box == 7 || box == 8)choice=(int)(Math.random()*3)+6;
        else choice = (int)(Math.random()*9);
        //System.out.println("col choice = " + choice);
        return choice;
    }
    public static int getCol(int box)
    {
        //System.out.println("box: " + box);
        int choice;
        if(box == 0 || box == 3 || box == 6) choice=(int)(Math.random()*3);
        else if(box == 1 || box == 4 || box == 7) choice=(int)(Math.random()*3)+3;
        else if(box == 2 ||box == 5 || box == 8)choice=(int)(Math.random()*3)+6;
        else choice = (int)(Math.random()*9);
        //System.out.println("row choice = " + choice);
        return choice;
    }

    public static Tree runTests(int n) {
        Tree tree = new Tree();
        int numXwins=0;
        int totalmovessWhenX=0;
        int totalmovessWhenO=0;
        int numOwins=0;
        int totalmovess=0;
        for (int times = 0; times < n; times++) {
            State[] states = new State[82];
            states[0] = new State(new int[9][9]);

            int[][] previous = new int[9][9];
            int[][] current = new int[9][9];

            int row, col;
            int targetBox = -1;
            int[] doneBoxes = new int[9];
            boolean invalid;
            int stop =0;
            for (int moves = 1; moves < 81 + 1; moves++) {
                //add states[moves - 1] to tree
                invalid = true;
                if(moves==2)
                {
                    tree.addStartPoints(states[moves - 1]);
                    //tree.getNodes().get(states[moves - 1].getGoodHashCode()).printBoard();
                    //System.out.println();
                }
                else if (moves>2)
                {
                    //System.out.println(moves);
                    tree.addNode(states[moves-1]);
                    //System.out.println("Checking node in hashmap correct:");
                    //tree.getNodes().get(states[moves-1].getGoodHashCode()).printBoard();
                    Tree.getNodes().get(states[moves-1].getGoodHashCode()).addPreviousState(states[moves-2]);
                    //states[moves-1].addPreviousState(states[moves-2]);
                }

                while (invalid) {
                    //System.out.println("targetBox1 " + targetBox);

                    row = getRow(targetBox);
                    col = getCol(targetBox);

                    if (col > 8 || row > 8 || previous[row][col] != 0 ||
                            !inBox(targetBox, row, col) || doneBoxes[whatBox(row, col)] != 0) {
                    }
                    else {
                        //int[][] temp = new int[states[moves-1].getBoard().length][states[moves-1].getBoard()[0].length];
                        //states[moves-1].printBoard();

                        //int[][] temp = states[moves-1].getBoard();
                        //int[][] tempCopy = new int[9][9];

                        //copy into temp

                        for (int i = 0; i < previous.length; i++) {

                            for (int j = 0; j < previous[i].length; j++) {
                                current[i][j] = previous[i][j];
                            }
                        }



                        //System.out.println("Moves: " + moves);
                        if (moves % 2 == 0) current[row][col] = 2;
                        else current[row][col] = 1;
                        states[moves] = new State(current);
                        invalid = false;
                        targetBox = nextBox(row, col);
                        doneBoxes[whatBox(row, col)] = boxDone(row, col, current);

                        int k = 0;
                        int[][] checkWinner = new int[3][3];
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                checkWinner[i][j] = doneBoxes[k++];
                            }
                        }

                        //states[moves-1].setBoard(tempCopy);

                        if (isWinner(checkWinner) == 2) {
                            //System.out.println("Moves5: " + moves);
                            //add states[moves] to tree and that O won
                            tree.addEndingPoint(states[moves]);
                            Tree.getNodes().get(states[moves].getGoodHashCode()).addPreviousState(states[moves-1]);
                            Tree.getNodes().get(states[moves].getGoodHashCode()).setOWinRate(1);
                            Tree.getNodes().get(states[moves].getGoodHashCode()).setXWinRate(0);
                            //states[moves].addPreviousState(states[moves-1]);
                            //states[moves].setOWinRate(1);
                            //states[moves].setXWinRate(0);
                            numOwins++;
                            totalmovess = totalmovess + moves;
                            totalmovessWhenO = totalmovessWhenO + moves;
                            stop=moves;
                            moves = 82;
                            //System.out.println("Moves4: " + moves);
                        } else if (isWinner(checkWinner) == 1) {
                            //System.out.println("Moves3: " + moves);
                            //add states[moves] to tree and that X won
                            tree.addEndingPoint(states[moves]);
                            Tree.getNodes().get(states[moves].getGoodHashCode()).addPreviousState(states[moves-1]);
                            Tree.getNodes().get(states[moves].getGoodHashCode()).setOWinRate(0);
                            Tree.getNodes().get(states[moves].getGoodHashCode()).setXWinRate(1);
                            //states[moves].addPreviousState(states[moves-1]);
                            //states[moves].setOWinRate(0);
                            //states[moves].setXWinRate(1);
                            numXwins++;
                            totalmovess = totalmovess + moves;
                            totalmovessWhenX = totalmovessWhenX + moves;
                            stop=moves;
                            moves = 82;
                            //System.out.println("Moves2: " + moves);
                        } else {
                            //System.out.println("Moves1: " + moves);
                            if (doneBoxes[targetBox] == 1 || doneBoxes[nextBox(row, col)] != 0) targetBox = -1;
                            //System.out.println("targetBox " + targetBox);
                        }
                    }
                }
                for(int i=0; i<previous.length; i++)
                {
                    for(int j=0; j<previous[0].length; j++)
                    {
                        previous[i][j]=current[i][j];
                    }
                }

            }
            //Add next Nodes up to one less
            for(int i=1; i<stop; i++)
            {
                //states[i].printBoard();
                //System.out.println();
                Tree.getNodes().get(states[i].getGoodHashCode()).addNextState(states[i+1]);
                //states[i].addNextState(states[i+1]);
                //states[i].printNextStates();
            }

           // Iterator<Map.Entry<Integer, State>> it = tree.getEndPoints().entrySet().iterator();





            if((times+1)%1000==0) System.out.println(times);
        }
        tree.assignRate();





        //tree.printStartingStates();

        System.out.println("total moves: " + totalmovess);
        System.out.println("number of nodes: " + Tree.getNodes().size());
        System.out.println("Average moves in " + n + " games: " + (double)totalmovess/n);
        System.out.println("total starting moves: " + tree.getStartPoints().size());
        System.out.println("total ending moves: " + tree.getEndPoints().size());

        System.out.println("X won " + numXwins + " games: " + 100*(double)numXwins/n + "%");
        System.out.println("Average moves when X wins " + (double)totalmovessWhenX/numXwins);

        System.out.println("O won " + numOwins + " games: " + 100*(double)numOwins/n + "%");
        System.out.println("Average moves when O wins " + (double)totalmovessWhenO/numOwins);
        return tree;
    }

}

