package service;

import model.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class DataUtiles {
    private static File file;
    private Properties props;

    public DataUtiles() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL resource = classLoader.getResource(Constants.PROPERTIES_NAME);
        if (resource == null) {
            throw new IllegalArgumentException("ERROR: the file " + Constants.PROPERTIES_NAME + "is not found!");
        }
        file = new File(resource.getFile());
        props = new Properties();


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
