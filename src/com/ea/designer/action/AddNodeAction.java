package com.ea.designer.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.ea.designer.DefaultApplication;
import com.ea.designer.config.AppResources;
import com.ea.designer.ui.Node;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name AddNodeAction.java
 * @package_name：com.ea.designer.action
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class AddNodeAction extends AbstractAction implements PropertyChangeAble {
    public static final String ID = "mainmenubar.addnodeaction";
    protected DefaultApplication app;

    public AddNodeAction(DefaultApplication app) {
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
        if (app.getView().getActiveTab() != null) {
            Node node = new Node(0,0,100,100);
            app.getView().getActiveTab().addNode(node);
        }
    }

}
