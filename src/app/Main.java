package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Game;
import model.GameClock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

//        GameClock clock = new GameClock();
//
//        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.scheduleAtFixedRate(() -> System.out.println(clock.getElapsedTimeMillis() / 1000), 1, 1, TimeUnit.SECONDS);

        Game game = new Game(Game.FieldSize.EASY, Game.GameLevel.HARD);
        System.out.println(game.fieldString());

        System.out.println(game.getSurroundingBombCount(0,0));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
