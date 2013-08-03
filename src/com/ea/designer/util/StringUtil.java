package com.ea.designer.util;
/**
 * ****************************************************************************
 * @function：
 * @author yss
 * @file_name StringUtil.java
 * @package_name：com.ea.designer.util
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人  修改时间  修改内容
 * 
 * ****************************************************************************
 */
public class StringUtil {

    public static boolean isNull(String str) {
        if (str == null || "".equals(str.trim()) || "null".equals(str.trim()))
            return true;
        return false;
    }

}
