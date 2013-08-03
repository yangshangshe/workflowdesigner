package com.ea.designer.test;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;


import com.ea.designer.Canvas;
import com.ea.designer.ui.Arrow;
import com.ea.designer.ui.Cell;
import com.ea.designer.ui.Node;

public class CanvasDemo {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Demo");
        Canvas canvas = new Canvas();

        List<Cell> nodeList = new ArrayList<Cell>();
        Node n1 = new Node("101", 200, 300, 245, 360, "n1");
        Node n2 = new Node("102", 400, 400, 445, 460, "n2");
        Node n3 = new Node("103", 400, 100, 445, 160, "n3");
        Node n4 = new Node("104", 600, 100, 645, 160, "n4");
      
        canvas.addNode(n1);
        canvas.addNode(n2);
        canvas.addNode(n3);
        canvas.addNode(n4);
        
        n1.addArrow(new Arrow(n1, n2));
        n1.addArrow(new Arrow(n1, n3));
        n3.addArrow(new Arrow(n3, n4));
        n1.addArrow(new Arrow(n1, n4));
        nodeList.add(n1);
        nodeList.add(n2);
        nodeList.add(n3);
        nodeList.add(n4);

       

        frame.add(canvas);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(3);
    }

}
