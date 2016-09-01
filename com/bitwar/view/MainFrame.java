package com.bitwar.view;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.bitwar.analyze.Analyzer;
import com.bitwar.analyze.Analyzer.OnAnalyzeListener;
import com.bitwar.view.iview.AppFrame;
import com.bitwar.view.iview.CodeView;
import com.bitwar.view.iview.ControlView;
import com.bitwar.view.iview.ResultView;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class MainFrame extends JFrame implements AppFrame {

	private CodeView codePanel = new CodePanel(this);
	private ControlView controlPanel = new ControlPanel(this);
	private ResultView resultPanel = new ResultPanel(this);

	public MainFrame() {
		super("比特大战");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(900, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		panel.setLayout(new XYLayout());
		panel.add((Component) codePanel, new XYConstraints(10, 10, 560, 280));
		panel.add((Component) controlPanel, new XYConstraints(10, 300, 560, 50));
		panel.add((Component) resultPanel, new XYConstraints(600, 10, 280, 380));
		setContentPane(panel);
		setVisible(true);
	}

	@Override
	public void onClickButtonOk() {
		final String code = codePanel.getCode();
		resultPanel.setResult("正在分析，请稍后...");
		Analyzer.analyze(code, new OnAnalyzeListener() {
			@Override
			public void onFinish(String result) {
				resultPanel.setResult(result);
			}
			
			@Override
			public void onError(String error) {
				resultPanel.setResult(error);
			}

			@Override
			public void progress(String str) {
				resultPanel.appendResult(str);
			}
		});	
	}

	@Override
	public void showCode(String str) {
		codePanel.showCode(str);
	}

	@Override
	public void appendCode(String str) {
		codePanel.appendCode(str);	
	}
}
