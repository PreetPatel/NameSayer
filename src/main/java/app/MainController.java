package app;

import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private Text creationNameDisplay;

    @FXML
    private JFXButton playButton;

    @FXML
    private JFXButton deleteButton;

    @FXML
    private Text introText;

    @FXML
    private JFXMasonryPane creationsPane;

    private String selectedCreation;

    @FXML
    private void playbuttonhandler() throws Exception{

        PlayCreation.setMediaToPlay(selectedCreation);

        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("PlayCreation.fxml"));
        anchorPane.getChildren().add(newLoadedPane);

    }

    private void deleteCreation(String creationToDelete) {
        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "rm ./library/" + creationToDelete + ".mp4");
            Process process = builder.start();
            loadCreationsOntoPane();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    private void deletebuttonhandler() {
        stackPane.setVisible(true);
        JFXDialogLayout dialogContent = new JFXDialogLayout();
        JFXDialog deleteDialog = new JFXDialog(stackPane,dialogContent,JFXDialog.DialogTransition.CENTER);

        Text header = new Text("Delete " + selectedCreation);
        header.setStyle("-fx-font-size: 30; -fx-font-family: 'Lato Heavy'");
        dialogContent.setHeading(header);

        Text content = new Text("Do you really want to delete this?");
        content.setStyle("-fx-font-size: 25; -fx-font-family: 'Lato Medium'");
        dialogContent.setBody(content);

        JFXButton confirmDelete = new JFXButton();
        confirmDelete.setText("Delete");
        confirmDelete.setStyle("-fx-background-color: #03b5aa; -fx-text-fill: white; -fx-font-family: 'Lato Medium'; -fx-font-size: 25;");
        confirmDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteCreation(selectedCreation);
                deleteDialog.close();
                stackPane.setVisible(false);

            }
        });

        deleteDialog.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
            @Override
            public void handle(JFXDialogEvent event) {
                stackPane.setVisible(false);
            }
        });


        dialogContent.setActions(confirmDelete);
        deleteDialog.show();

    }

    private boolean checkFileExists(String name) {
        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ls ./library -1 | grep -i " + name + ".mp4");
            Process process = builder.start();
            InputStream stdout = process.getInputStream();
            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
            String line = null;
            if((line = stdoutBuffered.readLine()) != null ) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @FXML
    private void createButtonHandler() {
        stackPane.setVisible(true);
        JFXDialogLayout dialogContent = new JFXDialogLayout();
        JFXDialog createDialog = new JFXDialog(stackPane,dialogContent,JFXDialog.DialogTransition.CENTER);

        Text header = new Text("Add new Creation");
        header.setStyle("-fx-font-size: 30; -fx-font-family: 'Lato Heavy'");
        dialogContent.setHeading(header);

        Text content = new Text("Please Enter A Name For This Creation:");
        content.setStyle("-fx-font-size: 25; -fx-font-family: 'Lato Medium'");
        dialogContent.setBody(content);

        JFXTextField field = new JFXTextField();
        field.setPromptText("Lets get started with a memorable name");
        dialogContent.setBody(field);
        JFXButton nextStep = new JFXButton();
        nextStep.setDisable(true);

            field.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (!field.getText().matches("^[\\w\\-. ]+$") || field.getText().isEmpty() || checkFileExists(field.getText())) {
                        nextStep.setDisable(true);
                        field.setStyle("-fx-background-color: #ff4b52;");
                        field.setPromptText("Please type a valid name");
                    } else {
                        field.setStyle("-fx-background-color: white;");
                        nextStep.setDisable(false);
                    }
                }
            });



        nextStep.setText("Next");
        nextStep.setStyle("-fx-background-color: #03b5aa; -fx-text-fill: white; -fx-font-family: 'Lato Medium'; -fx-font-size: 25;");
        nextStep.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stackPane.setVisible(false);
                try {
                    CreateCreation.setNameOfCreation(field.getText());
                    Pane newLoadedPane = FXMLLoader.load(getClass().getResource("CreateCreation.fxml"));
                    anchorPane.getChildren().add(newLoadedPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        createDialog.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
            @Override
            public void handle(JFXDialogEvent event) {
                stackPane.setVisible(false);
            }
        });


        dialogContent.setActions(nextStep);
        createDialog.show();
    }


    @FXML
    private void creationsbuttonhandler(ActionEvent e) {
        introText.setVisible(false);
        playButton.setVisible(true);
        deleteButton.setVisible(true);
        String text = ((JFXButton)e.getSource()).getId();
        selectedCreation = text;
        creationNameDisplay.setText(selectedCreation);


    }

    private void loadCreationsOntoPane() {
        creationsPane.getChildren().clear();
        stackPane.setVisible(false);
        playButton.setVisible(false);
        deleteButton.setVisible(false);
        introText.setVisible(true);
        try {
            ProcessBuilder builder = new ProcessBuilder("./src/main/resources/scripts/checkDir.sh");
            Process process = builder.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        ObservableList<JFXButton> creationsList = FXCollections.<JFXButton>observableArrayList();

        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ls ./library -1 | sed -e 's/\\..*$//'");
            Process process = builder.start();
            InputStream stdout = process.getInputStream();
            InputStream stderr = process.getErrorStream();
            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
            String line = null;
            while ((line = stdoutBuffered.readLine()) != null )
            {
                JFXButton button = new JFXButton();
                button.setText(line);
                button.setId(line);
                button.setStyle("-fx-background-color: #03b5aa; -fx-text-fill: white; -fx-font-family: 'Lato Medium'; -fx-font-size: 25;");
                button.setOnAction(this::creationsbuttonhandler);
                creationsList.add(button);

            }
            creationsPane.getChildren().addAll(creationsList);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        loadCreationsOntoPane();
    }

}
