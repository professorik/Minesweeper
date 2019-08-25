package UI;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Game;
import model.Piece;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    private MenuItem newGame;

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

    private Button restart;
    private int cFlags = MainOfGameTable.game.getTotalBombCount();
    private Label countOfFlags;

    private Parent node;

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
        getInstruction.setOnAction(event -> showInstructionDialog());
        newGame.setOnAction(event -> startGame());
    }

    private void startGame() {
        hat.getChildren().clear();
        was = new ArrayList<>();

        node = null;
        try {
            node = FXMLLoader.load(getClass().getResource("/UI/timer.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        node.setPickOnBounds(true);

        HBox pane0 = new HBox();
        HBox pane1 = new HBox();

        ImageView flag = new ImageView(new Image("flag2.png" , 64 ,64 , true, true , false));
        ImageView clock = new ImageView(new Image("clock.png" , 64 ,64 , true, true , false));
        restart = new Button(); restart.setFocusTraversable(false);
        restart.setOnAction(event -> startGame());
        restart.setGraphic(new ImageView(new Image("smiling.png" , 64 , 64 ,true, true, false)));
        countOfFlags = new Label(String.valueOf(cFlags)); countOfFlags.setFont(Font.font(50));

        pane0.getChildren().addAll(flag, countOfFlags);
        pane1.getChildren().addAll(clock, node);

        hat.getChildren().addAll(pane0, restart, pane1);
        hat.setSpacing(240 / String.valueOf(cFlags).length());
        hat.setPadding(new Insets( 15 , 50 ,0 , 50));

        cFlags = MainOfGameTable.game.getTotalBombCount();
        countOfFlags.setText(String.valueOf(cFlags));
        count = 0;
        table = MainOfGameTable.game.getField();

        GridPane root = new GridPane();
        if (WIDTH < 16 && HEIGHT < 16){
            root.setTranslateX(1080 / WIDTH);
            root.setTranslateY(1080 / HEIGHT);
        }

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

                Image flagImg = new Image("flag2.png", size / 2, size / 2, true, true, true);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    int y = (int) Math.floor(((double) index) / HEIGHT);
                    int x = index % HEIGHT;
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        if (table[y][x].isFlagged()){
                            table[y][x].toggleFlag();
                            ++cFlags;
                            countOfFlags.setText(String.valueOf(cFlags));
                            button.setGraphic(null);
                        }else if (cFlags > 0) {
                            table[y][x].toggleFlag();
                            --cFlags;
                            countOfFlags.setText(String.valueOf(cFlags));
                            button.setGraphic(new ImageView(flagImg));
                        }
                     //   solve(root);
                    } else if (!table[y][x].isFlagged()){
                        if (fl){
                            for (int k = 0; k < WIDTH; k++) {
                                for (int l = 0; l < HEIGHT; l++) {
                                      clearIndex(root, k * HEIGHT + l);
                                }
                            }
                            stopGame(false);
                        }else {
                            cleanFromZero(index, root);
                            if (MainOfGameTable.game.getTotalBombCount() == WIDTH * HEIGHT - count){
                                stopGame(true);
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

    private void stopGame(boolean fl){
        if (fl){
            restart.setGraphic(new ImageView(new Image("happy.png" , 64 , 64 ,true, true, true)));
        }else{
            restart.setGraphic(new ImageView(new Image("sad.png" , 64 , 64 ,true, true, false)));
        }
        Label score = new Label(((Label)node.getChildrenUnmodifiable().get(0)).getText()); score.setFont(Font.font(50));
        ((HBox)hat.getChildren().get(2)).getChildren().remove(node);
        ((HBox)hat.getChildren().get(2)).getChildren().add(score);
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

    private void showInstructionDialog() {
        final Stage dialogStage = new Stage();
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Instruction");
        dialogStage.getIcons().add(new Image("document.png"));

        Button noBtn = new Button("Cancel");

        noBtn.setOnAction(event -> dialogStage.close());

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setMinHeight(400);

        try (FileReader reader = new FileReader("res\\instruction.txt")) {
            int c;
            String kar = "";
            while ((c = reader.read()) != -1) {
                kar += String.valueOf((char) c);
            }
            textArea.setText(kar);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox.setSpacing(10.0);
        hBox.getChildren().addAll(noBtn);

        VBox vBox = new VBox();
        vBox.setSpacing(10.0);
        vBox.getChildren().addAll(textArea , hBox);
        vBox.setPadding(new Insets(10, 10, 0, 10));

        dialogStage.setScene(new Scene(vBox));
        dialogStage.show();
    }

    private void cleanFromZero(int index, GridPane gridPane){
        ArrayList<Integer> stack = new ArrayList<>();
        stack.add(index);
        int y = (int) Math.floor(((double) stack.get(0)) / HEIGHT);
        int x = stack.get(0) % HEIGHT;
        while (!table[y][x].isRigged()) {
            ++count;
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
