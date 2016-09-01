package com.bitwar;

public class Condition {
	public String strategyName = "";

	private Condition() {
	}

	public static class Branch extends Condition {
		public String expression;
		public Integer result = null;
		public String id = null;

		@Override
		public String toString() {
			return "branch: if " + expression + "  =>  " + (result == null ? id : result + "");
		}
	}

	public static class Extend extends Condition {
		public String className;

		@Override
		public String toString() {
			return "extend " + className;
		}
	}

	public static class Assign extends Condition {
		public String exp;
		public String id;

		@Override
		public String toString() {
			return id + " = " + exp;
		}
	}

	public static class Round extends Condition {
		public String id;

		@Override
		public String toString() {
			return "map round -> " + id;
		}
	}

	public static class Random extends Condition {
		public String id;
		public String num;

		@Override
		public String toString() {
			return id + " = random(" + num + ")";
		}
	}

	public static class Current extends Condition {
		public String id;

		@Override
		public String toString() {
			return "map current -> " + id;
		}
	}

	public static class Enermy extends Condition {
		public String from;
		public String to;
		public String id;

		@Override
		public String toString() {
			return id + " = " + "enermy[" + from + "..." + to + "]";
		}
	}

	public static class self extends Condition {
		public int from;
		public int to;
		public String id;

		@Override
		public String toString() {
			return id + " = " + "self[" + from + "..." + to + "]";
		}
	}
}