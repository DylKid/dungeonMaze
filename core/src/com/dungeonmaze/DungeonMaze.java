package com.dungeonmaze;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gridPlayer.GridHandler;

import java.util.ArrayList;
import java.util.List;

public class DungeonMaze extends ApplicationAdapter implements GridHandler {
	SpriteBatch batch;
	Texture img;

	int GRIDHEIGHT = 6;
	int GRIDWIDTH = 6;

	int squareSizeHeight = 40;
	int squareSizeWidth = 40;

	int renderCount = 0;
	int witchCount = 0;
	int coinCount = 0;

	List<Texture> witch;
	int witchX;
	int witchY;

	MazeGrid grid;

	List<MazeCell> coinLocs;

	MazeCell startLoc;

	MazeCell finishLoc;

	OrthographicCamera cam;

	private int score = 25;

	private BitmapFont headerFont;


	private int scoreHeight;

	boolean firstRender;

	private int levelCount = 1;

	private int scoreSpeed = 32;

	private boolean gameOver;
	int gameOverCount = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		firstRender = true;
		gameOver = false;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		scoreHeight = 30;

		//FileHandle fileHandle = new FileHandle();
		headerFont =  new BitmapFont(Gdx.files.internal("headerFont.fnt"), false);

		// Constructs a new OrthographicCamera, using the given viewport width and height
		// Height is multiplied by aspect ratio.
		cam = new OrthographicCamera(GRIDWIDTH *squareSizeWidth, (GRIDHEIGHT *squareSizeHeight) + scoreHeight);

		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();

		int startX = (int) (Math.random() * GRIDWIDTH);
		int startY = (int) (Math.random() * GRIDHEIGHT);

		this.startLoc = new MazeCell(startX, startY);

		witchX = startX;
		witchY = startY;
		grid = new MazeGrid(GRIDHEIGHT, GRIDWIDTH, squareSizeWidth,squareSizeHeight, startLoc, cam);
		witch = new ArrayList();
		coinLocs = grid.getCoinLocs();

		for(int i = 1; i < 8; i++){
			String witchName = String.format("witch%d.png", i);
			witch.add(new Texture(witchName));
		}


		finishLoc = grid.getFinish();



		//Gdx.graphics.setWindowedMode(800,800);
		//Gdx.graphics.setResizable(false);

	}

	public void reset(){
		gameOver = false;
		finishLoc = grid.getFinish();
		cam = new OrthographicCamera(GRIDWIDTH *squareSizeWidth, (GRIDHEIGHT *squareSizeHeight) + scoreHeight);

		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();

		int startX = (int) (Math.random() * GRIDWIDTH);
		int startY = (int) (Math.random() * GRIDHEIGHT);

		this.startLoc = new MazeCell(startX, startY);
		witchX = startX;
		witchY = startY;
		grid.reset(GRIDHEIGHT, GRIDWIDTH, startLoc,cam);
		coinLocs = grid.getCoinLocs();
		finishLoc = grid.getFinish();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(gameOver){
			cam = new OrthographicCamera(400, 400);
			cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
			cam.update();
			renderGameOver();
			gameOverCount++;
			if(gameOverCount > 100){
				Gdx.app.exit();
			}
		} else {

			handleCollisions();
			if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && witchX > 0
					&& grid.isValidMove(witchX, witchY, (witchX - 1), witchY)) {
				witchX--;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && witchY <= GRIDHEIGHT
					&& grid.isValidMove(witchX, witchY, witchX, witchY + 1)) {
				witchY++;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && witchY > 0
					&& grid.isValidMove(witchX, witchY, witchX, witchY - 1)) {
				witchY--;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && witchX <= GRIDWIDTH
					&& grid.isValidMove(witchX, witchY, witchX + 1, witchY)) {
				witchX++;
			}
			grid.drawGrid();
			renderCount++;
			if (renderCount % 10 == 0) {
				witchCount = ((witchCount + 1) % witch.size());
				coinCount = ((coinCount + 1)) % 4;
			}
			if (renderCount % scoreSpeed == 0) {
				if (score == 0) {
					//renderGameOver();
					//sleep(5000);
					//Gdx.app.exit();
					gameOver = true;
				}
				score--;
				//renderScore();
			}
			grid.renderWitchAt(witchX, witchY, witch.get(witchCount));
			grid.renderCoins(coinCount, coinLocs);
			grid.renderTreasureAt(finishLoc.getX(), finishLoc.getY());
			renderScore();
		}
	}

	public void handleCollisions(){
		if(witchX == finishLoc.getX() && witchY == finishLoc.getY()){
			score += 20;
			levelCount += 1;
			GRIDWIDTH++;
			GRIDHEIGHT++;
			this.reset();
			scoreSpeed-= 1;
			//	renderScore();
		}

		Integer k = null;
		for(int i = 0; i < coinLocs.size(); i++){
			MazeCell coin = coinLocs.get(i);
			if(witchX == coin.getX() && witchY == coin.getY()){
				score += 4;
				renderScore();
				k = i;
			}
		}
		if(k !=null){
			coinLocs.remove((int) k);
		}
	}

	public void renderScore(){
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		final GlyphLayout layout = new GlyphLayout(headerFont, String.format("Time:%d  Level:%d" ,score,levelCount));
		//final GlyphLayout layout2 = new GlyphLayout(headerFont, String.format("Level %d", levelCount));
		int fontX = 1;
		//int levelFontX =
		float fontY = cam.viewportHeight - 5;
		//int fontY = 15;
		headerFont.draw(batch, layout, fontX, fontY);
		//headerFont.draw(batch, layout2, levelFontX, fontY);
		batch.end();
	}

	public void renderGameOver(){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		final GlyphLayout layout = new GlyphLayout(headerFont, String.format("             GAME OVER\n" +
				"         You got to Level %d" ,levelCount));

		//final GlyphLayout layout2 = new GlyphLayout(headerFont, String.format("Level %d", levelCount));
		int fontX = 5;
		//int levelFontX =
		float fontY = 250;
		//int fontY = 15;
		headerFont.draw(batch, layout, fontX, fontY);
		//headerFont.draw(batch, layout2, levelFontX, fontY);
		batch.end();
	}

	//public void drawUpSection()

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		grid.dispose();
	}

	private static void sleep(long milis){
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public MazeGrid getGrid(){
		return this.grid;
	}

	public MazeCell getWitchLoc() {
		return new MazeCell(witchX, witchY);
	}

	public MazeCell getTreasureLoc(){
		return finishLoc;
	}

	public MazeCell getStartLoc(){
		return this.startLoc;
	}

	@Override
	public void moveUp() {
		if (witchY <= GRIDHEIGHT
				&& grid.isValidMove(witchX, witchY, witchX, witchY + 1)) {
			witchY++;
		}
	}

	@Override
	public void moveDown() {
		if (witchY > 0
				&& grid.isValidMove(witchX, witchY, witchX, witchY - 1)) {
			witchY--;
		}
	}

	@Override
	public void moveLeft() {
		if (witchX > 0
				&& grid.isValidMove(witchX, witchY, (witchX - 1), witchY)) {
			witchX--;
		}
	}

	@Override
	public void moveRight() {
		if (witchX <= GRIDWIDTH
				&& grid.isValidMove(witchX, witchY, witchX + 1, witchY)) {
			witchX++;
		}
	}

	@Override
	public int getHeight() {
		return GRIDHEIGHT;
	}

	@Override
	public int getWidth() {
		return GRIDWIDTH;
	}
}
