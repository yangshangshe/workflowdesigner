package com.ea.designer.event;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


import com.ea.designer.Canvas;
import com.ea.designer.ui.Arrow;
import com.ea.designer.ui.Cell;

/**
 * ****************************************************************************
 * 
 * @function：JTextField modify document listener
 * @author yss
 * @file_name ModifyJTFDocumentListener.java
 * @package_name：com.ea.designer.event
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class ModifyJTFDocumentListener implements DocumentListener {

    private Canvas canvas;
    private Arrow arrow;
    private Cell cell;

    public ModifyJTFDocumentListener(Canvas canvas) {
        this.canvas = canvas;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public void setArrow(Arrow arrow) {
        this.cell = null;
        this.arrow = arrow;
    }

    private void changeText() {
        if (arrow != null)
            canvas.getArrows().get(canvas.getArrows().indexOf(arrow)).setText(canvas.getModifyJTF().getText());
        if (cell != null)
            cell.setText(canvas.getModifyJTF().getText());

        canvas.setChanged(true);
    }

    public void changedUpdate(DocumentEvent e) {
        changeText();
    }

    public void insertUpdate(DocumentEvent e) {
        changeText();
    }

    public void removeUpdate(DocumentEvent e) {
        changeText();
    }

    public void setCell(Cell cell) {
        this.arrow = null;
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

}
