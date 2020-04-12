package xyz.spedcord.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private File file;
    private Properties properties;

    public Config(File file, String[] defaultValues) throws IOException {
        if (defaultValues.length % 2 != 0)
            throw new IllegalStateException("Uneven amount of keys and values");

        this.file = file;
        this.properties = new Properties();

        init(defaultValues);
    }

    public Config(File file) throws IOException {
        this(file, new String[]{});
    }

    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public boolean contains(String key) {
        return properties.containsKey(key);
    }

    public void save() throws IOException {
        properties.store(new FileOutputStream(file), null);
    }

    private void init(String[] defaultValues) throws IOException {
        if (!file.exists()) {
            if (file.getParentFile() != null)
                file.getParentFile().mkdirs();
            file.createNewFile();

            if (defaultValues.length != 0) {
                for (int i = 0; i < defaultValues.length; i++) {
                    properties.setProperty(defaultValues[i], defaultValues[i + 1]);
                    i++;
                }
                properties.store(new FileOutputStream(file), null);
            }
        }

        properties.load(new FileInputStream(file));
    }

    public File getFile() {
        return file;
    }

}
