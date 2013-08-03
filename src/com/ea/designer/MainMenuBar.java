package com.ea.designer;

import java.awt.Component;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.ea.designer.action.AboutAction;
import com.ea.designer.action.AddNodeAction;
import com.ea.designer.action.ConnectAction;
import com.ea.designer.action.DeleteProjectAction;
import com.ea.designer.action.ExitAction;
import com.ea.designer.action.NewProjectAction;
import com.ea.designer.action.SaveAction;
import com.ea.designer.action.SaveAllAction;
import com.ea.designer.config.AppManager;
import com.ea.designer.config.AppResources;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name MainMenuBar.java
 * @package_name：com.ea.designer
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class MainMenuBar extends JMenuBar {

    JMenu fileMenu;
    JMenu editMenu;
    JMenu helpMenu;
    protected DefaultApplication app;

    public MainMenuBar(DefaultApplication app) {
        init(app);
        initComponent();
    }

    private void init(DefaultApplication app) {
        this.app = app;
    }

    private void initComponent() {

        this.add(createFileMenu());
        this.add(createEditMenu());
        this.add(createHelpMenu());

    }

    private Component createEditMenu() {
        AppResources resource = getResource();
        editMenu = new JMenu();
        resource.configMenu(editMenu, "mainmenubar.editmenu");

        editMenu.add(app.getModel().getAction(AddNodeAction.ID));
        editMenu.add(app.getModel().getAction(ConnectAction.ID));
        editMenu.addSeparator();
        editMenu.add(app.getModel().getAction(DeleteProjectAction.ID));

        return editMenu;
    }

    private Component createHelpMenu() {
        AppResources resource = getResource();

        helpMenu = new JMenu();
        resource.configMenu(helpMenu, "mainmenubar.helpmenu");

        helpMenu.add(app.getModel().getAction(AboutAction.ID));

        return helpMenu;
    }

    private Component createFileMenu() {
        AppResources resource = getResource();

        fileMenu = new JMenu();
        resource.configMenu(fileMenu, "mainmenubar.filemenu");

        fileMenu.add(app.getModel().getAction(NewProjectAction.ID));
        fileMenu.addSeparator();
        fileMenu.add(app.getModel().getAction(SaveAction.ID));
        fileMenu.add(app.getModel().getAction(SaveAllAction.ID));
        fileMenu.addSeparator();
        fileMenu.add(app.getModel().getAction(ExitAction.ID));

        return fileMenu;
    }

    public AppResources getResource() {
        return AppManager.getResources();
    }

}
