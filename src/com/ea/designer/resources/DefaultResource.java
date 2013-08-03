package com.ea.designer.resources;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name DefaultResource.java
 * @package_name：com.ea.designer.resources
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class DefaultResource implements Resource {

    private long id;
    private String text;
    private String desc;

    public DefaultResource() {
        id = (int) System.nanoTime();
    }

    public String getDescription() {
        return desc;
    }

    public long getID() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public void setID(long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }
}
