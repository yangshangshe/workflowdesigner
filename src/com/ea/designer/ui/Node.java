package com.ea.designer.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import com.ea.designer.bean.HotSpotArea;
import com.ea.designer.config.AppManager;
import com.ea.designer.resources.NodeResource;
import com.ea.designer.util.IOHelper;
import com.ea.designer.util.StringUtil;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name Node.java
 * @package_name：com.ea.designer.ui
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class Node extends Cell {

    /**
     * node name
     */
    private String text = AppManager.getResources().getString("canvas.default.node");
    /**
     * node image url
     */
    private String nodeImage = AppManager.getResources().getImageIconPath("canvas.default.node");
    /**
     * node image
     */
    private ImageIcon nodeIcon;

    public Node(NodeResource node) {

        this(node.getStartX(), node.getStartY(), node.getEndX(), node.getEndY());
        setId(String.valueOf(node.getID()));
        if (!StringUtil.isNull(node.getNodeImage())) {
            setNodeImage(node.getNodeImage());
            setNodeIcon(IOHelper.readIcons(AppManager.getResources().getImageDir() + node.getNodeImage()));
        }
        this.text = node.getText();

    }

    /**
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public Node(int startX, int startY, int endX, int endY) {
        super(startX, startY, endX, endY);
        loadImage();
    }

    /**
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param nodeName
     */
    public Node(int startX, int startY, int endX, int endY, String nodeName) {
        this(startX, startY, endX, endY);
        this.text = nodeName;
    }

    /**
     * 
     * @param id
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param nodeName
     */
    public Node(String id, int startX, int startY, int endX, int endY, String nodeName) {
        this(startX, startY, endX, endY, nodeName);
        this.id = id;
    }

    /**
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param nodeName
     * @param nodeImage
     */
    public Node(int startX, int startY, int endX, int endY, String nodeName, String nodeImage) {
        this(startX, startY, endX, endY, nodeName);
        this.nodeImage = nodeImage;
    }

    /**
     * 
     * @param id
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param nodeName
     * @param nodeImage
     */
    public Node(String id, int startX, int startY, int endX, int endY, String nodeName, String nodeImage) {
        this(startX, startY, endX, endY, nodeName, nodeImage);
        this.id = id;
    }

    private void loadImage() {
        nodeIcon = IOHelper.readIcons(AppManager.getResources().getImageDir() + nodeImage);
    }

    public void resetCellSize(Rectangle rect) {
        super.resetCellSize(rect);
        this.setBounds(rect);
        repaint();
    }

    protected void paintComponent(Graphics g) {

        int width = this.getWidth();
        int height = this.getHeight();
        int iconWidth = nodeIcon.getIconWidth();
        int iconHeight = nodeIcon.getIconHeight();
        int iconStringSpace = 10;

        g.clearRect(0, 0, width, height);

        FontMetrics fm = g.getFontMetrics();
        int stringHeight = fm.getHeight();
        int stringWidth = fm.stringWidth(text);

        int iconX = (width - iconWidth) / 2;
        int iconY = (height - iconHeight - stringHeight - iconStringSpace) / 2;
        g.drawImage(nodeIcon.getImage(), iconX, iconY, this);

        int stringX = (width - stringWidth) / 2;
        int stringY = iconY + iconHeight + iconStringSpace;
        g.drawString(text, stringX, stringY);

        if (isResizing()) {
            createHotSpotShapes();

            Graphics2D g2 = (Graphics2D) g;

            Color originalColor = g2.getColor();
            g2.setColor(hotColor);
            for (HotSpotArea hotspot : resizeHotSpotAreas) {
                g2.fillRect(hotspot.getArea().x, hotspot.getArea().y, hotspot.getArea().width, hotspot.getArea().height);
            }

            g2.setColor(originalColor);
        }

        if (isConnecting()) {
            Graphics2D g2 = (Graphics2D) g;
            Color originalColor = g2.getColor();
            g2.setColor(Color.red);
            g2.drawRect(connectHotSpotArea.getArea().x, connectHotSpotArea.getArea().y, connectHotSpotArea.getArea().width, connectHotSpotArea.getArea().height);
            g2.setColor(originalColor);
        }

    }

    private void createHotSpotShapes() {

        resizeHotSpotAreas.clear();

        int width = this.getWidth();
        int height = this.getHeight();

        int middleX = (width - 3 * HOT_SPOT_WIDTH) / 2;
        int middleY = (height - 3 * HOT_SPOT_WIDTH) / 2;

        Rectangle nwArea = new Rectangle();
        nwArea.setBounds(0, 0, HOT_SPOT_WIDTH, HOT_SPOT_WIDTH);
        resizeHotSpotAreas.add(new HotSpotArea(Cursor.NW_RESIZE_CURSOR, nwArea));

        Rectangle nArea = new Rectangle();
        nArea.setBounds(middleX, 0, HOT_SPOT_WIDTH, HOT_SPOT_WIDTH);
        resizeHotSpotAreas.add(new HotSpotArea(Cursor.N_RESIZE_CURSOR, nArea));

        Rectangle neArea = new Rectangle();
        neArea.setBounds(width - HOT_SPOT_WIDTH, 0, HOT_SPOT_WIDTH, HOT_SPOT_WIDTH);
        resizeHotSpotAreas.add(new HotSpotArea(Cursor.NE_RESIZE_CURSOR, neArea));

        Rectangle wArea = new Rectangle();
        wArea.setBounds(0, middleY, HOT_SPOT_WIDTH, HOT_SPOT_WIDTH);
        resizeHotSpotAreas.add(new HotSpotArea(Cursor.W_RESIZE_CURSOR, wArea));

        Rectangle eArea = new Rectangle();
        eArea.setBounds(width - HOT_SPOT_WIDTH, middleY, HOT_SPOT_WIDTH, HOT_SPOT_WIDTH);
        resizeHotSpotAreas.add(new HotSpotArea(Cursor.E_RESIZE_CURSOR, eArea));

        Rectangle swArea = new Rectangle();
        swArea.setBounds(0, height - HOT_SPOT_WIDTH, HOT_SPOT_WIDTH, HOT_SPOT_WIDTH);
        resizeHotSpotAreas.add(new HotSpotArea(Cursor.SW_RESIZE_CURSOR, swArea));

        Rectangle sArea = new Rectangle();
        sArea.setBounds(middleX, height - HOT_SPOT_WIDTH, HOT_SPOT_WIDTH, HOT_SPOT_WIDTH);
        resizeHotSpotAreas.add(new HotSpotArea(Cursor.S_RESIZE_CURSOR, sArea));

        Rectangle seArea = new Rectangle();
        seArea.setBounds(width - HOT_SPOT_WIDTH, height - HOT_SPOT_WIDTH, HOT_SPOT_WIDTH, HOT_SPOT_WIDTH);
        resizeHotSpotAreas.add(new HotSpotArea(Cursor.SE_RESIZE_CURSOR, seArea));

    }

    public String getNodeImage() {
        return nodeImage;
    }

    public void setNodeImage(String nodeImage) {
        this.nodeImage = nodeImage;
        loadImage();
        repaint();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ImageIcon getNodeIcon() {
        return nodeIcon;
    }

    public void setNodeIcon(ImageIcon nodeIcon) {
        this.nodeIcon = nodeIcon;
    }

    public String toString() {
        return getText();
    }

}
