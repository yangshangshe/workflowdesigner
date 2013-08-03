package com.ea.designer.ui;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


import com.ea.designer.Canvas;
import com.ea.designer.bean.NameValueBean;
import com.ea.designer.resources.EditorResource;
import com.ea.designer.util.StringUtil;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name Arrow.java
 * @package_name：com.ea.designer.ui
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class Arrow {
    /**
     * cell for arrow start
     */
    private Cell startNode;
    /**
     * cell for arrow end
     */
    private Cell endNode;
    /**
     * arrow's display area
     */
    private Polygon arrowArea;
    /**
     * the text for arrow
     */
    private String text;
    /**
     * the editor type for modify arrow
     */
    private String type;
    /**
     * the default editor value for modify arrow
     */
    private String value;
    /**
     * the optionid for editor when type is combo
     */
    private String optionId;
    /**
     * the optionname for editor when type is combo
     */
    private String optionName;

    public ArrowTextEditor getEditor(String value, final Canvas canvas) {
        ArrowTextEditor editor = new ArrowTextEditor();
        if (EditorResource.EDITOR_TYPE_JTEXTFIELD.equals(getEditorType())) {
            final JTextField field = new JTextField();
            field.setText(value);
            field.setBounds(0, 0, 100, 20);
            field.getDocument().addDocumentListener(new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    changeArrowText();
                }

                private void changeArrowText() {
                    canvas.getArrows().get(canvas.getArrows().indexOf(Arrow.this)).setText(field.getText());
                    canvas.setChanged(true);
                }

                public void insertUpdate(DocumentEvent e) {
                    changeArrowText();
                }

                public void removeUpdate(DocumentEvent e) {
                    changeArrowText();
                }

            });
            editor.setEditor(field);
        } else if (EditorResource.EDITOR_TYPE_JCOMBOBOX.equals(getEditorType())) {
            final JComboBox comboBox = new JComboBox();
            comboBox.setBounds(0, 0, 100, 20);
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            if (StringUtil.isNull(optionId) && !StringUtil.isNull(optionName)) {
                String[] names = optionName.split(",");
                for (int i = 0; i < names.length; i++) {
                    NameValueBean bean = new NameValueBean(names[i], names[i]);
                    model.addElement(bean);
                }
            } else if (!StringUtil.isNull(optionId) && !StringUtil.isNull(optionName)) {
                String[] names = optionName.split(",");
                String[] ids = optionId.split(",");
                for (int i = 0, n = names.length > ids.length ? names.length : ids.length; i < n; i++) {
                    NameValueBean bean = new NameValueBean();
                    bean.setName(names.length > i ? names[i] : "");
                    bean.setValue(ids.length > i ? ids[i] : "");
                    model.addElement(bean);
                }
            }
            if (!StringUtil.isNull(value)) {
                model.setSelectedItem(new NameValueBean(value, value));
            } else if (!StringUtil.isNull(value)) {
                model.setSelectedItem(new NameValueBean(value, value));
            }
            comboBox.setModel(model);

            comboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        canvas.getArrows().get(canvas.getArrows().indexOf(Arrow.this)).setText(((NameValueBean) comboBox.getSelectedItem()).getValue());
                        canvas.setChanged(true);
                    }
                }
            });

            editor.setEditor(comboBox);
        }
        return editor;
    }

    public String getEditorOptionId() {
        return optionId;
    }

    public void setEditorOptionId(String editorOptionId) {
        this.optionId = editorOptionId;
    }

    public String getEditorOptionName() {
        return optionName;
    }

    public void setEditorOptionName(String editorOptionName) {
        this.optionName = editorOptionName;
    }

    public String getEditorType() {
        return type;
    }

    public void setEditorType(String editorType) {
        this.type = editorType;
    }

    public String getEditorValue() {
        return value;
    }

    public void setEditorValue(String editorValue) {
        this.value = editorValue;
    }

    public Arrow(Cell startNode, Cell endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.arrowArea = getArrowArea(startNode, endNode);
    }

    public Arrow(Cell startNode, Cell endNode, String text) {
        this(startNode, endNode);
        this.text = text;
    }

    public Arrow(Cell startNode, Cell endNode, Polygon arrowArea) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.arrowArea = arrowArea;
    }

    public Arrow(Cell startNode, Cell endNode, Polygon arrowArea, String text) {
        this(startNode, endNode, arrowArea);
        this.text = text;
    }

    public void resetArrowArea(Cell startNode, Cell endNode) {
        this.arrowArea = getArrowArea(startNode, endNode);
    }

    private Polygon getArrowArea(Cell startNode, Cell endNode) {
        Point ps = null, pe = null;
        pe = calculatorArrowEndPoint(startNode, endNode);
        ps = calculatorArrowStartPoint(startNode, endNode);
        Polygon arrowArea = getArrow(ps, pe);
        return arrowArea;
    }

    private Point calculatorArrowStartPoint(Cell cell, Cell endCell) {
        int csx = cell.getStartX() + cell.getCellWidth() / 2;
        int csy = cell.getStartY() + cell.getCellHeight() / 2;
        int cex = endCell.getStartX() + endCell.getCellWidth() / 2;
        int cey = endCell.getStartY() + endCell.getCellHeight() / 2;
        int k;
        if (cex - csx == 0) {
            k = 1;
        } else {
            k = (cey - csy) / (cex - csx);
        }

        int asx, asy;
        // east edge
        asx = cell.getStartX() + cell.getCellWidth();
        asy = k * (asx - cex) + cey;
        Point ps = new Point(asx, asy);
        Point pe = new Point(cex, cey);
        Point pp = null;
        if (cell.containsIncludeEdge(SwingUtilities.convertPoint(cell.getParent(), ps, cell))) {
            if (pp == null) {
                pp = ps;
            } else {
                pp = getPointWithMinDistance(pe, pp, ps);
            }
        }

        // south edge
        asy = cell.getEndY();
        if (k == 0) {
            asx = csx;
        } else {
            asx = (asy - cey) / k + cex;
        }
        ps = new Point(asx, asy);
        if (cell.containsIncludeEdge(SwingUtilities.convertPoint(cell.getParent(), ps, cell))) {
            if (pp == null) {
                pp = ps;
            } else {
                pp = getPointWithMinDistance(pe, pp, ps);
            }
        }

        // west edge
        asx = cell.getStartX();
        asy = k * (asx - cex) + cey;
        ps = new Point(asx, asy);
        if (cell.containsIncludeEdge(SwingUtilities.convertPoint(cell.getParent(), ps, cell))) {
            if (pp == null) {
                pp = ps;
            } else {
                pp = getPointWithMinDistance(pe, pp, ps);
            }
        }

        // north edge
        asy = cell.getStartY();
        if (k == 0) {
            asx = csx;
        } else {
            asx = (asy - cey) / k + cex;
        }
        ps = new Point(asx, asy);
        if (cell.containsIncludeEdge(SwingUtilities.convertPoint(cell.getParent(), ps, cell))) {
            if (pp == null) {
                pp = ps;
            } else {
                pp = getPointWithMinDistance(pe, pp, ps);
            }
        }

        if (pp == null) {
            pp = new Point(csx, csy);
        }

        return pp;
    }

    private Point calculatorArrowEndPoint(Cell cell, Cell endCell) {
        int csx = cell.getStartX() + cell.getCellWidth() / 2;
        int csy = cell.getStartY() + cell.getCellHeight() / 2;
        int cex = endCell.getStartX() + endCell.getCellWidth() / 2;
        int cey = endCell.getStartY() + endCell.getCellHeight() / 2;
        int aex = 0, aey = 0;
        int k;
        if (cex - csx == 0) {
            k = 1;
        } else {
            k = (cey - csy) / (cex - csx);
        }

        if (cell.getStartX() + cell.getCellWidth() / 2 <= endCell.getStartX()) {
            // cell's Rectangle center in destination cell's left edge's left
            // side
            aex = endCell.getStartX();
            aey = k * (aex - cex) + cey;
            if (aey < endCell.getStartY())
                aey = endCell.getStartY();
            if (aey > endCell.getEndY())
                aey = endCell.getEndY();
        } else if (cell.getStartX() + cell.getCellWidth() / 2 >= endCell.getEndX()) {
            // cell's Rectangle center in destination cell's right edge's right
            // side
            aex = endCell.getStartX() + endCell.getCellWidth();
            aey = k * (aex - cex) + cey;
            if (aey < endCell.getStartY())
                aey = endCell.getStartY();
            if (aey > endCell.getEndY())
                aey = endCell.getEndY();
        } else {// cell's Rectangle center between destination cell's Rectangle
            if (k != 0) {
                if (cell.getStartY() > endCell.getEndY()) {// cell's Rectangle
                    // center below
                    // destination
                    // cell's south edge
                    aey = endCell.getEndY();
                    aex = (aey - cey) / k + cex;
                } else {// cell's Rectangle center aboard destination cell's
                    // north edge
                    aey = endCell.getStartY();
                    aex = (aey - cey) / k + cex;
                }
            }
        }

        if (aex == 0 && aey == 0) {
            aex = cex;
            aey = cey;
        }

        return new Point(aex, aey);
    }

    private Point getPointWithMinDistance(Point dest, Point compare, Point compare2) {
        double dist1 = Math.abs(dest.x - compare.x) * Math.abs(dest.x - compare.x) + Math.abs(dest.y - compare.y) * Math.abs(dest.y - compare.y);
        double dist2 = Math.abs(dest.x - compare2.x) * Math.abs(dest.x - compare2.x) + Math.abs(dest.y - compare2.y) * Math.abs(dest.y - compare2.y);
        if (dist1 >= dist2)
            return compare2;
        else
            return compare;
    }

    public static Polygon getArrow(Point p0, Point pe) {
        // Geometry of arrow
        double spacing = 2;
        double width = 8;
        double arrow = 10;

        double dx = pe.getX() - p0.getX();
        double dy = pe.getY() - p0.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        double length = dist - 2 * spacing - arrow;

        // Computes the norm and the inverse norm
        double nx = dx / dist;
        double ny = dy / dist;
        double basex = length * nx;
        double basey = length * ny;
        double floorx = width * ny / 3;
        double floory = -width * nx / 3;

        // Computes points
        double p0x = p0.getX() - floorx / 2 + spacing * nx;
        double p0y = p0.getY() - floory / 2 + spacing * ny;
        double p1x = p0x + floorx;
        double p1y = p0y + floory;
        double p2x = p1x + basex;
        double p2y = p1y + basey;
        double p3x = p2x + floorx;
        double p3y = p2y + floory;
        // p4 not required
        double p5x = p3x - 3 * floorx;
        double p5y = p3y - 3 * floory;

        Polygon poly = new Polygon();
        poly.addPoint((int) Math.round(p0x), (int) Math.round(p0y));
        poly.addPoint((int) Math.round(p1x), (int) Math.round(p1y));
        poly.addPoint((int) Math.round(p2x), (int) Math.round(p2y));
        poly.addPoint((int) Math.round(p3x), (int) Math.round(p3y));
        poly.addPoint((int) Math.round(pe.getX() - spacing * nx), (int) Math.round(pe.getY() - spacing * ny));
        poly.addPoint((int) Math.round(p5x), (int) Math.round(p5y));
        poly.addPoint((int) Math.round(p5x + floorx), (int) Math.round(p5y + floory));
        return poly;
    }

    public Polygon getArrowArea() {
        return arrowArea;
    }

    public void setArrowArea(Polygon arrowArea) {
        this.arrowArea = arrowArea;
    }

    public Cell getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public Cell getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int hashCode() {
        return startNode.hashCode() * endNode.hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Arrow) {
            if (((Arrow) o).getStartNode().equals(this.getStartNode()) && ((Arrow) o).getEndNode().equals(this.getEndNode())) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return startNode.getId() + " -> " + endNode.getId() + " " + text + " [" + arrowArea.getBounds() + "]";
    }

}
