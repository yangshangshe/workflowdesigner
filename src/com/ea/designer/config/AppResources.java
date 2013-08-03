package com.ea.designer.config;

import java.io.File;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.ea.designer.util.StringUtil;

/**
 * ****************************************************************************
 * 
 * @function： the manager for the application resource
 * @author yss
 * @file_name AppResources.java
 * @package_name：com.ea.designer.config
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class AppResources {

    private final ResourceBundle resource;

    public AppResources(ResourceBundle r) {
        this.resource = r;
    }

    public static AppResources getResources(String boundleName, Locale locale) {
        return new AppResources(ResourceBundle.getBundle(boundleName, locale));
    }

    public ResourceBundle getBundle() {
        return this.resource;
    }

    /**
     * config application menu info
     * 
     * @param menu
     *            menu
     * @param name
     *            menu key in the config file
     */
    public void configMenu(JMenuItem menu, String name) {
        menu.setText(getString(name));
        if (!(menu instanceof JMenu)) {
            menu.setAccelerator(getAcc(name));
        }
        menu.setMnemonic(getMnem(name));
        menu.setIcon(getImageIcon(name, getClass()));
    }

    private KeyStroke getAcc(String key) {
        KeyStroke keyStroke = null;

        String string = null;
        try {
            string = this.resource.getString(new StringBuilder().append(key).append(".acc").toString());
        } catch (MissingResourceException e) {
        }
        keyStroke = string == null ? (KeyStroke) null : KeyStroke.getKeyStroke(string);

        return keyStroke;
    }

    private char getMnem(String key) {
        String string = null;
        try {
            string = this.resource.getString(new StringBuilder().append(key).append(".mnem").toString());
        } catch (MissingResourceException e) {
        }
        return (string == null) || (string.length() == 0) ? '\000' : string.charAt(0);
    }

    /**
     * get the key's value from the properties file
     * 
     * @param key
     * @return
     */
    public String getString(String key) {
        try {
            return this.resource.getString(key);
        } catch (MissingResourceException e) {
        }
        return key;
    }

    /**
     * get the key's iconpath from the properties file
     * 
     * @param key
     * @return
     */
    public String getImageIconPath(String key) {
        return this.resource.getString(key + ".icon");
    }

    /**
     * get the key's icon from the properties file
     * 
     * @param key
     * @return
     */
    public ImageIcon getImageIcon(String key) {
        return getImageIcon(key, super.getClass());
    }

    private ImageIcon getImageIcon(String key, Class<?> clazz) {
        if (StringUtil.isNull(key)) {
            return null;
        }
        try {
            String src = this.resource.getString(key + ".icon");
            String imageDir = getImageDir();
            return new ImageIcon(imageDir + src);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    public String getImageDir() {
        String imageDir = this.resource.getString("icon.dir");
        if (!imageDir.endsWith(File.separator)) {
            imageDir = imageDir + File.separator;
        }
        return System.getProperty("user.dir") + File.separator + imageDir;
    }

    public void configAction(Action action, String id) {
        configAction(action, id, super.getClass());
    }

    /**
     * config application's action
     * 
     * @param action
     * @param id
     * @param clazz
     */
    private void configAction(Action action, String id, Class<?> clazz) {
        action.putValue("Name", getString(id));
        action.putValue("AcceleratorKey", getAcc(id));
        action.putValue("MnemonicKey", new Integer(getMnem(id)));
        action.putValue("SmallIcon", getImageIcon(id, clazz));
        action.putValue("ShortDescription", getString(id));
    }

}
