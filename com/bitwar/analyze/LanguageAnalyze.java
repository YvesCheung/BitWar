package com.bitwar.analyze;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static com.bitwar.analyze.AnalyzeException.*;
import com.bitwar.Competition;
import com.bitwar.Strategy;
import com.bitwar.Token;
import com.bitwar.util.Log;

public class LanguageAnalyze {

	private Set<Strategy> strategys = new HashSet<>();
	private Competition comp;
	private List<Token> list;
	private int lineNum, columnNum;
	private String extra;

	public void analyze(List<Token> list) throws RuntimeException {
		this.list = list;
		this.strategys.clear();
		int index = 0;
		try {
			while (index < list.size()) {
				Token token = list.get(index);
				if (token.key.equals(Token.STRATEGY)) {
					index = getStrategy(index + 1);
				} else if (token.key.equals(Token.COMPETITION)) {
					index = getCompetition(index + 1);
				} else {
					throw new AnalyzeException(UNEXPECTED_TOKEN, token.lineNum, token.offset, token.value);
				}
			}
		} catch (NoSuchMethodException e) {
			throw new AnalyzeException(UNEXPECTED_TOKEN, lineNum, columnNum, e.toString());
		} catch (IllegalAccessException e) {
			throw new AnalyzeException(UNEXPECTED_TOKEN, lineNum, columnNum, e.toString());
		} catch (InvocationTargetException e) {
			throw new AnalyzeException(UNEXPECTED_TOKEN, lineNum, columnNum, "方法的参数或值不合法: " + extra);
		}
	}

	private int getStrategy(int index)
			throws RuntimeException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Token token;
		Strategy strategy = new Strategy();
		Class clazz = strategy.getClass();
		while (index < list.size()) {
			token = list.get(index++);

			if (token.key.equals(Token.POINT)) {
				/**
				 * 逐个获取对应的Strategy的操作符
				 */
				token = list.get(index++);
				String methodName = token.key;
				if (index < list.size())
					token = list.get(index);

				lineNum = token.lineNum;
				columnNum = token.offset;
				extra = token.value;

				if (token.key.equals(Token.EXP)) {
					String exp = token.value;
					index++;

					if (index < list.size())
						token = list.get(index);
					if (token.key.equals(Token.ARROW)) {
						index++;

						if (index >= list.size())
							throw new AnalyzeException(UNEXPECTED_END, token.lineNum, token.offset, token.value);
						token = list.get(index++);

						if (token.key.equals(Token.NUMBER)) {
							Integer number = Integer.valueOf(token.value);
							Method method = strategy.getClass().getDeclaredMethod(methodName, String.class,
									Integer.class);
							method.invoke(strategy, exp, number);
						} else if (token.key.equals(Token.FLOAT)) {
							Double number = Double.valueOf(token.value);
							Method method = strategy.getClass().getDeclaredMethod(methodName, String.class,
									Double.class);
							method.invoke(strategy, exp, number);
						} else if (token.key.equals(Token.IDENTIFIER)) {
							String id = token.value;
							Method method = strategy.getClass().getDeclaredMethod(methodName, String.class,
									String.class);
							method.invoke(strategy, exp, id);
						} else {
							index--;
						}
					} else {
						Method method = strategy.getClass().getDeclaredMethod(methodName, String.class);
						method.invoke(strategy, exp);
					}
				} else if (token.key.equals(Token.ARROW)) {
					index++;
					if (index >= list.size())
						throw new AnalyzeException(UNEXPECTED_END, token.lineNum, token.offset, token.value);
					token = list.get(index++);

					if (token.key.equals(Token.NUMBER)) {
						Integer number = Integer.valueOf(token.value);
						Method method = strategy.getClass().getDeclaredMethod(methodName, String.class, Integer.class);
						method.invoke(strategy, null, number);
					} else if (token.key.equals(Token.FLOAT)) {
						Double number = Double.valueOf(token.value);
						Method method = strategy.getClass().getDeclaredMethod(methodName, String.class, Double.class);
						method.invoke(strategy, null, number);
					} else if (token.key.equals(Token.IDENTIFIER)) {
						String id = token.value;
						Method method = strategy.getClass().getDeclaredMethod(methodName, String.class, String.class);
						method.invoke(strategy, null, id);
					} else {
						index--;
					}
				} else {
					Method method = strategy.getClass().getDeclaredMethod(methodName);
					method.invoke(strategy);
				}
			} else {
				index--;
				break;
			}
		} // for each operation

		boolean notSame = strategys.add(strategy);
		if (!notSame) {
			throw new AnalyzeException(EXIST_STRATEGY, 0, 0, strategy.name);
		}
		return index;
	}

	private int getCompetition(int index)
			throws RuntimeException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Token token;
		comp = new Competition();
		while (index < list.size()) {
			token = list.get(index++);

			if (token.key.equals(Token.POINT)) {
				/**
				 * 逐个获取对应的Strategy的操作符
				 */
				token = list.get(index++);
				String methodName = token.key;
				if (index < list.size())
					token = list.get(index);

				lineNum = token.lineNum;
				columnNum = token.offset;
				extra = token.value;

				if (token.key.equals(Token.EXP)) {
					String exp = token.value;
					index++;

					if (index < list.size())
						token = list.get(index);
					if (token.key.equals(Token.ARROW)) {
						index++;

						if (index >= list.size())
							throw new AnalyzeException(UNEXPECTED_END, token.lineNum, token.offset, token.value);
						token = list.get(index++);

						if (token.key.equals(Token.NUMBER)) {
							Integer number = Integer.valueOf(token.value);
							Method method = comp.getClass().getDeclaredMethod(methodName, String.class, Integer.class);
							method.invoke(comp, exp, number);
						} else if (token.key.equals(Token.FLOAT)) {
							Double number = Double.valueOf(token.value);
							Method method = comp.getClass().getDeclaredMethod(methodName, String.class, Double.class);
							method.invoke(comp, exp, number);
						} else if (token.key.equals(Token.IDENTIFIER)) {
							String id = token.value;
							Method method = comp.getClass().getDeclaredMethod(methodName, String.class, String.class);
							method.invoke(comp, exp, id);
						} else {
							index--;
						}
					} else {
						Method method = comp.getClass().getDeclaredMethod(methodName, String.class);
						method.invoke(comp, exp);
					}
				} else if (token.key.equals(Token.ARROW)) {
					index++;
					if (index >= list.size())
						throw new AnalyzeException(UNEXPECTED_END, token.lineNum, token.offset, token.value);
					token = list.get(index++);

					if (token.key.equals(Token.NUMBER)) {
						Integer number = Integer.valueOf(token.value);
						Method method = comp.getClass().getDeclaredMethod(methodName, String.class, Integer.class);
						method.invoke(comp, null, number);
					} else if (token.key.equals(Token.FLOAT)) {
						Double number = Double.valueOf(token.value);
						Method method = comp.getClass().getDeclaredMethod(methodName, String.class, Double.class);
						method.invoke(comp, null, number);
					} else if (token.key.equals(Token.IDENTIFIER)) {
						String id = token.value;
						Method method = comp.getClass().getDeclaredMethod(methodName, String.class, String.class);
						method.invoke(comp, null, id);
					} else {
						index--;
					}
				} else {
					Method method = comp.getClass().getDeclaredMethod(methodName);
					method.invoke(comp);
				}
			} else {
				index--;
				break;
			}
		} // for each operation
		return index;
	}

	public Set<Strategy> getStrategys() {
		return strategys;
	}

	public Competition getCompetition() {
		return comp;
	}
}
