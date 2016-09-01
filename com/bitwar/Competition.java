package com.bitwar;

import java.util.List;
import java.util.Set;

import com.bitwar.Strategy;
import com.bitwar.util.Log;

public class Competition {

	public void combat(String s1Name, String s2Name) {
		s1 = s1Name;
		s2 = s2Name;
	}

	public void combat(String sName) {
		if (!sName.equals(""))
			s1 = sName;
	}

	public void round(String number) throws NumberFormatException {
		int r = Integer.valueOf(number);
		if (r > 200)
			r = 200;
		round = r;
	}

	public void log(String exp) {
		log = true;
	}

	public Strategy getStrategy1(Set<Strategy> set) {
		if (s1 == null)
			return null;
		for (Strategy s : set) {
			if (s.name.equals(s1))
				return s;
		}
		return null;
	}

	public Strategy getStrategy2(Set<Strategy> set) {
		if (s2 == null)
			return null;
		for (Strategy s : set) {
			if (s.name.equals(s2))
				return s;
		}
		return null;
	}

	public int getRound() {
		return round;
	}

	public String s1 = null;
	public String s2 = null;
	public boolean log = false;
	public int round = 10;
}
