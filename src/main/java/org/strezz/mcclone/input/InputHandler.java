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

package org.strezz.mcclone.input;

import org.lwjgl.glfw.GLFW;
import org.strezz.mcclone.window.Window;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputHandler {

    private final Queue<Integer> downKeys;
    private final Queue<Integer> upKeys;
    private final Queue<Integer> pressedKeys;
    private boolean p = false;

    public InputHandler() {
        downKeys = new ConcurrentLinkedQueue<>();
        upKeys = new ConcurrentLinkedQueue<>();
        pressedKeys = new ConcurrentLinkedQueue<>();
    }

    public void gameInit() {
        downKeys.clear();
        upKeys.clear();
    }

    public void renderInit(Window window) {
        if (!p) {
            p = true;
            GLFW.glfwSetKeyCallback(window.getWindowID(), (window1, key, scancode, action, mods) -> {
                if (action == GLFW.GLFW_PRESS) {
                    downKeys.add(key);
                    upKeys.remove(key);
                    pressedKeys.add(key);
                } else if (action == GLFW.GLFW_RELEASE) {
                    downKeys.remove(key);
                    upKeys.add(key);
                    pressedKeys.remove(key);
                }
            });
        }
    }

    public boolean keyDown(Integer key) {
        if (p) {
            return downKeys.contains(key);
        }
        return false;
    }

    public boolean keyUp(Integer key) {
        if (p) {
            return upKeys.contains(key);
        }
        return false;
    }

    public boolean keyPressed(Integer key) {
        if (p) {
            return pressedKeys.contains(key);
        }
        return false;
    }


}
