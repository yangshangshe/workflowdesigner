package com.ea.designer.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ea.designer.log.Logger;
import com.ea.designer.resources.EditorResource;
import com.ea.designer.resources.NodeJumpResource;
import com.ea.designer.resources.NodeResource;
import com.ea.designer.resources.ProjectResource;
/**
 * ****************************************************************************
 * @function：
 * @author yss
 * @file_name XMLWriter.java
 * @package_name：com.ea.designer.util
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人  修改时间  修改内容
 * 
 * ****************************************************************************
 */
public class XmlWriter implements WorkFlowWriter {

    public boolean saveProject(ProjectResource project, String fileName) {
        try {
            Document doc;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder;

            dbBuilder = dbFactory.newDocumentBuilder();

            doc = dbBuilder.newDocument();

            if (doc != null) {

                doc.createComment("该文件已在" + DateUtil.getCurrentDate("") + "被修改!");

                Element projectEle = doc.createElement(XmlReader.XML_ELE_project);
                projectEle.setAttribute(XmlReader.XML_ATTR_CELL_text, project.getText());
                projectEle.setAttribute(XmlReader.XML_ATTR_CELL_desc, project.getDescription());
                projectEle.setAttribute(XmlReader.XML_ATTR_CELL_id, String.valueOf(project.getID()));

                for (NodeResource node : project.getDefineNodes()) {
                    Element nodeEle = doc.createElement(XmlReader.XML_ELE_node);
                    nodeEle.setAttribute(XmlReader.XML_ATTR_CELL_id, String.valueOf(node.getID()));
                    nodeEle.setAttribute(XmlReader.XML_ATTR_CELL_text, node.getText());
                    nodeEle.setAttribute(XmlReader.XML_ATTR_CELL_desc, node.getDescription());
                    nodeEle.setAttribute(XmlReader.XML_ATTR_CELL_startX, String.valueOf(node.getStartX()));
                    nodeEle.setAttribute(XmlReader.XML_ATTR_CELL_startY, String.valueOf(node.getStartY()));
                    nodeEle.setAttribute(XmlReader.XML_ATTR_CELL_endX, String.valueOf(node.getEndX()));
                    nodeEle.setAttribute(XmlReader.XML_ATTR_CELL_endY, String.valueOf(node.getEndY()));
                    nodeEle.setAttribute(XmlReader.XML_ATTR_CELL_nodeImage, node.getNodeImage());
                    projectEle.appendChild(nodeEle);

                    for (NodeJumpResource jump : node.getJumpNodes()) {
                        Element jumpEle = doc.createElement(XmlReader.XML_ELE_jump);
                        jumpEle.setAttribute(XmlReader.XML_ATTR_JUMP_toId, String.valueOf(jump.getToId()));
                        jumpEle.setAttribute(XmlReader.XML_ATTR_CELL_text, jump.getText());
                        jumpEle.setAttribute(XmlReader.XML_ATTR_CELL_desc, jump.getDescription());
                        nodeEle.appendChild(jumpEle);

                        EditorResource editor = jump.getEditor();
                        if (editor != null) {
                            Element editorEle = doc.createElement(XmlReader.XML_ELE_editor);
                            editorEle.setAttribute(XmlReader.XML_ATTR_EDITOR_value, editor.getValue());
                            editorEle.setAttribute(XmlReader.XML_ATTR_EDITOR_type, editor.getType());
                            editorEle.setAttribute(XmlReader.XML_ATTR_EDITOR_optionId, editor.getOptionId());
                            editorEle.setAttribute(XmlReader.XML_ATTR_EDITOR_optionName, editor.getOptionName());
                            jumpEle.appendChild(editorEle);
                        }
                    }
                }

                doc.appendChild(projectEle);

                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer transformer = transFactory.newTransformer();
                transformer.setOutputProperty("indent", "yes");

                DOMSource source = new DOMSource();
                source.setNode(doc);
                StreamResult result = new StreamResult();
                result.setOutputStream(new FileOutputStream(fileName));

                transformer.transform(source, result);

            }
        } catch (ParserConfigurationException e) {
            Logger.error(e.getMessage(), e);
        } catch (TransformerConfigurationException e) {
            Logger.error(e.getMessage(), e);
        } catch (TransformerException e) {
            Logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            Logger.error(e.getMessage(), e);
        }

        Logger.debug("保存文件成功!");

        return true;
    }

}
