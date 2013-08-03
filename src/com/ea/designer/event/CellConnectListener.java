package com.ea.designer.event;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;


import com.ea.designer.Canvas;
import com.ea.designer.ui.Cell;
import com.ea.designer.ui.Node;

/**
 * ****************************************************************************
 * 
 * @function：connect cell
 * @author yss
 * @file_name CellConnectListener.java
 * @package_name：com.ea.designer.event
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class CellConnectListener extends MouseInputAdapter {

    /**
     * the connect object
     */
    Cell connectObject;

    /**
     * the container for the connect object
     */
    Canvas canvas;

    public CellConnectListener(Cell connectObject, Canvas canvas) {
        this.connectObject = connectObject;
        this.canvas = canvas;
    }

    public void mouseMoved(MouseEvent e) {
        boolean isEnter = false;
        for (Cell cell : canvas.getCells()) {
            if (cell.getConnectHotSpotArea().getArea().contains(e.getPoint()) && cell.isConnecting()) {
                isEnter = true;
            }
        }
        if (isEnter) {
            canvas.setCursor(connectObject.getConnectHotSpotArea().getCursorType());
            canvas.removeCellDragListener(connectObject);
        } else {
            canvas.setCursor(Cursor.getDefaultCursor());
            canvas.addCellDragListener(connectObject);
        }
    }

    public void mouseDragged(MouseEvent e) {
        Point startP = new Point(connectObject.getCenterX(), connectObject.getCenterY());
        canvas.addTempArrow(SwingUtilities.convertPoint(connectObject, startP, canvas), SwingUtilities.convertPoint(connectObject, e.getPoint(), canvas));
    }

    public void mouseReleased(MouseEvent e) {
        Cell connectCell = null;
        for (Cell cell : canvas.getCells()) {
            Rectangle rect = SwingUtilities.convertRectangle(cell, cell.getConnectHotSpotArea().getArea(), canvas);
            Point p = SwingUtilities.convertPoint(connectObject, e.getPoint(), canvas);
            if (rect.contains(p) && cell.isConnecting()) {
                connectCell = cell;
            }
        }
        if (connectCell != null) {
            canvas.addArrow((Node) connectObject, (Node) connectCell);
            canvas.setChanged(true);
            canvas.removeTempArrow();
        } else {
            canvas.removeTempArrow();
        }
    }
}
