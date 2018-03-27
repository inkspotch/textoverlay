package ch.inkspot.textoverlay.internal;

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glShaderSource;

abstract class Shader {

    private final String source;
    private final int shaderType;

    public Shader(String source, int shaderType) {
        this.source = source;
        this.shaderType = shaderType;
    }

    public int compile() {
        int shaderId = glCreateShader(shaderType);

        if (shaderId == 0) {
            Log.d(getClass().getSimpleName(), "Failed to create shader.");
        }

        glShaderSource(shaderId, source);
        glCompileShader(shaderId);

        if (!isValidShader(shaderId)) {
            Log.d(getClass().getSimpleName(), "Failed to create a valid shader.");
        }

        return shaderId;
    }

    private boolean isValidShader(int shaderId) {
        int[] status = new int[1];
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, status, 0);

        if (status[0] == 0) {
            glDeleteShader(shaderId);
        }

        return status[0] == 0;
    }

    static class VertexShader extends Shader {
        public static final String source
                = "attribute vec4 a_Position; \n" +
                "attribute vec2 a_TextureCoordinates; \n" +

                "varying vec2 v_TextureCoordinates; \n" +

                "void main() { \n" +
                "    v_TextureCoordinates = a_TextureCoordinates; \n" +
                "    gl_Position = a_Position; \n" +
                "}";

        public VertexShader() {
            super(source, GL_VERTEX_SHADER);
        }
    }

    static class FragmentShader extends Shader {
        public static final String source
                = "precision mediump float; \n" +

                "uniform sampler2D u_TextureUnit; \n" +
                "varying vec2 v_TextureCoordinates; \n" +

                "void main() { \n" +
                "    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates); \n" +
                "}";

        public FragmentShader() {
            super(source, GL_FRAGMENT_SHADER);
        }
    }
}
