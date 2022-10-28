package catpouch.pip.client.util;

import net.minecraft.util.math.Matrix4f;

public class Matrix4fUtil {
    public static float[] multMatrixVec(float[] matrix, float[] vector) {
        float[] out = new float[4];

        for (int i = 0; i < 4; i++) {
            out[i] = (vector[0] * matrix[i]) + (vector[1] * matrix[4 + i]) + (vector[2] * matrix[8 + i]) + (vector[3] * matrix[12 + i]);
        }
        return(out);
    }
}
