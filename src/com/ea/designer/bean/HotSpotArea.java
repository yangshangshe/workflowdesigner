package com.ea.designer.bean;

import java.awt.Cursor;
import java.awt.Rectangle;

/**
 * ****************************************************************************
 * 
 * @function： hotspot area for mouse
 * @author yss
 * @file_name HotSpotArea.java
 * @package_name：com.ea.designer.bean
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class HotSpotArea {
    /**
     * the cursor type
     */
    private Cursor cursorType;
    /**
     * the hotspot area
     */
    private Rectangle area;

    public HotSpotArea(int cursorType, Rectangle area) {
        this.cursorType = new Cursor(cursorType);
        this.area = area;
    }

    public HotSpotArea() {

    }

    public Rectangle getArea() {
        return area;
    }

    public void setArea(Rectangle area) {
        this.area = area;
    }

    public Cursor getCursorType() {
        return cursorType;
    }

    public void setCursorType(Cursor cursorType) {
        this.cursorType = cursorType;
    }

}
