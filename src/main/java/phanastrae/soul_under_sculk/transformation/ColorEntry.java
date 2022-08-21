package phanastrae.soul_under_sculk.transformation;

public class ColorEntry {

	private int color;
	private int time;

	public ColorEntry(int color, int time) {
		setColor(color);
		setTime(time);
	}

	public int getColor() {
		return this.color;
	}

	public void setColor(int color) {
		this.color = (color < 0 || color > 0xFFFFFF) ? 0 : color;
	}

	public int getTime() {
		return this.time;
	}

	public void setTime(int time) {
		this.time = (time <= 0) ? 1 : time;
	}
}
