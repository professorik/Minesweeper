package UI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Game;
import model.Piece;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerOfGameTable implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private MenuItem setSizes;
    @FXML
    private MenuItem firstLength;
    @FXML
    private MenuItem secondLength;
    @FXML
    private MenuItem thirdLength;
    @FXML
    private MenuItem easy;
    @FXML
    private MenuItem medium;
    @FXML
    private MenuItem hard;

    @FXML
    private MenuItem getInstruction;


    private Piece[][] table;
    // private GridPane table;

    private int WIDTH =30;
    private int HEIGHT = 30;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startGame();

        setSizes.setOnAction(event -> showResizeDialog());
        firstLength.setOnAction(event -> {
            WIDTH = 9; HEIGHT = 9;
            MainOfGameTable.game = new Game(WIDTH, HEIGHT, MainOfGameTable.game.getLevel());
            startGame();
        });
        secondLength.setOnAction(event -> {
            WIDTH = 16; HEIGHT = 16;
            MainOfGameTable.game = new Game(WIDTH, HEIGHT, MainOfGameTable.game.getLevel());
            startGame();
        });
        thirdLength.setOnAction(event -> {
            WIDTH = 30; HEIGHT = 30;
            MainOfGameTable.game = new Game(WIDTH, HEIGHT, MainOfGameTable.game.getLevel());
            startGame();
        });
        easy.setOnAction(event -> {
            if (!MainOfGameTable.game.getLevel().equals(Game.GameLevel.EASY)) {
                MainOfGameTable.game = new Game(WIDTH, HEIGHT, Game.GameLevel.EASY);
                startGame();
            }
        });
        medium.setOnAction(event -> {
            if (!MainOfGameTable.game.getLevel().equals(Game.GameLevel.MEDIUM)) {
                MainOfGameTable.game = new Game(WIDTH, HEIGHT, Game.GameLevel.MEDIUM);
                startGame();
            }
        });
        hard.setOnAction(event -> {
            if (!MainOfGameTable.game.getLevel().equals(Game.GameLevel.HARD)) {
                MainOfGameTable.game = new Game(WIDTH, HEIGHT, Game.GameLevel.HARD);
                startGame();
            }
        });
        getInstruction.setOnAction(event -> System.out.println(getInstruction.getText()));
    }

    private void startGame() {
        table = MainOfGameTable.game.getField();

        GridPane root = new GridPane();
        root.setGridLinesVisible(true);

        double fieldWIDTH = (18 * 40.0 * (Math.log10(WIDTH) / Math.log10(30)))/WIDTH;
        double fieldHEIGHT = (18 * 40.0 * (Math.log10(HEIGHT) / Math.log10(30)))/HEIGHT;

        double size = Math.min(fieldWIDTH, fieldHEIGHT);

        //root.setMaxWidth((40 * WIDTH)/ 9);
       // root.setMaxHeight((40 * HEIGHT) / 9);
       // root.setVgap(10);
        System.out.println(size);

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(table[i][j].isRigged() ? 1 : 0);
            }
            System.out.println();
        }
        for (int i = 0; i < HEIGHT; i++) {
            root.getRowConstraints().add(new RowConstraints(size));
            root.getColumnConstraints().add(new ColumnConstraints(size));
            for (int j = 0; j < WIDTH; j++) {
                boolean fl = table[i][j].isRigged();

                AnchorPane pane2 = new AnchorPane();
                VBox pane = new VBox();
                pane.setFillWidth(true);
                pane.setMinWidth(size);
                pane.setMinHeight(size);
                pane.setPrefWidth(size);
                pane.setPrefHeight(size);
                pane.setMaxHeight(size);
                pane.setMaxWidth(size);
                // pane.getChildren().add(new Label("0"));

                Button button = new Button();
                pane.getChildren().add(button);
                pane.setVgrow(button, Priority.ALWAYS);

                button.setFocusTraversable(false);
                button.setMaxHeight(Double.MAX_VALUE);
                button.setMaxWidth(Double.MAX_VALUE);

                Label label = new Label("" + MainOfGameTable.game.getSurroundingBombCount(i , j));
                label.setVisible(false);
                Image image = new Image("bomb.png", 24, 24, true, true, true);
                ImageView imageView = new ImageView(image);
                imageView.setVisible(false);

                label.setTranslateX(12.5 * size / 40);
                label.setTranslateY(12.5 * size / 40);
                //label.setAlignment(Pos.CENTER);
                label.setTranslateX(12.5 * size / 40);
                label.setTranslateY(12.5 * size / 40);

                pane2.getChildren().addAll(pane, label, imageView);
                button.setOnAction(event -> {
                    button.setVisible(false);
                    if (fl){
                        imageView.setVisible(true);
                    }else {
                        label.setVisible(true);
                    }
                });
                root.add(pane2, j, i);
            }
        }

       // borderPane.setMaxSize(1000 , 800);
        //borderPane.setMinSize(1000, 800);
        //borderPane.setScaleX(1000);
       // borderPane.setPrefHeight(100);
       // borderPane.getScene().setFill(Color.BLACK);
        borderPane.setCenter(root);
    }

    private void showResizeDialog() {
        final Stage dialogStage = new Stage();
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Input sizes");

        Button yesBtn = new Button("OK");
        Button noBtn = new Button("Cancel");

        noBtn.setOnAction(event -> dialogStage.close());

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox.setSpacing(10.0);
        hBox.getChildren().addAll(yesBtn, noBtn);

        HBox enterX = new HBox();
        enterX.setAlignment(Pos.BASELINE_CENTER);
        enterX.setSpacing(10.0);
        TextField inputX = new TextField();
        inputX.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!"0123456789".contains(keyEvent.getCharacter()) || inputX.getText().length() > 1) {
                    keyEvent.consume();
                }
            }
        });
        enterX.getChildren().addAll(new Label("X:"), inputX);

        HBox enterY = new HBox();
        enterY.setAlignment(Pos.BASELINE_CENTER);
        enterY.setSpacing(10.0);
        TextField inputY = new TextField();
        inputY.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!"0123456789".contains(keyEvent.getCharacter()) || inputY.getText().length() > 1) {
                    keyEvent.consume();
                }
            }
        });
        enterY.getChildren().addAll(new Label("Y:"), inputY);

        VBox vBox = new VBox();
        vBox.setSpacing(10.0);
        vBox.getChildren().addAll(enterX, enterY, hBox);
        vBox.setPadding(new Insets(10, 10, 0, 10));

        yesBtn.setOnAction(event -> {
            if (inputX.getText().length() * inputY.getText().length() > 0) {
                WIDTH = Integer.valueOf(inputX.getText());
                HEIGHT = Integer.valueOf(inputY.getText());
                MainOfGameTable.game = new Game(WIDTH, HEIGHT, MainOfGameTable.game.getLevel());
                startGame();
            }
            dialogStage.close();
        });

        dialogStage.setScene(new Scene(vBox));
        dialogStage.show();
    }
}
