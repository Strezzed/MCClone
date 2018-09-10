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

package org.strezz.mcclone.render.object;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;
import org.strezz.mcclone.render.Texture;
import org.strezz.mcclone.render.shader.ShaderProgram;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {

    @Getter
    @Setter
    private ShaderProgram shader;

    private final float[] verts;
    private final int[] tris;
    private final float[] uv;

    @Getter
    private final Texture texture;

    private int coordVao;
    private int coordVbo;
    private int triVbo;
    private int uvVbo;

    @Getter
    private int vertexCount;

    @Getter
    private boolean built = false;

    public Mesh(float[] verts, int[] tris, float[] uv, Texture texture) {
        this(null, verts, tris, uv, texture);
    }

    public Mesh(ShaderProgram shader, float[] verts, int[] tris, float[] uv, Texture texture) {
        this.shader = shader;
        this.verts = verts;
        this.tris = tris;
        this.uv = uv;
        this.texture = texture;
        vertexCount = tris.length;
    }

    public void build() {
        if (!built) {
            built = true;
            FloatBuffer vertBuf = MemoryUtil.memAllocFloat(verts.length);
            vertBuf.put(verts).flip();

            IntBuffer triBuf = MemoryUtil.memAllocInt(tris.length);
            triBuf.put(tris).flip();

            FloatBuffer uvBuf = MemoryUtil.memAllocFloat(uv.length);
            uvBuf.put(uv).flip();

            coordVao = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(coordVao);

            coordVbo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, coordVbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertBuf, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

            triVbo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, triVbo);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, triBuf, GL15.GL_STATIC_DRAW);

            uvVbo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, uvVbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, uvBuf, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

            MemoryUtil.memFree(vertBuf);
            MemoryUtil.memFree(triBuf);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL30.glBindVertexArray(0);
        }
    }

    public void render() {
        if (texture != null) {
            if (!texture.isLoaded()) {
                texture.loadTexture();
            }
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
        }

        GL30.glBindVertexArray(coordVao);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        GL20.glDisableVertexAttribArray(0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(coordVbo);
        GL15.glDeleteBuffers(triVbo);

        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(0);
    }

    public boolean hasShader() {
        return shader != null;
    }
}
