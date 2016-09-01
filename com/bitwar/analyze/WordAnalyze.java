package com.bitwar.analyze;

import java.util.ArrayList;
import java.util.List;
import static com.bitwar.analyze.AnalyzeException.*;
import com.bitwar.Token;
import com.bitwar.util.Log;

/**
 * 词法分析器
 * 
 * @author 张宇
 */
public class WordAnalyze {
	//
	// 文法规则为
	// code <= clazz { op [ argv ] [ to ] }
	// clazz <= 类名
	// op <= .操作符
	// argv <= ( exp )
	// to <= -> value
	// exp <= 逻辑表达式|value
	// value <= 值|变量名
	//
	private static final int clazz = 0, op = 1, op_name = 9, argv = 2, exp = 3, to = 4, value = 5, id = 6, number = 7,
			floa = 8;

	/**
	 * 词法分析
	 * 
	 * @param code
	 *            源代码
	 * @return Token的列表
	 */
	public List<Token> analyze(String code) throws AnalyzeException {
		List<Token> list = new ArrayList<>();

		int index = 0;
		int lineNum = 1;
		int offset = 0;
		int status = clazz;
		String tmp = "";
		Token tok;

		while (index < code.length()) {

			char c = code.charAt(index);

			if (c == '\n') {
				lineNum++;
				offset = 0;
			}

			switch (status) {
			case clazz:
				/**
				 * 如果是单词 尝试组成clazzName token
				 */
				if (Character.isAlphabetic(c)) {// Every letter is
												// alphabetic,but not every
												// alphabetic is a letter
					tmp += c;
					index++;
					offset++;
				}
				/**
				 * 如果是. 或者空格，则完成clazz的Token并且状态转换到op
				 */
				else if (c == '.') {
					tok = Token.getToken(tmp, lineNum, offset);
					if (tok == null) {
						throw new AnalyzeException(NO_SUCH_CLAZZ, lineNum, offset, tmp);
					} else {
						list.add(tok);
						tmp = "";
					}
					status = op;
				}
				/**
				 * 如果是tmp=""的话，无视空格
				 */
				else if (Character.isWhitespace(c)) {
					if (tmp.equals("")) {
						offset++;
						index++;
					} else {
						tok = Token.getToken(tmp, lineNum, offset);
						if (tok == null) {
							throw new AnalyzeException(NO_SUCH_CLAZZ, lineNum, offset, tmp);
						} else {
							list.add(tok);
							tmp = "";
						}
						status = op;
					}
				}
				/**
				 * 其他 报错
				 */
				else {
					throw new AnalyzeException(UNKNOWN_TOKEN, lineNum, offset, c + "");
				}
				break;
			case op:
				/**
				 * 如果是. 完成Token
				 */
				if (c == '.') {
					list.add(new Token(Token.POINT, Token.POINT, lineNum, offset));
					offset++;
					index++;
					status = op_name;
				}
				/**
				 * 字母 形成操作符
				 */
				else if (Character.isAlphabetic(c)) {// Every letter is
														// alphabetic,but not
														// every alphabetic is a
														// letter
					status = clazz;
				}
				/**
				 * 无视空格
				 */
				else if (Character.isWhitespace(c)) {
					index++;
					offset++;
				}
				/**
				 * 其他 报错
				 */
				else {
					throw new AnalyzeException(UNKNOWN_TOKEN, lineNum, offset, c + "");
				}
				break;
			case op_name:
				/**
				 * 字母 形成操作符
				 */
				if (Character.isAlphabetic(c)) {// Every letter is
												// alphabetic,but not every
												// alphabetic is a letter
					tmp += c;
					index++;
					offset++;
				}
				/**
				 * 如果是空格 .跟操作符之间的空格可以无视 否则应该完成Token并转到状态argv
				 */
				else if (Character.isWhitespace(c)) {
					if (tmp.equals("")) {
						index++;
						offset++;
					} else {
						tok = Token.getToken(tmp, lineNum, offset);
						if (tok == null) {
							throw new AnalyzeException(NO_SUCH_OPERATION, lineNum, offset, tmp);
						} else {
							list.add(tok);
							tmp = "";
						}
						status = argv;
					}
				}
				/**
				 * 如果是.状态回到op
				 */
				else if (c == '.') {
					tok = Token.getToken(tmp, lineNum, offset);
					if (tok == null) {
						throw new AnalyzeException(NO_SUCH_OPERATION, lineNum, offset, tmp);
					} else {
						list.add(tok);
						tmp = "";
					}
					status = op;
				}
				/**
				 * 如果遇到( 开始argv
				 */
				else if (c == '(') {
					tok = Token.getToken(tmp, lineNum, offset);
					if (tok == null) {
						throw new AnalyzeException(NO_SUCH_OPERATION, lineNum, offset, tmp);
					} else {
						list.add(tok);
						tmp = "";
					}
					status = argv;
				}
				/**
				 * 遇到-可能是-> 转to状态
				 */
				else if (c == '-') {
					tok = Token.getToken(tmp, lineNum, offset);
					if (tok == null) {
						throw new AnalyzeException(NO_SUCH_OPERATION, lineNum, offset, tmp);
					} else {
						list.add(tok);
						tmp = "";
					}
					status = to;
				}
				/**
				 * 其他 报错
				 */
				else {
					throw new AnalyzeException(UNKNOWN_TOKEN, lineNum, offset, c + "");
				}
				break;
			case argv:
				/**
				 * 如果是.状态回到op
				 */
				if (c == '.') {
					status = op;
				}
				/**
				 * 如果是(则进入exp
				 */
				else if (c == '(') {
					status = exp;
					index++;
					offset++;
				}
				/**
				 * 无视空格
				 */
				else if (Character.isWhitespace(c)) {
					index++;
					offset++;
				}
				/**
				 * 遇到-可能是-> 转to状态
				 */
				else if (c == '-') {
					status = to;
				}
				/**
				 * 其他 报错
				 */
				else {
					throw new AnalyzeException(UNKNOWN_TOKEN, lineNum, offset, c + "");
				}
				break;
			case exp:
				if (c == ')') {
					list.add(new Token(Token.EXP, tmp, lineNum, offset));
					index++;
					offset++;
					tmp = "";
					status = to;
				} else {
					tmp += c;
					offset++;
					index++;
				}
				break;
			case to:
				/**
				 * 如果是.则到下一个op状态
				 */
				if (c == '.') {
					status = op;
				}
				/**
				 * 如果是->则进入value状态
				 */
				else if (c == '-') {
					index++;
					offset++;
					if (index >= code.length()) {
						throw new AnalyzeException(UNEXPECTED_END, lineNum, offset, c + "");
					}
					c = code.charAt(index);
					if (c == '>') {
						index++;
						offset++;
						status = value;
						list.add(new Token(Token.ARROW, Token.ARROW, lineNum, offset));
					} else {
						throw new AnalyzeException(UNKNOWN_TOKEN, lineNum, offset, "-" + c);
					}
				}
				/**
				 * 无视空格
				 */
				else if (Character.isWhitespace(c)) {
					index++;
					offset++;
				}
				/**
				 * 如果是字母 可能是另一个类
				 */
				else if (Character.isAlphabetic(c)) {
					status = clazz;
				}
				/**
				 * 其他 报错
				 */
				else {
					throw new AnalyzeException(UNKNOWN_TOKEN, lineNum, offset, c + "");
				}
				break;
			case value:
				/**
				 * 如果是变量首字母 转到状态id
				 */
				if (Character.isJavaIdentifierStart(c)) {
					tmp += c;
					index++;
					offset++;
					status = id;
				}
				/**
				 * 如果是数字 转到状态number
				 */
				else if (Character.isDigit(c)) {
					tmp += c;
					index++;
					offset++;
					status = number;
				}
				/**
				 * 无视空格
				 */
				else if (Character.isWhitespace(c)) {
					index++;
					offset++;
				}
				/**
				 * 其他 报错
				 */
				else {
					throw new AnalyzeException(UNKNOWN_TOKEN, lineNum, offset, c + "");
				}
				break;
			case id:
				/**
				 * 如果是变量
				 */
				if (Character.isJavaIdentifierPart(c)) {
					tmp += c;
					index++;
					offset++;
				}
				/**
				 * 空格结束id输入
				 */
				else if (Character.isWhitespace(c)) {
					list.add(new Token(Token.IDENTIFIER, tmp, lineNum, offset));
					index++;
					offset++;
					tmp = "";
					status = op;
				}
				/**
				 * 如果是. 到达下一个op
				 */
				else if (c == '.') {
					list.add(new Token(Token.IDENTIFIER, tmp, lineNum, offset));
					index++;
					offset++;
					tmp = "";
					status = op;
				}
				/**
				 * 其他 报错
				 */
				else {
					throw new AnalyzeException(UNKNOWN_TOKEN, lineNum, offset, c + "");
				}
				break;
			case number:
				/**
				 * 如果是数字 转到状态number
				 */
				if (Character.isDigit(c)) {
					tmp += c;
					index++;
					offset++;
				}
				/**
				 * 如果是数字的小数点 转到浮点数
				 */
				else if (c == '.') {
					tmp += c;
					index++;
					offset++;
					status = floa;
				}
				/**
				 * 空格结束number输入
				 */
				else if (Character.isWhitespace(c)) {
					list.add(new Token(Token.NUMBER, tmp, lineNum, offset));
					index++;
					offset++;
					tmp = "";
					status = op;
				}
				/**
				 * 其他 报错
				 */
				else {
					throw new AnalyzeException(UNKNOWN_TOKEN, lineNum, offset, c + "");
				}
				break;
			case floa:
				/**
				 * 如果是数字
				 */
				if (Character.isDigit(c)) {
					tmp += c;
					index++;
					offset++;
				}
				/**
				 * 空格结束number输入
				 */
				else if (Character.isWhitespace(c)) {
					list.add(new Token(Token.FLOAT, tmp, lineNum, offset));
					index++;
					offset++;
					tmp = "";
					status = op;
				}
				/**
				 * 如果是. 到达下一个op
				 */
				else if (c == '.') {
					list.add(new Token(Token.FLOAT, tmp, lineNum, offset));
					index++;
					offset++;
					tmp = "";
					status = op;
				}
				/**
				 * 其他 报错
				 */
				else {
					throw new AnalyzeException(UNKNOWN_TOKEN, lineNum, offset, c + "");
				}
				break;
			default:
				throw new AnalyzeException(UNKNOWN_TOKEN, lineNum, offset, c + "");
			}// switch status
		} // while(index<lenth)

		if (!tmp.equals("")) {
			switch (status) {
			case id:
				list.add(new Token(Token.IDENTIFIER, tmp, lineNum, offset));
				break;
			case number:
				list.add(new Token(Token.NUMBER, tmp, lineNum, offset));
				break;
			case floa:
				list.add(new Token(Token.FLOAT, tmp, lineNum, offset));
				break;
			default:
				throw new AnalyzeException(UNEXPECTED_END, lineNum, offset, tmp);
			}
		}

		return list;
	}
}
