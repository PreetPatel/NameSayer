package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.round;

public class PlayCreation {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MediaView view;

    @FXML
    private JFXSlider videoslider;

    @FXML
    private Text durationText;

    @FXML
    private JFXButton playButton;

    private static String mediaToPlay;

    private MediaPlayer mediaPlayer;

    private Duration duration = Duration.seconds(0);

    public static void setMediaToPlay(String media) {
        mediaToPlay = media;
    }

    public void playHandler() {
        mediaPlayer.setStartTime(duration);
        mediaPlayer.play();
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pauseHandler();
            }
        });
        playButton.setText("PAUSE");
    }

    public void pauseHandler() {
        duration = mediaPlayer.getCurrentTime();
        mediaPlayer.pause();
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playHandler();
            }
        });
        playButton.setText("PLAY");
    }

    public void backHandler() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Pane newLoadedPane = null;
        try {
            newLoadedPane = FXMLLoader.load(getClass().getResource("Home.fxml"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An Error occurred while trying to continue: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
        anchorPane.getChildren().add(newLoadedPane);
    }

    @FXML
    public void initialize() {

        String path = "/" + NameSayer.creationsPath +"/" + mediaToPlay + ".mp4";
        File file = new File(path);
       try {
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            view.setMediaPlayer(mediaPlayer);
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {

                    videoslider.setMax(media.getDuration().toSeconds());
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    mediaPlayer.play();

                    InvalidationListener sliderChangeListener = o -> {
                        Duration seekTo = Duration.seconds(videoslider.getValue());
                        mediaPlayer.seek(seekTo);
                    };

                    videoslider.valueProperty().addListener(sliderChangeListener);

                    mediaPlayer.currentTimeProperty().addListener(i -> {
                        videoslider.valueProperty().removeListener(sliderChangeListener);
                        Duration currentTime = mediaPlayer.getCurrentTime();
                        int value = (int) currentTime.toSeconds();
                        videoslider.setValue(value);
                        durationText.setText(Double.toString(round(currentTime.toSeconds())));

                        videoslider.valueProperty().addListener(sliderChangeListener);

                    });
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An Error occurred while trying to play this file.", "Error", JOptionPane.ERROR_MESSAGE);
            backHandler();
        }

    }
}
