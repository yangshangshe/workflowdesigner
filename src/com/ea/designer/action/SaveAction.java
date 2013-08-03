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
 * @function： save the modify 
 * @author yss
 * @file_name SaveAction.java
 * @package_name：com.ea.designer.action
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class SaveAction extends AbstractAction implements PropertyChangeAble {
    public static final String ID = "mainmenubar.saveaction";

    protected DefaultApplication app;

    public SaveAction(DefaultApplication app) {
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
                if (p instanceof SavePropertyChangeListener) {
                    activeCanvas.removePropertyChangeListener(p);
                }
            }
            activeCanvas.addPropertyChangeListener(new SavePropertyChangeListener());
        }
    }

    public void actionPerformed(ActionEvent e) {
        View view = app.getView();

        if (view.hasUnSaved()) {
            view.save();
        }
    }

    class SavePropertyChangeListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(ID)) {
                SaveAction.this.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
            }
        }
    };

}
