package com.ea.designer.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.ea.designer.resources.ProjectResource;
/**
 * ****************************************************************************
 * @function：
 * @author yss
 * @file_name WorkFlowReader.java
 * @package_name：com.ea.designer.util
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人  修改时间  修改内容
 * 
 * ****************************************************************************
 */
public interface WorkFlowReader {
    public ProjectResource readProject(String file) throws  IOException;
    public ProjectResource readProject(File file) throws IOException;
    public ProjectResource readProject(InputStream in) throws IOException;
}
