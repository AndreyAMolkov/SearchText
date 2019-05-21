package service;

import model.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
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

    private static File createResourceSubFolder() throws URISyntaxException, IOException {
        //       java.net.URL url = Main.class.getResource("/sample/");
//        System.out.println(url.getPath());
//        File fullPathToSubfolder = new File(url.toURI()).getAbsoluteFile();
//        File fullPathToSubfolder = (new File(Main.class.getResource("/sample/").getPath())).getAbsoluteFile();
//        System.out.println("----" + fullPathToSubfolder.getAbsolutePath() + "------");
//        String projectFolder = fullPathToSubfolder.getAbsolutePath().split("sample")[0];

//        System.out.println("----" + projectFolder + "------");
        File fileTest;


//        String pathFile = testResults.getAbsolutePath() + "\\" + Constants.PROPERTIES_NAME;
//        System.out.println("----" + pathFile + "------");
//        if (resource == null) {
//            fileTest = new File(pathFile);
//            fileTest.createNewFile();
//            System.out.println("Create new file -" + fileTest.getAbsolutePath());
//        }else{
//            fileTest = new File(resource.getPath());
        File fileDir = new File("C:\\temp\\config\\");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }

        fileTest = new File(fileDir.getAbsolutePath() + "\\" + Constants.PROPERTIES_NAME);
        if (!fileTest.exists()) {
            fileTest.createNewFile();
        }

//        }

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


}
