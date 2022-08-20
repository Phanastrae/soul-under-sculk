package phanastrae.soul_under_sculk.render;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

import java.nio.FloatBuffer;

public class RenderHelper {
	public static void doTranslation(MatrixStack stack, float targetOffsetX, float targetOffsetY, float targetOffsetZ, float additionalOffsetX, float additionalOffsetY, float additionalOffsetZ) {
		stack.translate((additionalOffsetX + targetOffsetX) / 16F, (additionalOffsetY + targetOffsetY) / 16F, (additionalOffsetZ + targetOffsetZ) / 16F);
	}

	public static void doScale(MatrixStack stack, float targetScaleX, float targetScaleY, float targetScaleZ, float originalScaleX, float originalScaleY, float originalScaleZ) {
		stack.scale(targetScaleX / originalScaleX, targetScaleY / originalScaleY, targetScaleZ / originalScaleZ);
	}
	public static void writeStackToModelPartTransform(MatrixStack stack, ModelPart part) {
		// i hate maths i hate maths i hate maths i hate maths i hate maths i hate maths
		// (i love maths) (this took too long)
		Matrix4f mat = stack.peek().getPosition();
		FloatBuffer buf = FloatBuffer.allocate(16);
		mat.write(buf, true);

		part.pivotX = buf.get(3) * 16;
		part.pivotY = buf.get(7) * 16;
		part.pivotZ = buf.get(11) * 16;
		double sx = Math.sqrt(buf.get(0) * buf.get(0) + buf.get(4) * buf.get(4) + buf.get(8) * buf.get(8));
		double sy = Math.sqrt(buf.get(1) * buf.get(1) + buf.get(5) * buf.get(5) + buf.get(9) * buf.get(9));
		double sz = Math.sqrt(buf.get(2) * buf.get(2) + buf.get(6) * buf.get(6) + buf.get(10) * buf.get(10));
		part.scaleX = (float)sx;
		part.scaleY = (float)sy;
		part.scaleZ = (float)sz;
		if(!(sx < 1E-6 || sy < 1E-6 || sz < 1E-6)) {
			float c = buf.get(8) / (float)sx;
			part.yaw = (float)Math.asin(-c);
			if(Math.abs(c) > 0.9999F) {
				//not 100% sure about this maths, haven't actually tested it lol
				double u = Math.atan2(4 * buf.get(6) / (float)sz - buf.get(1) / (float)sy, buf.get(5) / (float)sy - 2 * buf.get(2) / (float)sz);
				double v = Math.atan2(2*buf.get(1) / (float)sy, buf.get(5) / (float)sy);
				part.pitch = (float) (c * (u+v) / 2);
				part.roll = (float) (c * (u-v) / 2);
			} else {
				part.pitch = (float) Math.atan2(buf.get(9) / (float)sy, buf.get(10) / (float)sz);
				part.roll = (float) Math.atan2(buf.get(4) / (float)sx, buf.get(0) / (float)sx);
			}
		} else {
			// i'm just going to hope this never happens. don't use this system with any 0ish scale transforms please.
			part.pitch = part.yaw = part.roll = 0;
		}
	}
}
