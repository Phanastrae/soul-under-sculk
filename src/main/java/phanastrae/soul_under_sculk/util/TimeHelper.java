package phanastrae.soul_under_sculk.util;

public class TimeHelper {
	public static long countFrom = 0;

	public static void countFromNow() {
		countFrom = System.currentTimeMillis();
	}

	public static long timeElapsed() {
		return System.currentTimeMillis() - countFrom;
	}

	public static long timeElapsedTicks() {
		return timeElapsed() / 50;
	}

	public static double timeElapsedTicksDouble() {
		return timeElapsed() / 50d;
	}
}
