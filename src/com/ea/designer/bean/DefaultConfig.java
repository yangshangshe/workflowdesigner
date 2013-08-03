package com.ea.designer.bean;


import com.ea.designer.DefaultApplication;
import com.ea.designer.ProjectPanel;
import com.ea.designer.config.AppPreference;
import com.ea.designer.config.PreferenceManager;

/**
 * ****************************************************************************
 * 
 * @function：the default config for the application
 * @author yss
 * @file_name DefaultConfig.java
 * @package_name：com.ea.designer.bean
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class DefaultConfig {

    public static void initDefaultConfig() {
        AppPreference projectPref = PreferenceManager.getInstance().getPreference(ProjectPanel.class.getName());
        projectPref.initProp("database.path", "resources/project/");
        projectPref.save();
        AppPreference applicationPref = PreferenceManager.getInstance().getPreference(DefaultApplication.class.getName());
        applicationPref.initProp("window.title", "流程设计器V1.0");
        applicationPref.initProp("window.icon", "logo.png");
        applicationPref.initProp("nodeattributeeditpanel.message.idexist", "节点ID已经存在!");
        applicationPref.save();
    }

}
