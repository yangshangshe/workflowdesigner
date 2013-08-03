package com.ea.designer.resources;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name NodeAttributeResource.java
 * @package_name：com.ea.designer.resources
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class NodeAttributeResource extends DefaultMutableTreeNode implements Resource {

    protected Resource resource;

    public NodeAttributeResource() {
        resource = new DefaultResource();
    }

    public String getDescription() {
        return resource.getDescription();
    }

    public long getID() {
        return resource.getID();
    }

    public String getText() {
        return resource.getText();
    }

    public void setDescription(String desc) {
        resource.setDescription(desc);
    }

    public void setID(long id) {
        resource.setID(id);
    }

    public void setText(String text) {
        resource.setText(text);
    }

    public String toString() {
        return getDescription() + ":" + getText();
    }

}
