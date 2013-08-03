package com.ea.designer.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.ea.designer.bean.HotSpotArea;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name Cell.java
 * @package_name：com.ea.designer.ui
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public abstract class Cell extends JComponent {

    public static final int DEFAULT_CELL_WIDTH = 45;

    public static final int DEFAULT_CELL_HEIGHT = 60;

    /**
     * the description for cell
     */
    protected String desc;
    /**
     * id
     */
    protected String id;
    /**
     * start x
     */
    protected int startX;
    /**
     * start y
     */
    protected int startY;
    /**
     * end x
     */
    protected int endX;
    /**
     * end y
     */
    protected int endY;
    /**
     * is cell in resizing status
     */
    protected boolean isResizing = false;
    /**
     * the hotspot area for resize size
     */
    protected List<HotSpotArea> resizeHotSpotAreas = new ArrayList<HotSpotArea>();
    /**
     * is cell in connecting status
     */
    protected boolean isConnecting = false;
    /**
     * the hotspot area for connect cells
     */
    protected HotSpotArea connectHotSpotArea = new HotSpotArea();
    /**
     * the width for hotspot
     */
    protected static final int HOT_SPOT_WIDTH = 4;
    /**
     * the display color for hotspot
     */
    protected Color hotColor = new Color(0, 128, 200);

    /**
     * current cell's out arrow collection
     */
    private List<Arrow> toArrowList;

    private void initArrow() {
        toArrowList = new ArrayList<Arrow>();
    }

    public Cell() {
        this.id = String.valueOf(System.nanoTime());
        initArrow();
    }

    public Cell(int startX, int startY, int endX, int endY) {
        this();
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.setBounds(startX, startY, Math.abs(endX - startX), Math.abs(endY - startY));

        this.connectHotSpotArea.setCursorType(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.connectHotSpotArea.setArea(new Rectangle(getCellWidth() / 2, getCellHeight() / 2, HOT_SPOT_WIDTH * 2, HOT_SPOT_WIDTH * 2));
    }

    public Cell(int startX, int startY, int endX, int endY, String id) {
        this(startX, startY, endX, endY);
        this.id = id;
    }

    public abstract void setText(String text);

    public abstract String getText();

    public void addArrow(Arrow arrow) {
        if (!toArrowList.contains(arrow))
            toArrowList.add(arrow);
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public void resetCellSize(Rectangle rect) {
        this.startX = (int) rect.getX();
        this.startY = (int) rect.getY();
        this.endX = (int) (rect.getX() + rect.getWidth());
        this.endY = (int) (rect.getY() + rect.getHeight());
        this.setBounds(rect);
    }

    public int getCellWidth() {
        return Math.abs((int) (endX - startX));
    }

    public int getCellHeight() {
        return Math.abs((int) (endY - startY));
    }

    public int getCenterX() {
        return Math.abs(endX - startX) / 2;
    }

    public int getCenterY() {
        return Math.abs(endY - startY) / 2;
    }

    public boolean isResizing() {
        return isResizing;
    }

    public void setResizing(boolean isResizing) {
        this.isResizing = isResizing;
        this.repaint(0, 0, getWidth(), getHeight());
    }

    public List<HotSpotArea> getResizeHotSpotAreas() {
        return resizeHotSpotAreas;
    }

    public List<Arrow> getToArrowList() {
        return toArrowList;
    }

    public void setToArrowList(List<Arrow> toArrowList) {
        this.toArrowList = toArrowList;
    }

    public boolean containsIncludeEdge(Point p) {
        return (p.x >= 0) && (p.x <= getWidth()) && (p.y >= 0) && (p.y <= getHeight());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int hashCode() {
        return id.hashCode();
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof Cell) {
            if (((Cell) o).getId().equals(getId())) {
                return true;
            }
        }
        return false;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnecting(boolean isConnecting) {
        this.isConnecting = isConnecting;
    }

    public HotSpotArea getConnectHotSpotArea() {
        return connectHotSpotArea;
    }

    public void setConnectHotSpotArea(HotSpotArea connectHotSpotArea) {
        this.connectHotSpotArea = connectHotSpotArea;
    }
}
