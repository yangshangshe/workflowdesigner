package com.ea.designer.event;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;


import com.ea.designer.Canvas;
import com.ea.designer.ui.Arrow;

/**
 * ****************************************************************************
 * 
 * @function：arrow operator
 * @author yss
 * @file_name ArrowListener.java
 * @package_name：com.ea.designer.event
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class ArrowListener extends MouseInputAdapter {

    Canvas canvas;

    public ArrowListener(Canvas canvas) {
        this.canvas = canvas;
    }

    public void mouseClicked(MouseEvent e) {

        List<Arrow> arrows = canvas.getArrows();
        canvas.clearSelectAction();
        for (Arrow arrow : arrows) {
            if (arrow.getArrowArea().contains(e.getPoint())) {
                if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
                    Point position = e.getPoint();
                    SwingUtilities.convertPointToScreen(position, canvas);
                    canvas.showArrowPopupMenu(position, arrow);
                }
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    canvas.showModifyArrowJTF(arrow);
                } else {
                    canvas.markArrow(arrow);
                }
                return;
            } else {
                canvas.clearSelectAction();
            }
        }

    }

}
