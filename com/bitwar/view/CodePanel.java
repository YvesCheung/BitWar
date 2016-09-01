package com.bitwar.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.bitwar.view.iview.AppFrame;
import com.bitwar.view.iview.CodeView;

/**
 * 用于代码输入的组件
 * 
 * @author 张宇
 *
 */
public class CodePanel extends JPanel implements CodeView {
	private AppFrame app;
	private JTextArea ta_code;
	private int offset;
	private char lastKeyChar;

	public CodePanel(AppFrame mainFrame) {
		app = mainFrame;
		ta_code = new JTextArea();
		ta_code.setFont(new Font("宋体", Font.PLAIN, 24));
		ta_code.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				offset = e.getOffset();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				offset = e.getOffset();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		ta_code.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int pos = ta_code.getCaretPosition();
				if (e.getKeyChar() == '\n' && lastKeyChar != '\n') {
					ta_code.insert("\t", pos);
				} else if (e.getKeyChar() == '\"') {
					ta_code.insert("\"", pos);
					ta_code.setCaretPosition(pos);
				} else if (e.getKeyChar() == '(') {
					ta_code.insert(")", pos);
					ta_code.setCaretPosition(pos);
				}
				lastKeyChar = e.getKeyChar();
			}
		});
		ta_code.setWrapStyleWord(true);
		JScrollPane jsp = new JScrollPane(ta_code, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(560, 270));
		add(jsp);
	}

	private Character nextChar() {
		int index = ta_code.getCaretPosition();
		if (index >= 0 && index < ta_code.getText().length()) {
			return ta_code.getText().charAt(index);
		}
		return null;
	}

	private Character preChar() {
		int index = ta_code.getCaretPosition();
		if (index > 0 && index <= ta_code.getText().length()) {
			return ta_code.getText().charAt(index - 1);
		}
		return null;
	}

	@Override
	public String getCode() {
		return ta_code.getText();
	}

	@Override
	public void showCode(String str) {
		ta_code.setText(str);
	}

	@Override
	public void appendCode(String str) {
		ta_code.append(str);
	}
}
