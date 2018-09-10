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
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.strezz.mcclone.input.InputHandler;
import org.strezz.mcclone.render.Camera;
import org.strezz.mcclone.utils.MathsUtils;
import org.strezz.mcclone.window.Window;

public class CameraMovement {

    private final Vector2d cursorCurrent;
    private final Vector2d cursorPrev;

    @Getter
    private final Vector3f camChange;

    private final Vector2d center;

    private final Camera camera;

    private final float roationSpeed;

    public CameraMovement(Camera camera, float roationSpeed) {
        this.camera = camera;
        this.roationSpeed = roationSpeed;

        cursorCurrent = new Vector2d();
        cursorPrev = new Vector2d();
        camChange = new Vector3f();
        center = new Vector2d();
    }

    public void init(Window window) {
        center.set(new Vector2d((double) window.getWidth() / 2.0d, (double) window.getHeight() / 2.0d));

        GLFW.glfwSetCursorPosCallback(window.getWindowID(), (window1, xpos, ypos) -> cursorCurrent.set(xpos, ypos));

        GLFW.glfwSetInputMode(window.getWindowID(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

    public void render(Window window, InputHandler inputHandler) {
        double deltaX = cursorCurrent.x - cursorPrev.x;
        double deltaY = cursorCurrent.y - cursorPrev.y;

        if (cursorCurrent.x != cursorPrev.x) {
            cursorPrev.x = cursorCurrent.x;
            camera.rotate(0.0f, (float) deltaX * roationSpeed, 0.0f);
        }

        if (cursorCurrent.y != cursorPrev.y) {
            cursorPrev.y = cursorCurrent.y;
            camera.rotate((float) deltaY * roationSpeed, 0.0f, 0.0f);
        }

        camera.getRotation().x = MathsUtils.clamp(camera.getRotation().x, -90, 90);
        doMovement(inputHandler);
    }

    private void doMovement(InputHandler inputHandler) {
        camChange.set(0, 0, 0);

        if (inputHandler.keyPressed(GLFW.GLFW_KEY_W)) {
            camChange.z -= 1;
        }

        if (inputHandler.keyPressed(GLFW.GLFW_KEY_S)) {
            camChange.z += 1;
        }

        if (inputHandler.keyPressed(GLFW.GLFW_KEY_A)) {
            camChange.x -= 1;
        }

        if (inputHandler.keyPressed(GLFW.GLFW_KEY_D)) {
            camChange.x += 1;
        }

        if (inputHandler.keyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            camChange.y -= 1;
        }

        if (inputHandler.keyPressed(GLFW.GLFW_KEY_SPACE)) {
            camChange.y += 1;
        }
    }
}
