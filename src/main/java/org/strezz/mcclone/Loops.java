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

package org.strezz.mcclone;

import lombok.Getter;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.pmw.tinylog.Logger;
import org.strezz.mcclone.game.IGameLogic;
import org.strezz.mcclone.window.Window;

public final class Loops {

    private final long NANOS_PER_SECOND = 1000000000L;
    private final int targetUps = 60;
    private final double deltaTiming = (double) NANOS_PER_SECOND / (double) targetUps;

    private GLCapabilities glCapabilities;

    private Thread gameLoop;
    private Thread renderLoop;

    private boolean running = true;

    private long lastFrameCheck;
    private long lastUpdateCheck;
    private long updateTimer;
    private long frames;
    private long updates;

    @Getter
    private long debugFPS;

    @Getter
    private long debugUPS;

    public void startGame(Window window) {
        gameLoop = new Thread(this::gameLoop, "Game Loop");
        running = true;
        gameLoop.start();

        renderLoop = new Thread(() -> renderLoop(window), "Render Loop");
        renderLoop.start();
    }

    private void gameLoop() {
        Minecraft.mc.getLogicHandler().foreach(IGameLogic::gameInit);
        Logger.info("Starting Game Loop.");
        while (running) {
            long now = System.nanoTime();
            if (now - updateTimer >= deltaTiming) {
                updateTimer = now;
                tick();
            }
        }
        Minecraft.mc.getLogicHandler().foreach(IGameLogic::gameCleanup);
        Logger.info("Finished Game Loop.");
    }

    private void tick() {
        Minecraft.mc.getLogicHandler().foreach(IGameLogic::gameTick);

        updates++;
        long now = System.nanoTime();
        if (now - lastUpdateCheck >= NANOS_PER_SECOND) {
            lastUpdateCheck = now;
            debugUPS = updates;
            updates = 0;
        }
    }

    private void renderLoop(Window window) {
        window.initGLFW();
        window.createWindow();
        window.openWindow();
        glCapabilities = GL.createCapabilities();
        GL11.glClearColor(100f / 255f, 149f / 255f, 237f / 255f, 1f);
        Logger.info("Capabilities created and clear colour set.");
        Minecraft.mc.getLogicHandler().foreach(logic -> logic.renderInit(window));

        Logger.info("Starting Render Loop");
        while (running) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            if (window.shouldClose()) {
                stopGame();
            }
            Minecraft.mc.getLogicHandler().foreach(logic -> logic.renderUpdate(window));

            window.perLoop();

            frames++;
            long now = System.nanoTime();
            if (now - lastFrameCheck >= NANOS_PER_SECOND) {
                lastFrameCheck = now;
                debugFPS = frames;
                frames = 0;
            }
        }
        Minecraft.mc.getLogicHandler().foreach(logic -> logic.renderCleanup(window));
        window.destory();
        Logger.info("Finished Render Loop.");
    }

    private void stopGame() {
        running = false;
    }

}
