package com.ea.designer.action;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.ea.designer.DefaultApplication;
import com.ea.designer.config.AppManager;
import com.ea.designer.config.AppResources;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name AboutAction.java
 * @package_name：com.ea.designer.action
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class AboutAction extends AbstractAction implements PropertyChangeAble {

    public static final String ID = "mainmenubar.aboutaction";
    protected DefaultApplication app;

    public AboutAction(DefaultApplication app) {
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
        AboutWindow wind = new AboutWindow();
        if (app.getView().getActiveTab() != null) {
            wind.setLocation(app.getView().getActiveTab().getCenterPoint());
        } else {
            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension d = kit.getScreenSize();
            wind.setLocation((d.width-wind.getWidth())/2, (d.height-wind.getHeight())/2);
        }
        wind.setVisible(true);
    }

    class AboutWindow extends JDialog {

        private static final long serialVersionUID = 7382767161155192747L;

        public AboutWindow() {
            initComponent();
        }

        private void initComponent() {
            this.setTitle(AppManager.getResources().getString("mainmenubar.aboutaction"));
            this.setLayout(new BorderLayout());
            JLabel logLabel = new JLabel();

            JLabel infoLabel = new JLabel();
            infoLabel.setText(AppManager.getResources().getString("mainmenubar.aboutaction.info"));
            this.add(logLabel, BorderLayout.NORTH);
            this.add(infoLabel, BorderLayout.CENTER);
            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton okBtn = new JButton("确定");
            okBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            AboutWindow.this.setVisible(false);
                        }
                    });

                }
            });
            bottom.add(okBtn);
            this.add(bottom, BorderLayout.SOUTH);
            this.setSize(320, 240);
            this.setResizable(false);
        }
    }

}
