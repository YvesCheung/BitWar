package com.bitwar.analyze;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bitwar.Competition;
import com.bitwar.Strategy;
import com.bitwar.Token;
import com.bitwar.compiler.BitWarCompiler;
import com.bitwar.util.Log;

import sun.tools.java.Environment;

public class Analyzer {

	private static boolean running = false;

	/**
	 * 对代码进行分析
	 * 
	 * @param code
	 *            源代码
	 * @param onAnalyzeListener
	 *            结果回调
	 */
	public static void analyze(String code, OnAnalyzeListener onAnalyzeListener) {
		if (running)
			return;

		synchronized (Analyzer.class) {
			if (!running) {
				running = true;
				new Thread(new Runnable() {
					@Override
					public void run() {
						WordAnalyze wordAnalyze = new WordAnalyze();
						LanguageAnalyze languageAnalyze = new LanguageAnalyze();
						StrategyAnalyze strategyAnalyze = new StrategyAnalyze();

						try {
							List<Token> list = wordAnalyze.analyze(code);
							StringBuffer sb = new StringBuffer();
							sb.append("分析过程：\n");
							for (Token token : list) {
								sb.append(token.toString() + "\n");
							}
							sb.append("\n\n");
							languageAnalyze.analyze(list);
							strategyAnalyze.analyze(languageAnalyze.getStrategys());
							sb.append("分析结果：\n");
							for (Strategy s : languageAnalyze.getStrategys()) {
								sb.append(s + "\n\n");
							}

							onAnalyzeListener.onFinish(sb.toString());
							onAnalyzeListener.progress("正在比赛...\n");

							Competition comp = languageAnalyze.getCompetition();
							if (comp != null) {
								Strategy s1 = comp.getStrategy1(languageAnalyze.getStrategys());
								if (comp.s1 == null) {// 所有策略两两比赛
									Map<String, Long> map = new HashMap<>();
									for (Strategy a : languageAnalyze.getStrategys()) {
										for (Strategy b : languageAnalyze.getStrategys()) {
											if (!a.name.equals(b.name)) {
												String result = combat(a, b, comp.round, comp.log, map);
												sb.append(result);
											}
										}
									}
									sb.append("分数统计：\n");
									for (Entry<String, Long> entry : map.entrySet()) {
										sb.append(entry.getKey() + " : " + entry.getValue() + "分\n");
									}
								} else if (s1 != null) {
									if (comp.s2 == null) {// s1和所有策略比赛
										for (Strategy s2 : languageAnalyze.getStrategys()) {
											if (!s2.name.equals(s1.name)) {
												String result = combat(s1, s2, comp.round, comp.log);
												sb.append(result);
											}
										}
									} else {// s1和s2比赛
										Strategy s2 = comp.getStrategy2(languageAnalyze.getStrategys());
										if (s2 == null) {
											sb.append("\n不存在对应的比赛的策略:" + comp.s2);
										} else {
											String result = combat(s1, s2, comp.round, comp.log);
											sb.append(result);
										}
									}
								} else {
									sb.append("\n不存在对应的比赛的策略： " + comp.s1);
								}
							} // if(comp!=null)
							onAnalyzeListener.onFinish(sb.toString());
						} catch (AnalyzeException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | NoSuchMethodException | SecurityException
								| InstantiationException | ClassNotFoundException e) {
							e.printStackTrace();
							onAnalyzeListener.onError(e.toString());
						} finally {
							running = false;
						}
					}
				}).start();
			} else {
				onAnalyzeListener.onError("正在运行中");
			}
		}
	}

	private static String combat(Strategy s1, Strategy s2, int round, boolean log)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException, AnalyzeException, ClassNotFoundException {
		return combat(s1, s2, round, log, null);
	}

	private static String combat(Strategy s1, Strategy s2, int round, boolean log, Map<String, Long> record)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException, AnalyzeException, ClassNotFoundException {
		StringBuffer sb = new StringBuffer();
		BitWarCompiler c = new BitWarCompiler(s1, s2, round);
		Class<?> A = c.compile();
		Object a = A.newInstance();
		A.getDeclaredMethod(c.getMethodNameOfStartGame()).invoke(a);
		List<Integer> score1 = (List<Integer>) A.getDeclaredMethod(c.getMethodNameOfScore1()).invoke(a);
		List<Integer> score2 = (List<Integer>) A.getDeclaredMethod(c.getMethodNameOfScore2()).invoke(a);
		sb.append("比赛结果( " + s1.name + " - " + s2.name + " )：\n");
		long sum1 = 0, sum2 = 0;
		for (int i = 0; i < score1.size(); i++) {
			sum1 += score1.get(i);
			sum2 += score2.get(i);
			if (log)
				sb.append(score1.get(i) + " - " + score2.get(i) + "\n");
		}
		if (record != null) {
			Long sum = record.get(s1.name);
			if (sum == null)
				sum = 0l;
			record.put(s1.name, sum1 + sum);

			sum = record.get(s2.name);
			if (sum == null)
				sum = 0l;
			record.put(s2.name, sum2 + sum);
		}
		sb.append(s1.name + "总分：" + sum1 + "\n");
		sb.append(s2.name + "总分：" + sum2 + "\n\n");
		return sb.toString();
	}

	public interface OnAnalyzeListener {
		void onError(String error);

		void progress(String str);

		void onFinish(String result);
	}
}
