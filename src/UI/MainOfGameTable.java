package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Game;

public class MainOfGameTable extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/UI/app.fxml"));
        primaryStage.setTitle("Minesweeper");
        primaryStage.getIcons().add(new Image("bomb.png"));
        primaryStage.setScene(new Scene(root, 745, 900));
        primaryStage.show();
    }

    public static Game game;

    public static void main(String[] args) {
        game = new Game(30 , 30 , Game.GameLevel.EASY);
        launch(args);
    }
}
