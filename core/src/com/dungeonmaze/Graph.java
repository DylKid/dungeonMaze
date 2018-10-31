package com.dungeonmaze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dungeonmaze.CellLabel.CLEAR;

public class Graph {

    private Map<MazeCell, List<MazeCell>> adj;
    private Map<MazeCell, CellLabel> labels;
    private Map<MazeCell, Boolean> visited;
    private Map<MazeCell, MazeCell> parents;

    private Map<MazeCell, List<MazeCell>> travelled;
    private Map<MazeCell, List<MazeCell>> wallMap;
    private Map<MazeCell, List<MazeCell>> fakeMap;

    private List<MazeCell> deadEnds;

    private MazeCell startLoc;

    public Graph(){
        labels = new HashMap();
        visited = new HashMap();
        adj = new HashMap();
        parents = new HashMap();
        travelled = new HashMap();
        wallMap = new HashMap();
        fakeMap = new HashMap();
        deadEnds = new ArrayList();
    }

    public void addMazeCell(MazeCell mazeCell){
        if(adj.containsKey(mazeCell)){

        } else {
            adj.put(mazeCell, new ArrayList());
            labels.put(mazeCell, CLEAR);
            visited.put(mazeCell, false);
            travelled.put(mazeCell, new ArrayList());
            wallMap.put(mazeCell, new ArrayList());
            fakeMap.put(mazeCell, new ArrayList());

        }
    }

    public boolean isVisited(int x, int y){
        return visited.get(new MazeCell(x,y));
    }

    public CellLabel labelOf(int x, int y){
        return labels.get(new MazeCell(x,y));
    }

    public void addAllAdjMazeCell(MazeCell pos1, List<MazeCell> adjPosList){
        adj.get(pos1).addAll(adjPosList);
    }

    public void addAdjMazeCell(MazeCell pos1, MazeCell adjPos){
        adj.get(pos1).add(adjPos);
    }

    public void dfs(MazeCell start){
        this.startLoc = new MazeCell(start.getX(), start.getY());
        dfsVisit(start);
    }

    public void dfsVisit(MazeCell source){
        if(source == null){
            return;
        }
        //List<MazeCell> adjCells = adj.get(source);
        List<MazeCell> clearCells = new ArrayList();
        for(MazeCell m : adj.get(source)) {
            //if (labels.get(m) == CLEAR && !visited.get(m)) {
            if(!visited.get(m)) {
                clearCells.add(m);
            }
            //}
        }

        //Deadend
        if(clearCells.isEmpty()){
            if(!visited.get(source)) {
                deadEnds.add(source);
            }
            visited.put(source, true);
            System.out.println("DEADEND");
            dfsVisit(parents.get(source));
            return;
        }

        System.out.format("at x:%d y:%d\n", source.getX(),source.getY());
        int k = (int) (Math.random() * clearCells.size());
        MazeCell chosen = clearCells.get(k);

        /*for(MazeCell m : adj.get(source)){
            if(!m.equals(chosen) && !m.equals(parents.get(source))){
                adj.get(m).remove(source);
            }
        }*/


        //Only path that source can take is to the chosen
        /*List<MazeCell> newList = new ArrayList();
        newList.add(chosen);
        adj.put(source, newList);*/
        System.out.printf("CHOSEN:%s\n",chosen);
        //printAdj();
        visited.put(source, true);
        parents.put(chosen,source);
        travelled.get(source).add(chosen);
        travelled.get(chosen).add(source);
        //visited.put(chosen, true);
        dfsVisit(chosen);
    }

    public void printAdj(){
        String ret = "";
        for(MazeCell m : adj.keySet()){
            List<MazeCell> adjList = adj.get(m);
            ret += m.toString() + ": [";
            for(MazeCell mc : adjList){
                ret += mc.toString() + ",";
            }
            ret = ret.substring(0, ret.length()-1);
            ret += "]\n";
        }

        System.out.println(ret);
    }

    public void printTravelled(){
        String ret = "";
        for(MazeCell m : travelled.keySet()){
            List<MazeCell> adjList = travelled.get(m);
            ret += m.toString() + ": [";
            for(MazeCell mc : adjList){
                ret += mc.toString() + ",";
            }
            ret = ret.substring(0, ret.length()-1);
            ret += "]\n";
        }

        System.out.println(ret);
    }

    public void printWallMap(){
        String ret = "";
        for(MazeCell m : wallMap.keySet()){
            List<MazeCell> adjList = wallMap.get(m);
            ret += m.toString() + ": [";
            for(MazeCell mc : adjList){
                ret += mc.toString() + ",";
            }
            ret = ret.substring(0, ret.length()-1);
            ret += "]\n";
        }

        System.out.println(ret);
    }

    public void computeWallMap(){
        for(MazeCell m : travelled.keySet()){
            List<MazeCell> travelledList = travelled.get(m);
            List<MazeCell> adjList = adj.get(m);
            for(MazeCell mc : adjList){
                if(!travelledList.contains(mc)){
                    if(parents.get(mc)!=null){
                        if(!parents.get(mc).equals(m)){
                            wallMap.get(m).add(mc);
                        }
                    } else {
                        wallMap.get(m).add(mc);
                    }
                }
            }
        }
        //fakeMap.get(new MazeCell(0,0)).add(new MazeCell(0,1));
        fakeMap.get(new MazeCell(0,1)).add(new MazeCell(0,0));
        fakeMap.get(new MazeCell(0,0)).add(new MazeCell(0,1));
        //return wallMap;
    }

    public Map<MazeCell, List<MazeCell>> getWallMap(){
        return this.wallMap;
    }

    public Map<MazeCell, List<MazeCell>> getTravelled(){
        return this.travelled;
    }


    public List<MazeCell> getDeadEnds(){
        return this.deadEnds;
    }

    public MazeCell getFinish(){
        int max = 0;
        MazeCell maxCell = this.deadEnds.get(deadEnds.size()-1);
        for(MazeCell m : deadEnds){
           // max = Math.max(max, dist(startLoc,m));
            if(dist(startLoc,m) > max){
                maxCell = m;
                max = dist(startLoc,m);
            }
        }
        return maxCell;
        //return this.deadEnds.get(deadEnds.size()-1);
    }

    public int dist(MazeCell m1, MazeCell m2){
        return Math.abs((m1.getX() - m2.getX())) + Math.abs((m1.getY() - m2.getY()));
    }

    public Map<MazeCell, List<MazeCell>> getFakeWallMap(){
        return fakeMap;
    }

}
