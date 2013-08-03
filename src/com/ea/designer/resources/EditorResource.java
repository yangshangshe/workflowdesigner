package com.ea.designer.resources;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name EditorResource.java
 * @package_name：com.ea.designer.resources
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class EditorResource extends DefaultMutableTreeNode implements Resource {

    public static final String EDITOR_TYPE_JTEXTFIELD = "jtextfield";
    public static final String EDITOR_TYPE_JCOMBOBOX = "combo";

    private String type;
    private String value;
    private String optionId;
    private String optionName;

    protected Resource resource;

    public EditorResource() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
}
