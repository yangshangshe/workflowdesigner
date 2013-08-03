package com.ea.designer.config;

import java.util.Locale;

/**
 * ****************************************************************************
 * 
 * @function：application manager with resource and locale setting
 * @author yss
 * @file_name AppManager.java
 * @package_name：com.ea.designer.config
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class AppManager {

    private static String Labels = "com.ea.designer.config.Labels";
    private static Locale locale = Locale.getDefault();
    protected static AppResources r;

    public static AppResources getResources() {
        if (r == null) {
            r = AppResources.getResources(Labels, locale);
        }
        return r;
    }

    public static void setLocale(Locale l) {
        Locale.setDefault(l);
    }

    public static Locale getLocale() {
        return locale;
    }
}
