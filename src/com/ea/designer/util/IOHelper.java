package com.ea.designer.util;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.ea.designer.log.Logger;
/**
 * ****************************************************************************
 * @function：
 * @author yss
 * @file_name IOHelper.java
 * @package_name：com.ea.designer.util
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人  修改时间  修改内容
 * 
 * ****************************************************************************
 */
public class IOHelper {

    public static ImageIcon readIcons(String path){
        try {
            return new ImageIcon(ImageIO.read(new File(path)));
        } catch (IOException e) {
            Logger.error(e.getMessage(),e);
        }
        return null;
    }
    
}
