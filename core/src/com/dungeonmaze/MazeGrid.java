package com.dungeonmaze;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gridPlayer.Location;
import gridPlayer.StateHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MazeGrid extends StateHandler {

    private int squareHeight;
    private int squareWidth;

    SpriteBatch batch;

    List<Texture> squares;
    int[][] squareSpots;

    Texture wall_down;
    Texture wall_down_top;
    Texture wall_sideways;
    Texture wall_sideways_top;
    Texture greyWall;

    Texture treasure;

    MazeCell finish;
    List<Texture> coins;

    int witchCount = 0;
    int renderCount = 0;

    MazeCell treasureLoc;
    List<MazeCell> coinLocs;

    int squareSizeWidth;
    int squareSizeHeight;

    private MazeCell startLoc;

    Graph g;

    float wallWidth;

    private OrthographicCamera cam;

    public MazeGrid(int width, int height, int squareSizeHeight, int squareSizeWidth, MazeCell startLoc,
                    OrthographicCamera cam){
        this.squareHeight = height;
        this.squareWidth = width;
        this.squareSizeWidth = squareSizeWidth;
        this.cam = cam;
        this.squareSizeHeight = squareSizeHeight;

        //startLoc = new MazeCell(0,0);

        this.startLoc = startLoc;

        batch = new SpriteBatch();

        squares = new ArrayList();
        squareSpots = new int[squareWidth][squareHeight];
        greyWall = new Texture("greyWall1.jpg");

        wall_down = new Texture("wall_down.png");
        wall_down_top = new Texture("wall_down_top.png");
        wall_sideways = new Texture("wall_sideways.png");
        wall_sideways_top = new Texture("wall_sideways_top.png");

        wallWidth = (float) (squareSizeWidth * 0.2);


        coins = new ArrayList();

        for(int i = 1; i < 5; i++){
            coins.add(new Texture(String.format("coin%d.png",i)));
        }

        treasure = new Texture("treasure.png");

        for(int i = 1; i < 7; i++){
            String squareName = String.format("square%d.png", i);
            squares.add(new Texture(squareName));
        }


        g = new Graph();


        for(int i = 0; i < squareWidth	; i++){
            for(int j = 0; j < squareHeight; j++){
                List<MazeCell> l = new ArrayList();
                squareSpots[i][j] = (int) (Math.random() * 6);
                MazeCell newCell = new MazeCell(i,j);
                g.addMazeCell(newCell);
                if(i > 0){ l.add(new MazeCell(i-1, j));}
                if(j > 0){ l.add(new MazeCell(i, j-1));}
                if(i < (squareHeight-1)){ l.add(new MazeCell(i+1, j));}
                if(j < (squareWidth-1)){ l.add(new MazeCell(i, j+1));}
                g.addAllAdjMazeCell(newCell, l);
            }
        }

        g.dfs(startLoc);
        //System.out.println("sup");
        //g.printAdj();
        System.out.println("TRAVELLED");
        g.printTravelled();
        g.computeWallMap();
        //System.out.println("WALL MAP");
        //g.printWallMap();

        finish = g.getFinish();
        treasureLoc = finish;
        getCurrentGoal();

        //witchX = 0;
        //witchY = 0;
        //System.out.println(g.getWalls());
        //Gdx.graphics.setWindowedMode(GRIDWIDTH * (int) squareSizeWidth,GRIDHEIGHT * (int) squareSizeWidth);
        //Gdx.graphics.setResizable(false);
    }

    float sideWallHeight = squareSizeWidth - wallWidth;

    public void drawGrid(){
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        int x = 0;
        int y = 0;



        //drawWalls();

        Map<MazeCell, List<MazeCell>> wallMap = g.getWallMap();

        for(int i = 0; i < squareWidth; i++){
            for(int j = 0; j < squareHeight; j++){
                batch.draw(squares.get(squareSpots[i][j]),x,y, squareSizeWidth,squareSizeHeight);

                //if(i==2 && j == 2){
                    //float rightWallX = (float) (x + (0.8 * squareSizeWidth));
                    MazeCell m = new MazeCell(j,i);
                    List<MazeCell> walls = wallMap.get(m);
                    for(MazeCell mc: walls){
                        //right
                        if(mc.getX() == (m.getX() + 1) && m.getY() == m.getY()) {
                            drawRightWall(x, y);
                            //left
                        } else if (mc.getX() == (m.getX() - 1) && m.getY() == m.getY()){
                            drawLeftWall(x,y);
                            //up
                        } else if(mc.getY() == (m.getY() + 1) && m.getX() == m.getX()){
                            drawTopWall(x,y);
                            //down
                        } else if(mc.getY() == (m.getY() - 1) && m.getX() == m.getX()){
                            drawBottomWall(x,y);
                        }
                    }
                    /*if(i==2 && j == 2) {
                        drawTopWall(x, y);
                        drawBottomWall(x, y);
                    }*/

                    //float halfY = y - (float) (0.5 * squareSizeHeight);
                    //batch.draw(wall_down_top, x ,y, wallWidth, wallWidth);

                    //float topWallY = y + (float) (0.8 * squareSizeHeight);
                    //batch.draw(greyWall, x, topWallY, squareSizeWidth, wallWidth);

                    //batch.draw(greyWall, rightWallX, y, wallWidth, sideWallHeight);

                    //drawRightWall(x,y);
                    //drawLeftWall(x,y);
                    //drawBottomWall(x,y);
                    //drawTopWall(x,y);

                    //batch.draw(wall_sideways, x, y, squareSizeWidth, wallWidth);
               // }

                x += squareSizeWidth;
                /*if(g.labelOf(i,j)==WALL){
                    //System.out.format("Draw wall at x:%d, y:%d\n", i, j);
                    batch.draw(wall_sideways,x,y,squareSizeWidth,squareSizeHeight);
                } else if(g.isVisited(i,j)) {
                    batch.draw(wall_down_top, x, y, squareSizeWidth, GRIDHEIGHT);
                } else {
                    batch.draw(squares.get(squareSpots[i][j]),x,y, squareSizeWidth,squareSizeHeight);
                }*/
                //System.out.println("Draw x:" + x + " y:" + y);
            }
            x = 0;
            y += squareSizeHeight;
        }

        //renderTreasureAt(finish.getX(), finish.getY());

        //renderWitchAt(witchX,witchY);


        //for(int i = 0; i < GRIDWIDTH; i++){
        //    batch.draw(wall_sideways, i*squareSizeWidth, 10*squareSizeHeight, squareSizeWidth,squareSizeHeight);
        //}

        batch.end();
        //getCurrentLoc();
        //getCurrentGoal();
        if(this.isObserverSet()){
            this.updateNotify();
        }
        renderCount++;
    }

    //x & y in pixels
    public void drawRightWall(int x, int y){
        float rightWallX = (float) (x + (0.8 * squareSizeWidth)) + (float) (0.5 *wallWidth);
        for(int i = 1; i < 6; i++) {
            //System.out.printf("x:%d y:%f\n",x,(y+(i-1*wallWidth)));
            batch.draw(wall_down_top, rightWallX , y + ((i-1)*wallWidth), wallWidth, wallWidth);
        }
    }

    public void drawLeftWall(int x, int y){
        float leftWallX = x - (float) (0.5*wallWidth);
        for(int i = 1; i < 6; i++) {
            //System.out.printf("x:%d y:%f\n",x,(y+(i-1*wallWidth)));
            batch.draw(wall_down_top, leftWallX, y + ((i-1)*wallWidth), wallWidth, wallWidth);
        }
    }

    public void drawTopWall(int x, int y){
        float topWallY = y + (float) (0.8 * squareSizeHeight) + (float) (0.5*wallWidth);
        for(int i = 1; i < 6; i++) {
            batch.draw(wall_down_top, x + ((i-1)*wallWidth), topWallY, wallWidth, wallWidth);
        }
    }

    public void drawBottomWall(int x, int y){
        float bottomWallY = y - (float) (0.5*wallWidth);
        for(int i = 1; i < 6; i++) {
            batch.draw(wall_down_top, x + ((i-1)*wallWidth), bottomWallY, wallWidth, wallWidth);
        }
    }

    public void dispose(){
        batch.dispose();
        wall_down.dispose();
        wall_down_top.dispose();
        wall_sideways.dispose();
        wall_down_top.dispose();
        for(Texture t : squares){
            t.dispose();
        }
    }

    MazeCell witchLoc;

    public void renderWitchAt(int x, int y, Texture w){
        witchLoc = new MazeCell(x,y);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        float witchHeight = (float) (w.getWidth() * 1.5);
        float witchWidth = (float) (w.getHeight() * 1.5);
        float witchX = (float) (x * squareSizeWidth + (0.2 * squareSizeWidth));
        float witchY = (float) (y * squareSizeHeight + (0.2) * squareSizeHeight);
        batch.draw(w, witchX, witchY, witchHeight, witchWidth);
        batch.end();
        //witchCount = ((witchCount+1) % witch.size());
    }

    public void renderTreasureAt(int x, int y){
        treasureLoc = new MazeCell(x,y);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        float treasureHeight = (float) (treasure.getWidth() * 1.5);
        float treasureWidth = (float) (treasure.getHeight() * 1.5);
        float treasureX = (float) (x * squareSizeWidth + (0.2 * squareSizeWidth));
        float treasureY = (float) (y * squareSizeHeight + (0.2) * squareSizeHeight);
        batch.draw(treasure, treasureX, treasureY, treasureHeight, treasureWidth);
        batch.end();
    }

    public void renderCoinAt(int x, int y, int coinCount){
        float coinHeight = (float) (coins.get(coinCount).getWidth() * 1.5);
        float coinWidth = (float) (coins.get(coinCount).getHeight() * 1.5);
        float coinX = (float) (x * squareSizeWidth + (0.4 * squareSizeWidth));
        float coinY = (float) (y * squareSizeHeight + (0.4) * squareSizeHeight);
        batch.draw(coins.get(coinCount), coinX, coinY, coinHeight, coinWidth);
    }

    public void renderCoins(int coinCount, List<MazeCell> coins){
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        for(MazeCell m : coins){
            renderCoinAt(m.getX(), m.getY(), coinCount);
        }
        batch.end();
    }

    public boolean isValidMove(int fromX, int fromY, int toX, int toY){
        MazeCell from = new MazeCell(fromX, fromY);
        MazeCell to = new MazeCell(toX, toY);
        if(g.getTravelled().get(from).contains(to)){
            return true;
        } else {
            return false;
        }
    }


    public List<MazeCell> getCoinLocs(){
        List<MazeCell> coinLocs = g.getDeadEnds();
        coinLocs.remove(finish);
        return coinLocs;
    }

    public void reset(int height, int width, MazeCell startLoc, OrthographicCamera cam){
        this.squareHeight = height;
        this.squareWidth = width;
        this.cam = cam;
        squareSpots = new int[squareWidth][squareHeight];
        g = new Graph();
        for(int i = 0; i < squareWidth	; i++){
            for(int j = 0; j < squareHeight; j++){
                List<MazeCell> l = new ArrayList();
                squareSpots[i][j] = (int) (Math.random() * 6);
                MazeCell newCell = new MazeCell(i,j);
                g.addMazeCell(newCell);
                if(i > 0){ l.add(new MazeCell(i-1, j));}
                if(j > 0){ l.add(new MazeCell(i, j-1));}
                if(i < (squareHeight-1)){ l.add(new MazeCell(i+1, j));}
                if(j < (squareWidth-1)){ l.add(new MazeCell(i, j+1));}
                g.addAllAdjMazeCell(newCell, l);
            }
        }
        g.dfs(startLoc);
        witchLoc = startLoc;
        //System.out.println("sup");
        //g.printAdj();
        System.out.println("TRAVELLED");
        g.printTravelled();
        g.computeWallMap();
        //System.out.println("WALL MAP");
        //g.printWallMap();

        finish = g.getFinish();
        treasureLoc = finish;
        if(isObserverSet()) {
            updateSuccess();
        }
    }

    public MazeCell getFinish(){
        return finish;
    }

    public Map<MazeCell,List<MazeCell>> getTravelledMap(){
        return this.g.getTravelled();
    }

    @Override
    public Location getCurrentLoc() {
        return witchLoc;
    }

    @Override
    public Location getCurrentGoal() {
        return treasureLoc;
    }

    @Override
    public Map<Location, List<Location>> getValidMoveList() {
        Map<Location, List<Location>> m = new HashMap();
        for(Map.Entry<MazeCell, List<MazeCell>> e : this.getTravelledMap().entrySet()){
            List<Location> l = new ArrayList();
            l.addAll(e.getValue());
            m.put(e.getKey(),l);
        }
        return m;
    }
}
