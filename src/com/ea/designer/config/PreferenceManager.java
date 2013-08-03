package com.ea.designer.config;

import java.util.HashMap;
import java.util.Map;

/**
 * ****************************************************************************
 * 
 * @function： application perfrence manager
 * @author yss
 * @file_name PreferenceManager.java
 * @package_name：com.ea.designer.config
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class PreferenceManager {

    private static final Map<String, AppPreference> prefs = new HashMap<String, AppPreference>();

    private static PreferenceManager instance = new PreferenceManager();

    public static PreferenceManager getInstance() {
        return instance;
    }

    public AppPreference getPreference(String path) {
        AppPreference pref = getExistPreference(path);
        if (pref == null) {
            pref = createPref(path);
        }
        return pref;
    }

    private AppPreference createPref(String path) {
        AppPreference pref = new AppPreference(path);
        prefs.put(path, pref);
        return pref;
    }

    private AppPreference getExistPreference(String path) {
        return prefs.get(path);
    }

    public void save() {
        for (String key : prefs.keySet()) {
            prefs.get(key).save();
        }
    }

}
