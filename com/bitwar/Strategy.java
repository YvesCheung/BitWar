package com.bitwar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bitwar.Condition.Assign;
import com.bitwar.Condition.Branch;
import com.bitwar.Condition.Current;
import com.bitwar.Condition.Enermy;
import com.bitwar.Condition.Extend;
import com.bitwar.Condition.Random;
import com.bitwar.Condition.Round;
import com.bitwar.util.Log;

public class Strategy {

	public String name = "T" + (int) (Math.random() * Integer.MAX_VALUE);

	public List<Condition> conditions = new ArrayList<>();

	public String result = "0";

	public void name(String s) {
		name = s.trim();
	}

	public void branch(String exp, Integer value) throws RuntimeException {
		int v = value.intValue();
		if (v != 0 && v != 1)
			throw new RuntimeException();
		exp = exp.replace(" and ", " && ");
		exp = exp.replace(" or ", " || ");
		Branch c = new Branch();
		c.expression = exp;
		c.result = value;
		conditions.add(c);
	}

	public void branch(String exp, String id) {
		exp = exp.replace(" and ", " && ");
		exp = exp.replace(" or ", " || ");
		Branch c = new Branch();
		c.expression = exp;
		c.id = id;
		conditions.add(c);
	}

	public void extend(String name) {
		Extend e = new Extend();
		e.className = name;
		conditions.add(e);
	}

	public void result(String exp, Integer value) throws RuntimeException {
		if (exp != null)
			throw new RuntimeException();
		result = value + "";
	}

	public void result(String exp, String id) throws RuntimeException {
		if (exp != null)
			throw new RuntimeException();
		result = id;
	}

	public void map(String exp, String id) {
		Assign a = new Assign();
		a.exp = exp;
		a.id = id;
		conditions.add(a);
	}

	public void cur(String exp, String id) {
		if (exp != null)
			throw new RuntimeException();
		Current c = new Current();
		c.id = id;
		conditions.add(c);
	}

	public void round(String exp, String id) {
		if (exp != null)
			throw new RuntimeException();
		Round r = new Round();
		r.id = id;
		conditions.add(r);
	}

	public void enermy(String exp, String id) throws RuntimeException {
		String[] numbers = exp.split("\\...");
		int size = numbers.length;
		if (size == 1) {
			Enermy e = new Enermy();
			e.from = numbers[0];
			e.to = numbers[0];
			e.id = id;
			conditions.add(e);
		} else if (size == 2) {
			Enermy e = new Enermy();
			e.from = numbers[0];
			e.to = numbers[1];
			e.id = id;
			conditions.add(e);
		} else {
			throw new RuntimeException();
		}
	}

	public void self(String exp, String id) throws RuntimeException {
		String[] numbers = exp.split("\\...");
		int size = numbers.length;
		if (size == 1) {
			Enermy e = new Enermy();
			e.from = numbers[0];
			e.to = numbers[0];
			e.id = id;
			conditions.add(e);
		} else if (size == 2) {
			Enermy e = new Enermy();
			e.from = numbers[0];
			e.to = numbers[1];
			e.id = id;
			conditions.add(e);
		} else {
			throw new RuntimeException();
		}
	}

	public void random(String number, String id) throws NumberFormatException {
		Random r = new Random();
		r.id = id;
		r.num = number;
		conditions.add(r);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Strategy: " + name + "\n");
		for (Condition c : conditions) {
			sb.append(c + "\n");
		}
		sb.append("result: " + result);
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		Log.i((Strategy) obj);
		if (obj instanceof Strategy) {
			return ((Strategy) obj).name.equals(name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
