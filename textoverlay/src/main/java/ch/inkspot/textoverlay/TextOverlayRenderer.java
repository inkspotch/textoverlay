/*
 * Copyright (C) 2018 Brad Holmes <developer@theoryandthunder.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */

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
