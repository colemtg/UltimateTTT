import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.*;

public class Tree {
    private HashSet<Integer> startPoints = new HashSet<>();
    private HashSet<Integer> endPoints = new HashSet<>();
    private static HashMap<Integer, State> nodes = new HashMap<>();

    public void addStartPoints(State input) {
        //System.out.println("Adding start point:");
        //input.printBoard();
        if (nodes.containsKey(input.getGoodHashCode())) {
            //System.out.println("Already have start point:");
            //input.printBoard();
            //System.out.println("conflict");
            //nodes.get(input.getGoodHashCode()).printBoard();
            //System.out.println(nodes.get(input.getGoodHashCode()).getGoodHashCode());
            //System.out.println();
            //input.printBoard();
            //System.out.println(input.getGoodHashCode());
            //System.out.println();
            //System.out.println();
            nodes.get(input.getGoodHashCode()).incrementCount();
        } else {
            //System.out.println("Adding start point:");
            //input.printBoard();
            //System.out.println("Hashcode: " + input.getGoodHashCode());
            nodes.put(input.getGoodHashCode(), input);
            startPoints.add(input.getGoodHashCode());
        }
    }

    public void addEndingPoint(State input) {
        if (nodes.containsKey(input.getGoodHashCode())) {
            /*
            System.out.println("Already have end point:");
            input.printBoard();
            System.out.println("conflict");
            nodes.get(input.getGoodHashCode()).printBoard();
            System.out.println(nodes.get(input.getGoodHashCode()).getGoodHashCode());
            System.out.println();
            input.printBoard();
            System.out.println(input.getGoodHashCode());
            System.out.println();
            System.out.println();
            */
            nodes.get(input.getGoodHashCode()).incrementCount();
        } else {
            nodes.put(input.getGoodHashCode(), input);
            endPoints.add(input.getGoodHashCode());
        }
    }

    public void addNode(State input) {
        // System.out.println(input.getHashCode());
        //input.printBoard();
        if (nodes.containsKey(input.getGoodHashCode())) {
            /*
            System.out.println("conflict");
            nodes.get(input.getGoodHashCode()).printBoard();
            System.out.println(nodes.get(input.getGoodHashCode()).getGoodHashCode());
            System.out.println();
            input.printBoard();
            System.out.println(input.getGoodHashCode());
            System.out.println();
            System.out.println();
            */
            nodes.get(input.getGoodHashCode()).incrementCount();

        } else {
            //System.out.println("Adding node:");
            nodes.put(input.getGoodHashCode(), input);
            //input.printBoard();
            //System.out.println(nodes.get(input.getGoodHashCode()).getGoodHashCode());
            //System.out.println("Object: " + input);
        }

    }

    public HashSet<Integer> getStartPoints() {
        return startPoints;
    }

    public HashSet<Integer> getEndPoints() {
        return endPoints;
    }

    public static HashMap<Integer, State> getNodes() {
        return nodes;
    }

    public void printStartingStates()
    {
        Iterator<Integer> it = startPoints.iterator();
        System.out.println("Starting states:");
        while(it.hasNext())
        {
            Integer next = it.next();
            //nodes.get(next).printBoard();
            State.printBoard(nodes.get(next).getBoard());
            System.out.println(next);
            System.out.println();
        }
    }

    public void printEndingStates()
    {
        Iterator<Integer> it = endPoints.iterator();
        System.out.println("Ending states:");
        while(it.hasNext())
        {
            Integer next = it.next();
            State.printBoard(nodes.get(next).getBoard());
            //nodes.get(next).printBoard();
            System.out.println();
        }
    }

    public void printNodes()
    {
        Iterator<Map.Entry<Integer, State>> it = nodes.entrySet().iterator();
        System.out.println("All states:");
        while(it.hasNext())
        {
            Map.Entry<Integer, State> next = it.next();
            State.printBoard(next.getValue().getBoard());
            //next.getValue().printBoard();
            System.out.println();
        }
    }
    public void assignRate()
    {
        HashSet<Integer> goneTo = new HashSet<>();
        PriorityQueue<State> queue = new PriorityQueue<>();
        Iterator<Integer> it = endPoints.iterator();
        while(it.hasNext())
        {
            Integer next = it.next();
            Iterator<Integer> it4 = nodes.get(next).getPreviousStates().iterator();
            while(it4.hasNext())
            {
                Integer next2 = it4.next();
                queue.add(nodes.get(Tree.getNodes().get(next2).getGoodHashCode()));
            }
            goneTo.add(nodes.get(next).getGoodHashCode());
        }
        while(!queue.isEmpty())
        {
            State current = nodes.get(queue.remove().getGoodHashCode());
            //System.out.println("Removing:");
            //current.printBoard();
            if(!goneTo.contains(current.getGoodHashCode()))
            {
                goneTo.add(current.getGoodHashCode());
                double tempO=0;
                double tempX=0;
                double totCount=0;
                Iterator<Integer> it2 = current.getNextStates().iterator();
                /*
                if(current.getNextStates().size()>1)
                {
                    System.out.println("current state");
                    current.printBoard();
                }
                */
                while(it2.hasNext())
                {
                    Integer next = it2.next();
                    /*
                    if(current.getNextStates().size()>1)
                    {
                        System.out.println("next state");
                        next.getValue().printBoard();
                        System.out.println("O:" + next.getValue().getOWinRate()*next.getValue().getCount());
                        System.out.println("X:" + next.getValue().getXWinRate()*next.getValue().getCount());
                        System.out.println("Count: " + next.getValue().getCount());
                    }
                    */
                    tempO=tempO+(Tree.getNodes().get(next).getOWinRate()*Tree.getNodes().get(next).getCount());
                    tempX=tempX+(Tree.getNodes().get(next).getXWinRate()*Tree.getNodes().get(next).getCount());
                    totCount=totCount+Tree.getNodes().get(next).getCount();
                }
                /*
                if(totCount!=1)
                {
                    System.out.println("Temp O: " + tempO);
                    System.out.println("Temp X: " + tempX);
                    System.out.println("total count: " + totCount);
                }
                */

                current.setOWinRate(tempO/totCount);
                current.setXWinRate(tempX/totCount);

                Iterator<Integer> it3 = current.getPreviousStates().iterator();
                while(it3.hasNext())
                {
                    Integer next = it3.next();
                    queue.add(nodes.get(Tree.getNodes().get(next).getGoodHashCode()));
                }

            }
        }
        State temp = new State(new int[9][9]);
        addNode(temp);
        Iterator<Integer> it6 = startPoints.iterator();
        double tempO=0;
        double tempX=0;
        double totCount=0;
        while(it6.hasNext())
        {
            Integer next = it6.next();
            temp.addNextState(nodes.get(next));
            tempO=tempO+(nodes.get(next).getOWinRate()*nodes.get(next).getCount());
            tempX=tempX+(nodes.get(next).getXWinRate()*nodes.get(next).getCount());
            totCount=totCount+nodes.get(next).getCount();
        }
        nodes.get(temp.getGoodHashCode()).setOWinRate(tempO/totCount);
        nodes.get(temp.getGoodHashCode()).setXWinRate(tempX/totCount);

        //temp.setOWinRate(tempO/totCount);
        //temp.setXWinRate(tempX/totCount);
    }

}
