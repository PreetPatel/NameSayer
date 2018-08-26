/**
 * StringValidator.java
 * Provides methods for a string such as if it is a valid format, has a file with that string name and allows
 * a file to be deleted
 *
 * Copyright Preet Patel, 2018
 * @Author Preet Patel
 * Date Created: 13 August, 2018
 */

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
        boolean fileExists = checkFileExists();
        if (!validInput || empty || fileExists || invalidName) {
            return false;
        }else {
            return true;
        }
    }

    public boolean checkFileExists() {
        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ls " + NameSayer.creationsPath +"/ -1 | grep -i '" + fileName + ".mp4'");
            Process process = builder.start();
            InputStream stdout = process.getInputStream();
            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
            String line = null;
            if((line = stdoutBuffered.readLine()) != null && !fileName.equalsIgnoreCase("")) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An Error occurred while trying to continue: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public void deleteFile() {
        if (checkFileExists()) {
            try {
                ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "find " + NameSayer.creationsPath + " -maxdepth 1 -iname \"" + fileName + ".mp4" + "\" -exec rm {} \\; ");
                builder.start();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "An Error occurred while trying to continue: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
