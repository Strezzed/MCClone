/*******************************************************************************
 * MCClone a Minecraft Clone
 * Copyright (C) 2018  Keir Davis
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package org.strezz.mcclone.render.shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import org.pmw.tinylog.Logger;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public final class ShaderProgram {

    private final int programID;
    private final int errorLength = 1024;
    private final Map<String, Integer> uniforms;

    private int vertex;
    private int fragment;

    public ShaderProgram() {
        programID = GL20.glCreateProgram();
        if (programID == 0) {
            Logger.error("Shader program could not be created.");
        }
        uniforms = new HashMap<>();
    }

    public static void unbind() {
        GL20.glUseProgram(0);
    }

    public void createVertex(String code) throws Exception {
        vertex = createShader(GL20.GL_VERTEX_SHADER, code);
    }

    public void createFragment(String code) throws Exception {
        fragment = createShader(GL20.GL_FRAGMENT_SHADER, code);
    }

    public void link() throws Exception {
        GL20.glLinkProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Could not link shader: " + GL20.glGetProgramInfoLog(programID, errorLength));
        }

        if (vertex != 0) {
            GL20.glDetachShader(programID, vertex);
        }

        if (fragment != 0) {
            GL20.glDetachShader(programID, fragment);
        }

        GL20.glValidateProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0) {
            Logger.warn("Shader validation warning: " + GL20.glGetProgramInfoLog(programID, errorLength));
        }
    }

    private int createShader(int type, String shaderCode) throws Exception {
        int shaderID = GL20.glCreateShader(type);
        if (shaderID == 0) {
            throw new RuntimeException("Could not create shader");
        }

        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Could not compile shader: " + GL20.glGetShaderInfoLog(shaderID, errorLength));
        }

        GL20.glAttachShader(programID, shaderID);
        return shaderID;
    }

    public void createUniform(String name) throws Exception {
        int location = GL20.glGetUniformLocation(programID, name);
        if (location < 0) {
            throw new RuntimeException("Couldn't create uniform: " + name);
        }
        uniforms.put(name, location);
    }

    public void setUniform(String name, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            GL20.glUniformMatrix4fv(uniforms.get(name), false, fb);
        }
    }

    public void setUniform(String name, int value) {
        GL20.glUniform1i(uniforms.get(name), value);
    }

    public void setUniform(String name, float value) {
        GL20.glUniform1f(uniforms.get(name), value);
    }

    public void setUniform(String name, Vector3f value) {
        GL20.glUniform3f(uniforms.get(name), value.x, value.y, value.z);
    }

    public void setUniform(String name, Vector4f value) {
        GL20.glUniform4f(uniforms.get(name), value.x, value.y, value.z, value.w);
    }

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void cleanUp() {
        unbind();
        if (programID != 0) {
            GL20.glDeleteProgram(programID);
            Logger.info("Cleaned up program: " + programID);
        }
    }

}
