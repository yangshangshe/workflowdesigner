package com.ea.designer.event;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;


import com.ea.designer.Canvas;
import com.ea.designer.bean.HotSpotArea;
import com.ea.designer.ui.Cell;

/**
 * ****************************************************************************
 * 
 * @function：change the cell's size
 * @author yss
 * @file_name CellReSizeListener.java
 * @package_name：com.ea.designer.event
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class CellReSizeListener extends MouseInputAdapter {

    /**
     * the object for resize
     */
    Cell resizeObject;

    /**
     * the canvas contains the resize object
     */
    Canvas canvas;

    /**
     * the point before mouse drag
     */
    Point orgPoint;

    Rectangle orgRect;
    Rectangle dstRect;
    int orgWidth;
    int orgHeight;

    public CellReSizeListener(Cell resizeObject, Canvas canvas) {
        this.canvas = canvas;
        this.resizeObject = resizeObject;
    }

    public void mouseClicked(MouseEvent e) {
        for (Cell cell : canvas.getCells())
            cell.setResizing(false);
        resizeObject.setResizing(true);
        orgPoint = SwingUtilities.convertPoint(resizeObject, e.getPoint(), resizeObject.getParent());
    }

    public void mouseReleased(MouseEvent e) {
        orgPoint = SwingUtilities.convertPoint(resizeObject, e.getPoint(), resizeObject.getParent());
        canvas.setCursor(Cursor.getDefaultCursor());
    }

    public void mouseMoved(MouseEvent e) {
        List<HotSpotArea> hotSpots = resizeObject.getResizeHotSpotAreas();
        for (HotSpotArea hotSpot : hotSpots) {
            if (hotSpot.getArea().contains(e.getPoint())) {
                canvas.setCursor(hotSpot.getCursorType());
                return;
            }
        }
        if (canvas.getCursor().getType() != Cursor.HAND_CURSOR) {
            canvas.setCursor(Cursor.getDefaultCursor());
        }
    }

    public void mousePressed(MouseEvent e) {
        orgPoint = SwingUtilities.convertPoint(resizeObject, e.getPoint(), resizeObject.getParent());
        orgRect = resizeObject.getBounds();
        orgWidth = (int) orgRect.getWidth();
        orgHeight = (int) orgRect.getHeight();
    }

    public void mouseDragged(MouseEvent e) {
        int cursorType = canvas.getCursor().getType();
        Point dstPoint = SwingUtilities.convertPoint(resizeObject, e.getPoint(), resizeObject.getParent());
        dstRect = new Rectangle();

        switch (cursorType) {
        case Cursor.W_RESIZE_CURSOR:
            wResize(dstPoint);
            resetObjectCellSize();
            break;
        case Cursor.N_RESIZE_CURSOR:
            nResize(dstPoint);
            resetObjectCellSize();
            break;
        case Cursor.NW_RESIZE_CURSOR:
            nwResize(dstPoint);
            resetObjectCellSize();
            break;
        case Cursor.E_RESIZE_CURSOR:
            eResize(dstPoint);
            resetObjectCellSize();
            break;
        case Cursor.NE_RESIZE_CURSOR:
            neResize(dstPoint);
            resetObjectCellSize();
            break;
        case Cursor.S_RESIZE_CURSOR:
            sResize(dstPoint);
            resetObjectCellSize();
            break;
        case Cursor.SE_RESIZE_CURSOR:
            seResize(dstPoint);
            resetObjectCellSize();
            break;
        case Cursor.SW_RESIZE_CURSOR:
            swResize(dstPoint);
            resetObjectCellSize();
            break;
        }

    }

    private void resetObjectCellSize() {
        canvas.clearSelectAction();

        resizeObject.resetCellSize(dstRect);
        canvas.updateArrow(resizeObject);
        canvas.repaint(resizeObject.getStartX(), resizeObject.getStartY(), resizeObject.getEndX(), resizeObject.getEndY());
        canvas.setChanged(true);
    }

    private void swResize(Point dstPoint) {
        if (dstPoint.x > orgRect.x + orgWidth && dstPoint.y < orgRect.y) {
            dstRect.setBounds((int) orgRect.x + orgWidth, (int) dstPoint.y, (int) dstPoint.x - (orgRect.x + orgWidth), (int) orgRect.y - dstPoint.y);
        } else {
            dstRect.setBounds((int) dstPoint.x, (int) orgRect.y, (int) orgRect.x + orgWidth - dstPoint.x, (int) dstPoint.y - orgRect.y);
        }
    }

    private void neResize(Point dstPoint) {
        if (dstPoint.x > orgRect.x && dstPoint.y < orgRect.y + orgHeight) {
            dstRect.setBounds((int) orgRect.x, (int) dstPoint.y, (int) dstPoint.x - orgRect.x, (int) orgRect.y + orgHeight - dstPoint.y);
        } else {
            dstRect.setBounds((int) dstPoint.x, (int) orgRect.y + orgHeight, (int) orgRect.x - dstPoint.x, (int) dstPoint.y - (orgRect.y + orgHeight));
        }
    }

    private void nwResize(Point dstPoint) {
        if (dstPoint.x > orgRect.x + orgWidth && dstPoint.y > orgRect.y + orgHeight) {
            dstRect.setBounds((int) orgRect.x + orgWidth, (int) orgRect.y + orgHeight, (int) dstPoint.x - (orgRect.x + orgWidth), (int) dstPoint.y - (orgRect.y + orgHeight));
        } else {
            dstRect.setBounds((int) dstPoint.x, (int) dstPoint.y, (int) (orgRect.x + orgWidth) - dstPoint.x, (int) (orgRect.y + orgHeight) - dstPoint.y);
        }
    }

    private void seResize(Point dstPoint) {
        if (dstPoint.x > orgRect.x && dstPoint.y > orgRect.y) {
            dstRect.setBounds((int) orgRect.x, (int) orgRect.y, (int) dstPoint.x - orgRect.x, (int) dstPoint.y - orgRect.y);
        } else {
            dstRect.setBounds((int) dstPoint.x, (int) dstPoint.y, (int) orgRect.x - dstPoint.x, (int) orgRect.y - dstPoint.y);
        }
    }

    private void sResize(Point dstPoint) {
        if (dstPoint.y > orgRect.y) {
            dstRect.setBounds((int) orgRect.x, (int) orgRect.y, (int) orgRect.getWidth(), (int) dstPoint.y - orgRect.y);
        } else {
            dstRect.setBounds((int) orgRect.x, (int) dstPoint.y, (int) orgRect.getWidth(), (int) orgRect.y - dstPoint.y

            );
        }
    }

    private void nResize(Point dstPoint) {
        if (dstPoint.y > orgRect.y + orgHeight) {
            dstRect.setBounds((int) orgRect.x, (int) orgRect.y + orgHeight, (int) orgRect.getWidth(), (int) dstPoint.y - (orgRect.y + orgHeight));
        } else {
            dstRect.setBounds((int) orgRect.x, (int) dstPoint.y, (int) orgRect.getWidth(), (int) (orgRect.y + orgHeight) - dstPoint.y);
        }
    }

    private void eResize(Point dstPoint) {
        if (dstPoint.x > orgRect.x) {
            dstRect.setBounds((int) orgRect.x, (int) orgRect.y, (int) dstPoint.x - orgRect.x, (int) orgRect.getHeight());
        } else {
            dstRect.setBounds((int) dstPoint.x, (int) orgRect.y, (int) orgRect.x - dstPoint.x, (int) orgRect.getHeight());
        }
    }

    private void wResize(Point dstPoint) {
        if (dstPoint.x > orgRect.x + orgWidth) {
            dstRect.setBounds((int) orgRect.x + orgWidth, (int) orgRect.y, (int) dstPoint.x - (orgRect.x + orgWidth), (int) orgRect.getHeight());
        } else {
            dstRect.setBounds((int) dstPoint.x, (int) orgRect.y, (int) orgRect.x + orgWidth - dstPoint.x, (int) orgRect.getHeight());
        }
    }

}
