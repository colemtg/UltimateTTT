import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.*;

public class Tree {
    private HashMap<Integer, State> startPoints = new HashMap<>();
    private HashMap<Integer, State> endPoints = new HashMap<>();
    private HashMap<Integer, State> nodes = new HashMap<>();

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
            startPoints.put(input.getGoodHashCode(), input);
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
            endPoints.put(input.getGoodHashCode(), input);
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

    public HashMap<Integer, State> getStartPoints() {
        return startPoints;
    }

    public HashMap<Integer, State> getEndPoints() {
        return endPoints;
    }

    public HashMap<Integer, State> getNodes() {
        return nodes;
    }

    public void printStartingStates()
    {
        Iterator<Map.Entry<Integer, State>> it = startPoints.entrySet().iterator();
        System.out.println("Starting states:");
        while(it.hasNext())
        {
            Map.Entry<Integer, State> next = it.next();
            next.getValue().printBoard();
            System.out.println(next.getKey());
            System.out.println();
        }
    }

    public void printEndingStates()
    {
        Iterator<Map.Entry<Integer, State>> it = endPoints.entrySet().iterator();
        System.out.println("Ending states:");
        while(it.hasNext())
        {
            Map.Entry<Integer, State> next = it.next();
            next.getValue().printBoard();
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
            next.getValue().printBoard();
            System.out.println();
        }
    }
    public void assignRate()
    {
        HashSet<Integer> goneTo = new HashSet<>();
        PriorityQueue<State> queue = new PriorityQueue<>();
        Iterator<Map.Entry<Integer, State>> it = endPoints.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<Integer, State> next = it.next();
            Iterator<Map.Entry<Integer, State>> it4 = next.getValue().getPreviousStates().entrySet().iterator();
            while(it4.hasNext())
            {
                Map.Entry<Integer, State> next2 = it4.next();
                queue.add(nodes.get(next2.getValue().getGoodHashCode()));
            }
            goneTo.add(next.getValue().getGoodHashCode());
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
                Iterator<Map.Entry<Integer, State>> it2 = current.getNextStates().entrySet().iterator();
                /*
                if(current.getNextStates().size()>1)
                {
                    System.out.println("current state");
                    current.printBoard();
                }
                */
                while(it2.hasNext())
                {
                    Map.Entry<Integer, State> next = it2.next();
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
                    tempO=tempO+(next.getValue().getOWinRate()*next.getValue().getCount());
                    tempX=tempX+(next.getValue().getXWinRate()*next.getValue().getCount());
                    totCount=totCount+next.getValue().getCount();
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

                Iterator<Map.Entry<Integer, State>> it3 = current.getPreviousStates().entrySet().iterator();
                while(it3.hasNext())
                {
                    Map.Entry<Integer, State> next = it3.next();
                    queue.add(nodes.get(next.getValue().getGoodHashCode()));
                }

            }
        }
        State temp = new State(new int[9][9]);
        addNode(temp);
        Iterator<Map.Entry<Integer, State>> it6 = startPoints.entrySet().iterator();
        double tempO=0;
        double tempX=0;
        double totCount=0;
        while(it6.hasNext())
        {
            Map.Entry<Integer, State> next = it6.next();
            temp.addNextState(next.getValue());
            tempO=tempO+(next.getValue().getOWinRate()*next.getValue().getCount());
            tempX=tempX+(next.getValue().getXWinRate()*next.getValue().getCount());
            totCount=totCount+next.getValue().getCount();
        }
        nodes.get(temp.getGoodHashCode()).setOWinRate(tempO/totCount);
        nodes.get(temp.getGoodHashCode()).setXWinRate(tempX/totCount);

        //temp.setOWinRate(tempO/totCount);
        //temp.setXWinRate(tempX/totCount);
    }
}
