package com.bitwar;

public class StrategyExample {
	public static String getAlways1() {
		return "Strategy.name(永远合作)" + "\n\t.result->1";
	}

	public static String getRandom() {
		return "Strategy.name(随机1/3概率)" + "\n\t.random(3)->i" + "\n\t.branch(i==0)->0" + "\n\t.result->1";
	}

	public static String getAgainst() {
		return "Strategy.name(针锋相对)" + "\n\t.cur->c" + "\n\t.branch(c==1)->1" + "\n\t.enermy(c-1)->j"
				+ "\n\t.branch(true)->j" + "\n\t.result->0";
	}

	public static String getHonerDetector() {
		return "Strategy.name(老实人探测器)" + "\n\t.extend(针锋相对)" + "\n\t.random(3)->r" + "\n\t.branch(r==0)->0";
	}

	public static String getNeverForgive() {
		return "Strategy.name(永不原谅)" + "\n\t.cur->c" + "\n\t.branch(c==1)->1" + "\n\t.enermy(c-1...1)->cnt"
				+ "\n\t.branch(cnt<c-1)->0" + "\n\t.result->1";
	}

	public static String get2Revenge() {
		return "Strategy.name(两报还一报)" + "\n\t.cur->c" + "\n\t.branch(c==1)->1" + "\n\t.enermy(c-1...1)->total"
				+ "\n\t.branch(total<=c-3)->0" + "\n\t.result->1";
	}

	public static String getAlways0() {
		return "Strategy.name(永远背叛)";
	}

	public static String getCold() {
		return "Strategy.name(冷血)" + "\n\t.extend(永不原谅)" + "\n\t.extend(永远背叛)";
	}
	
	public static String getComp() {
		return "Competition.round(500)\n";
	}
}
