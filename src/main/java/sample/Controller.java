package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import model.Constants;
import service.DataUtiles;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private final String BASE_PATH = "basePath";
    @FXML
    public Button button;
    @FXML
    public Button buttonStart;
    @FXML
    public Button buttonSearchWhithExtention;
    @FXML
    public Button buttonSetBasePath;
    @FXML
    public CheckBox checkBoxJava;
    @FXML
    public TextField textFieldBasePath;
    @FXML
    public TextField textFieldSource;
    @FXML
    public TextField textFieldWord;
    @FXML
    public TextArea textAreaMessage;
    @FXML
    HTMLEditor htmlEditor;
    private List<String> listResult;
    private int count;
    private String lookingFor;
    private String basePath;
    private DataUtiles dataUtiles;
    private String extention;


    public void initialize() {
        lookingFor = "";

        Properties p;
        basePath = null;
        try {
            dataUtiles = new DataUtiles();
            basePath = dataUtiles.getProperties(BASE_PATH);
            if (basePath == null) {
                basePath = "D:\\";
            }
            textFieldBasePath.setText(basePath);
            textFieldSource.setText(basePath);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void fileOpen() {
        File directory = getPathFromDirectoryChooser();
        if (directory == null || !directory.exists()) {
            return;
        }
        textFieldSource.setText(directory.getPath());
    }

    private File getPathFromDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(basePath));
        return directoryChooser.showDialog(null);
    }

    public void onCreateBasePathSetting() throws IOException {
        File directory = getPathFromDirectoryChooser();

        dataUtiles.saveProperties(BASE_PATH, directory.getPath());
        basePath = directory.getPath();
        textFieldBasePath.setText(basePath);
        textFieldSource.setText(basePath);
    }

    private void setExtention() {
        boolean result = checkBoxJava.isSelected();
        extention = (result) ? Constants.EXTENTION_JAVA : Constants.EMPTY;
    }

    public void onStart() {
        setExtention();
        listResult = new ArrayList<>();
        Runnable st = () -> {
            System.out.println(LocalTime.now() + " Start for \"" + lookingFor + "\" " + " at " + LocalTime.now() + "---------------------------------------------------------------------------------------------------------\n");
            System.out.println(LocalTime.now() + "-----------------------------------------------------------------------------------------------------------------------------------------------------\n");
            lookingFor = textFieldWord.getText();
            addMessage("Start for " + lookingFor + " " + "at" + LocalTime.now() + "---------------------------------------------------------------------------------------------------------\n");
            if (lookingFor != null && !lookingFor.isEmpty()) {
                try {
                    Files.walk(Paths.get(textFieldSource.getText()))
                            .filter(Files::isRegularFile).forEach((one) -> {
                        try {
                            readLines(one);
                        } catch (IOException e) {
                            // e.printStackTrace();
                        }
                    });
                } catch (Exception e) {

                    addMessage(e.getMessage() + "\n");
                }
            } else {
                addMessage("The text or path field is empty" + "\n");
                return;
            }
            addMessage("\n" + "Stop for \"" + lookingFor + "\" " + " at " + LocalTime.now());
            System.out.println(LocalTime.now() + " Stop for " + lookingFor + " " + " at " + LocalTime.now() + "---------------------------------------------------------------------------------------------------------\n");
//        System.out.println(LocalTime.now());
//            textAreaMessage.appendText(LocalTime.now().toString());
//            textAreaMessage.appendText("\n");
//            for(String one:listResult) {
//                textAreaMessage.appendText(one);
//                textAreaMessage.appendText("\n");
//            }
//            textAreaMessage.appendText(LocalTime.now().toString());
//            textAreaMessage.appendText("\n");
        };
        Thread start = new Thread(st);
        start.start();
    }


    private void readLines(Path path) throws IOException {
        if (!path.toString().endsWith(extention)) {
            return;
        }
        List<String> list = new ArrayList<>();
        Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1);
        List<String> listAll = lines.collect(Collectors.toList());
        for (int i = 0; i < listAll.size(); i++) {
            if (listAll.get(i) != null && listAll.get(i).toLowerCase().contains(lookingFor.toLowerCase())) {
                list.add("" + i + "    " + listAll.get(i));
            }
        }
        if (list.size() > 0) {

            listResult.add(path.toString());
            System.out.println(path.toString());
            addMessage(path.toString());
            for (String one : list) {
                this.count = count + 1;
                System.out.println(" " + count + ":  " + one);
                addMessage("\n" + count + ":  " + one);
                listResult.add(" " + count + ":  " + one);
            }
            System.out.println("--------------");
            addMessage("\n" + "--------------");
            listResult.add("--------------");
        }

    }

    private void addMessage(String line) {
        Platform.runLater(() -> textAreaMessage.appendText(line));
    }

}