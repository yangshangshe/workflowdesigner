package com.ea.designer.exception;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name XmlConfigError.java
 * @package_name：com.ea.designer.exception
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class XmlConfigError extends Exception {

    public XmlConfigError(String msg) {
        super(msg);
    }

    public XmlConfigError(String msg, Throwable e) {
        super(msg, e);
    }

}
