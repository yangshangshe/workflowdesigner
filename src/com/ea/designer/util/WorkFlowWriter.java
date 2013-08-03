package com.ea.designer.util;

import com.ea.designer.resources.ProjectResource;
/**
 * ****************************************************************************
 * @function：
 * @author yss
 * @file_name WorkFlowWriter.java
 * @package_name：com.ea.designer.util
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人  修改时间  修改内容
 * 
 * ****************************************************************************
 */
public interface WorkFlowWriter {
    public abstract boolean saveProject(ProjectResource project, String fileName);
}
