package UI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Timer implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    private int i;

    //public static boolean fl = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timeline timeline = new Timeline();

        Label time = new Label("00:00");
        time.setFont(Font.font(50));
        anchorPane.getChildren().add(time);

        Duration totalDelay = Duration.ZERO;
        for (i = 0; i < 6000; ++i) {
            String value = "";
            if (i < 600){
                value = "0";
            }
            value += String.valueOf(i / 60);
            if (i > 60*99){
                value = "99";
            }
            value += ":";
            if (i % 60 < 10){
                value += "0";
            }
            value += String.valueOf(i % 60);
            if (i > 60*99 + 59){
                value = value.substring(0 , value.length() - 2);
                value += "59";
            }
            final String kar = value;
            KeyFrame frame = new KeyFrame(totalDelay, e -> time.setText(kar));
            timeline.getKeyFrames().add(frame);
            totalDelay = totalDelay.add(new Duration(1000));
        }
        timeline.play();

    }
}
