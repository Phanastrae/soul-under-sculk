package phanastrae.soul_under_sculk.command;

import phanastrae.soul_under_sculk.transformation.TransformationType;

public class TransformationArgument {
	private final TransformationType transformationType;

	public TransformationArgument(TransformationType transformationType) {
		this.transformationType = transformationType;
	}


	public TransformationType getTransformation() {
		return this.transformationType;
	}
}
