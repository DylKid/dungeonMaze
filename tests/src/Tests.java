import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dungeonmaze.DungeonMaze;
import gridPlayer.GridPlayer;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests {
    private static LwjglApplicationConfiguration config;
    private static DungeonMaze mazeGame;
    private static LwjglApplication application;

    @BeforeClass
    public static void setup(){
        config = new LwjglApplicationConfiguration();
        mazeGame = new DungeonMaze();
        application = new LwjglApplication(mazeGame, config);
    }

    @Test
    public void runGame(){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                mazeGame.create();
            }
        });
    }

    @Test
    public void gridPlayerTest(){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                mazeGame.create();
            }
        });

        //wait for startup
        sleep(2000);

        GridPlayer gridPlayer = new GridPlayer(mazeGame, mazeGame.getGrid());

        mazeGame.getGrid().setObserver(gridPlayer);
        gridPlayer.setsearchUntil("BFS", 10);
    }

    private static void sleep(long milis){
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
