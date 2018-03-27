package ch.inkspot.textoverlay.example;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ch.inkspot.textoverlay.TextGenerator;
import ch.inkspot.textoverlay.TextOverlayRenderer;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

public class MainActivity extends AppCompatActivity {

    private static final DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GLSurfaceView view = new GLSurfaceView(this);
        view.setEGLContextClientVersion(2);
        view.setRenderer(new TextOverlayRenderer(this, new TextGenerator() {
            @Override
            public String generate() {
                return dateFormat.format(new Date());
            }
        }, new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
                glClearColor(0, 0, 0, 0);
            }

            @Override
            public void onSurfaceChanged(GL10 gl10, int width, int height) {
                glViewport(0, 0, width, height);
            }

            @Override
            public void onDrawFrame(GL10 gl10) {
                glClear(GL_COLOR_BUFFER_BIT);
            }
        }));
        setContentView(view);
    }
}
