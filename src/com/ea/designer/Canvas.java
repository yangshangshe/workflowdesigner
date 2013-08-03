package com.ea.designer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import com.ea.designer.action.SaveAction;
import com.ea.designer.action.SaveAllAction;
import com.ea.designer.event.ArrowListener;
import com.ea.designer.event.CellConnectListener;
import com.ea.designer.event.CellDragListener;
import com.ea.designer.event.CellModifyListener;
import com.ea.designer.event.CellReSizeListener;
import com.ea.designer.log.Logger;
import com.ea.designer.resources.EditorResource;
import com.ea.designer.resources.NodeJumpResource;
import com.ea.designer.resources.NodeResource;
import com.ea.designer.resources.ProjectResource;
import com.ea.designer.ui.Arrow;
import com.ea.designer.ui.ArrowTextEditor;
import com.ea.designer.ui.CanvasPopupMenu;
import com.ea.designer.ui.Cell;
import com.ea.designer.ui.Node;
import com.ea.designer.util.StringUtil;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name Canvas.java
 * @package_name：com.ea.designer
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class Canvas extends JComponent {

    private static final int NET_POINT_SPACE = 6;
    private static final Color NET_POINT_COLOR = Color.green.darker();
    private static final float STROKE_WIDTH = 1.2f;

    /**
     * all cells display in the canvas but without arrows (cell's relation)
     */
    private List<Cell> cells = new ArrayList<Cell>();
    /**
     * all arrows display in the canvas (cell's relation)
     */
    private List<Arrow> arrows = new ArrayList<Arrow>();
    /**
     * current select arrows
     */
    private List<Arrow> selectArrows = new ArrayList<Arrow>();
    /**
     * current select cells
     */
    private List<Cell> selectCells = new ArrayList<Cell>();
    /**
     * temp arrow frow cell's connecting
     */
    private List<Point> tempArrow = new ArrayList<Point>();

    private CanvasPopupMenu popupMenu;
    /**
     * default modify JTextField for cell and arrow
     */
    private JTextField defaultModifyJTF;
    /**
     * arrow text editor (jtextfield,combo)
     */
    private ArrowTextEditor arrowTextEditor;

    /**
     * current display in the canvas's project
     */
    private ProjectResource displayProject;
    /**
     * the view
     */
    private View displayView;

    /**
     * is the canvas's cell or arrow infomation changed
     */
    private boolean isChanged = false;

    public boolean isChanged() {
        return isChanged;
    }

    public View getDisplayView() {
        return displayView;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
        if (isChanged)
            firePropertyChange(SaveAction.ID, false, isChanged);
        else
            firePropertyChange(SaveAction.ID, true, isChanged);

        if (getDisplayView() != null) {
            for (Canvas curCanvas : getDisplayView().getTabs()) {
                if (curCanvas.isChanged()) {
                    firePropertyChange(SaveAllAction.ID, false, true);
                    return;
                }
            }
            firePropertyChange(SaveAllAction.ID, true, false);
        }
    }

    public Canvas() {
        setUpSetting();
        createUI();
        displayCells();
    }

    public ProjectResource getDisplayProject() {
        convertCanvasNodeToProjectNode();
        return displayProject;
    }

    private void convertCanvasNodeToProjectNode() {
        for (Cell cell : cells) {
            if (displayProject.getNodeResource(cell.getId()) != null) {
                displayProject.getNodeResource(cell.getId()).setStartX(cell.getStartX());
                displayProject.getNodeResource(cell.getId()).setStartY(cell.getStartY());
                displayProject.getNodeResource(cell.getId()).setEndX(cell.getEndX());
                displayProject.getNodeResource(cell.getId()).setEndY(cell.getEndY());
                displayProject.getNodeResource(cell.getId()).setText(cell.getText());
                displayProject.getNodeResource(cell.getId()).setDescription(cell.getDesc());
                if (cell instanceof Node)
                    displayProject.getNodeResource(cell.getId()).setNodeImage(((Node) cell).getNodeImage());
            } else {
                NodeResource node = new NodeResource();
                node.setID(Long.parseLong(cell.getId()));
                node.setStartX(cell.getStartX());
                node.setStartY(cell.getStartY());
                node.setEndX(cell.getEndX());
                node.setEndY(cell.getEndY());
                node.setText(cell.getText());
                node.setDescription(cell.getDesc());
                if (cell instanceof Node)
                    node.setNodeImage(((Node) cell).getNodeImage());
                displayProject.addNodeResource(node);
            }
        }

        for (Arrow arrow : arrows) {
            for (NodeResource node : displayProject.getDefineNodes()) {
                boolean isNewArrow = true;
                for (NodeJumpResource jump : node.getJumpNodes()) {
                    if (node.getID() == Long.parseLong(arrow.getStartNode().getId()) && jump.getToId() == Long.parseLong(arrow.getEndNode().getId())) {
                        displayProject.getNodeResource(String.valueOf(node.getID())).getJumpNodes().get(node.getJumpNodes().indexOf(jump)).setText(arrow.getText());
                        isNewArrow = false;
                        if (jump.getEditor() != null) {
                            jump.getEditor().setType(arrow.getEditorType());
                            jump.getEditor().setOptionId(arrow.getEditorOptionId());
                            jump.getEditor().setOptionName(arrow.getEditorOptionName());
                            jump.getEditor().setValue(arrow.getEditorValue());
                        } else {
                            EditorResource editor = new EditorResource();
                            editor.setType(arrow.getEditorType());
                            editor.setValue(arrow.getEditorValue());
                            editor.setOptionId(arrow.getEditorOptionId());
                            editor.setOptionName(arrow.getEditorOptionName());
                            jump.setEditor(editor);
                        }

                    }
                }

                if (isNewArrow && Long.parseLong(arrow.getStartNode().getId()) == node.getID()) {
                    NodeJumpResource newJump = new NodeJumpResource();
                    newJump.setText(arrow.getText());
                    newJump.setToId(Long.parseLong(arrow.getEndNode().getId()));
                    EditorResource editor = new EditorResource();
                    editor.setType(arrow.getEditorType());
                    editor.setValue(arrow.getEditorValue());
                    editor.setOptionId(arrow.getEditorOptionId());
                    editor.setOptionName(arrow.getEditorOptionName());
                    newJump.setEditor(editor);
                    displayProject.getNodeResource(String.valueOf(node.getID())).getJumpNodes().add(newJump);
                }
            }

        }
    }

    public void setDisplayProject(ProjectResource displayProject) {
        this.displayProject = displayProject;
    }

    private void createUI() {
        popupMenu = new CanvasPopupMenu(this);

        defaultModifyJTF = new JTextField();
        defaultModifyJTF.setBounds(0, 0, 100, 20);
        defaultModifyJTF.setOpaque(true);
        defaultModifyJTF.setVisible(false);
        add(defaultModifyJTF);
    }

    public void showArrowPopupMenu(Point position, Arrow arrow) {
        clearSelectAction();
        popupMenu.changeToArrowOpt(arrow);
        popupMenu.setLocation(position);
        popupMenu.setVisible(true);
    }

    public void showCellPopupMenu(Point position, Cell cell) {
        clearSelectAction();
        popupMenu.changeToCellOpt(cell);
        popupMenu.setLocation(position);
        popupMenu.setVisible(true);
    }

    private void displayCells() {

        Component[] comps = this.getComponents();
        for (Component c : comps) {
            if (c instanceof Cell) {
                this.remove(c);
            }
        }

        Cell curCell;
        for (int i = cells.size() - 1; i >= 0; i--) {
            curCell = cells.get(i);
            this.add(curCell);
            setUpCellListeners(curCell);
        }

        addArrowListeners();

        resizeDimension(null);
    }

    /**
     * resize the cavas dimension , fix the scroll status
     * 
     * @param dragObject
     */
    public void resizeDimension(Cell dragObject) {
        int maxX = 0, maxY = 0;

        Cell curCell;
        for (int i = cells.size() - 1; i >= 0; i--) {
            curCell = cells.get(i);
            maxX = maxX > curCell.getEndX() ? maxX : curCell.getEndX();
            maxY = maxY > curCell.getEndY() ? maxY : curCell.getEndY();
        }

        if (dragObject != null) {
            maxX = maxX > dragObject.getEndX() ? maxX : dragObject.getEndX();
            maxY = maxY > dragObject.getEndY() ? maxY : dragObject.getEndY();
        }

        this.setPreferredSize(new Dimension(maxX, maxY));

        if (this.getParent() != null) {
            this.getParent().validate();
        }

    }

    public void removeArrowListener() {
        MouseListener[] mls = getMouseListeners();
        for (MouseListener l : mls) {
            if (l instanceof ArrowListener) {
                removeMouseListener(l);
            }
        }
        MouseMotionListener[] mmls = getMouseMotionListeners();
        for (MouseMotionListener l : mmls) {
            if (l instanceof ArrowListener) {
                removeMouseMotionListener(l);
            }
        }
    }

    public void addArrowListeners() {
        removeArrowListener();
        ArrowListener arrowListener = new ArrowListener(this);
        addMouseListener(arrowListener);
        addMouseMotionListener(arrowListener);
    }

    public void addCellConnectListener(Cell curCell) {
        removeCellConnectListener(curCell);
        CellConnectListener cellConnectListener = new CellConnectListener(curCell, this);
        curCell.addMouseListener(cellConnectListener);
        curCell.addMouseMotionListener(cellConnectListener);

    }

    public void removeCellConnectListener(Cell curCell) {
        if (curCell == null)
            return;
        MouseListener[] mls = curCell.getMouseListeners();
        for (MouseListener l : mls) {
            if (l instanceof CellConnectListener) {
                curCell.removeMouseListener(l);
            }
        }
        MouseMotionListener[] mmls = curCell.getMouseMotionListeners();
        for (MouseMotionListener l : mmls) {
            if (l instanceof CellConnectListener) {
                curCell.removeMouseMotionListener(l);
            }
        }
    }

    public void addCellDragListener(Cell curCell) {
        if (curCell == null)
            return;
        removeCellDragListener(curCell);
        CellDragListener dragCellLst = new CellDragListener(curCell, this);
        curCell.addMouseListener(dragCellLst);
        curCell.addMouseMotionListener(dragCellLst);
    }

    public void addCellResizeListener(Cell curCell) {
        if (curCell == null)
            return;
        removeCellResizeListener(curCell);
        CellReSizeListener resizeCellLst = new CellReSizeListener(curCell, this);
        curCell.addMouseListener(resizeCellLst);
        curCell.addMouseMotionListener(resizeCellLst);
    }

    public void addCellModifyListener(Cell curCell) {
        if (curCell == null)
            return;
        removeCellModifyListener(curCell);
        CellModifyListener cellModifyLst = new CellModifyListener(curCell, this);
        curCell.addMouseListener(cellModifyLst);
        curCell.addMouseMotionListener(cellModifyLst);
    }

    public void removeCellModifyListener(Cell curCell) {
        if (curCell == null)
            return;
        MouseListener[] mls = curCell.getMouseListeners();
        for (MouseListener l : mls) {
            if (l instanceof CellModifyListener) {
                curCell.removeMouseListener(l);
            }
        }
        MouseMotionListener[] mmls = curCell.getMouseMotionListeners();
        for (MouseMotionListener l : mmls) {
            if (l instanceof CellModifyListener) {
                curCell.removeMouseMotionListener(l);
            }
        }
    }

    public void removeCellResizeListener(Cell curCell) {
        if (curCell == null)
            return;
        MouseListener[] mls = curCell.getMouseListeners();
        for (MouseListener l : mls) {
            if (l instanceof CellReSizeListener) {
                curCell.removeMouseListener(l);
            }
        }
        MouseMotionListener[] mmls = curCell.getMouseMotionListeners();
        for (MouseMotionListener l : mmls) {
            if (l instanceof CellReSizeListener) {
                curCell.removeMouseMotionListener(l);
            }
        }
    }

    public void removeCellDragListener(Cell curCell) {
        if (curCell == null)
            return;
        MouseListener[] mls = curCell.getMouseListeners();
        for (MouseListener l : mls) {
            if (l instanceof CellDragListener) {
                curCell.removeMouseListener(l);
            }
        }
        MouseMotionListener[] mmls = curCell.getMouseMotionListeners();
        for (MouseMotionListener l : mmls) {
            if (l instanceof CellDragListener) {
                curCell.removeMouseMotionListener(l);
            }
        }
    }

    private void setUpCellListeners(Cell curCell) {
        addCellDragListener(curCell);
        addCellResizeListener(curCell);
        addCellModifyListener(curCell);
    }

    private void setUpSetting() {
        this.setLayout(null);

    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Rectangle repaintArea = g2.getClipBounds();
        g2.clearRect(repaintArea.x, repaintArea.y, repaintArea.width, repaintArea.height);

        paintNetPoint(g2);
        paintCellRelation(g2);
        paintCellRelationLabel(g2);
        paintSelectedMark(g2);
        paintLineArrow(g2);
    }

    private void paintLineArrow(Graphics2D g2) {
        for (int i = 0; i < tempArrow.size(); i = i + 2) {
            if (i + 1 < tempArrow.size()) {
                Polygon arrow = Arrow.getArrow(tempArrow.get(i), tempArrow.get(i + 1));
                g2.fill(arrow);
            }
        }
    }

    private void paintCellRelationLabel(Graphics2D g2) {
        Font originalFont = g2.getFont();
        Font labelFont = new Font(originalFont.getFamily(), Font.PLAIN, 14);
        g2.setFont(labelFont);
        for (Arrow arrow : arrows) {
            if (!StringUtil.isNull(arrow.getText())) {
                int x, y;
                Rectangle arrowArea = arrow.getArrowArea().getBounds();
                x = (int) arrowArea.getCenterX();
                y = (int) arrowArea.getCenterY();
                FontMetrics fm = g2.getFontMetrics();
                int stringWidth = fm.stringWidth(arrow.getText());
                g2.drawString(arrow.getText(), x - stringWidth / 2, y);
            }
        }
        g2.setFont(originalFont);
    }

    private void paintSelectedMark(Graphics2D g2) {
        Stroke originalStroke = g2.getStroke();
        Color originalColor = g2.getColor();
        float[] dash = { 4f, 0f, 2f };
        g2.setStroke(new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 2f));
        g2.setColor(Color.green);
        for (Arrow arrow : selectArrows) {
            for (int i = 0; i < arrow.getArrowArea().npoints; i++) {
                if (i == arrow.getArrowArea().npoints - 1) {
                    g2.drawLine(arrow.getArrowArea().xpoints[i], arrow.getArrowArea().ypoints[i], arrow.getArrowArea().xpoints[0], arrow.getArrowArea().ypoints[0]);
                } else {
                    g2.drawLine(arrow.getArrowArea().xpoints[i], arrow.getArrowArea().ypoints[i], arrow.getArrowArea().xpoints[i + 1], arrow.getArrowArea().ypoints[i + 1]);
                }
            }
        }
        g2.setStroke(originalStroke);
        g2.setColor(originalColor);
    }

    private void paintCellRelation(Graphics2D g) {
        Stroke originalStroke = g.getStroke();

        g.setStroke(new BasicStroke(STROKE_WIDTH));

        for (Cell cell : cells) {
            for (Arrow arrow : cell.getToArrowList()) {
                if (!arrows.contains(arrow)) {
                    arrows.add(arrow);
                }
                g.fill(arrow.getArrowArea());
            }
        }
        g.setStroke(originalStroke);
    }

    public void clearSelectAction() {
        for (Cell cell : getCells()) {
            cell.setConnecting(false);
            cell.setResizing(false);
        }
        tempArrow.clear();
        selectArrows.clear();
        selectCells.clear();
        defaultModifyJTF.setVisible(false);
        defaultModifyJTF.getDocument().removeDocumentListener(popupMenu.getMdfDcListener());
        defaultModifyJTF.setText("");
        popupMenu.setVisible(false);
        if (arrowTextEditor != null && arrowTextEditor.getEditor() != null) {
            remove(arrowTextEditor.getEditor());
        }
        repaint();
    }

    public void markArrow(Arrow arrow) {
        selectArrows.clear();
        selectArrows.add(arrow);
        repaint();
    }

    public void addTempArrow(Point startP, Point endP) {
        removeTempArrow();
        tempArrow.add(startP);
        tempArrow.add(endP);
        repaint();
    }

    public void removeTempArrow() {
        tempArrow.clear();
        repaint();
    }

    public void showModifyCellJTF(Cell cell) {
        Rectangle displayLocation = cell.getBounds();
        getModifyJTF().setText(cell.getText());
        getModifyJTF().setLocation((int) displayLocation.getCenterX() - cell.getCellWidth() / 2, (int) displayLocation.getCenterY() - 10);
        getModifyJTF().setVisible(true);
        getModifyJTF().requestFocus();

        popupMenu.getMdfDcListener().setCell(cell);
        getModifyJTF().getDocument().addDocumentListener(popupMenu.getMdfDcListener());
    }

    public void showModifyArrowJTF(Arrow arrow) {
        Rectangle displayLocation = arrow.getArrowArea().getBounds();
        arrowTextEditor = arrow.getEditor(arrow.getText(), this);
        if (arrowTextEditor.getEditor() != null) {

            arrowTextEditor.getEditor().setLocation((int) displayLocation.getCenterX() - 50, (int) displayLocation.getCenterY() - 10);
            arrowTextEditor.getEditor().setVisible(true);
            arrowTextEditor.getEditor().requestFocus();
            add(arrowTextEditor.getEditor());
            repaint();
        } else {
            getModifyJTF().setText(arrow.getText());
            getModifyJTF().setLocation((int) displayLocation.getCenterX() - 50, (int) displayLocation.getCenterY() - 10);
            getModifyJTF().setVisible(true);
            getModifyJTF().requestFocus();
            popupMenu.getMdfDcListener().setArrow(arrow);
            getModifyJTF().getDocument().addDocumentListener(popupMenu.getMdfDcListener());
        }
    }

    public void addNode(Node node) {
        if (!cells.contains(node)) {
            cells.add(node);
            displayCells();
            repaint();
        }
    }

    /**
     * add a arrow to canvas with startNode and endNode
     * 
     * @param startNode
     * @param endNode
     */
    public void addArrow(Node startNode, Node endNode) {
        Arrow arrow = new Arrow(startNode, endNode);
        if (!arrows.contains(arrow)) {
            arrows.add(arrow);
            for (Cell cell : cells) {
                if (cell.getId().equals(startNode.getId())) {
                    cell.addArrow(arrow);
                }
            }
        }
        repaint();
    }

    /**
     * remove a arrow from the canvas
     * 
     * @param arrow
     */
    public void removeArrow(Arrow arrow) {
        if (arrows.contains(arrow)) {
            arrows.remove(arrow);
        }
        for (Cell cell : getCells()) {
            if (arrow.getStartNode().equals(cell)) {
                if (cell.getToArrowList().contains(arrow)) {
                    cell.getToArrowList().remove(arrow);
                    setChanged(true);
                    Logger.debug("移除节点跳转关系:" + arrow.getStartNode() + " -" + arrow.getText() + "-> " + arrow.getEndNode());
                }
            }
        }
        for (NodeResource node : displayProject.getDefineNodes()) {
            for (int i = node.getJumpNodes().size(); i > 0; i--) {
                NodeJumpResource jump = node.getJumpNodes().get(i - 1);
                if (node.getID() == Long.parseLong(arrow.getStartNode().getId()) && jump.getToId() == Long.parseLong(arrow.getEndNode().getId())) {
                    displayProject.getNodeResource(String.valueOf(node.getID())).getJumpNodes().remove(jump);
                }
            }
        }

        getSelectArrows().remove(arrow);

        repaint();
    }

    /**
     * remove a cell from the canvas
     * 
     * @param node
     */
    public void removeNode(Cell node) {
        if (cells.contains(node)) {
            cells.remove(node);
            displayProject.getDefineNodes().remove(displayProject.getNodeResource(node.getId()));

            for (Cell cell : cells) {
                for (int i = cell.getToArrowList().size(); i > 0; i--) {
                    Arrow arrow = cell.getToArrowList().get(i - 1);
                    if (arrow.getStartNode().getId().equals(node.getId()) || arrow.getEndNode().getId().equals(node.getId())) {
                        removeArrow(arrow);
                    }
                }
            }

            displayCells();
            setChanged(true);
            repaint();
        }
    }

    private void paintNetPoint(Graphics2D g2) {

        Color originalColor = g2.getColor();
        g2.setColor(NET_POINT_COLOR);
        for (int i = 0; i < this.getWidth(); i = i + NET_POINT_SPACE) {
            for (int j = 0; j < this.getHeight(); j = j + NET_POINT_SPACE) {
                g2.drawOval(i, j, 1, 1);
            }
        }
        g2.setColor(originalColor);
    }

    public List<Arrow> getArrows() {
        return arrows;
    }

    public void setArrows(List<Arrow> arrows) {
        this.arrows = arrows;
    }

    public List<Arrow> getSelectArrows() {
        return selectArrows;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
        displayCells();
    }

    /**
     * get all the cells displayed in the canvas(without arrows)
     * 
     * @return
     */
    public List<Cell> getCells() {
        return cells;
    }

    /**
     * get a cell from the canvas
     * 
     * @param cell
     * @return
     */
    public Cell getCell(Cell cell) {
        return cells.get(cells.indexOf(cell));
    }

    /**
     * get a cell from the canvas by id
     * 
     * @param id
     * @return
     */
    public Cell getCell(String id) {
        for (Cell cell : getCells()) {
            if (cell.getId().equals(id))
                return cell;
        }
        return null;
    }

    public JTextField getModifyJTF() {
        return defaultModifyJTF;
    }

    public void setDisplayView(View displayView) {
        this.displayView = displayView;
    }

    /**
     * update the assign cell's arrows
     * 
     * @param cell
     */
    public void updateArrow(Cell cell) {
        for (Arrow arrow : arrows) {
            if (arrow.getStartNode().getId().equals(cell.getId())) {
                arrow.resetArrowArea(cell, arrow.getEndNode());
            }
            if (arrow.getEndNode().getId().equals(cell.getId())) {
                arrow.resetArrowArea(arrow.getStartNode(), cell);
            }
        }
    }

    /**
     * get the canvas's center point
     * 
     * @return
     */
    public Point getCenterPoint() {
        return new Point(getWidth() / 2, getHeight() / 2);
    }

    /**
     * check is the assign id is already exist in the canvas
     * 
     * @param id
     * @return
     */
    public boolean isCellIdExist(String id) {
        for (Cell cell : getCells()) {
            if (cell.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

}
