package app;

import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.AreaChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import java.io.File;
import java.io.IOException;

public class CreateCreation {

    @FXML
    private JFXButton close;

    @FXML
    private JFXButton record;

    @FXML
    private Text loaderText;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AreaChart<Number,Number> ac;

    @FXML
    private JFXButton listenAudio;

    @FXML
    private JFXButton keepAudio;

    @FXML
    private JFXButton redoAudio;


    private static String _nameOfCreation;

    public static void setNameOfCreation(String nameOfCreation) {
        _nameOfCreation = nameOfCreation;
    }

    @FXML
    private void handleCloseButton() throws Exception{
        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("Home.fxml"));
        anchorPane.getChildren().add(newLoadedPane);
    }


    @FXML
    private void initialize() {
        loaderText.setText("Press the button below and pronounce the name: \"" + _nameOfCreation + "\"");
        listenAudio.setVisible(false);
        keepAudio.setVisible(false);
        redoAudio.setVisible(false);
    }

    @FXML
    private void startRecord() throws IOException, InterruptedException {
        record.setDisable(true);
        record.setText("Recording...");
        close.setDisable(true);
        Thread thread = new Thread(new createAudioFile());
        thread.start();
        record.setVisible(false);
        Thread buttonThread = new Thread(new showButtons());
        buttonThread.start();

    }

    private class showButtons extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            Thread.sleep(5000);
            listenAudio.setVisible(true);
            keepAudio.setVisible(true);
            redoAudio.setVisible(true);
            return null;
        }
    }

    @FXML
    private void playAudio() {
        Thread playAudio = new Thread(new playAudioFile());
        playAudio.start();

    }

    private class playAudioFile extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            String path = "library/" + _nameOfCreation + "_audio.mp3";
            File file = new File(path);
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.play();
                }
            });
            return null;
        }
    }

    private class createAudioFile extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -t 5 -f alsa -ac 2 -i default ./library/"+ _nameOfCreation + "_audio.mp3");
            Process process = builder.start();
            return null;
        }
    }

    @FXML
    private void redoButtonHandler() throws Exception {

        Thread deleteAudio = new Thread(new deleteAudioFile());
        deleteAudio.start();
        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("CreateCreation.fxml"));
        anchorPane.getChildren().add(newLoadedPane);
    }

    private class deleteAudioFile extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "rm ./library/"+ _nameOfCreation + "_audio.mp3");
            Process process = builder.start();
            return null;
        }
    }

    @FXML
    private void keepButtonHandler() throws Exception {
        Thread merge = new Thread(new mergeAudioVideo());
        merge.start();
        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("Home.fxml"));
        anchorPane.getChildren().add(newLoadedPane);

    }

    private class mergeAudioVideo extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            String command = "ffmpeg -f lavfi -i color=c=white:s=1920x1080:d=5 -vf \"drawtext=fontsize=60: " +
                    "fontcolor=black:x=(w-text_w)/2:y=(h-text_h)/2:text='" + _nameOfCreation +"'\" ./library/"+ _nameOfCreation +"_video.mp4 2>/dev/null && " +
                    "ffmpeg -i ./library/"+ _nameOfCreation +"_video.mp4 -i ./library/"+ _nameOfCreation +"_audio.mp3 -codec copy -shortest " +
                    "./library/"+ _nameOfCreation +".mp4 2> /dev/null && " +
                    "rm ./library/"+ _nameOfCreation +"_video.mp4 2>/dev/null && " +
                    "rm ./library/"+ _nameOfCreation +"_audio.mp3 2>/dev/null";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);
            Process process = builder.start();
            return null;
        }
    }

}
