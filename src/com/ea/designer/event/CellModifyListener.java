package com.ea.designer.event;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;


import com.ea.designer.Canvas;
import com.ea.designer.ui.Cell;

/**
 * ****************************************************************************
 * 
 * @function：cell modify
 * @author yss
 * @file_name CellModifyListener.java
 * @package_name：com.ea.designer.event
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class CellModifyListener extends MouseInputAdapter {

    private Cell modifyCell;
    private Canvas canvas;

    public CellModifyListener(Cell modifyCell, Canvas canvas) {
        this.canvas = canvas;
        this.modifyCell = modifyCell;
    }

    public void mouseEntered(MouseEvent e) {
        canvas.removeArrowListener();
    }

    public void mouseExited(MouseEvent e) {
        canvas.addArrowListeners();
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
            Point position = e.getPoint();
            SwingUtilities.convertPointToScreen(position, canvas);
            canvas.showCellPopupMenu(SwingUtilities.convertPoint(modifyCell, position, canvas), modifyCell);
        }

        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            canvas.showModifyCellJTF(modifyCell);
        }
    }

}
