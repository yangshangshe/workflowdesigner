package com.ea.designer.log;

import com.ea.designer.util.DateUtil;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name Logger.java
 * @package_name：com.ea.designer.log
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class Logger {

    private static String DEBUG_LABEL_INFO = "[debug]" + DateUtil.getCurrentDate(null) + " : ";

    public static void debug(String msg) {
        System.out.println(DEBUG_LABEL_INFO + msg);
    }

    public static void error(String msg) {
        System.out.println(msg);
    }

    public static void error(String msg, Exception e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
    }
}
