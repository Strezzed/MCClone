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
import org.pmw.tinylog.Logger;
import org.strezz.mcclone.object.GameObject;
import org.strezz.mcclone.window.Window;

public class Transformation {

    private final Matrix4f projectionMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f viewMatrix;

    public Transformation() {
        projectionMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        Logger.info("Initialized transformations.");
    }

    /**
     * Gets the projection matrix.
     *
     * @param fov    The FOV in radians.
     * @param window The main GLFW window.
     * @param near   The near clipping plane.
     * @param far    The far clipping plane.
     * @return The Projection matrix.
     */
    public Matrix4f getProjectionMatrix(float fov, Window window, float near, float far) {
        return projectionMatrix.identity().perspective(fov, (float) window.getWidth() / (float) window.getHeight(), near, far);
    }

    /**
     * Gets the model-view matrix.
     *
     * @param obj        The game object.
     * @param viewMatrix The view matrix.
     * @return Model-view matrix.
     */
    public Matrix4f getModelViewMatrix(GameObject obj, Matrix4f viewMatrix) {
        Vector3f rotation = obj.getRotation();
        modelViewMatrix.identity().translate(obj.getPosition()).rotateX((float) Math.toRadians(rotation.x)).rotateY((float) Math.toRadians(rotation.y)).rotateZ((float) Math.toRadians(rotation.z)).scale(obj.getScale());
        Matrix4f currentViewMatrix = new Matrix4f(viewMatrix);
        return currentViewMatrix.mul(modelViewMatrix);
    }

    /**
     * Gets the view matrix.
     *
     * @param camera The camera.
     * @return View matrix.
     */
    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f camPos = camera.getPosition();
        Vector3f camRot = camera.getRotation();

        viewMatrix.identity().rotate((float) Math.toRadians(camRot.x), new Vector3f(1, 0, 0)).rotate((float) Math.toRadians(camRot.y), new Vector3f(0, 1, 0));
        return viewMatrix.translate(-camPos.x, -camPos.y, -camPos.z);
    }


}
