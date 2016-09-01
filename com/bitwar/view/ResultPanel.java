package com.bitwar.view;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.bitwar.view.iview.AppFrame;
import com.bitwar.view.iview.ResultView;

public class ResultPanel extends JPanel implements ResultView{
	private AppFrame app;
	private JTextArea ta_result;

	public ResultPanel(AppFrame mainFrame) {
		app = mainFrame;
		ta_result = new JTextArea();
		ta_result.setEditable(false);
		JScrollPane jsp = new JScrollPane(ta_result);
		jsp.setPreferredSize(new Dimension(280, 330));
		add(jsp);
	}

	@Override
	public void setResult(String result) {
		ta_result.setText(result);
	}

	@Override
	public void appendResult(String progress) {
		ta_result.append(progress);
	}
}
