package com.ea.designer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.ea.designer.action.AboutAction;
import com.ea.designer.action.AddNodeAction;
import com.ea.designer.action.ConnectAction;
import com.ea.designer.action.DeleteProjectAction;
import com.ea.designer.action.ExitAction;
import com.ea.designer.action.NewProjectAction;
import com.ea.designer.action.SaveAction;
import com.ea.designer.action.SaveAllAction;
import com.ea.designer.bean.DefaultConfig;
import com.ea.designer.config.AppManager;
import com.ea.designer.config.AppPreference;
import com.ea.designer.config.AppResources;
import com.ea.designer.config.PreferenceManager;
import com.ea.designer.log.Logger;
import com.ea.designer.util.IOHelper;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name DefaultApplication.java
 * @package_name：com.ea.designer
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class DefaultApplication implements Application {

    protected Model model;
    protected MainMenuBar menuBar;
    protected ToolBarPanel toolBar;
    protected JFrame window;
    protected DefaultView view;

    public DefaultApplication() {
        setLAF();
    }

    public void init() {

        DefaultConfig.initDefaultConfig();

        model = new DefaultModel();
        model.setName(getPreference().get("window.title"));

        view = new DefaultView(this);

        window = new JFrame(getName());
        window.setIconImage(IOHelper.readIcons(getLogoPath()).getImage());
        window.setDefaultCloseOperation(3);
        window.setLocale(AppManager.getLocale());
        window.setContentPane(getContainer());
        window.add(view);
    }

    private String getLogoPath() {
        return AppManager.getResources().getImageDir() + PreferenceManager.getInstance().getPreference(DefaultApplication.class.getName()).get("window.icon");
    }

    private Container getContainer() {
        return this.window.getContentPane();
    }

    private String getName() {
        return getModel().getName();
    }

    protected void setLAF() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }

        if (UIManager.getString("OptionPane.css") == null)
            UIManager.put("OptionPane.css", "");
    }

    public void start() {
        initModelActions();
        setupMenuToolbar();
        setWindowSizeLocation();
        window.setVisible(true);
        window.toFront();
    }

    private void setWindowSizeLocation() {
        AppPreference pref = getPreference();

        int extendedState = 0;
        Dimension size;
        Point location;
        try {
            String[] s = pref.get("window.size").split(" ");
            size = new Dimension(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
            String[] l = pref.get("window.location").split(" ");
            location = new Point(Integer.parseInt(l[0]), Integer.parseInt(l[1]));
            extendedState = pref.getInteger("window.extended.state");
        } catch (Exception e) {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            size = new Dimension(dimension.width / 2, dimension.height / 2);
            location = new Point((dimension.width - this.window.getSize().width) / 2, (dimension.height - this.window.getSize().height) / 3);
        }

        this.window.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                AppPreference pref = DefaultApplication.this.getPreference();
                int es = DefaultApplication.this.window.getExtendedState();
                pref.put("window.extended.state", Integer.valueOf((es == 1) ? 0 : es));
                if (es == 0) {
                    Dimension d = DefaultApplication.this.window.getSize();
                    pref.put("window.size", d.width + " " + d.height);
                }
                pref.save();
            }

            public void componentMoved(ComponentEvent e) {
                AppPreference pref = DefaultApplication.this.getPreference();
                int es = DefaultApplication.this.window.getExtendedState();
                pref.put("window.extended.state", Integer.valueOf((es == 1) ? 0 : es));
                if (es == 0) {
                    Dimension d = DefaultApplication.this.window.getSize();
                    pref.put("window.size", d.width + " " + d.height);
                }
                Point p = DefaultApplication.this.window.getLocation();
                pref.put("window.location", p.x + " " + p.y);
                pref.save();
            }
        });
        this.window.setSize(size);
        this.window.setPreferredSize(size);
        this.window.setLocation(location);
        this.window.setExtendedState(extendedState);
    }

    private AppPreference getPreference() {
        return PreferenceManager.getInstance().getPreference(DefaultApplication.class.getName());
    }

    private void setupMenuToolbar() {
        menuBar = new MainMenuBar(this);
        window.setJMenuBar(menuBar);
        createToolBar();
        this.window.getContentPane().add(this.toolBar, "First");
    }

    private void createToolBar() {
        if (toolBar != null) {
            this.toolBar.removeAll();
            this.window.getContentPane().remove(toolBar);
        }

        toolBar = new ToolBarPanel(this);
    }

    private void initModelActions() {

        model.putAction(SaveAction.ID, new SaveAction(this));
        model.putAction(SaveAllAction.ID, new SaveAllAction(this));
        model.putAction(ConnectAction.ID, new ConnectAction(this));
        model.putAction(ExitAction.ID, new ExitAction(this));
        model.putAction(AboutAction.ID, new AboutAction(this));
        model.putAction(NewProjectAction.ID, new NewProjectAction(this));
        model.putAction(AddNodeAction.ID, new AddNodeAction(this));
        model.putAction(DeleteProjectAction.ID, new DeleteProjectAction(this));
    }

    public AppResources getResource() {
        return AppManager.getResources();
    }

    public Model getModel() {
        return model;
    }

    public DefaultView getView() {
        return view;
    }

}
