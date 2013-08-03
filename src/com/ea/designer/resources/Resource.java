package com.ea.designer.resources;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name Resource.java
 * @package_name：com.ea.designer.resources
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public interface Resource {
    long ILLEGAL_ID = 0;

    long getID();

    void setID(long id);

    String getText();

    void setText(String text);

    String getDescription();

    void setDescription(String desc);

}
