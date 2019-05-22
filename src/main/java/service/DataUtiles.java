package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Constants;
import model.Line;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

public class DataUtiles {
    private static File file;
    private Properties props;

    public DataUtiles() throws IOException, URISyntaxException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        file = createResourceSubFolder();
//        String pathFile = file.getAbsolutePath();
//        URL resource = classLoader.getResource(pathFile);
//
//        if (resource == null) {
//
//            this.file.createNewFile();
//                System.out.println("Create new file -" + file.getAbsolutePath());
//        }else {
//            this.file = new File(pathFile);
//        }
        props = new Properties();


    }

    private static File createResourceSubFolder() throws IOException {
        File fileTest;
        File fileDir = new File("C:\\temp\\config\\");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }

        fileTest = new File(fileDir.getAbsolutePath() + "\\" + Constants.PROPERTIES_NAME);
        if (!fileTest.exists()) {
            fileTest.createNewFile();
        }

        return fileTest;
    }
    public String getProperties(String key) throws IOException {
        String result = null;
        try (FileInputStream fi = new FileInputStream(file)) {
            props.load(fi);
        }
        if (props.containsKey(key)) {
            result = props.getProperty(key);
        }
        return result;
    }

    public void saveProperties(String key, String value) throws IOException {
        try (FileOutputStream fr = new FileOutputStream(file)) {
            props.setProperty(key, value);
            props.store(fr, "Properties");
        }
    }

    public File getFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    public ObservableList<Line> getLineList(List<String> list) {
        ObservableList<Line> listResult = FXCollections.observableArrayList();
        for (String one : list) {
            listResult.add(getLine(one));
        }

        return listResult;
    }

    public Line getLine(String line) {
        String[] buffer;
        Line result;
        if (line.contains(Constants.SEPARATOR)) {
            buffer = (line.split(Constants.SEPARATOR));
            result = new Line(buffer[0].trim(), buffer[1].trim(), buffer[2].trim());
        } else {
            result = new Line("-----", "------", line);
        }
        return result;
    }
}
