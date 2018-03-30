package ch.inkspot.textoverlay.internal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.opengl.GLUtils;
import android.util.Log;
import android.util.TypedValue;

import ch.inkspot.textoverlay.TextGenerator;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static ch.inkspot.textoverlay.internal.Constants.BYTES_PER_FLOAT;

public class TextOverlay {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final VertexArray vertexArray = new VertexArray(new float[]{
            -1.0f, -1.0f, 0f, 1f,
            1.0f, -1.0f, 1f, 1f,
            -1.0f, 1.0f, 0f, 0f,
            1.0f, 1.0f, 1f, 0f
    });
    private final Paint paint = new Paint();
    private final TextGenerator textGenerator;
    private final float padding;

    private int textureId;
    private int width;
    private int height;

    private float x,y;

    public TextOverlay(Context context, TextGenerator textGenerator) {
        this.textGenerator = textGenerator;
        int[] textureId = new int[1];
        glGenTextures(1, textureId, 0);

        if (textureId[0] == 0) {
            Log.d(getClass().getSimpleName(), "Failed to load texture");
        }

        this.textureId = textureId[0];

        paint.setColor(Color.WHITE);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, context.getResources().getDisplayMetrics()));
        padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void bindData(TextureShaderProgram program) {
        vertexArray.setVertexAttribPointer(0, program.aPositionLocation, POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, program.aTextureCoordinates, TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        if (textGenerator == null) return;

        String text = textGenerator.generate();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        if (x == 0 && y == 0) {
            float textLength = paint.measureText(text);
            x = (width - (int) textLength) >> 1;
            y = height * .9f;
        }
        canvas.drawText(text, x, y, paint);

        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();

        glGenerateMipmap(GL_TEXTURE_2D);

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

    }
}
