package ch.inkspot.textoverlay.internal;

import static android.opengl.GLES20.*;

public class TextureShaderProgram extends ShaderProgram {

    private final int uTextureUnitLocation;
    public final int aPositionLocation;
    public final int aTextureCoordinates;

    public TextureShaderProgram() {
        super(new Shader.VertexShader(), new Shader.FragmentShader());

        uTextureUnitLocation = glGetUniformLocation(programId, Uniforms.U_TEXTURE_UNIT);
        aPositionLocation = glGetAttribLocation(programId, Attributes.A_POSITION);
        aTextureCoordinates = glGetAttribLocation(programId, Attributes.A_TEXTURE_COORDINATES);
    }

    public void setUniforms() {
        glActiveTexture(GL_TEXTURE0);
        glUniform1i(uTextureUnitLocation, 0);
    }
}
