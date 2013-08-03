package com.ea.designer;

import javax.swing.JToolBar;
/**
 * ****************************************************************************
 * @function：
 * @author yss
 * @file_name ToolBarPanel.java
 * @package_name：com.ea.designer
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人  修改时间  修改内容
 * 
 * ****************************************************************************
 */
public class ToolBarPanel extends JToolBar {

    protected DefaultApplication app;

    public ToolBarPanel(DefaultApplication app) {
        init(app);
        initToolBar();
    }

    private void init(DefaultApplication app) {
        this.app = app;
    }

    private void initToolBar() {
        for (JToolBar tb : app.getModel().createToolBar(app)) {
            this.add(tb);
        }
    }
}
