package com.bitwar.compiler;

import com.bitwar.Competition;
import com.bitwar.Strategy;

public class BitWarCompiler extends DynamicCompiler {
	private Strategy s1, s2;
	private int round;

	public BitWarCompiler(Strategy s1, Strategy s2, int round) {
		this.s1 = s1;
		this.s2 = s2;
		this.round = round;
	}

	@Override
	public String getClassName() {
		return "_TEMP_COMP";
	}

	@Override
	public String getMethodNameOfStartGame() {
		return "start";
	}

	@Override
	public String getMethodNameOfScore1() {
		return "getScore1";
	}

	@Override
	public String getMethodNameOfScore2() {
		return "getScore2";
	}

	@Override
	public String getMethodNameOfChoose1() {
		return "getChoose1";
	}

	@Override
	public String getMethodNameOfChoose2() {
		return "getChoose2";
	}

	@Override
	public Strategy getStrategy1() {
		return s1;
	}

	@Override
	public Strategy getStrategy2() {
		return s2;
	}

	@Override
	public int getRound() {
		return round;
	}
}
