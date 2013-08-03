package com.ea.designer.ui;

import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Customized fields can easily be created by extending the model and changing
 * the default model provided. For example, the following piece of code will
 * create a field that holds only digit characters. It will work even if text is
 * pasted into from the clipboard or it is altered via programmatic changes.
 * 
 * 
 */
public class DigitOnlyField extends JTextField {

    /**
     * 
     */
    private static final long serialVersionUID = 8384787369612949227L;

    public DigitOnlyField() {
        super();
    }

    public DigitOnlyField(int cols) {
        super(cols);
    }

    protected Document createDefaultModel() {
        return new UpperCaseDocument();
    }

    static class UpperCaseDocument extends PlainDocument {

        /**
         * 
         */
        private static final long serialVersionUID = -4170536906715361215L;

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

            if (str == null) {
                return;
            }
            char[] upper = str.toCharArray();
            String filtered = "";
            for (int i = 0; i < upper.length; i++) {
                if (Character.isDigit(Character.codePointAt(upper, i))) {
                    filtered += upper[i];
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
            super.insertString(offs, filtered, a);
        }
    }
}
