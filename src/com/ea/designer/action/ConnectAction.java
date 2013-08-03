package com.ea.designer.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


import com.ea.designer.DefaultApplication;
import com.ea.designer.View;
import com.ea.designer.config.AppResources;
import com.ea.designer.ui.Cell;
/**
 * ****************************************************************************
 * @function： connect cell to cell with arrow
 * @author yss
 * @file_name ConnectAction.java
 * @package_name：com.ea.designer.action
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人  修改时间  修改内容
 * 
 * ****************************************************************************
 */
public class ConnectAction extends AbstractAction implements PropertyChangeAble {

    public static final String ID = "mainmenubar.connectaction";
    protected DefaultApplication app;
    private static boolean pressed = false;

    public ConnectAction(DefaultApplication app) {
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
        View view = app.getView();
        if (view.getActiveTab() != null) {
            for (Cell cell : view.getActiveTab().getCells()) {
                if (!pressed) {
                    view.getActiveTab().addCellConnectListener(cell);
                    cell.setConnecting(true);
                } else {
                    view.getActiveTab().removeCellConnectListener(cell);
                    cell.setConnecting(false);
                }
            }
            view.getActiveTab().repaint();
        }
        pressed = !pressed;
    }

}
