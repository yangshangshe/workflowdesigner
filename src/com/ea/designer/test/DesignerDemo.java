package com.ea.designer.test;

import javax.swing.SwingUtilities;

import com.ea.designer.DefaultApplication;

public class DesignerDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DefaultApplication app = new DefaultApplication();
                app.init();
                app.start();
            }
        });

    }

}
