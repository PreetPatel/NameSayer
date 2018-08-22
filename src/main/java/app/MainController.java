package app;

import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import javax.swing.*;
import java.io.*;

public class MainController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private StackPane stackPane;

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
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "rm " + NameSayer.creationsPath +"/'" + creationToDelete + "'.*");
            Process process = builder.start();
            loadCreationsOntoPane();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"An error occurred: "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
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

    @FXML
    private void createButtonHandler() {

                stackPane.setVisible(true);
                JFXDialogLayout dialogContent = new JFXDialogLayout();
                JFXDialog createDialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.CENTER);

                Text header = new Text("Add new Creation");
                header.setStyle("-fx-font-size: 30; -fx-font-family: 'Lato Heavy'");
                dialogContent.setHeading(header);

                Text content = new Text("Please Enter A Name For This Creation:");
                content.setStyle("-fx-font-size: 25; -fx-font-family: 'Lato Medium'");
                dialogContent.setBody(content);

                JFXTextField field = new JFXTextField();
                field.setPromptText("Enter name here");
                dialogContent.setBody(field);
                JFXButton nextStep = new JFXButton();
                nextStep.setDisable(true);

                field.setOnKeyReleased(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {

                        String fileName = field.getText().trim();
                        StringValidator stringValidator = new StringValidator(fileName);

                        if (!stringValidator.isValid()) {
                            nextStep.setDisable(true);
                            field.setStyle("-fx-background-color: #ff4b52;");
                            field.setPromptText("Please type a valid name");
                        } else {
                            field.setStyle("-fx-background-color: white;");
                            nextStep.setDisable(false);
                            if (event.getCode() == KeyCode.ENTER) {
                                gotoCreateCreation(field.getText());
                            }
                        }
                    }
                });

                nextStep.setText("Next");
                nextStep.setStyle("-fx-background-color: #03b5aa; -fx-text-fill: white; -fx-font-family: 'Lato Medium'; -fx-font-size: 25;");
                nextStep.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        gotoCreateCreation(field.getText());
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

    private void gotoCreateCreation(String text) {
        stackPane.setVisible(false);
        try {
            CreateCreation.setNameOfCreation(text);
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("CreateCreation.fxml"));
            anchorPane.getChildren().add(newLoadedPane);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An Error occurred while trying to continue: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    @FXML
    private void creationsbuttonhandler(ActionEvent e) {
        playButton.setVisible(true);
        deleteButton.setVisible(true);
        String text = ((JFXButton)e.getSource()).getText();
        selectedCreation = text;
        introText.setText(selectedCreation);


    }

    private void loadCreationsOntoPane() {
        creationsPane.getChildren().clear();
        stackPane.setVisible(false);
        playButton.setVisible(false);
        deleteButton.setVisible(false);
        introText.setVisible(true);
        introText.setText("Select one of the following creations to get started");

            File storage = new File(NameSayer.creationsPath);
            if (!storage.exists()) {
                if (!storage.mkdirs()) {
                    JOptionPane.showMessageDialog(null, "An Error occurred while trying to load creations ", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "rm " + NameSayer.creationsPath +"/*_audio.*; rm " + NameSayer.creationsPath +"/*_video.*");
            Process process = builder.start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An Error occurred while trying to continue: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        ObservableList<JFXButton> creationsList = FXCollections.<JFXButton>observableArrayList();

        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ls " + NameSayer.creationsPath +"/ -1 | sed -e 's/\\..*$//'");
            Process process = builder.start();
            InputStream stdout = process.getInputStream();
            InputStream stderr = process.getErrorStream();
            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
            String line;
            while ((line = stdoutBuffered.readLine()) != null )
            {
                JFXButton button = new JFXButton();
                button.setMnemonicParsing(false);
                button.setText(line);
                button.setId(line);
                button.setStyle("-fx-background-color: #03b5aa; -fx-text-fill: white; -fx-font-family: 'Lato Medium'; -fx-font-size: 25;");
                button.setOnAction(this::creationsbuttonhandler);
                creationsList.add(button);

            }
            creationsPane.getChildren().addAll(creationsList);


        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An Error occurred while trying to continue: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void initialize() {
        loadCreationsOntoPane();
    }

}
