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

package ch.inkspot.textoverlay.internal;

import android.util.Log;

import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glUseProgram;

public class ShaderProgram {
    public static class Uniforms {
        public static final String U_TEXTURE_UNIT = "u_TextureUnit";
    }

    public static class Attributes {
        public static final String A_POSITION = "a_Position";
        public static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    }

    private final Shader.VertexShader vertexShader;
    private final Shader.FragmentShader fragmentShader;
    protected final int programId;

    public ShaderProgram(Shader.VertexShader vertexShader, Shader.FragmentShader fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;

        programId = glCreateProgram();

        if (programId == 0) {
            Log.d("ShaderUtil", "Program could not be created.");
        }

        glAttachShader(programId, vertexShader.compile());
        glAttachShader(programId, fragmentShader.compile());
        glLinkProgram(programId);

        int[] linkStatus = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);

        if (linkStatus[0] == 0) {
            glDeleteProgram(programId);
            Log.v("ShaderUtil", "Results of validating program: " + linkStatus[0]
                    + "\nLog:" + glGetProgramInfoLog(programId));
            Log.d("ShaderUtil", "Program could not be linked.");
        }
    }

    public void useProgram() {
        glUseProgram(programId);
    }
}
