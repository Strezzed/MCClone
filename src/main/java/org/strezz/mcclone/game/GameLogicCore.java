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

import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;
import org.pmw.tinylog.Logger;
import org.strezz.mcclone.Minecraft;
import org.strezz.mcclone.window.Window;

import java.text.NumberFormat;

public class GameLogicCore implements IGameLogic {


    @Override
    public void gameInit() {

    }

    @Override
    public void gameTick() {

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

    }

    @Override
    public void renderUpdate(Window window) {
        window.setWindowTitle(buildWindowTitle(window));
    }

    @Override
    public void renderCleanup(Window window) {

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
