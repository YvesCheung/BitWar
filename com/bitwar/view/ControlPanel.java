package com.bitwar.view;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeListener;

import com.bitwar.StrategyExample;
import com.bitwar.util.Log;
import com.bitwar.view.iview.AppFrame;
import com.bitwar.view.iview.CodeView;
import com.bitwar.view.iview.ControlView;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class ControlPanel extends JPanel implements ControlView {
	private AppFrame app;
	private JButton btn_api = new JButton("文档");
	private JButton btn_comp = new JButton("比赛示例");
	private JButton btn_ok = new JButton("运行");
	private final String[] example = new String[] { "", "永远合作", "随机", "针锋相对", "老实人探测器", "永不原谅", "两报还一报" };
	private JLabel example_name = new JLabel("策略例子:");
	private JComboBox<String> comboBox = new JComboBox<>(example);

	public ControlPanel(AppFrame mainFrame) {
		app = mainFrame;
		setLayout(new XYLayout());

		btn_ok.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				app.onClickButtonOk();
			}
		});

		btn_api.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					String root = System.getProperty("user.dir");
					File file = new File(root, "API文档.CHM");
					Log.i(file);
					Desktop.getDesktop().open(file);
				} catch (Exception e1) {
					app.showCode("文档不存在！");
				}
			}
		});

		btn_comp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				app.showCode(StrategyExample.getAlways1());
				app.appendCode("\n\n" + StrategyExample.getRandom());
				app.appendCode("\n\n" + StrategyExample.getAgainst());
				app.appendCode("\n\n" + StrategyExample.getHonerDetector());
				app.appendCode("\n\n" + StrategyExample.getNeverForgive());
				app.appendCode("\n\n" + StrategyExample.get2Revenge());
				app.appendCode("\n\n" + StrategyExample.getCold());
				app.appendCode("\n\n" + StrategyExample.getAlways0());
				app.appendCode("\n\n" + StrategyExample.getComp());
			}
		});

		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				String stategy = (String) comboBox.getSelectedItem();
				switch (stategy) {
				case "永远合作":
					app.showCode(StrategyExample.getAlways1());
					break;
				case "随机":
					app.showCode(StrategyExample.getRandom());
					break;
				case "针锋相对":
					app.showCode(StrategyExample.getAgainst());
					break;
				case "老实人探测器":
					app.showCode(StrategyExample.getAgainst());
					app.appendCode("\n\n" + StrategyExample.getHonerDetector());
					break;
				case "永不原谅":
					app.showCode(StrategyExample.getNeverForgive());
					break;
				case "两报还一报":
					app.showCode(StrategyExample.get2Revenge());
					break;
				default:
					app.showCode("");
					break;
				}
			}
		});

		add(btn_api, new XYConstraints(0, 10, 100, 30));
		add(btn_comp, new XYConstraints(120, 10, 100, 30));
		add(example_name, new XYConstraints(280, 10, 60, 30));
		add(comboBox, new XYConstraints(350, 10, 120, 30));
		add(btn_ok, new XYConstraints(480, 10, 80, 30));
	}
}
