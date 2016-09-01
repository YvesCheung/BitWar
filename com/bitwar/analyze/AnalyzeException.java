package com.bitwar.analyze;

/**
 * 分析时异常
 * 
 * @author 张宇
 *
 */
public class AnalyzeException extends RuntimeException {

	// 错误所在行号
	public int lineNum;
	// 错误所在列号
	public int offset;
	// 错误码
	public int errorCode;
	// 错误出现时的词语
	public String extra;

	public AnalyzeException(int errorCode, int lineNum, int offset, String extra) {
		super(getError(errorCode));
		this.lineNum = lineNum;
		this.errorCode = errorCode;
		this.offset = offset;
		this.extra = extra;
	}

	public static final int NO_SUCH_CLAZZ = -1;
	public static final int NO_SUCH_OPERATION = -2;
	public static final int UNKNOWN_TOKEN = -3;
	public static final int UNEXPECTED_END = -4;
	public static final int UNEXPECTED_TOKEN = -5;
	public static final int MISSING_PARAMETER = -6;
	public static final int ILLEGAL_RESULT = -7;
	public static final int EXIST_STRATEGY = -8;
	public static final int NO_SUCH_STRATEGT = -9;
	public static final int EXTEND_SELF = -10;
	public static final int IO = -11;
	public static final int COMPILE = -12;

	public static String getError(int code) {
		return getError(code, "");
	}

	public static String getError(int code, String name) {
		switch (code) {
		case NO_SUCH_CLAZZ:
			return "不存在这样的类名 " + name;
		case UNKNOWN_TOKEN:
			return "未知的符号 " + name;
		case NO_SUCH_OPERATION:
			return "不存在对应的操作符 " + name;
		case UNEXPECTED_END:
			return "结束得很突然 " + name;
		case UNEXPECTED_TOKEN:
			return "不应该出现这个符号 " + name;
		case MISSING_PARAMETER:
			return "缺少参数 " + name;
		case ILLEGAL_RESULT:
			return "结果不合法 " + name + " ,应为1或0";
		case EXIST_STRATEGY:
			return "存在同名的策略 " + name;
		case NO_SUCH_STRATEGT:
			return "不存在该策略 " + name;
		case EXTEND_SELF:
			return "不能继承自己 " + name;
		case IO:
			return "文件读写出错：" + name;
		case COMPILE:
			return "编译错误：\n" + name;
		default:
			return "未知的错误";
		}
	}

	@Override
	public String toString() {
		return "错误代码： " + errorCode + "\n错误描述: " + getError(errorCode, extra) + "\n在行号：  " + lineNum + " 列号： " + offset;
	}
}
