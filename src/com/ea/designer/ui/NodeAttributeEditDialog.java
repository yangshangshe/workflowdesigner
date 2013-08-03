package com.ea.designer.ui;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;


import com.ea.designer.Canvas;
import com.ea.designer.bean.NameValueBean;
import com.ea.designer.config.AppManager;
import com.ea.designer.log.Logger;
import com.ea.designer.resources.NodeJumpResource;
import com.ea.designer.resources.NodeResource;
import com.ea.designer.util.ReflectionUtil;
import com.ea.designer.util.StringUtil;
import com.ea.designer.util.XmlReader;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name NodeAttributeEditDialog.java
 * @package_name：com.ea.designer.ui
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class NodeAttributeEditDialog extends JDialog {

    private JScrollPane arrtEditScrollPane;
    private AttributeEditTable attrEditTable;
    private NodeResource editDestination;
    private Cell cell;
    private String beforeId;

    private Canvas canvas;

    public NodeAttributeEditDialog(Cell cell, Canvas canvas) {

        this.editDestination = new NodeResource();
        this.editDestination.setStartX(cell.getStartX());
        this.editDestination.setStartY(cell.getStartY());
        this.editDestination.setEndX(cell.getEndX());
        this.editDestination.setEndY(cell.getEndY());
        this.editDestination.setDescription(cell.getDesc());
        this.editDestination.setID(Long.parseLong(cell.getId()));
        this.editDestination.setText(cell.getText());
        if (cell instanceof Node)
            this.editDestination.setNodeImage(((Node) cell).getNodeImage());

        this.cell = cell;
        
        beforeId = cell.getId();

        this.canvas = canvas;

        initComponent();

    }

    private void initComponent() {

        attrEditTable = new AttributeEditTable();
        arrtEditScrollPane = new JScrollPane(attrEditTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setLayout(new BorderLayout());
        add(arrtEditScrollPane, BorderLayout.CENTER);
        setSize(320, 240);
        setTitle(AppManager.getResources().getString("nodeattributeeditpanel.title"));
        setAlwaysOnTop(true);
    }

    class AttributeEditTable extends JTable {

        public AttributeEditTable() {

            List<NameValueBean> list = new ArrayList<NameValueBean>();
            Field[] fields = XmlReader.class.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().startsWith("XML_ATTR_CELL_")) {
                    NameValueBean nvb = new NameValueBean();
                    nvb.setName(field.getName().replaceAll("XML_ATTR_CELL_", ""));

                    Object value = ReflectionUtil.getFieldValue(editDestination, nvb.getName());
                    if (value == null) {
                        value = ReflectionUtil.getFieldValue(editDestination.getResource(), nvb.getName());
                    }
                    if (StringUtil.isNull(String.valueOf(value)))
                        value = "";
                    nvb.setValue(String.valueOf(value));
                    list.add(nvb);
                }
            }

            String[] columnNames = { AppManager.getResources().getString("nodeattributeeditpanel.table.columnattrname"), AppManager.getResources().getString("nodeattributeeditpanel.table.columnattrvalue") };

            Object[][] columnValues = new Object[list.size()][2];
            for (int i = 0; i < list.size(); i++) {
                columnValues[i][0] = list.get(i).getName();
                columnValues[i][1] = list.get(i).getValue();
            }

            DefaultTableModel tableModel = new DefaultTableModel(columnValues, columnNames);
            this.setModel(tableModel);
        }

        public boolean isCellEditable(int row, int column) {
            if (column == 0) {
                return false;
            }
            return true;
        }

        public void editingStopped(ChangeEvent e) {
            TableCellEditor editor = getCellEditor();
            if (editor != null) {
                Object value = editor.getCellEditorValue();
                setValueAt(value, editingRow, editingColumn);

                String attrName = String.valueOf(getValueAt(editingRow, 0));
                Cell modifyCell = canvas.getCells().get(canvas.getCells().indexOf(cell));
                
                if(attrName.equals(XmlReader.XML_ATTR_CELL_id)){
                   if( canvas.isCellIdExist(String.valueOf(value)) && !value.equals(beforeId)){
                       JOptionPane.showMessageDialog(this, AppManager.getResources().getString("nodeattributeeditpanel.message.idexist"));
                       return;
                   }
                }

                String beforeModifyCellId = modifyCell.getId();
                canvas.getCells().remove(modifyCell);
                NodeResource nodeResource = new NodeResource();
                nodeResource.setID(Long.parseLong(modifyCell.getId()));
                canvas.getDisplayProject().getDefineNodes().remove(nodeResource);
                ReflectionUtil.setFieldValue(modifyCell, attrName, value);
                modifyCell.resetCellSize(new Rectangle(modifyCell.getStartX(), modifyCell.getStartY(), Math.abs(modifyCell.getEndX() - modifyCell.getStartX()), Math.abs(modifyCell.getEndY() - modifyCell.getStartY())));
                /**
                 * if the id attribute is modified , change this node contains
                 * in arrow
                 */
                for (Cell cell : canvas.getCells()) {
                    for (Arrow arrow : cell.getToArrowList()) {
                        if (arrow.getStartNode().getId().equals(beforeModifyCellId)) {
                            arrow.getStartNode().setId(modifyCell.getId());
                        }
                        if (arrow.getEndNode().getId().equals(beforeModifyCellId)) {
                            arrow.getEndNode().setId(modifyCell.getId());
                        }
                    }
                }

                /**
                 * if the id attribute is modified , we need to delete the old
                 * jump ( add the new jump will done in canvas's
                 * convertCanvasNodeToProjectNode ,canvas can recognition the
                 * add option but can't recognition the delete option)
                 */
                for (NodeResource node : canvas.getDisplayProject().getDefineNodes()) {
                    for (int i = node.getJumpNodes().size(); i > 0; i--) {
                        NodeJumpResource jump = node.getJumpNodes().get(i - 1);
                        if (String.valueOf(jump.getToId()).equals(beforeModifyCellId)) {
                            Logger.debug("find the old jump " + jump);
                            canvas.getDisplayProject().getNodeResource(String.valueOf(node.getID())).getJumpNodes().remove(jump);
                        }
                    }
                }
                canvas.getCells().add(modifyCell);
                canvas.setChanged(true);
                canvas.repaint();

                removeEditor();
            }
        }

    }

}
