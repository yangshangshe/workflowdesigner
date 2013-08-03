package com.ea.designer.render;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.ea.designer.config.AppManager;
import com.ea.designer.resources.NodeAttributeResource;
import com.ea.designer.resources.NodeJumpResource;
import com.ea.designer.resources.NodeResource;
import com.ea.designer.resources.ProjectResource;
import com.ea.designer.resources.Resource;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name ProjectTreeCellRenderer.java
 * @package_name：com.ea.designer.render
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class ProjectTreeCellRenderer extends DefaultTreeCellRenderer {

    private Icon projectIcon;
    private Icon nodeIcon;
    private Icon jumpIcon;
    private Icon attributeIcon;

    public ProjectTreeCellRenderer() {
        projectIcon = AppManager.getResources().getImageIcon("projectpanel.tree.project");
        nodeIcon = AppManager.getResources().getImageIcon("projectpanel.tree.node");
        jumpIcon = AppManager.getResources().getImageIcon("projectpanel.tree.jump");
        attributeIcon = AppManager.getResources().getImageIcon("projectpanel.tree.attribute");
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (!(value instanceof Resource))
            return this;
        Resource r = (Resource) value;

        if (r instanceof ProjectResource)
            setIcon(projectIcon);
        else if (r instanceof NodeResource)
            setIcon(nodeIcon);
        else if (r instanceof NodeJumpResource)
            setIcon(jumpIcon);
        else if (r instanceof NodeAttributeResource)
            setIcon(attributeIcon);

        return this;
    }

}
