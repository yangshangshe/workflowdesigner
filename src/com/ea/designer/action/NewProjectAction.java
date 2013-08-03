package com.ea.designer.action;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ea.designer.DefaultApplication;
import com.ea.designer.config.AppManager;
import com.ea.designer.config.AppResources;
import com.ea.designer.resources.ProjectResource;
import com.ea.designer.ui.DigitOnlyField;
import com.ea.designer.util.XmlWriter;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name NewProjectAction.java
 * @package_name：com.ea.designer.action
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class NewProjectAction extends AbstractAction implements PropertyChangeAble {
    public static final String ID = "mainmenubar.newprojectaction";
    protected DefaultApplication app;

    public NewProjectAction(DefaultApplication app) {
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
        NewProjectDialog dialog = new NewProjectDialog();
        if (app.getView().getActiveTab() != null) {
            dialog.setLocation(app.getView().getActiveTab().getCenterPoint());
        } else {
            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension d = kit.getScreenSize();
            dialog.setLocation((d.width - dialog.getWidth()) / 2, (d.height - dialog.getHeight()) / 2);
        }
        dialog.setVisible(true);
    }

    class NewProjectDialog extends JDialog {

        private JPanel editPanel;
        private JPanel btnPanel;

        private JTextField fileName;
        private DigitOnlyField id;
        private JTextField text;
        private JTextField desc;
        private JLabel fileNameLb;
        private JLabel idLb;
        private JLabel textLb;
        private JLabel descLb;

        private JButton confirmBtn;

        public NewProjectDialog() {
            initComponent();
        }

        private void initComponent() {

            this.setSize(320, 240);
            this.setTitle(AppManager.getResources().getString("mainmenubar.newprojectaction"));

            fileNameLb = new JLabel(AppManager.getResources().getString("mainmenubar.newprojectaction.label_filename"));
            idLb = new JLabel(AppManager.getResources().getString("mainmenubar.newprojectaction.label_id"));
            textLb = new JLabel(AppManager.getResources().getString("mainmenubar.newprojectaction.label_text"));
            descLb = new JLabel(AppManager.getResources().getString("mainmenubar.newprojectaction.label_desc"));
            fileName = new JTextField();
            id = new DigitOnlyField();
            text = new JTextField();
            desc = new JTextField();

            editPanel = new JPanel(new GridBagLayout());
            editPanel.setBorder(BorderFactory.createTitledBorder(AppManager.getResources().getString("mainmenubar.newprojectaction.label_workflow")));
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.gridx = 0;
            c.gridwidth = 1;
            editPanel.add(fileNameLb, c);
            c.gridx = 1;
            c.gridwidth = 2;
            editPanel.add(fileName, c);
            c.gridx = 0;
            c.gridwidth = 1;
            editPanel.add(idLb, c);
            c.gridx = 1;
            c.gridwidth = 2;
            editPanel.add(id, c);
            c.gridx = 0;
            c.gridwidth = 1;
            editPanel.add(textLb, c);
            c.gridx = 1;
            c.gridwidth = 2;
            editPanel.add(text, c);
            c.gridx = 0;
            c.gridwidth = 1;
            editPanel.add(descLb, c);
            c.gridx = 1;
            c.gridwidth = 2;
            editPanel.add(desc, c);

            btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            confirmBtn = new JButton(AppManager.getResources().getString("mainmenubar.newprojectaction.label_create"));
            confirmBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ProjectResource project = new ProjectResource();
                    if (isEmpty(fileName)) {
                        showDialog(AppManager.getResources().getString("mainmenubar.newprojectaction.filename_notempty"));
                        return;
                    } else if (!fileName.getText().endsWith(".xml")) {
                        fileName.setText(fileName.getText() + ".xml");
                    }

                    if (isEmpty(id)) {
                        showDialog(AppManager.getResources().getString("mainmenubar.newprojectaction.id_notempty"));
                        return;
                    }
                    if (isEmpty(text)) {
                        showDialog(AppManager.getResources().getString("mainmenubar.newprojectaction.text_notempty"));
                        return;
                    }
                    project.setID(Long.valueOf(id.getText()));
                    project.setText(text.getText());
                    project.setFileName(fileName.getText());
                    project.setDescription(desc.getText());
                    XmlWriter writer = new XmlWriter();
                    String databasePath = app.getModel().getDatabasePath();
                    if (writer.saveProject(project, databasePath + fileName.getText())) {
                        app.getView().reloadProject();
                        app.getView().showProject(project, null);
                        NewProjectDialog.this.dispose();
                    }
                }

                private void showDialog(String message) {
                    JOptionPane.showMessageDialog(null, message);
                }

                private boolean isEmpty(JTextField jtf) {
                    return jtf.getText().trim().length() <= 0;
                }
            });
            btnPanel.add(confirmBtn);

            this.setLayout(new BorderLayout());
            this.add(editPanel, BorderLayout.CENTER);
            this.add(btnPanel, BorderLayout.SOUTH);

        }

    }

}
