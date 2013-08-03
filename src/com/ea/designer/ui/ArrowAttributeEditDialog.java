package com.ea.designer.ui;

import java.awt.BorderLayout;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import com.ea.designer.Canvas;
import com.ea.designer.bean.NameValueBean;
import com.ea.designer.config.AppManager;
import com.ea.designer.resources.EditorResource;
import com.ea.designer.resources.NodeJumpResource;
import com.ea.designer.util.ReflectionUtil;
import com.ea.designer.util.StringUtil;
import com.ea.designer.util.XmlReader;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name ArrowAttributeEditDialog.java
 * @package_name：com.ea.designer.ui
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class ArrowAttributeEditDialog extends JDialog {

    private JScrollPane attrEditScrollPane;
    private ArrowAttributeEditTable attrEditTable;
    private NodeJumpResource editDestination;
    private Arrow arrow;
    private Canvas canvas;

    public ArrowAttributeEditDialog(Arrow arrow, Canvas canvas) {
        this.arrow = arrow;
        this.canvas = canvas;
        this.editDestination = new NodeJumpResource();
        editDestination.setText(arrow.getText());
        editDestination.setToId(Long.parseLong(arrow.getEndNode().getId()));
        EditorResource editor = new EditorResource();
        editor.setOptionId(arrow.getEditorOptionId());
        editor.setOptionName(arrow.getEditorOptionName());
        editor.setType(arrow.getEditorType());
        editor.setValue(arrow.getEditorValue());
        editDestination.setEditor(editor);

        initComponent();

    }

    private void initComponent() {
        attrEditTable = new ArrowAttributeEditTable();
        attrEditScrollPane = new JScrollPane(attrEditTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setLayout(new BorderLayout());
        add(attrEditScrollPane, BorderLayout.CENTER);
        setSize(320, 240);
        setTitle(AppManager.getResources().getString("nodeattributeeditpanel.title"));
        setAlwaysOnTop(true);

    }

    class ArrowAttributeEditTable extends JTable {

        public ArrowAttributeEditTable() {
            List<NameValueBean> list = new ArrayList<NameValueBean>();

            NameValueBean nvb = new NameValueBean();
            nvb.setName(XmlReader.XML_ATTR_CELL_text);
            nvb.setValue(editDestination.getText());
            list.add(nvb);

            Field[] fields = XmlReader.class.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().startsWith("XML_ATTR_EDITOR_")) {
                    nvb = new NameValueBean();
                    nvb.setName(field.getName().replaceAll("XML_ATTR_EDITOR_", ""));

                    Object value = ReflectionUtil.getFieldValue(editDestination, nvb.getName());
                    if (value == null) {
                        value = ReflectionUtil.getFieldValue(editDestination.getEditor(), nvb.getName());
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

        public TableCellEditor getCellEditor(int row, int column) {
            TableColumn tableColumn = getColumnModel().getColumn(column);
            TableCellEditor editor = tableColumn.getCellEditor();
            if (editor == null) {
                editor = getDefaultEditor(getColumnClass(column));
            }
            if (getValueAt(row, 0).equals(XmlReader.XML_ATTR_EDITOR_type)) {
                JComboBox comboBox = new JComboBox();
                comboBox.addItem(EditorResource.EDITOR_TYPE_JTEXTFIELD);
                comboBox.addItem(EditorResource.EDITOR_TYPE_JCOMBOBOX);
                return new DefaultCellEditor(comboBox);
            }
            return editor;
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
                Arrow editArrow = canvas.getArrows().get(canvas.getArrows().indexOf(arrow));
                ReflectionUtil.setFieldValue(editArrow, attrName, value);

                canvas.setChanged(true);
                canvas.repaint();

                removeEditor();
            }
        }

    }

}
