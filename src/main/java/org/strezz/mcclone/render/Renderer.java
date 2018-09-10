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

package org.strezz.mcclone.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.strezz.mcclone.io.Resources;
import org.strezz.mcclone.object.GameObject;
import org.strezz.mcclone.render.shader.ShaderProgram;
import org.strezz.mcclone.window.Window;

public class Renderer {
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float NEAR = 0.01f;
    private static final float FAR = 1000.0f;

    private ShaderProgram basicShader;
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Transformation transformation;

    private Vector3f ambient = new Vector3f(0.2f, 0.2f, 0.2f);

    public void init() {
        try {
            transformation = new Transformation();

            basicShader = new ShaderProgram();
            basicShader.createVertex(Resources.getResourceAsString("minecraft:shader/basic/basic.vert"));
            basicShader.createFragment(Resources.getResourceAsString("minecraft:shader/basic/basic.frag"));
            basicShader.link();

            basicShader.createUniform("projectionMatrix");
            basicShader.createUniform("modelViewMatrix");
            basicShader.createUniform("texture_sampler");
            basicShader.createUniform("ambientLight");
            basicShader.createUniform("blockLight");

            GL11.glEnable(GL11.GL_DEPTH_TEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(Window window, Camera camera, GameObject gameObject, float light) {
        if (gameObject != null && gameObject.getMesh() != null) {
            if (gameObject.getMesh().hasShader()) {
                gameObject.getMesh().getShader().bind();
            } else {
                basicShader.bind();
            }

            projectionMatrix = transformation.getProjectionMatrix(FOV, window, NEAR, FAR);
            basicShader.setUniform("projectionMatrix", projectionMatrix);
            basicShader.setUniform("blockLight", new Vector3f(light, light, light));
            basicShader.setUniform("ambientLight", ambient);

            viewMatrix = transformation.getViewMatrix(camera);
            basicShader.setUniform("texture_sampler", 0);

            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameObject, viewMatrix);
            basicShader.setUniform("modelViewMatrix", modelViewMatrix);

            gameObject.render();

            ShaderProgram.unbind();
        }
    }

    public void cleanup() {
        basicShader.cleanUp();
    }
}
