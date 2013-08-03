package com.ea.designer.resources;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name NodeJumpResource.java
 * @package_name：com.ea.designer.resources
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class NodeJumpResource extends DefaultMutableTreeNode implements Resource {

    protected long toId;

    protected EditorResource editor;

    protected Resource resource;

    public NodeJumpResource() {
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

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof NodeJumpResource) {
            if (((NodeJumpResource) o).getID() == getID())
                return true;
        }
        return false;
    }

    public String toString() {
        return String.valueOf(toId);
    }

    public EditorResource getEditor() {
        return editor;
    }

    public void setEditor(EditorResource editor) {
        this.editor = editor;
    }

}
