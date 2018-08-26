/**
 * Namesayer.java
 * Entry point for app. Initialises the first scene
 *
 * Copyright Preet Patel, 2018
 * @Author Preet Patel
 * Date Created: 13 August, 2018
 */

package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NameSayer extends Application {

    /** Path for storing universal creations */
    public static final String creationsPath = System.getProperty("user.home") + "/Documents/NameSayer";

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadScene("Home.fxml", primaryStage);
    }

    /** Loads FXML onto the scene and renders the first scene: MainController.java */
    public void loadScene(String source, Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource(source));
        Parent layout = loader.load();
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("NameSayer");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
