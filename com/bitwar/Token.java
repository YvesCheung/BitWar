package com.bitwar;

import java.lang.reflect.Field;

public class Token {
	public String key;
	public Integer lineNum;
	public Integer offset;
	public String value;

	public static final String STRATEGY = "Strategy";
	public static final String COMPETITION = "Competition";

	public static final String NAME = "name";
	public static final String BRANCH = "branch";
	public static final String RESULT = "result";
	public static final String EXTEND = "extend";
	public static final String SET = "map";
	public static final String CUR = "cur";
	public static final String RANDOM = "random";
	public static final String ENERMY = "enermy";
	public static final String SELF = "self";

	public static final String COMBAT = "combat";
	public static final String ROUND = "round";
	public static final String LOG = "log";

	public static final String EXP = "exp";
	public static final String NUMBER = "number";
	public static final String FLOAT = "float";
	public static final String IDENTIFIER = "id";
	public static final String POINT = ".";
	public static final String ARROW = "->";

	public Token() {
	}

	public Token(String key, String value, int lineNum, int offset) {
		this.key = key;
		this.value = value;
		this.lineNum = lineNum;
		this.offset = offset;
	}

	/**
	 * 根据word获取对应的token，如果不存在则返回null
	 */
	public static Token getToken(String word, Integer lineNum, Integer offset) {
		Token token = new Token();
		Class clazz = token.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			try {
				Object obj = field.get(token);
				if (obj != null && obj.equals(word)) {
					token.key = (String) obj;
					token.value = (String) obj;
					token.lineNum = lineNum;
					token.offset = offset;
					return token;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return key + " : " + value + " in (" + lineNum + "," + offset + ")";
	}
}
