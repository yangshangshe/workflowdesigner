package com.ea.designer;

import java.util.List;

import javax.swing.JToolBar;

import com.ea.designer.config.AppResources;
import com.ea.designer.resources.ProjectResource;
import com.ea.designer.resources.Resource;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name View.java
 * @package_name：com.ea.designer
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public interface View {

    public abstract DefaultApplication getApplication();

    public abstract Model getModel();

    public abstract AppResources getResource();

    public abstract void save();

    public abstract boolean hasUnSaved();

    public abstract Canvas getActiveTab();

    public abstract List<Canvas> getTabs();

    public abstract boolean isProjectOpened(ProjectResource project);

    public abstract JToolBar getToolbar();

    public abstract void registerActionPropertyListeners();

    public abstract void saveAll();
    
    public abstract void reloadProject();
    
    public abstract void showProject(Resource project, Resource active) ;
}
