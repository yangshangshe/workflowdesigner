package com.ea.designer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ea.designer.action.AddNodeAction;
import com.ea.designer.action.ConnectAction;
import com.ea.designer.action.PropertyChangeAble;
import com.ea.designer.action.SaveAction;
import com.ea.designer.config.AppManager;
import com.ea.designer.config.AppResources;
import com.ea.designer.resources.ProjectResource;
import com.ea.designer.resources.Resource;
import com.ea.designer.ui.ClosedTabPanel;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name DefaultView.java
 * @package_name：com.ea.designer
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class DefaultView extends JComponent implements View {

    protected DefaultApplication app;

    private ClosedTabPanel workFlowDisplay;
    private ProjectPanel projectPanel;
    private JSplitPane mainSplitPanel;

    public DefaultView(DefaultApplication application) {
        init(application);
        initComponent();
    }

    private void init(DefaultApplication application) {
        this.app = application;
    }

    public void reloadProject() {
        projectPanel.initResource();
        projectPanel.initComponent();
        workFlowDisplay.removeAll();
        validate();
    }

    private void initComponent() {

        this.setLayout(new BorderLayout());

        projectPanel = new ProjectPanel(this);
        workFlowDisplay = new ClosedTabPanel();
        workFlowDisplay.addChangeListener(new CloseTabChangeListener());

        mainSplitPanel = new JSplitPane();
        mainSplitPanel.setDividerSize(10);
        mainSplitPanel.setAutoscrolls(true);
        mainSplitPanel.setOneTouchExpandable(true);
        mainSplitPanel.setLeftComponent(projectPanel);
        mainSplitPanel.setRightComponent(workFlowDisplay);
        add(mainSplitPanel, BorderLayout.CENTER);

    }

    public Canvas getActiveTab() {
        if (workFlowDisplay.getSelectedComponent() != null)
            return (Canvas) ((JScrollPane) workFlowDisplay.getSelectedComponent()).getViewport().getView();
        else
            return null;
    }

    public List<Canvas> getTabs() {
        List<Canvas> list = new ArrayList<Canvas>();
        for (int i = 0; i < workFlowDisplay.getComponentCount(); i++) {
            list.add((Canvas) ((JScrollPane) workFlowDisplay.getComponent(i)).getViewport().getView());
        }
        return list;
    }

    public void addTab(String title, Component content) {
        JScrollPane scrollPanel = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        workFlowDisplay.addTab(title, scrollPanel);
        workFlowDisplay.setSelectedComponent(scrollPanel);
    }

    public void setSelectedCanvas(Resource resource) {
        for (int i = 0; i < workFlowDisplay.getComponentCount(); i++) {
            Canvas c = ((Canvas) ((JScrollPane) workFlowDisplay.getComponent(i)).getViewport().getView());
            if (c.getDisplayProject().getID() == resource.getID()) {
                workFlowDisplay.setSelectedIndex(i);
            }
        }
    }

    public boolean isProjectOpened(ProjectResource project) {
        for (int i = 0; i < workFlowDisplay.getComponentCount(); i++) {
            Canvas c = ((Canvas) ((JScrollPane) workFlowDisplay.getComponent(i)).getViewport().getView());
            if (c.getDisplayProject().getID() == project.getID()) {
                return true;
            }
        }
        return false;
    }

    public Model getModel() {
        return app.getModel();
    }

    public AppResources getResource() {
        return AppManager.getResources();
    }

    public DefaultApplication getApplication() {
        return app;
    }

    public void save() {
        if (projectPanel.saveActiveProject()) {
            projectPanel.initComponent();
            getActiveTab().setChanged(false);
        }
    }

    public boolean hasUnSaved() {
        if (getActiveTab() != null)
            return getActiveTab().isChanged();
        return false;
    }

    public JToolBar getToolbar() {
        JToolBar defualtViewBar = new JToolBar();

        defualtViewBar.add(getAction(SaveAction.ID));
        defualtViewBar.addSeparator();
        defualtViewBar.add(getAction(AddNodeAction.ID));
        defualtViewBar.add(getAction(ConnectAction.ID));

        return defualtViewBar;
    }

    private Action getAction(String id) {
        return app.getModel().getAction(id);
    }

    public void registerActionPropertyListeners() {
        Map<String, Action> actions = getModel().getActions();
        for (String key : actions.keySet()) {
            if (actions.get(key) instanceof PropertyChangeAble) {
                ((PropertyChangeAble) actions.get(key)).registerPropertyListeners();
            }
        }
    }

    class CloseTabChangeListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            registerActionPropertyListeners();
            if (getActiveTab() != null) {
                getActiveTab().setChanged(getActiveTab().isChanged());
                getModel().setActiveCanvas(getActiveTab());
            }

        }
    }

    public void saveAll() {
        for (Canvas canvas : getTabs()) {
            if (canvas.isChanged()) {
                projectPanel.saveProject(canvas.getDisplayProject());
                canvas.setChanged(false);
            }
        }
        projectPanel.initComponent();
    }

    public void showProject(Resource project, Resource active) {
        projectPanel.showProjectWorkFlow(project, active);
    }

}
