package com.ea.designer.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import com.ea.designer.log.Logger;

/**
 * ****************************************************************************
 * 
 * @function：the manager for application preference file 
 * @author yss
 * @file_name AppPreference.java
 * @package_name：com.ea.designer.config
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class AppPreference {

    private String path;
    private String absolutePath;
    private Properties prop;

    public AppPreference(String path) {
        this.absolutePath = ".configurations" + File.separator + path;
        this.path = absolutePath + File.separator + path + ".wfd";
        this.prop = new Properties();
        init();
    }

    private void init() {
        File dir = new File(absolutePath);
        if (!dir.exists())
            dir.mkdirs();
        File wfd = new File(path);
        if (!wfd.exists()) {
            try {
                wfd.createNewFile();
            } catch (IOException e) {
                Logger.error(e.getMessage(), e);
            }
        }
        try {
            prop.load(new FileInputStream(wfd));
        } catch (FileNotFoundException e) {
            Logger.error(e.getMessage(), e);
        } catch (IOException e) {
            Logger.error(e.getMessage(), e);
        }
    }

    public Integer getInteger(String key) {
        try {
            return Integer.parseInt(this.prop.getProperty(key));
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return -1;
    }

    public String get(String key) {
        try {
            return this.prop.getProperty(key);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return key;
    }

    public synchronized void initProp(Object key, Object value) {
        if (this.prop.getProperty((String) key) == null) {
            this.prop.put(key, value.toString());
        }
    }

    public synchronized void put(Object key, Object value) {
        this.prop.put(key, value.toString());
    }

    public synchronized void save() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(path));
            this.prop.store(fos, Calendar.getInstance().toString());
            fos.flush();

        } catch (FileNotFoundException e) {
            Logger.error(e.getMessage(), e);
        } catch (IOException e) {
            Logger.error(e.getMessage(), e);
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

}
