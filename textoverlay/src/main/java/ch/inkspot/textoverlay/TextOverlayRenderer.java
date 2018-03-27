package ch.inkspot.textoverlay;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ch.inkspot.textoverlay.internal.TextOverlay;
import ch.inkspot.textoverlay.internal.TextureShaderProgram;

public class TextOverlayRenderer implements GLSurfaceView.Renderer {
    private final Context context;
    private final TextGenerator textGenerator;
    private final GLSurfaceView.Renderer renderer;

    private TextureShaderProgram program;
    private TextOverlay textOverlay;

    public TextOverlayRenderer(Context context, TextGenerator textGenerator, GLSurfaceView.Renderer renderer) {
        this.context = context;
        this.textGenerator = textGenerator;
        this.renderer = renderer;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        renderer.onSurfaceCreated(unused, config);

        program = new TextureShaderProgram();
        textOverlay = new TextOverlay(context, textGenerator);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        renderer.onSurfaceChanged(unused, width, height);

        textOverlay.setDimensions(width, height);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        renderer.onDrawFrame(unused);

        program.useProgram();
        program.setUniforms();
        textOverlay.bindData(program);
        textOverlay.draw();
    }

}
