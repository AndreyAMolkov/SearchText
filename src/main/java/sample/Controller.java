package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import model.Constants;
import model.Line;
import service.DataUtiles;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    private static final String BASE_PATH = "basePath";
    private static final String EXTENSION_FILE = "extensionOfFile";
    private static final String TO_LOWER_CASE = "toLowerCase";
    private static final String PATH_WITH_TEST = "pathWithTest";
    private static final String DON_T_USE_EXTENSION = "dontUseExtension";
    @FXML
    public Label labelCount;
    @FXML
    public Button button;
    @FXML
    public Button buttonStart;
    @FXML
    public Button buttonClearTextField;
    @FXML
    public Button buttonSetBasePath;
    @FXML
    public TabPane tabPaneGeneral;


    @FXML
    public TableView tableView;
    @FXML
    public CheckBox checkBoxJava;
    @FXML
    public CheckBox checkBoxCustom;
    @FXML
    public CheckBox checkBoxLowCase;
    @FXML
    public CheckBox checkBoxPathWithTest;
    @FXML
    public CheckBox checkBoxAddForNameTab;
    @FXML
    public TextField textFieldBasePath;
    @FXML
    public TextField textFieldSource;
    @FXML
    public TextField textFieldWord;
    @FXML
    public TextField textFieldCustomExtension;
    @FXML
    public TextField textFieldDontUseExtesion;
    @FXML
    public TextArea textAreaMessage;

    private List<String> listResult;
    private int count;
    private String lookingFor;
    private String basePath;
    private Boolean extensionOfJava;
    private boolean toLowCase;
    private DataUtiles dataUtiles;
    private String extension;
    private List<String> dontUseExtensionList;
    private Boolean pathWithTest;
    private ObservableList<Line> listObservable;
    private File file;
    private Integer countLabel;
    private List<Tab> listTab;

    public static void autoResizeColumns(TableView<?> table) {
        //Set the right policy
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().stream().forEach(column ->
        {
            //Minimal width = columnheader
            Text t = new Text(column.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < table.getItems().size(); i++) {
                //cell must not be empty
                if (column.getCellData(i) != null) {
                    t = new Text(column.getCellData(i).toString());
                    double calcwidth = t.getLayoutBounds().getWidth();
                    //remember new max-width
                    if (calcwidth > max) {
                        max = calcwidth;
                    }
                }
            }
            //set the new max-widht with some extra space
            column.setPrefWidth(max + 10.0d);
        });
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
        dataUtiles.saveProperties(EXTENSION_FILE, Boolean.toString(checkBoxJava.isSelected()));
        dataUtiles.saveProperties(TO_LOWER_CASE, Boolean.toString(checkBoxLowCase.isSelected()));
        dataUtiles.saveProperties(PATH_WITH_TEST, Boolean.toString(checkBoxPathWithTest.isSelected()));
        dataUtiles.saveProperties(DON_T_USE_EXTENSION, textFieldDontUseExtesion.getText().trim());
        basePath = directory.getPath();
        textFieldBasePath.setText(basePath);
        textFieldSource.setText(basePath);
    }

    public void onSetExtensionJava() {
        checkBoxCustom.setSelected(false);
        boolean result = checkBoxJava.isSelected();
        if (result) {
            //         checkBoxCustom.setSelected(false);
            extension = Constants.EXTENTION_JAVA;
        } else {
            extension = Constants.EMPTY;
        }
    }

    private void setDontUseExtension() {
        String text = textFieldDontUseExtesion.getText();


        if (isNullOrEmpty(text)) {
            dontUseExtensionList = null;

        } else {
            dontUseExtensionList = Arrays.asList(text.split(Constants.SEPARATOR));
        }
    }

    private boolean isNullOrEmpty(String line) {
        boolean result = false;
        if (line == null || line.isEmpty() || " ".contains(line.trim())) {
            result = true;
        }

        return result;
    }

    public void onSetExtensionCustom() {
        boolean result = checkBoxCustom.isSelected();
        if (result) {
            //           checkBoxJava.setSelected(false);
            extension = textFieldCustomExtension.getText();
        } else {
            extension = Constants.EMPTY;
        }
    }
    public void setToLowerCase() {
        toLowCase = checkBoxLowCase.isSelected();
    }

    public void setPathWithTest() {
        pathWithTest = checkBoxPathWithTest.isSelected();
    }

    public void initialize() {
        countLabel = 0;
        file = null;
        lookingFor = "";
        extensionOfJava = true;
        toLowCase = true;
        pathWithTest = true;
        basePath = null;
        extension = Constants.EMPTY;
        listTab = new ArrayList<>();
        dontUseExtensionList = null;
        try {
            dataUtiles = new DataUtiles();
            basePath = dataUtiles.getProperties(BASE_PATH);
            if (basePath == null) {
                basePath = "D:\\";
            }
            if (dataUtiles.getProperties(EXTENSION_FILE) != null) {
                extensionOfJava = Boolean.parseBoolean(dataUtiles.getProperties(EXTENSION_FILE));
            }
            if (dataUtiles.getProperties(TO_LOWER_CASE) != null) {
                toLowCase = Boolean.parseBoolean(dataUtiles.getProperties(TO_LOWER_CASE));
            }
            if (dataUtiles.getProperties(PATH_WITH_TEST) != null) {
                pathWithTest = Boolean.parseBoolean(dataUtiles.getProperties(PATH_WITH_TEST));
            }
            if (dataUtiles.getProperties(DON_T_USE_EXTENSION) != null) {
                String line = dataUtiles.getProperties(DON_T_USE_EXTENSION);
                dontUseExtensionList = Arrays.asList(line.split(Constants.SEPARATOR));
                textFieldDontUseExtesion.setText(line);
            }
            textFieldBasePath.setText(basePath);
            textFieldSource.setText(basePath);
            checkBoxJava.setSelected(extensionOfJava);
            checkBoxLowCase.setSelected(toLowCase);

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            addMessage("\n" + e.getMessage());
        }
    }

    public void onClear() {

        textAreaMessage.setText("");
        removeTabAll();
    }

    private void removeTabAll() {
        if (tabPaneGeneral.getTabs().size() > 1) {
            tabPaneGeneral.getTabs().remove(1, tabPaneGeneral.getTabs().size());
        }

    }
    public void onStart() {
        countLabel = 0;
        setDontUseExtension();
        if (checkBoxCustom.isSelected()) {
            onSetExtensionCustom();
        }
        if (checkBoxJava.isSelected()) {
            onSetExtensionJava();
        }

        listResult = new ArrayList<>();
        lookingFor = textFieldWord.getText();
        createTableColumn();
        Runnable first = () -> {
            try {
                setCountLabel();
            } catch (IOException e) {
                e.printStackTrace();
                addMessage(e.getMessage() + "\n");
            }
        };
        Thread prepare = new Thread(first);
        try {
            prepare.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            addMessage(e.getMessage() + "\n");
        }
        Runnable st = () -> {

            print("Start for " + Constants.SEPARATOR + lookingFor + Constants.SEPARATOR + "at " + LocalTime.now() + "----------------------------------------------------------------------");
            if (lookingFor != null && !lookingFor.isEmpty()) {
                try {
                    setCountLabel();
                } catch (IOException e) {
                    e.printStackTrace();
                    addMessage(e.getMessage() + "\n");
                }

                try {
                    Files.walk(Paths.get(textFieldSource.getText()))
                            .filter(Files::isRegularFile)
                            .forEach(one -> readLines(one));
                } catch (Exception e) {
                    e.printStackTrace();
                    addMessage(e.getMessage() + "\n");
                }
            } else {
                print("The text or path field is empty");
                return;
            }
            print("Stop for " + Constants.SEPARATOR + lookingFor + Constants.SEPARATOR + "at " + LocalTime.now() + "----------------------------------------------------------------------");
            print("");
            print("");
        };
        Thread start = new Thread(st);
        start.start();
    }

    public void setCountLabel() throws IOException {
        Platform.runLater(() -> labelCount.setText("working"));
        this.countLabel = (int) Files.walk(Paths.get(textFieldSource.getText())).count();
        addMessageLabelCount(countLabel, true);
    }


    private void addMessage(String line) {
        Platform.runLater(() -> textAreaMessage.appendText(line));
    }

    private void addMessageLabelCount(Integer count, boolean set) {
        if (!set && count % 1000 == 0) {
            Platform.runLater(() -> labelCount.setText(count.toString()));
        }
    }

    private boolean isWrongPath(Path path) {
        boolean result = false;
        String pathString = path.toString();
        if (dontUseExtensionList != null) {
            for (String exclude : dontUseExtensionList) {
                if (pathString.contains(exclude)) {
                    result = true;
                }
            }

        }
        return result;
    }
    private void readLines(Path path) {
        addMessageLabelCount(countLabel--, false);
        if (isWrongPath(path) || !path.toString().endsWith(extension) || (!pathWithTest && path.toString().toLowerCase().contains(Constants.TEST))) {
            return;
        }

        List<String> list = new ArrayList<>();
        List<String> listAll;
        try {
            listAll = Files
                    .readAllLines(path, StandardCharsets.ISO_8859_1)
                    .stream()
                    .collect(Collectors.toList());
            for (int i = 0; i < listAll.size(); i++) {
                if (listAll.get(i) != null && toFindText(listAll.get(i))) {
                    list.add("" + (i +1) + Constants.SEPARATOR + listAll.get(i));
                }
            }
            if (!list.isEmpty()) {
                print(path.toString());
                for (String one : list) {
                    this.count = count + 1;
                    print(" " + count + Constants.SEPARATOR + one);
                }
                print("--------------");
            }
        } catch (IOException e) {
            addMessage(e.getMessage() + "  for - " + path.toString());
        }


    }

    private boolean isLowCase() {
        return checkBoxLowCase.isSelected();
    }


    private boolean toFindText(String line) {
        String modifiedLookingFor = lookingFor;
        String modifiedLine = line;
        if (isLowCase()) {
            modifiedLine = line.toLowerCase();
            modifiedLookingFor = lookingFor.toLowerCase();
        }
        return modifiedLine.contains(modifiedLookingFor);
    }

    private void setFile(String path) {
        this.file = dataUtiles.getFile(path);
    }

    public void openFile() throws IOException {
        if (file != null) {
            Desktop.getDesktop().open(file);
        }
    }

    private void createTableColumn() {
        createNewTab();
        TableColumn<Line, String> number = new TableColumn<>("number");
        TableColumn<Line, String> position = new TableColumn<>("position");
        TableColumn<Line, String> comment = new TableColumn<>("comment");

        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        position.setCellValueFactory(new PropertyValueFactory<>("position"));
        comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                onChoose();
            }
        });
        listObservable = dataUtiles.getLineList(listResult);
        tableView.setItems(listObservable);
        tableView.getColumns().addAll(number, position, comment);

    }

    private void onChoose() {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            Line selectedPerson = (Line) tableView.getSelectionModel().getSelectedItem();
            setFile(selectedPerson.getComment());
            try {
                openFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void print(String line) {
        Platform.runLater(() -> printCase(line));
        autoResizeColumns(tableView);
    }

    private void printCase(String line) {
        System.out.println(line);
        addMessage("\n" + line);
        if (listObservable != null) {
            listObservable.add(dataUtiles.getLine(line));
        }
    }

    private void createNewTab() {
        this.count = 0;
        Tab tab = new Tab();
        String extensionLine;
        if(checkBoxAddForNameTab.isSelected()) {
            extensionLine = extension + "-X" + textFieldDontUseExtesion.getText();
        }else{
            extensionLine = extension;
        }
        tab.setText(lookingFor + " " + extensionLine);
        tab.setClosable(true);

        tableView = new TableView();
        tab.setContent(tableView);
        listTab.add(tab);
        tabPaneGeneral.getTabs().add(tab);
    }

}