package com.ea.designer.event;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;


import com.ea.designer.Canvas;
import com.ea.designer.ui.Cell;

/**
 * ****************************************************************************
 * 
 * @function： drag cell
 * @author yss
 * @file_name CellDragListener.java
 * @package_name：com.ea.designer.event
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class CellDragListener extends MouseInputAdapter {

    /**
     * the original point before drag
     */
    Point originalPoint = new Point(0, 0);

    /**
     * the drag object
     */
    Cell dragObject;

    /**
     * the container for the drag object
     */
    Canvas canvas;

    public CellDragListener(Cell dragObject, Canvas canvas) {
        this.canvas = canvas;
        this.dragObject = dragObject;
    }

    public void mouseDragged(MouseEvent e) {

        for (Cell cell : canvas.getCells())
            cell.setResizing(false);
        dragObject.setResizing(true);

        if (canvas.getCursor().getType() != Cursor.DEFAULT_CURSOR)
            return;
        Point destinationPoint = SwingUtilities.convertPoint(dragObject, e.getPoint(), dragObject.getParent());
        dragObject.setLocation(dragObject.getX() + (destinationPoint.x - originalPoint.x), dragObject.getY() + (destinationPoint.y - originalPoint.y));

        originalPoint = destinationPoint;

        dragObject.setStartX(dragObject.getX() + (destinationPoint.x - originalPoint.x));
        dragObject.setStartY(dragObject.getY() + (destinationPoint.y - originalPoint.y));
        dragObject.setEndX(dragObject.getStartX() + dragObject.getWidth());
        dragObject.setEndY(dragObject.getStartY() + dragObject.getHeight());
        canvas.updateArrow(dragObject);

        canvas.resizeDimension(dragObject);
        canvas.clearSelectAction();
        canvas.setChanged(true);

        dragObject.getParent().repaint();

    }

    public void mousePressed(MouseEvent e) {
        if (canvas.getCursor().getType() != Cursor.DEFAULT_CURSOR)
            return;
        originalPoint = SwingUtilities.convertPoint(dragObject, e.getPoint(), dragObject.getParent());
    }

}
