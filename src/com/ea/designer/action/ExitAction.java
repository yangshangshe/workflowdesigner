package com.ea.designer.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


import com.ea.designer.DefaultApplication;
import com.ea.designer.View;
import com.ea.designer.config.AppResources;

/**
 * ****************************************************************************
 * 
 * @function：exit this application
 * @author yss
 * @file_name ExitAction.java
 * @package_name：com.ea.designer.action
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class ExitAction extends AbstractAction implements PropertyChangeAble {

    public static final String ID = "mainmenubar.exitaction";
    protected DefaultApplication app;

    public ExitAction(DefaultApplication app) {
        init(app);
        initAction(ID);
    }

    private void init(DefaultApplication app) {
        this.app = app;
    }

    private void initAction(String id) {
        AppResources r = this.app.getResource();
        r.configAction(this, ID);
    }

    public void registerPropertyListeners() {

    }

    public void actionPerformed(ActionEvent e) {
        try {
            View view = app.getView();
            view.saveAll();
        } finally {
            System.exit(0);
        }
    }

}
