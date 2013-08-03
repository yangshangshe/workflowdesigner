package com.ea.designer.resources;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name ProjectResource.java
 * @package_name：com.ea.designer.resources
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class ProjectResource extends DefaultMutableTreeNode implements Resource {

    protected String fileName;
    protected Resource resource;
    protected List<NodeResource> defineNodes;

    public ProjectResource() {
        resource = new DefaultResource();
        defineNodes = new ArrayList<NodeResource>();
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

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setID(long id) {
        resource.setID(id);
    }

    public void setText(String text) {
        resource.setText(text);
    }

    public List<NodeResource> getDefineNodes() {
        return defineNodes;
    }

    public void addNodeResource(NodeResource node) {
        if (!defineNodes.contains(node)) {
            defineNodes.add(node);
        }
    }

    public NodeResource getNodeResource(String id) {
        NodeResource searchNode = new NodeResource();
        searchNode.setID(Long.parseLong(id));
        if (defineNodes.indexOf(searchNode) != -1)
            return defineNodes.get(defineNodes.indexOf(searchNode));
        else
            return null;
    }

    public String toString() {
        return resource.getText();
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof ProjectResource) {
            if (((ProjectResource) o).getID() == getID())
                return true;
        }
        return false;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
