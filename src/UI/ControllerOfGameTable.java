package UI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;

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
                pane.setFillWidth(true);pane.setPrefWidth(40);pane.setPrefHeight(40);
               // pane.getChildren().add(new Label("0"));

                Button button = new Button();
                pane.getChildren().add(button);
                pane.setVgrow(button, Priority.ALWAYS);

                button.setFocusTraversable(false);
                button.setMaxHeight(Double.MAX_VALUE);
                button.setMaxWidth(Double.MAX_VALUE);

                Label label = new Label("12");
                label.setVisible(false);
                label.setTranslateX(12.5);
                label.setTranslateY(12.5);

                pane2.getChildren().addAll(pane , label);
                button.setOnAction(event -> {button.setVisible(false); label.setVisible(true);});
                root.add(pane2 , j , i);
            }
        }

        borderPane.setCenter(root);

        setSizes.setOnAction(event -> System.out.println(setSizes.getText()));
        setBombsQuantity.setOnAction(event -> System.out.println(setBombsQuantity.getText()));
        setLevel.setOnAction(event -> System.out.println(setLevel.getText()));
        getInstruction.setOnAction(event -> System.out.println(getInstruction.getText()));
    }
}
