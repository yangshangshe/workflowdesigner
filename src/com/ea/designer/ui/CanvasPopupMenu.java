package com.ea.designer.ui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.ea.designer.Canvas;
import com.ea.designer.config.AppManager;
import com.ea.designer.event.ModifyJTFDocumentListener;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name CanvasPopupMenu.java
 * @package_name：com.ea.designer.ui
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class CanvasPopupMenu extends JPopupMenu {

    JMenuItem deleteOpt;
    JMenuItem modifyTextOpt;
    JMenuItem addOpt;
    JMenuItem modifyAttrOpt;

    Canvas canvas;
    ModifyJTFDocumentListener mdfDcListener;

    Cell optingCell;
    Arrow optingArrow;

    public CanvasPopupMenu(Canvas canvas) {
        this.canvas = canvas;
        createUI();
    }

    private void createUI() {
        mdfDcListener = new ModifyJTFDocumentListener(canvas);

        addOpt = new JMenuItem(AppManager.getResources().getString("canvaspopupmenu.popup.cell.add"));
        deleteOpt = new JMenuItem(AppManager.getResources().getString("canvaspopupmenu.popup.cell.delete"));
        modifyTextOpt = new JMenuItem(AppManager.getResources().getString("canvaspopupmenu.popup.cell.modifytext"));
        modifyAttrOpt = new JMenuItem(AppManager.getResources().getString("canvaspopupmenu.popup.cell.modifyattr"));

        this.add(addOpt);
        this.add(modifyTextOpt);
        this.add(modifyAttrOpt);
        this.addSeparator();
        this.add(deleteOpt);

        this.setFocusable(true);

    }

    public void changeToCellOpt(Cell optingCell) {
        this.optingCell = optingCell;
        addOpt.setVisible(true);
        modifyAttrOpt.setVisible(true);

        addOpt.setText(AppManager.getResources().getString("canvaspopupmenu.popup.cell.add"));
        addOpt.setIcon(AppManager.getResources().getImageIcon("canvaspopupmenu.popup.cell.add"));
        deleteOpt.setText(AppManager.getResources().getString("canvaspopupmenu.popup.cell.delete"));
        deleteOpt.setIcon(AppManager.getResources().getImageIcon("canvaspopupmenu.popup.cell.delete"));
        modifyTextOpt.setText(AppManager.getResources().getString("canvaspopupmenu.popup.cell.modifytext"));
        modifyTextOpt.setIcon(AppManager.getResources().getImageIcon("canvaspopupmenu.popup.cell.modifytext"));
        modifyAttrOpt.setText(AppManager.getResources().getString("canvaspopupmenu.popup.cell.modifyattr"));
        modifyAttrOpt.setIcon(AppManager.getResources().getImageIcon("canvaspopupmenu.popup.cell.modifyattr"));

        clearMenuItemActionListeners();

        addOpt.addActionListener(new AddCellActionListener());
        deleteOpt.addActionListener(new DeleteCellActionListener());
        modifyTextOpt.addActionListener(new ModifyCellActionListener());
        modifyAttrOpt.addActionListener(new ModifyCellAttrActionListener());

    }

    private void clearMenuItemActionListeners() {
        ActionListener[] addAls = addOpt.getActionListeners();
        for (ActionListener l : addAls) {
            if (l instanceof AddCellActionListener) {
                addOpt.removeActionListener(l);
            }
        }
        ActionListener[] delAls = deleteOpt.getActionListeners();
        for (ActionListener l : delAls) {
            if (l instanceof DeleteCellActionListener) {
                deleteOpt.removeActionListener(l);
            }
            if (l instanceof DeleteRelationActionListener) {
                deleteOpt.removeActionListener(l);
            }
        }
        ActionListener[] modAls = modifyTextOpt.getActionListeners();
        for (ActionListener l : modAls) {
            if (l instanceof ModifyCellActionListener) {
                modifyTextOpt.removeActionListener(l);
            }
            if (l instanceof ModifyRelationActionListener) {
                modifyTextOpt.removeActionListener(l);
            }
        }

        ActionListener[] modAttrAls = modifyAttrOpt.getActionListeners();
        for (ActionListener l : modAttrAls) {
            if (l instanceof ModifyCellAttrActionListener) {
                modifyAttrOpt.removeActionListener(l);
            }
            if(l instanceof ModifyRelationAttrActionListener){
                modifyAttrOpt.removeActionListener(l);
            }
        }
        
    }

    public void changeToArrowOpt(Arrow optingArrow) {
        this.optingArrow = optingArrow;
        addOpt.setVisible(false);

        deleteOpt.setText(AppManager.getResources().getString("canvaspopupmenu.popup.arrow.delete"));
        deleteOpt.setIcon(AppManager.getResources().getImageIcon("canvaspopupmenu.popup.arrow.delete"));
        modifyTextOpt.setText(AppManager.getResources().getString("canvaspopupmenu.popup.arrow.modifytext"));
        modifyTextOpt.setIcon(AppManager.getResources().getImageIcon("canvaspopupmenu.popup.arrow.modifytext"));
        modifyAttrOpt.setText(AppManager.getResources().getString("canvaspopupmenu.popup.arrow.modifyattr"));
        modifyAttrOpt.setIcon(AppManager.getResources().getImageIcon("canvaspopupmenu.popup.arrow.modifyattr"));

        clearMenuItemActionListeners();

        deleteOpt.addActionListener(new DeleteRelationActionListener());
        modifyTextOpt.addActionListener(new ModifyRelationActionListener());
        modifyAttrOpt.addActionListener(new ModifyRelationAttrActionListener());
    }

    public ModifyJTFDocumentListener getMdfDcListener() {
        return mdfDcListener;
    }

    class AddCellActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Point position = CanvasPopupMenu.this.getLocation();
            position = SwingUtilities.convertPoint(optingCell, position, canvas);
            int startX = position.x;
            int startY = position.y;
            int endX = startX + Cell.DEFAULT_CELL_WIDTH;
            int endY = startY + Cell.DEFAULT_CELL_HEIGHT;
            Node newNode = new Node(startX, startY, endX, endY);
            canvas.addNode(newNode);
            canvas.setChanged(true);
            CanvasPopupMenu.this.setVisible(false);
        }
    }

    class ModifyCellActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CanvasPopupMenu.this.setVisible(false);
            canvas.showModifyCellJTF(optingCell);
        }
    }

    class DeleteCellActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CanvasPopupMenu.this.setVisible(false);
            canvas.removeNode(optingCell);
        }
    }

    class ModifyCellAttrActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CanvasPopupMenu.this.setVisible(false);
            NodeAttributeEditDialog editDialog = new NodeAttributeEditDialog(optingCell, canvas);
            editDialog.setLocation(canvas.getCenterPoint());
            editDialog.setVisible(true);
        }
    }

    class ModifyRelationActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CanvasPopupMenu.this.setVisible(false);
            canvas.showModifyArrowJTF(optingArrow);
        }
    }

    class DeleteRelationActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CanvasPopupMenu.this.setVisible(false);
            canvas.removeArrow(optingArrow);
        }
    }
    
    class ModifyRelationAttrActionListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            CanvasPopupMenu.this.setVisible(false);
            ArrowAttributeEditDialog editDialog = new ArrowAttributeEditDialog(optingArrow, canvas);
            editDialog.setLocation(canvas.getCenterPoint());
            editDialog.setVisible(true);
        }
        
    }

}
