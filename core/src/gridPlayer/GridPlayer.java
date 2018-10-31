package gridPlayer;

import java.util.*;

public class GridPlayer implements StateObserver {

    private int height;
    private int width;
    private GridHandler gridHandler;
    private StateHandler stateHandler;
    private SearchEnum search;
    private int count;
    private int successes;
    private int neededSucceses;
    private Graph g;
    private Location currentGoal;
    private int currX;
    private int currY;
    private boolean setup = false;
    private int bfsIndex = 0;
    private List<Location> currBFSPath;

    public GridPlayer(GridHandler gridHandler, StateHandler stateHandler){
        this.height = gridHandler.getHeight();
        this.width = gridHandler.getWidth();
        this.gridHandler = gridHandler;
        this.stateHandler = stateHandler;
        count = 1;
        g = new Graph();
        search = SearchEnum.NONE;
    }

    public void blindlySearchUntil(int successes) {
        this.neededSucceses = successes;
        search = SearchEnum.BLIND;
    }

    public void setsearchUntil(String name, int successes){
        if(name.equals("blind")){
            search = SearchEnum.BLIND;
        } else if(name.equals("BFS")){
            search = SearchEnum.BFS;
        }
        this.neededSucceses = successes;
        while(this.successes < neededSucceses){
            sleep(80);
        }
    }

    public List<Position> getPathTo(int x, int y){
        int xCount = currX;
        int yCount = currY;

        List<Position> l = new ArrayList();
        while(xCount != x){
            xCount = ((xCount + 1) % width);
            l.add(new Position(xCount, yCount));
        }
        while(yCount != y){
            yCount = ((yCount + 1) % height);
            l.add(new Position(xCount, yCount));
        }
        return l;
    }


    //Create graph with full access in the grid
    public void createBasicGraph(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                List l = new ArrayList();
                Position newPos = new Position(i,j);
                g.addPosition(newPos);
                if(i > 0){ l.add(new Position(i-1, j));}
                if(j > 0){ l.add(new Position(i, j-1));}
                if(i < (height-1)){ l.add(new Position(i+1, j));}
                if(j < (width-1)){ l.add(new Position(i, j+1));}
                g.addAllAdjPosition(newPos, l);
            }
        }
    }


    @Override
    public void runNotify() {
        System.out.println("Run notify");
        switch(search){
            case BLIND:
                blindSearch();
            case BFS:
                runBFS();
            case DIJKSTRA:
                break;
            case ASTAR:
                break;
            case NONE:
                break;
        }
    }

    @Override
    public void notifySuccess() {
        successes++;
        if(search == SearchEnum.BFS){
            setup = false;
        }
    }

    private void blindSearch(){
        currentGoal = stateHandler.getCurrentGoal();
        if(currentGoal != null) {
            moveToward(currentGoal);
            currX = stateHandler.getCurrentLoc().getX();
            currY = stateHandler.getCurrentLoc().getY();
        }
    }


    public void bfs(Position start, Position end){
        Queue<Position> q = new LinkedList();
        q.add(start);
        while(!q.isEmpty()){

        }
    }


    private void moveToward(Location moveTo){
        if(moveTo.getX() > currX) {
            gridHandler.moveRight();
        } else if(moveTo.getX() < currX){
            gridHandler.moveLeft();
        } else if(moveTo.getY() > currY){
            gridHandler.moveUp();
        } else if(moveTo.getY() < currY){
            gridHandler.moveDown();
        }
    }

    public void goNuts(){
                switch(count%4){
            case 0:
                System.out.println("LEFT");
                gridHandler.moveLeft();
                break;
            case 1:
                System.out.println("UP");
                gridHandler.moveUp();
                break;
            case 2:
                System.out.println("RIGHT");
                gridHandler.moveRight();
                break;
            case 3:
                System.out.println("DOWN");
                gridHandler.moveDown();
                break;
            default:
                System.out.println("Do nothing");
                break;
        }
        count += (int) (Math.random()*4);
    }

    private static void sleep(long milis){
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  <T extends Location>  void addAdjLocations(Location pos1, List<T> adjPosList){
        Position pos = new Position(pos1.getX(), pos1.getY());
        List<Location> posList = new ArrayList();
        for(Location l : adjPosList){
            posList.add(new Position(l));
        }
        g.addAllAdjPosition(pos1, posList);
    }

    public void setupForBFS(){
        currentGoal = stateHandler.getCurrentGoal();
        Position source = new Position(currX, currY);
        Map<Location, List<Location>> validAdj = stateHandler.getValidMoveList();
        g.setAdj(validAdj);
        if(currentGoal!=null) {
            currBFSPath = g.bfs(source, currentGoal);
            bfsIndex = 0;
            search = SearchEnum.BFS;
        }
    }

    public void runBFS(){
        currX = stateHandler.getCurrentLoc().getX();
        currY = stateHandler.getCurrentLoc().getY();
        if(setup) {
            System.out.println("Running BFS");
            if (bfsIndex < currBFSPath.size()) {
                Location moveTo = currBFSPath.get(bfsIndex);
                System.out.printf("MOVING FROM: (%d, %d) TO:%s", currX, currY, moveTo);
                moveToward(moveTo);
                bfsIndex++;
            }
        } else {
            System.out.println("Setting up");
            setupForBFS();
            setup = true;
        }
    }
}
