package com.dungeonmaze;

import gridPlayer.Location;

public class MazeCell implements Location {

    private int x;
    private int y;
    private Object contents;
    //private CellLabel cellLabel;
    //private boolean visited;

    public MazeCell(int x, int y) {
        this.x = x;
        this.y = y;
        //cellLabel = CellLabel.CLEAR;
        //visited = false;
    }

    @Override
    public String toString(){
        return String.format("Cell(%d, %d)",x,y);
    }

    /*public void visit(){
        this.visited = true;
    }

    public boolean isVisited(){
        return visited;
    }*/

    /*public CellLabel getCellLabel() {
        return cellLabel;
    }

    public void setCellLabel(CellLabel cellLabel) {
        this.cellLabel = cellLabel;
    }*/

    public MazeCell(int x, int y, Object contents) {
        this.x = x;
        this.y = y;
        this.contents = contents;
    }

    @Override
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Object getContents() {
        return contents;
    }

    public void setContents(Object contents) {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof MazeCell){
            MazeCell otherMazeCell = (MazeCell) o;
            if(this.x == otherMazeCell.getX() && this.y == otherMazeCell.getY()){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        int a = 17;
        a += x;
        a *= y;
        return a;
    }
    
}
