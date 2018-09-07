/*******************************************************************************
 * Minecraft a Minecraft Clone
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

package org.strezz.mcclone.window;

import lombok.Getter;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;
import org.pmw.tinylog.Logger;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Creates a window for the game.
 *
 * @author Keir Davis
 */
public class Window {

    @Getter
    private long windowID;
    @Getter
    private int width;
    @Getter
    private int height;
    @Getter
    private String windowTitle;
    @Getter
    private boolean vSync;

    /**
     * Creates a GLFW window.
     *
     * @param width
     * @param height
     * @param windowTitle
     * @param vSync
     */
    public Window(int width, int height, String windowTitle, boolean vSync) {
        this.width = width;
        this.height = height;
        this.windowTitle = windowTitle;
        this.vSync = vSync;
    }

    public void createWindow() {
        windowID = glfwCreateWindow(width, height, windowTitle, MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowID == MemoryUtil.NULL) {
            throw new RuntimeException("GLFW window could not be created!");
        }

        // TODO: 06/09/2018 Window Callbacks

        glfwMakeContextCurrent(windowID);
        glfwSwapInterval((vSync) ? 1 : 0);
        glfwFocusWindow(windowID);

        centerOnScreen();

        Logger.info("Window Created");
    }

    /**
     * Initialize GLFW
     */
    public void initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("GLFW could not be initialized");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        Logger.info("GLFW Initialized.");
    }

    /**
     * Opens the window.
     */
    public void openWindow() {
        glfwShowWindow(windowID);
    }

    /**
     * Gets whether or not the window should close.
     *
     * @return true/false
     */
    public boolean shouldClose() {
        return glfwWindowShouldClose(windowID);
    }

    public void setWindowTitle(String title) {
        glfwSetWindowTitle(windowID, title);
    }

    /**
     * Called every Fame.
     */
    public void perLoop() {
        glfwSwapBuffers(windowID);
        glfwPollEvents();
    }

    /**
     * Centers the window on the screen.
     */
    private void centerOnScreen() {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowID, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
    }

    /**
     * Destroys the window and ends GLFW.
     */
    public void destory() {
        Callbacks.glfwFreeCallbacks(windowID);
        glfwDestroyWindow(windowID);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
