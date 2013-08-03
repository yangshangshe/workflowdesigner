package com.ea.designer;

import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JToolBar;

import com.ea.designer.resources.ProjectResource;
/**
 * ****************************************************************************
 * @function：
 * @author yss
 * @file_name Model.java
 * @package_name：com.ea.designer
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人  修改时间  修改内容
 * 
 * ****************************************************************************
 */
public interface Model {

    public abstract void putAction(String id, Action action);

    public abstract Action getAction(String id);
    
    public abstract Map<String, Action> getActions();

    public abstract String getName();
    
    public abstract void setName(String name);

    public abstract ProjectResource getActiveProject();

    public abstract void setActiveCanvas(Canvas activeCanvas);

    public abstract boolean addProjectResource(ProjectResource project);

    public abstract boolean removeProjectResource(ProjectResource project);

    public abstract void clearProjectResource();

    public abstract boolean isExistProject(ProjectResource project);

    public abstract List<JToolBar> createToolBar(DefaultApplication app);
    
    public abstract String getDatabasePath();

}
