package phanastrae.soul_under_sculk.transformation;

import net.minecraft.nbt.NbtCompound;
import phanastrae.soul_under_sculk.SoulUnderSculk;

import java.util.ArrayList;

public class CompositeColorEntry {

	private ArrayList<ColorEntry> colorEntries;
	private int totalTime = 0;
	private boolean doInterpolation;
	private boolean scaleWithSpeed;

	public static final boolean DEFAULT_DO_INTERPOLTION = true;
	public static final boolean DEFAULT_SCALE_WITH_SPEED = false;

	public CompositeColorEntry() {
		this(true, false);
	}

	public CompositeColorEntry(boolean doInterpolation, boolean scaleWithSpeed) {
		setDoInterpolation(doInterpolation);
		setScaleWithSpeed(scaleWithSpeed);
		this.colorEntries = new ArrayList<ColorEntry>();
	}

	public int getTotalTime() {
		return this.totalTime;
	}

	public boolean getDoInterpolation() {
		return this.doInterpolation;
	}

	public void setDoInterpolation(boolean b) {
		this.doInterpolation = b;
	}

	public boolean getScaleWithSpeed() {
		return this.scaleWithSpeed;
	}

	public void setScaleWithSpeed(boolean b) {
		this.scaleWithSpeed = b;
	}

	public ArrayList<ColorEntry> getColorEntries() {
		return this.colorEntries;
	}

	public void addColorEntry(int color, int time) {
		this.addColorEntry(new ColorEntry(color, time));
	}

	public void addColorEntry(ColorEntry colorEntry) {
		this.colorEntries.add(colorEntry);
		this.totalTime += colorEntry.getTime();
	}

	public int getColorAtTime(int time) {
		if(totalTime <= 0) {
			return 0xFFFFFF;
		}
		return getColorAtTime((float)(Math.floorMod(time, totalTime)));
	}

	public int getColorAtTime(float time) {
		if(totalTime <= 0 || this.colorEntries.size() == 0) {
			return 0xFFFFFF;
		}
		if(this.colorEntries.size() == 1) {
			return this.colorEntries.get(0).getColor();
		}
		float localTime = ((Math.floorMod((int)Math.floor(time), totalTime) + ((int)(time - Math.floor(time)))) % totalTime);

		int index = -1;
		for(int i = 0; i < this.colorEntries.size() && index == -1; i++) {
			int colorTime = colorEntries.get(i).getTime();
			if(localTime >= colorTime) {
				localTime -= colorTime;
			} else {
				index = i;
			}
		}
		int currentColor = this.colorEntries.get(index).getColor();
		if(!this.doInterpolation) {
			return currentColor;
		}
		int colorTime = this.colorEntries.get(index).getTime();
		if(colorTime <= 0) return currentColor;

		int nextColor = this.colorEntries.get((index + 1) % this.colorEntries.size()).getColor();
		float progress = localTime / colorTime;

		return this.lerpColor(progress, currentColor, nextColor);
	}

	public static int lerpColor(float progress, int currentColor, int nextColor) {
		int rCurrent = (currentColor & 0xFF0000) >> 16;
		int gCurrent = (currentColor & 0xFF00) >> 8;
		int bCurrent = (currentColor & 0xFF);
		int rNext = (nextColor & 0xFF0000) >> 16;
		int gNext = (nextColor & 0xFF00) >> 8;
		int bNext = (nextColor & 0xFF);
		int rNew = (int)(rNext * progress + rCurrent * (1-progress));
		int gNew = (int)(gNext * progress + gCurrent * (1-progress));
		int bNew = (int)(bNext * progress + bCurrent * (1-progress));
		if(rNew < 0) rNew = 0;
		if(gNew < 0) gNew = 0;
		if(bNew < 0) bNew = 0;
		if(rNew > 255) rNew = 255;
		if(gNew > 255) gNew = 255;
		if(bNew > 255) bNew = 255;
		return (rNew << 16) + (gNew << 8) + (bNew);
	}

	public void writeNbt(NbtCompound nbt, String key) {
		NbtCompound compNbt = new NbtCompound();

		if(this.colorEntries != null) {
			int[] colors = new int[colorEntries.size()];
			int[] times = new int[colorEntries.size()];
			for (int i = 0; i < colorEntries.size(); i++) {
				colors[i] = colorEntries.get(i).getColor();
				times[i] = colorEntries.get(i).getTime();
			}
			compNbt.putIntArray("Colors", colors);
			compNbt.putIntArray("Times", times);
		}
		compNbt.putBoolean("DoInterpolation", doInterpolation);
		compNbt.putBoolean("ScaleWithSpeed", scaleWithSpeed);

		nbt.put(key, compNbt);
	}

	public void readNbt(NbtCompound nbt, String key) {
		NbtCompound compNbt = nbt.getCompound(key);

		int[] colors = compNbt.getIntArray("Colors");
		int[] times = compNbt.getIntArray("Times");
		int size = Math.min(colors.length, times.length);
		this.colorEntries = new ArrayList<ColorEntry>();
		for(int i = 0; i < size; i++) {
			this.addColorEntry(colors[i], times[i]);
		}
		if(compNbt.contains("DoInterpolation")) {
			this.doInterpolation = compNbt.getBoolean("DoInterpolation");
		}
		if(compNbt.contains("ScaleWithSpeed")) {
			this.scaleWithSpeed = compNbt.getBoolean("ScaleWithSpeed");
		}

		nbt.put(key, compNbt);
	}
}
