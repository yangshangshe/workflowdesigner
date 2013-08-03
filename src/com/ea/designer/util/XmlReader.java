package com.ea.designer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ea.designer.log.Logger;
import com.ea.designer.resources.EditorResource;
import com.ea.designer.resources.NodeJumpResource;
import com.ea.designer.resources.NodeResource;
import com.ea.designer.resources.ProjectResource;
/**
 * ****************************************************************************
 * @function：
 * @author yss
 * @file_name XmlReader.java
 * @package_name：com.ea.designer.util
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人  修改时间  修改内容
 * 
 * ****************************************************************************
 */
public class XmlReader implements WorkFlowReader {

    public static final String XML_ATTR_CELL_id = "id";
    public static final String XML_ATTR_CELL_text = "text";
    public static final String XML_ATTR_CELL_desc = "desc";

    public static final String XML_ATTR_CELL_startX = "startX";
    public static final String XML_ATTR_CELL_startY = "startY";
    public static final String XML_ATTR_CELL_endX = "endX";
    public static final String XML_ATTR_CELL_endY = "endY";
    public static final String XML_ATTR_CELL_nodeImage = "nodeImage";

    public static final String XML_ATTR_JUMP_toId = "toId";

    public static final String XML_ATTR_EDITOR_type = "type";
    public static final String XML_ATTR_EDITOR_value = "value";
    public static final String XML_ATTR_EDITOR_optionId = "optionId";
    public static final String XML_ATTR_EDITOR_optionName = "optionName";

    public static final String XML_ELE_project = "project";
    public static final String XML_ELE_node = "node";
    public static final String XML_ELE_jump = "jump";
    public static final String XML_ELE_editor = "editor";

    public ProjectResource readProject(String file) throws IOException {
        return readProject(new FileInputStream(new File(file)));
    }

    public ProjectResource readProject(File file) throws IOException {
        return readProject(new FileInputStream(file));
    }

    public ProjectResource readProject(InputStream in) throws IOException {
        try {
            ProjectResource project = new ProjectResource();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;

            builder = factory.newDocumentBuilder();

            Document document;
            document = builder.parse(in);

            Element projectEle = document.getDocumentElement();
            project.setID(Integer.parseInt(projectEle.getAttributeNode(XML_ATTR_CELL_id).getValue()));
            project.setText(projectEle.getAttributeNode(XML_ATTR_CELL_text).getValue());
            project.setDescription(projectEle.getAttributeNode(XML_ATTR_CELL_desc).getValue());

            NodeList nodeEles = document.getElementsByTagName(XML_ELE_node);
            for (int i = 0, n = nodeEles.getLength(); i < n; i++) {
                Element nodeEle = (Element) nodeEles.item(i);

                NodeResource node = new NodeResource();
                node.setID(Long.parseLong(nodeEle.getAttributeNode(XML_ATTR_CELL_id).getValue()));
                node.setStartX(Integer.parseInt(nodeEle.getAttributeNode(XML_ATTR_CELL_startX).getValue()));
                node.setStartY(Integer.parseInt(nodeEle.getAttributeNode(XML_ATTR_CELL_startY).getValue()));
                node.setEndX(Integer.parseInt(nodeEle.getAttributeNode(XML_ATTR_CELL_endX).getValue()));
                node.setEndY(Integer.parseInt(nodeEle.getAttributeNode(XML_ATTR_CELL_endY).getValue()));
                node.setText(nodeEle.getAttributeNode(XML_ATTR_CELL_text).getValue());
                if (nodeEle.getAttributeNode(XML_ATTR_CELL_nodeImage) != null)
                    node.setNodeImage(nodeEle.getAttributeNode(XML_ATTR_CELL_nodeImage).getValue());

                NodeList jumpEles = nodeEle.getChildNodes();
                for (int j = 0; j < jumpEles.getLength(); j++) {
                    if (jumpEles.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        if (XML_ELE_jump.equals(jumpEles.item(j).getNodeName())) {
                            NodeJumpResource jump = new NodeJumpResource();
                            jump.setToId(Long.parseLong(jumpEles.item(j).getAttributes().getNamedItem(XML_ATTR_JUMP_toId).getNodeValue()));
                            jump.setText(jumpEles.item(j).getAttributes().getNamedItem(XML_ATTR_CELL_text).getNodeValue());
                            node.addJumpNode(jump);

                            NodeList editorEles = jumpEles.item(j).getChildNodes();
                            for (int k = 0; k < editorEles.getLength(); k++) {
                                if (editorEles.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                    if (XML_ELE_editor.equals(editorEles.item(k).getNodeName())) {
                                        EditorResource editor = new EditorResource();
                                        if (editorEles.item(k).getAttributes().getNamedItem(XML_ATTR_EDITOR_type) != null)
                                            editor.setType(editorEles.item(k).getAttributes().getNamedItem(XML_ATTR_EDITOR_type).getNodeValue());
                                        if (editorEles.item(k).getAttributes().getNamedItem(XML_ATTR_EDITOR_value) != null)
                                            editor.setValue(editorEles.item(k).getAttributes().getNamedItem(XML_ATTR_EDITOR_value).getNodeValue());
                                        if (editorEles.item(k).getAttributes().getNamedItem(XML_ATTR_EDITOR_optionId) != null)
                                            editor.setOptionId(editorEles.item(k).getAttributes().getNamedItem(XML_ATTR_EDITOR_optionId).getNodeValue());
                                        if (editorEles.item(k).getAttributes().getNamedItem(XML_ATTR_EDITOR_optionName) != null)
                                            editor.setOptionName(editorEles.item(k).getAttributes().getNamedItem(XML_ATTR_EDITOR_optionName).getNodeValue());
                                        jump.setEditor(editor);
                                    }
                                }
                            }

                        }
                    }
                }

                project.addNodeResource(node);
            }
            return project;
        } catch (ParserConfigurationException e) {
            Logger.error(e.getMessage(), e);
        } catch (SAXException e) {
            Logger.error(e.getMessage(), e);
        }
        return null;

    }
}
