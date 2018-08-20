package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

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

    @FXML
    private JFXButton pauseButton;

    private static String mediaToPlay;

    private MediaPlayer mediaPlayer;

    public static void setMediaToPlay(String media) {
        mediaToPlay = media;
    }

    public void playHandler() {
        mediaPlayer.play();
    }

    public void pauseHandler() {
        mediaPlayer.stop();
    }

    public void backHandler() throws Exception{
        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("Home.fxml"));
        anchorPane.getChildren().add(newLoadedPane);
    }

    @FXML
    public void initialize() {
        String path = "library/" + mediaToPlay + ".mp4";
        File file = new File(path);
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        view.setMediaPlayer(mediaPlayer);
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {

                videoslider.setMax(media.getDuration().toSeconds() - 0.3);

                mediaPlayer.play();

                InvalidationListener sliderChangeListener = o-> {
                    Duration seekTo = Duration.seconds(videoslider.getValue());
                    mediaPlayer.seek(seekTo);
                };

                videoslider.valueProperty().addListener(sliderChangeListener);

                mediaPlayer.currentTimeProperty().addListener( i -> {
                    videoslider.valueProperty().removeListener(sliderChangeListener);
                    Duration currentTime = mediaPlayer.getCurrentTime();
                    int value = (int) currentTime.toSeconds();
                    videoslider.setValue(value);
                    durationText.setText(Double.toString(round(currentTime.toSeconds())));

                    videoslider.valueProperty().addListener(sliderChangeListener);

                });
            }
        });

    }
}
