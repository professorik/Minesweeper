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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Piece;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerOfGameTable implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private MenuItem setSizes;
    @FXML
    private MenuItem setBombsQuantity;
    @FXML
    private MenuItem setLevel;
    @FXML
    private MenuItem getInstruction;


    private Piece[][] table = MainOfGameTable.game.getField();
    // private GridPane table;

    private final int WIDTH = 9;
    private final int HEIGHT = 9;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GridPane root = new GridPane();
        root.setGridLinesVisible(true);

        for (int i = 0; i < HEIGHT; i++) {
            root.getRowConstraints().add(new RowConstraints(40));
            root.getColumnConstraints().add(new ColumnConstraints(40));
            for (int j = 0; j < WIDTH; j++) {
                AnchorPane pane2 = new AnchorPane();
                VBox pane = new VBox();
                pane.setFillWidth(true);
                pane.setPrefWidth(40);
                pane.setPrefHeight(40);
                // pane.getChildren().add(new Label("0"));

                Button button = new Button();
                pane.getChildren().add(button);
                pane.setVgrow(button, Priority.ALWAYS);

                button.setFocusTraversable(false);
                button.setMaxHeight(Double.MAX_VALUE);
                button.setMaxWidth(Double.MAX_VALUE);

                Label label = new Label(table[i][j].isRigged()? "1" : "0");
                label.setVisible(false);
                //label.setAlignment(Pos.CENTER);
                  label.setTranslateX(12.5);
                label.setTranslateY(12.5);

                pane2.getChildren().addAll(pane, label);
                button.setOnAction(event -> {
                    button.setVisible(false);
                    label.setVisible(true);
                });
                root.add(pane2, j, i);
            }
        }

        borderPane.setCenter(root);

        setSizes.setOnAction(event -> showResizeDialog());
        setBombsQuantity.setOnAction(event -> System.out.println(setBombsQuantity.getText()));
        setLevel.setOnAction(event -> System.out.println(setLevel.getText()));
        getInstruction.setOnAction(event -> System.out.println(getInstruction.getText()));
    }


    private void showResizeDialog() {
        final Stage dialogStage = new Stage();
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Input sizes");

        Button yesBtn = new Button("OK");
        Button noBtn = new Button("Cancel");

        yesBtn.setOnAction(event -> {
            dialogStage.close();
        });
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

        dialogStage.setScene(new Scene(vBox));
        dialogStage.show();
    }
}
