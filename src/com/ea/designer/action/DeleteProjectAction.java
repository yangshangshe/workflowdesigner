package com.ea.designer.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.ea.designer.DefaultApplication;
import com.ea.designer.config.AppManager;
import com.ea.designer.config.AppResources;

public class DeleteProjectAction extends AbstractAction implements PropertyChangeAble {

    public static final String ID = "mainmenubar.deleteprojectaction";
    protected DefaultApplication app;

    public DeleteProjectAction(DefaultApplication app) {
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
        if (app.getModel().getActiveProject() != null) {
            int choose = JOptionPane.showConfirmDialog(null, AppManager.getResources().getString("mainmenubar.deleteprojectaction.label_deleteconfirm") + app.getModel().getActiveProject().getText() + "?");
            if (choose == JOptionPane.YES_OPTION) {
                File file = new File(app.getModel().getDatabasePath() + app.getModel().getActiveProject().getFileName());
                if (file.exists() && file.delete()){
                    app.getView().reloadProject();
                    app.getModel().setActiveCanvas(null);
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, AppManager.getResources().getString("mainmenubar.deleteprojectaction.label_noactiveproject"));
        }
    }

}
