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

package org.strezz.mcclone.game;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.pmw.tinylog.Logger;
import org.strezz.mcclone.Minecraft;
import org.strezz.mcclone.block.Blocks;
import org.strezz.mcclone.input.InputHandler;
import org.strezz.mcclone.object.GameObject;
import org.strezz.mcclone.render.Camera;
import org.strezz.mcclone.render.Renderer;
import org.strezz.mcclone.render.object.CameraMovement;
import org.strezz.mcclone.utils.IconUtils;
import org.strezz.mcclone.utils.MathsUtils;
import org.strezz.mcclone.window.Window;
import org.strezz.mcclone.world.World;
import org.strezz.mcclone.world.chunk.ChunkData;
import org.strezz.mcclone.world.chunk.ChunkGenerator;
import org.strezz.mcclone.world.chunk.MeshChunk;

import java.text.NumberFormat;

public class GameLogicCore implements IGameLogic {

    private final float cameraSpeed = 0.1f;
    private final float cameraRotateSpeed = 0.5f;
    private float light = 0.8f;

    private InputHandler input;
    private Renderer renderer;
    private Camera camera;
    private CameraMovement cameraMovement;
    private World world;

    public GameLogicCore() {
        world = new World();
    }

    @Override
    public void gameInit() {
        Blocks.init();
        camera = new Camera();
        camera.setPosition(0.0f, 10.0f, 1.5f);
        ChunkData test = new ChunkData(new Vector3i());
        ChunkGenerator.generate(test);
        world.addObjectToWorld(new GameObject(new Vector3f(0.0f, 0.0f, 0.0f), MeshChunk.buildChunkMesh(test)));
    }

    @Override
    public void gameTick() {
        if (cameraMovement != null) {
            Vector3f move = new Vector3f(cameraMovement.getCamChange());
            camera.move(move.x * cameraSpeed, move.y * cameraSpeed, move.z * cameraSpeed);
        }

        Logger.info(String.format("x:%s | y:%s | z:%s", camera.getPosition().x, camera.getPosition().y, camera.getPosition().z));

        if (input != null) {
            input.gameInit();


            if (input.keyPressed(GLFW.GLFW_KEY_UP)) {
                light += 0.01f;
            } else if (input.keyPressed(GLFW.GLFW_KEY_DOWN)) {
                light -= 0.01f;
            }
            light = MathsUtils.clamp(light, 0.0f, 1.0f);
        }

    }

    @Override
    public void gameCleanup() {

    }

    @Override
    public void renderInit(Window window) {
        Logger.info("");
        Logger.info("Minecraft Version:" + "1.0");
        Logger.info("LWJGL Version:" + Version.getVersion());
        Logger.info("OpenGL:" + GL11.glGetString(GL11.GL_VERSION));
        Logger.info("");

        try {
            IconUtils.setIcon("minecraft/textures/misc/icon.png", window);
        } catch (Exception e) {
            e.printStackTrace();
        }

        renderer = new Renderer();
        renderer.init();
        input = new InputHandler();
        input.renderInit(window);
        cameraMovement = new CameraMovement(camera, cameraRotateSpeed);
        cameraMovement.init(window);
    }

    @Override
    public void renderUpdate(Window window) {
        if (input.keyUp(GLFW.GLFW_KEY_ESCAPE)) {
            Minecraft.mc.stopGame();
        }

        if (input.keyUp(GLFW.GLFW_KEY_P)) {
            GLFW.glfwSetInputMode(window.getWindowID(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }

        GameObject[] objs = world.getObjectsInWorld();
        for (GameObject obj : objs) {
            renderer.render(window, camera, obj, light);
        }

        if (input.keyPressed(GLFW.GLFW_KEY_F)) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);

        } else {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }

        window.setWindowTitle(buildWindowTitle(window));

        cameraMovement.render(window, input);

    }

    @Override
    public void renderCleanup(Window window) {
        GameObject[] objs = world.getObjectsInWorld();
        for (GameObject obj : objs) {
            obj.renderCleanup();
        }
    }

    private String buildWindowTitle(Window window) {
        StringBuilder sb = new StringBuilder();
        sb.append("Minecraft v1.0 | UPS: ");
        sb.append(NumberFormat.getInstance().format(Minecraft.mc.getLoops().getDebugUPS()) + " ");
        sb.append("| FPS: ");
        sb.append(NumberFormat.getInstance().format(Minecraft.mc.getLoops().getDebugFPS()) + " ");
        sb.append("| ");
        sb.append(NumberFormat.getInstance().format(window.getWidth()));
        sb.append("x");
        sb.append(NumberFormat.getInstance().format(window.getHeight()));
        return sb.toString();
    }
}
