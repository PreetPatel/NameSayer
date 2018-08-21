package app;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringValidator {

    private String fileName;
    public StringValidator(String filename) {
        fileName = filename;
    }

    public boolean isValid() {
        boolean invalidName = fileName.contains("_audio") || fileName.contains("_video");
        boolean validInput = fileName.matches("^[\\w\\-. ]+$");
        boolean empty = fileName.isEmpty();
        boolean fileExists = checkFileExists(fileName);
        if (!validInput || empty || fileExists || invalidName) {
            return false;
        }else {
            return true;
        }
    }

    private boolean checkFileExists(String name) {
        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ls "+ NameSayer.creationsPath +" -1 | grep -i " + name + ".mp4");
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
            JOptionPane.showMessageDialog(null, "An Error occurred while trying to continue: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        return false;
    }
}
