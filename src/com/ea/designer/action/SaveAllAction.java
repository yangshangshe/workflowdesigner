package com.ea.designer.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;


import com.ea.designer.Canvas;
import com.ea.designer.DefaultApplication;
import com.ea.designer.DefaultView;
import com.ea.designer.View;
import com.ea.designer.config.AppResources;

/**
 * ****************************************************************************
 * 
 * @function： save all modify
 * @author yss
 * @file_name SaveAllAction.java
 * @package_name：com.ea.designer.action
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class SaveAllAction extends AbstractAction implements PropertyChangeAble {
    public static final String ID = "mainmenubar.saveallaction";

    protected DefaultApplication app;

    public SaveAllAction(DefaultApplication app) {
        init(app);
        initAction(ID);
    }

    private void initAction(String id) {
        AppResources r = this.app.getResource();
        r.configAction(this, ID);
        setEnabled(false);
    }

    private void init(DefaultApplication app) {
        this.app = app;
    }

    public void registerPropertyListeners() {
        DefaultView view = (DefaultView) app.getView();
        if (view.getActiveTab() != null) {
            Canvas activeCanvas = view.getActiveTab();
            PropertyChangeListener[] pcls = activeCanvas.getPropertyChangeListeners();
            for (PropertyChangeListener p : pcls) {
                if (p instanceof SaveAllPropertyChangeListener) {
                    activeCanvas.removePropertyChangeListener(p);
                }
            }
            activeCanvas.addPropertyChangeListener(new SaveAllPropertyChangeListener());
        }
    }

    public void actionPerformed(ActionEvent e) {
        View view = app.getView();
        view.saveAll();
    }

    class SaveAllPropertyChangeListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(ID)) {
                SaveAllAction.this.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
            }
        }
    };

}
