package com.ea.designer.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name DateUtil.java
 * @package_name：com.ea.designer.util
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class DateUtil {

    public static String getCurrentDate(String formatText) {
        if (StringUtil.isNull(formatText))
            formatText = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(formatText);
        Calendar calendar = Calendar.getInstance();
        return format.format(calendar.getTime());
    }

}
