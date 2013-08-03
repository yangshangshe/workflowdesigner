package com.ea.designer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.ea.designer.config.AppPreference;
import com.ea.designer.config.PreferenceManager;
import com.ea.designer.exception.XmlConfigError;
import com.ea.designer.log.Logger;
import com.ea.designer.render.ProjectTreeCellRenderer;
import com.ea.designer.resources.NodeAttributeResource;
import com.ea.designer.resources.NodeJumpResource;
import com.ea.designer.resources.NodeResource;
import com.ea.designer.resources.ProjectResource;
import com.ea.designer.resources.Resource;
import com.ea.designer.ui.Arrow;
import com.ea.designer.ui.Cell;
import com.ea.designer.ui.Node;
import com.ea.designer.util.ReflectionUtil;
import com.ea.designer.util.XmlWriter;
import com.ea.designer.util.XmlReader;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name ProjectPanel.java
 * @package_name：com.ea.designer
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class ProjectPanel extends JComponent {

    private JScrollPane projectScrollPane;
    private JTree projectTree;
    private List<File> projectFiles;
    private DefaultView view;

    private final MouseListener treeMouseListener = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            int selRow = projectTree.getRowForLocation(e.getX(), e.getY());
            TreePath selPath = projectTree.getPathForLocation(e.getX(), e.getY());
            if (selRow != -1) {
                if (e.getClickCount() == 2) {
                    Resource resource = (Resource) selPath.getLastPathComponent();
                    if (resource instanceof ProjectResource) {
                        if (!view.isProjectOpened((ProjectResource) resource)) {
                            showProjectWorkFlow(resource, null);
                        } else {
                            activeProjectWorkFlow(resource, null);
                        }
                        view.registerActionPropertyListeners();
                    } else if (resource instanceof NodeResource) {
                        Resource projectResource = (Resource) ((NodeResource) resource).getParent();
                        if (projectResource instanceof ProjectResource) {
                            if (!view.isProjectOpened((ProjectResource) projectResource)) {
                                showProjectWorkFlow(projectResource, resource);
                            } else {
                                activeProjectWorkFlow(projectResource, resource);
                            }
                        }
                    } else if (resource instanceof NodeJumpResource) {
                        Resource projectResource = (Resource) ((NodeJumpResource) resource).getParent().getParent();
                        if (!view.isProjectOpened((ProjectResource) projectResource)) {
                            showProjectWorkFlow(projectResource, null);
                        } else {
                            activeProjectWorkFlow(projectResource, null);
                        }
                        activeArrow(resource);
                    }
                }
            }
        }

        private void activeArrow(Resource resource) {
            if (resource != null && resource instanceof NodeJumpResource && view.getActiveTab() != null) {
                NodeResource startNode = (NodeResource) ((NodeJumpResource) resource).getParent();
                Cell start = view.getActiveTab().getCell(String.valueOf(startNode.getID()));
                Cell end = view.getActiveTab().getCell(String.valueOf(((NodeJumpResource) resource).getToId()));
                Arrow arrow = new Arrow(start, end);
                view.getActiveTab().clearSelectAction();
                view.getActiveTab().markArrow(arrow);
            }
        }

        private void activeProjectWorkFlow(Resource resource, Resource active) {
            view.setSelectedCanvas(resource);
            activeNodeCell(active);
        }

    };

    private void activeNodeCell(Resource active) {
        if (active != null) {
            view.getActiveTab().clearSelectAction();
            if (active instanceof NodeResource && view.getActiveTab() != null) {
                for (Cell cell : view.getActiveTab().getCells()) {
                    cell.setResizing(false);
                    if (cell.getId().equals(String.valueOf(active.getID()))) {
                        view.getActiveTab().getCell(cell).setResizing(true);
                    }
                }
            }
        }
    }

    public void showProjectWorkFlow(Resource resource, Resource active) {

        ProjectResource project = (ProjectResource) resource;

        Canvas workflowCanvas = new Canvas();
        workflowCanvas.setDisplayProject(project);
        workflowCanvas.setDisplayView(view);

        Map<String, Node> canJumpNodes = new HashMap<String, Node>();
        Node workflowNode;
        for (NodeResource node : project.getDefineNodes()) {
            workflowNode = new Node(node);
            canJumpNodes.put(String.valueOf(node.getID()), workflowNode);
            workflowCanvas.addNode(workflowNode);
        }

        for (NodeResource node : project.getDefineNodes()) {
            for (NodeJumpResource jump : node.getJumpNodes()) {
                Logger.debug("节点跳转关系:" + node.getID() + " -" + jump.getText() + "-> " + jump.getToId());
                Arrow arrow = new Arrow(canJumpNodes.get(String.valueOf(node.getID())), canJumpNodes.get(String.valueOf(jump.getToId())), jump.getText());
                if (jump.getEditor() != null) {
                    arrow.setEditorType(jump.getEditor().getType());
                    arrow.setEditorValue(jump.getEditor().getValue());
                    arrow.setEditorOptionId(jump.getEditor().getOptionId());
                    arrow.setEditorOptionName(jump.getEditor().getOptionName());
                }
                canJumpNodes.get(String.valueOf(node.getID())).addArrow(arrow);
            }
        }

        view.addTab(project.getText(), workflowCanvas);

        getModel().setActiveCanvas(workflowCanvas);

        activeNodeCell(active);

    }

    public ProjectPanel() {
        initResource();
        initComponent();
    }

    public ProjectPanel(DefaultView container) {
        this.view = container;

        initResource();
        initComponent();
    }

    public void initResource() {
        projectFiles = new ArrayList<File>();
        File projectDir = new File(getDataPath());
        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }
        for (File file : projectDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".xml"))
                    return true;
                return false;
            }

        })) {
            projectFiles.add(file);
        }
    }

    public void initComponent() {
        for (Component comp : this.getComponents()) {
            remove(comp);
        }

        projectTree = new JTree();
        projectTree.setCellRenderer(new ProjectTreeCellRenderer());
        projectTree.setShowsRootHandles(true);
        projectTree.setRootVisible(false);
        projectTree.addMouseListener(treeMouseListener);
        buildTree();
        this.setLayout(new BorderLayout());

        projectScrollPane = new JScrollPane(projectTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.add(projectScrollPane, BorderLayout.CENTER);
        this.validate();
    }

    public ProjectResource getActiveProject() {
        return getModel().getActiveProject();
    }

    public boolean saveActiveProject() {
        ProjectResource project = getActiveProject();
        if (project == null)
            return false;
        return saveProject(project);
    }

    public boolean saveProject(ProjectResource project) {
        XmlWriter xmlWrite = new XmlWriter();
        return xmlWrite.saveProject(project, getDataPath() + project.getFileName());

    }

    private void buildTree() {
        try {

            getModel().clearProjectResource();

            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            XmlReader xmlReader = new XmlReader();

            for (File file : projectFiles) {
                ProjectResource projectRes = xmlReader.readProject(file);
                projectRes.setFileName(file.getName());
                root.add(projectRes);
                if (getModel().isExistProject(projectRes)) {
                    throw new XmlConfigError("不允许存在重复的项目编号" + projectRes.getID());
                }
                getModel().addProjectResource(projectRes);
                for (NodeResource nodeRes : projectRes.getDefineNodes()) {
                    projectRes.add(nodeRes);
                    for (NodeJumpResource nodeJumpRes : nodeRes.getJumpNodes()) {
                        nodeRes.add(nodeJumpRes);
                    }

                    Field[] fields = XmlReader.class.getDeclaredFields();
                    for (Field field : fields) {
                        if (field.getName().startsWith("XML_ATTR_CELL_")) {
                            addNodeAttributeResource(nodeRes, field.getName().replaceAll("XML_ATTR_CELL_", ""));
                        }
                    }
                }
            }
            projectTree.setModel(new DefaultTreeModel(root));
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }

    }

    private void addNodeAttributeResource(NodeResource nodeRes, String fieldName) {
        Object value = ReflectionUtil.getFieldValue(nodeRes, fieldName);
        if (value == null) {
            value = ReflectionUtil.getFieldValue(nodeRes.getResource(), fieldName);
        }
        NodeAttributeResource attrRes = new NodeAttributeResource();
        attrRes.setText(String.valueOf(value));
        attrRes.setDescription(fieldName);
        nodeRes.add(attrRes);
    }

    protected String getDataPath() {
        AppPreference pref = PreferenceManager.getInstance().getPreference(ProjectPanel.class.getName());
        String dataPath = pref.get("database.path");
        dataPath += dataPath.endsWith(File.separator) ? "" : File.separator;
        return dataPath;
    }

    public Model getModel() {
        return view.getModel();
    }

}
