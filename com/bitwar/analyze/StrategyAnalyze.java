package com.bitwar.analyze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bitwar.Condition;
import com.bitwar.Condition.Extend;
import com.bitwar.Strategy;

public class StrategyAnalyze {

	public void analyze(Set<Strategy> strategies) throws AnalyzeException {

		Map<String, Strategy> strategy = new HashMap<>();
		for (Strategy s : strategies) {
			strategy.put(s.name, s);
		}

		for (Strategy s : strategies) {
			List<Condition> newConditions = new ArrayList<>();
			Set<String> extendNames = new HashSet<>();// 记录下已继承的类，防止死循环
			extendNames.add(s.name);

			for (int i = 0; i < s.conditions.size(); i++) {
				Condition c = s.conditions.get(i);
				if (c instanceof Extend) {
					Extend e = (Extend) c;

					// 如果已经继承过这个类则跳过
					if (extendNames.contains(e.className))
						continue;
					extendNames.add(e.className);

					// 找到继承的父类 并把父类的语句加进来
					Strategy parent = strategy.get(e.className);
					if (parent == null) {
						throw new AnalyzeException(AnalyzeException.NO_SUCH_CLAZZ, 0, 0, e.className);
					}
					s.conditions.addAll(i + 1, parent.conditions);
					s.result = parent.result;
				} else {
					newConditions.add(c);
				}
			}
			s.conditions = newConditions;
		}
	}
}
