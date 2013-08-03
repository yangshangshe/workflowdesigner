package com.ea.designer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JToolBar;

import com.ea.designer.config.PreferenceManager;
import com.ea.designer.resources.ProjectResource;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name DefaultModel.java
 * @package_name：com.ea.designer
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class DefaultModel implements Model {

    protected String name;
    protected Canvas activeCanvas;
    private final Map<String, Action> actions;
    private List<ProjectResource> projects;

    public DefaultModel() {
        actions = new HashMap<String, Action>();
        projects = new ArrayList<ProjectResource>();
    }

    public boolean addProjectResource(ProjectResource project) {
        if (!isExistProject(project)) {
            return projects.add(project);
        }
        return false;
    }

    public boolean removeProjectResource(ProjectResource project) {
        if (isExistProject(project)) {
            return projects.remove(project);
        }
        return false;
    }

    public void clearProjectResource() {
        if (projects != null)
            projects.clear();
    }

    public boolean isExistProject(ProjectResource project) {
        return projects.contains(project);
    }

    public Action getAction(String id) {
        return ((Action) this.actions.get(id));
    }

    public Map<String, Action> getActions() {
        return actions;
    }

    public void putAction(String id, Action action) {
        if (action == null) {
            this.actions.remove(id);
            return;
        }
        this.actions.put(id, action);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ProjectResource getActiveProject() {
        if (activeCanvas != null)
            return activeCanvas.getDisplayProject();
        return null;
    }

    public void setActiveCanvas(Canvas activeCanvas) {
        this.activeCanvas = activeCanvas;
    }

    public List<JToolBar> createToolBar(DefaultApplication app) {
        List<JToolBar> toolbarList = new ArrayList<JToolBar>();
        toolbarList.add(app.getView().getToolbar());
        return toolbarList;
    }

    public String getDatabasePath() {
        return PreferenceManager.getInstance().getPreference(ProjectPanel.class.getName()).get("database.path");
    }

}
