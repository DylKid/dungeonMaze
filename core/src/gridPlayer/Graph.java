package gridPlayer;

import java.util.*;

public class Graph {

    private Map<Position, List<Position>> adj;

    public Graph(){
        adj = new HashMap();
    }

    public void addPosition(Position Position){
        if(adj.containsKey(Position)){

        } else {
            adj.put(Position, new ArrayList());
        }
    }

    public void setAdj(Map<Location, List<Location>> newAdj){
        adj = new HashMap();
        for(Map.Entry<Location, List<Location>> e : newAdj.entrySet()){
            this.addAllAdjPosition(e.getKey(),e.getValue());
        }
    }

    public void addAllAdjPosition(Location pos1, List<Location> adjPosList){
        Position pos = new Position(pos1);
        List<Position> posList = new ArrayList();
        for(Location l : adjPosList){
            posList.add(new Position(l));
        }
        if(adj.get(pos1) == null){
            adj.put(pos, posList);
        } else {
            adj.get(pos).addAll(posList);
        }
    }

    public void addAdjPosition(Position pos1, Position adjPos){
        adj.get(pos1).add(adjPos);
    }

    private HashMap<Position, Boolean> visited;
    private HashMap<Position, Position> parents;
    private HashMap<Position, Integer> distance;

    public List<Location> bfs(Location s, Location d){
        Position source = new Position(s);
        Position destination = new Position(d);
        visited = new HashMap();
        parents = new HashMap();
        distance = new HashMap();
        for(Position loc : adj.keySet()){
            visited.put(loc, false);
            parents.put(loc, null);
            distance.put(loc, Integer.MAX_VALUE);
        }
        Queue<Position> Q = new LinkedList();
        Q.add(source);
        distance.put(source, 0);
        while(!Q.isEmpty()){
            Position curr = Q.poll();

            List<Position> posList = adj.get(curr);


            for(Position pos : posList){
                int dist = distance.get(curr) + 1;
                if(dist < distance.get(pos) && !visited.get(pos)){
                    Q.add(pos);
                    distance.put(pos, dist);
                    parents.put(pos, curr);
                }
            }
            visited.put(curr, true);
        }
        List<Location> ret = getPath(new ArrayList(), destination);
        Collections.reverse(ret);
        return ret;
    }

    public List<Location> getPath(List<Location> list, Position curr){
        if(parents.get(curr) == null){
            return list;
        } else {
            list.add(curr);
            return getPath(list, parents.get(curr));
        }
    }

    private int dist(Position l1, Position l2){
        return Math.abs(l1.getX() - l2.getX()) + Math.abs(l1.getY() - l2.getY());
    }

}
