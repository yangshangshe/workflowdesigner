package com.ea.designer;

import com.ea.designer.config.AppResources;
/**
 * ****************************************************************************
 * @function：
 * @author yss
 * @file_name Application.java
 * @package_name：com.ea.designer
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人  修改时间  修改内容
 * 
 * ****************************************************************************
 */
public interface Application {

    public abstract AppResources getResource();

    public abstract Model getModel();

    public abstract DefaultView getView();
    
    

}
