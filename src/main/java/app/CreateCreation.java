package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

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
    private JFXProgressBar progressBar;

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
        progressBar.setVisible(false);
    }

    @FXML
    private void startRecord() throws IOException, InterruptedException {
        record.setDisable(true);
        record.setText("Recording...");
        progressBar.setVisible(true);
        close.setDisable(true);
        Thread thread = new Thread(new createAudioFile());
        thread.start();
        record.setVisible(false);
        progressBar.setVisible(false);

    }

    private class createAudioFile extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -t 5 -f alsa -ac 2 -i default ./library/"+ _nameOfCreation + "_audio.flv");
            Process process = builder.start();
            return null;
        }
    }

}
