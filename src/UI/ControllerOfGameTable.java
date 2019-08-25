package UI;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Game;
import model.Piece;

import java.net.URL;
import java.util.ArrayList;
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

    @FXML
    private HBox hat;

    private Piece[][] table;
    // private GridPane table;

    private int WIDTH = 30;
    private int HEIGHT = 30;
    private final double minSizes = 20;

    ArrayList<Integer> was = new ArrayList<>();

    private ScrollPane scrollPane;

    private int count = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startGame();



        setSizes.setOnAction(event -> showResizeDialog());
        firstLength.setOnAction(event -> {
            WIDTH = 9;
            HEIGHT = 9;
            MainOfGameTable.game = new Game(WIDTH, HEIGHT, MainOfGameTable.game.getLevel());
            startGame();
        });
        secondLength.setOnAction(event -> {
            WIDTH = 16;
            HEIGHT = 16;
            MainOfGameTable.game = new Game(WIDTH, HEIGHT, MainOfGameTable.game.getLevel());
            startGame();
        });
        thirdLength.setOnAction(event -> {
            WIDTH = 30;
            HEIGHT = 30;
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

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                System.out.print(table[i][j].isRigged()? "b" : MainOfGameTable.game.getSurroundingBombCount(i , j));
            }
            System.out.println();
        }

        GridPane root = new GridPane();
        root.setGridLinesVisible(true);

        double fieldWIDTH = (18 * 40.0 * (Math.log10(WIDTH) / Math.log10(30))) / WIDTH;
        double fieldHEIGHT = (18 * 40.0 * (Math.log10(HEIGHT) / Math.log10(30))) / HEIGHT;

        double size = Math.min(fieldWIDTH, fieldHEIGHT);
        size = Math.max(size, minSizes);

        for (int i = 0; i < WIDTH; i++) {
            root.getRowConstraints().add(new RowConstraints(size));

            if (i < HEIGHT) {
                root.getColumnConstraints().add(new ColumnConstraints(size));
            }

            for (int j = 0; j < HEIGHT; j++) {
                int index = i * HEIGHT + j;

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

                Button button = new Button();
                pane.getChildren().add(button);
                pane.setVgrow(button, Priority.ALWAYS);

                button.setFocusTraversable(false);
                button.setMaxHeight(Double.MAX_VALUE);
                button.setMaxWidth(Double.MAX_VALUE);

                Image image;
                if (!fl) {
                    image = new Image(MainOfGameTable.game.getSurroundingBombCount(i, j) + ".png", size, size, true, true, true);
                } else {
                    image = new Image("bomb.png", size, size, true, true, true);
                }
                ImageView imageView = new ImageView(image);
                imageView.setVisible(false);

                pane2.getChildren().addAll(pane, imageView);

                imageView.setFitWidth(size);
                imageView.setFitHeight(size);

                Image smile = new Image("flag2.png", size / 2, size / 2, true, true, true);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        button.setGraphic(new ImageView(smile));
                     //   solve(root);
                    } else {
                        if (fl){
                            for (int k = 0; k < WIDTH; k++) {
                                for (int l = 0; l < HEIGHT; l++) {
                                      clearIndex(root, k * HEIGHT + l);
                                }
                            }
                            Label label = new Label("You lose");
                            borderPane.setBottom(label);
                        }else {
                            cleanFromZero(index, root);
                            ++count;
                            if (MainOfGameTable.game.getTotalBombCount() == WIDTH * HEIGHT - count){
                                Label label = new Label("You win!");
                                borderPane.setBottom(label);
                            }
                            button.setVisible(false);
                            imageView.setVisible(true);
                        }
                    }
                });
                root.add(pane2, j, i);
            }
        }

        scrollPane = new ScrollPane(root);
        root.setPadding(new Insets(10, 10, 10, 10));
        borderPane.setCenter(scrollPane);
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
                WIDTH = Integer.valueOf(inputY.getText());
                HEIGHT = Integer.valueOf(inputX.getText());
                MainOfGameTable.game = new Game(WIDTH, HEIGHT, MainOfGameTable.game.getLevel());
                startGame();
            }
            dialogStage.close();
        });

        dialogStage.setScene(new Scene(vBox));
        dialogStage.show();
    }

    private void cleanFromZero(int index, GridPane gridPane){
        ArrayList<Integer> stack = new ArrayList<>();
        stack.add(index);
        int y = (int) Math.floor(((double) stack.get(0)) / HEIGHT);
        int x = stack.get(0) % HEIGHT;
        while (!table[y][x].isRigged()) {
            clearIndex(gridPane, stack.get(0));
            int count = MainOfGameTable.game.getSurroundingBombCount(y, x);
            if (count == 0) {
                if (x > 0 && x < HEIGHT - 1) {
                    if (y > 0 && y < HEIGHT - 1) {
                        clearIndex(gridPane, stack.get(0) - WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) - WIDTH);
                        clearIndex(gridPane, stack.get(0) - WIDTH + 1);
                        clearIndex(gridPane, stack.get(0) + 1);
                        clearIndex(gridPane, stack.get(0) - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH);
                        clearIndex(gridPane, stack.get(0) + WIDTH + 1);

                        stack.add(stack.get(0) + WIDTH + 1);
                        stack.add(stack.get(0) + WIDTH);
                        stack.add(stack.get(0) + WIDTH - 1);
                        stack.add(stack.get(0) - WIDTH + 1);
                        stack.add(stack.get(0) - WIDTH);
                        stack.add(stack.get(0) - WIDTH - 1);
                        stack.add(stack.get(0) + 1);
                        stack.add(stack.get(0) - 1);
                    } else if (y == 0) {
                        clearIndex(gridPane, stack.get(0) + 1);
                        clearIndex(gridPane, stack.get(0) - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH);
                        clearIndex(gridPane, stack.get(0) + WIDTH + 1);

                        stack.add(stack.get(0) + 1);
                        stack.add(stack.get(0) - 1);
                        stack.add(stack.get(0) + WIDTH - 1);
                        stack.add(stack.get(0) + WIDTH);
                        stack.add(stack.get(0) + WIDTH + 1);
                    } else {
                        clearIndex(gridPane, stack.get(0) - WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) - WIDTH);
                        clearIndex(gridPane, stack.get(0) - WIDTH + 1);
                        clearIndex(gridPane, stack.get(0) + 1);
                        clearIndex(gridPane, stack.get(0) - 1);

                        stack.add(stack.get(0) - WIDTH - 1);
                        stack.add(stack.get(0) - WIDTH);
                        stack.add(stack.get(0) - WIDTH + 1);
                        stack.add(stack.get(0) + 1);
                        stack.add(stack.get(0) - 1);
                    }
                } else if (x == 0) {
                    if (y > 0 && y < HEIGHT - 1) {
                        clearIndex(gridPane, stack.get(0) - WIDTH);
                        clearIndex(gridPane, stack.get(0) - WIDTH + 1);
                        clearIndex(gridPane, stack.get(0) + 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH);
                        clearIndex(gridPane, stack.get(0) + WIDTH + 1);

                        stack.add(stack.get(0) - WIDTH);
                        stack.add(stack.get(0) - WIDTH + 1);
                        stack.add(stack.get(0) + 1);
                        stack.add(stack.get(0) + WIDTH);
                        stack.add(stack.get(0) + WIDTH + 1);
                    } else if (y == 0) {
                        clearIndex(gridPane, stack.get(0) + 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH);
                        clearIndex(gridPane, stack.get(0) + WIDTH + 1);

                        stack.add(stack.get(0) + 1);
                        stack.add(stack.get(0) + WIDTH);
                        stack.add(stack.get(0) + WIDTH + 1);
                    } else {
                        clearIndex(gridPane, stack.get(0) - WIDTH);
                        clearIndex(gridPane, stack.get(0) - WIDTH + 1);
                        clearIndex(gridPane, stack.get(0) + 1);

                        stack.add(stack.get(0) - WIDTH);
                        stack.add(stack.get(0) - WIDTH + 1);
                        stack.add(stack.get(0) + 1);
                    }
                } else {
                    if (y > 0 && y < HEIGHT - 1) {
                        clearIndex(gridPane, stack.get(0) - WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) - WIDTH);
                        clearIndex(gridPane, stack.get(0) - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH);

                        stack.add(stack.get(0) - WIDTH - 1);
                        stack.add(stack.get(0) - WIDTH);
                        stack.add(stack.get(0) - 1);
                        stack.add(stack.get(0) + WIDTH - 1);
                        stack.add(stack.get(0) + WIDTH);
                    } else if (y == 0) {
                        clearIndex(gridPane, stack.get(0) - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH);

                        stack.add(stack.get(0) - 1);
                        stack.add(stack.get(0) + WIDTH - 1);
                        stack.add(stack.get(0) + WIDTH);
                    } else {
                        clearIndex(gridPane, stack.get(0) - WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) - WIDTH);
                        clearIndex(gridPane, stack.get(0) - 1);

                        stack.add(stack.get(0) - WIDTH - 1);
                        stack.add(stack.get(0) - WIDTH);
                        stack.add(stack.get(0) - 1);
                    }
                }
            }

            was.add(stack.get(0));
            stack.remove(0);
            if (was.size() * stack.size() > 0) {
                while (was.indexOf(stack.get(0)) != -1) {
                    if (stack.size() == 1) {
                        stack.remove(0);
                        break;
                    } else {
                        stack.remove(0);
                    }
                }
            }
            if (stack.size() == 0) {
                return;
            }
            y = (int) Math.floor(((double) stack.get(0)) / HEIGHT);
            x = stack.get(0) % HEIGHT;
        }
    }

    private void solve(GridPane gridPane) {
        // int temp = WIDTH;
        /// WIDTH = HEIGHT;
        //  HEIGHT = temp;
        ArrayList<Integer> stack = new ArrayList<>();
        stack.add((int) (Math.random() * WIDTH * HEIGHT));
        int y = (int) Math.floor(((double) stack.get(0)) / HEIGHT);
        int x = stack.get(0) % HEIGHT;
        while (!table[y][x].isRigged()) {
            clearIndex(gridPane, stack.get(0));
            int count = MainOfGameTable.game.getSurroundingBombCount(y, x);
           // System.out.println(stack.get(0));
            if (count == 0) {
                if (x > 0 && x < HEIGHT - 1) {
                    if (y > 0 && y < HEIGHT - 1) {
                        clearIndex(gridPane, stack.get(0) - WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) - WIDTH);
                        clearIndex(gridPane, stack.get(0) - WIDTH + 1);
                        clearIndex(gridPane, stack.get(0) + 1);
                        clearIndex(gridPane, stack.get(0) - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH);
                        clearIndex(gridPane, stack.get(0) + WIDTH + 1);

                        stack.add(stack.get(0) + WIDTH + 1);
                        stack.add(stack.get(0) + WIDTH);
                        stack.add(stack.get(0) + WIDTH - 1);
                        stack.add(stack.get(0) - WIDTH + 1);
                        stack.add(stack.get(0) - WIDTH);
                        stack.add(stack.get(0) - WIDTH - 1);
                        stack.add(stack.get(0) + 1);
                        stack.add(stack.get(0) - 1);
                    } else if (y == 0) {
                        clearIndex(gridPane, stack.get(0) + 1);
                        clearIndex(gridPane, stack.get(0) - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH);
                        clearIndex(gridPane, stack.get(0) + WIDTH + 1);

                        stack.add(stack.get(0) + 1);
                        stack.add(stack.get(0) - 1);
                        stack.add(stack.get(0) + WIDTH - 1);
                        stack.add(stack.get(0) + WIDTH);
                        stack.add(stack.get(0) + WIDTH + 1);
                    } else {
                        clearIndex(gridPane, stack.get(0) - WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) - WIDTH);
                        clearIndex(gridPane, stack.get(0) - WIDTH + 1);
                        clearIndex(gridPane, stack.get(0) + 1);
                        clearIndex(gridPane, stack.get(0) - 1);

                        stack.add(stack.get(0) - WIDTH - 1);
                        stack.add(stack.get(0) - WIDTH);
                        stack.add(stack.get(0) - WIDTH + 1);
                        stack.add(stack.get(0) + 1);
                        stack.add(stack.get(0) - 1);
                    }
                } else if (x == 0) {
                    if (y > 0 && y < HEIGHT - 1) {
                        clearIndex(gridPane, stack.get(0) - WIDTH);
                        clearIndex(gridPane, stack.get(0) - WIDTH + 1);
                        clearIndex(gridPane, stack.get(0) + 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH);
                        clearIndex(gridPane, stack.get(0) + WIDTH + 1);

                        stack.add(stack.get(0) - WIDTH);
                        stack.add(stack.get(0) - WIDTH + 1);
                        stack.add(stack.get(0) + 1);
                        stack.add(stack.get(0) + WIDTH);
                        stack.add(stack.get(0) + WIDTH + 1);
                    } else if (y == 0) {
                        clearIndex(gridPane, stack.get(0) + 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH);
                        clearIndex(gridPane, stack.get(0) + WIDTH + 1);

                        stack.add(stack.get(0) + 1);
                        stack.add(stack.get(0) + WIDTH);
                        stack.add(stack.get(0) + WIDTH + 1);
                    } else {
                        clearIndex(gridPane, stack.get(0) - WIDTH);
                        clearIndex(gridPane, stack.get(0) - WIDTH + 1);
                        clearIndex(gridPane, stack.get(0) + 1);

                        stack.add(stack.get(0) - WIDTH);
                        stack.add(stack.get(0) - WIDTH + 1);
                        stack.add(stack.get(0) + 1);
                    }
                } else {
                    if (y > 0 && y < HEIGHT - 1) {
                        clearIndex(gridPane, stack.get(0) - WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) - WIDTH);
                        clearIndex(gridPane, stack.get(0) - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH);

                        stack.add(stack.get(0) - WIDTH - 1);
                        stack.add(stack.get(0) - WIDTH);
                        stack.add(stack.get(0) - 1);
                        stack.add(stack.get(0) + WIDTH - 1);
                        stack.add(stack.get(0) + WIDTH);
                    } else if (y == 0) {
                        clearIndex(gridPane, stack.get(0) - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) + WIDTH);

                        stack.add(stack.get(0) - 1);
                        stack.add(stack.get(0) + WIDTH - 1);
                        stack.add(stack.get(0) + WIDTH);
                    } else {
                        clearIndex(gridPane, stack.get(0) - WIDTH - 1);
                        clearIndex(gridPane, stack.get(0) - WIDTH);
                        clearIndex(gridPane, stack.get(0) - 1);

                        stack.add(stack.get(0) - WIDTH - 1);
                        stack.add(stack.get(0) - WIDTH);
                        stack.add(stack.get(0) - 1);
                    }
                }
            } else if (count == 8) {
                stack.set(0, (int) (Math.random() * WIDTH * HEIGHT));
            }else if (count == 1){
                //TODO
            }

            was.add(stack.get(0));
            stack.remove(0);
            if (was.size() * stack.size() > 0) {
                while (was.indexOf(stack.get(0)) != -1) {
                    if (stack.size() == 1) {
                        stack.remove(0);
                        break;
                    } else {
                        stack.remove(0);
                    }
                }
            }
            if (stack.size() == 0) {
                stack.add((int) (Math.random() * WIDTH * HEIGHT));
            }
            y = (int) Math.floor(((double) stack.get(0)) / HEIGHT);
            x = stack.get(0) % HEIGHT;
        }
    }

    private void clearIndex(GridPane gridPane, int index) {
        try {
            AnchorPane anchorPane = (AnchorPane) gridPane.getChildren().get(index + 1);
            VBox vBox = (VBox) anchorPane.getChildren().get(0);
            ImageView imageView = (ImageView) anchorPane.getChildren().get(1);
            imageView.setVisible(true);
            vBox.getChildren().get(0).setVisible(false);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
    }
}
