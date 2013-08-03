package com.ea.designer.resources;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name NodeResource.java
 * @package_name：com.ea.designer.resources
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class NodeResource extends DefaultMutableTreeNode implements Resource {

    protected int startX;
    protected int startY;
    protected int endX;
    protected int endY;
    protected String nodeImage;

    protected Resource resource;
    protected List<NodeJumpResource> jumpNodes;

    public NodeResource() {
        resource = new DefaultResource();
        jumpNodes = new ArrayList<NodeJumpResource>();
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
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

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String toString() {
        return resource.getText();
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof NodeResource) {
            if (((NodeResource) o).getID() == getID())
                return true;
        }
        return false;
    }

    public List<NodeJumpResource> getJumpNodes() {
        return jumpNodes;
    }

    public void addJumpNode(NodeJumpResource jump) {
        if (!jumpNodes.contains(jump)) {
            jumpNodes.add(jump);
        }
    }

    public String getNodeImage() {
        return nodeImage;
    }

    public void setNodeImage(String nodeImage) {
        this.nodeImage = nodeImage;
    }

}
