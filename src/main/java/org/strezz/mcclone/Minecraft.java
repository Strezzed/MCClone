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
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.FileWriter;
import org.strezz.mcclone.game.GameLogicHandler;
import org.strezz.mcclone.window.Window;

public final class Minecraft {

    public static Minecraft mc;

    @Getter
    private Window window;

    @Getter
    private GameLogicHandler logicHandler;

    @Getter
    private Loops loops;

    public static void main(String[] args) {
        createLogger(true, true);
        Logger.info("Starting Minecraft");
        mc = new Minecraft();
        mc.start();
        Logger.info("Game Ended!");
    }

    private static void createLogger(boolean fileLogging, boolean consoleLogging) {
        if (!fileLogging && !consoleLogging) {
            return;
        }

        final Configurator logConfigurator = Configurator.defaultConfig();
        logConfigurator.removeAllWriters();

        if (fileLogging) {
            logConfigurator.addWriter(new FileWriter("game.log"));
        }

        if (consoleLogging) {
            logConfigurator.addWriter(new ConsoleWriter());
        }

        logConfigurator.formatPattern("[MC] {date:dd-MM-YYYY HH:mm:ss} [{thread}] {level}: {message}");
        logConfigurator.activate();
    }

    private void start() {
        logicHandler = new GameLogicHandler();
        logicHandler.loadLogic();

        window = new Window(1280, 720, "", false);
        loops = new Loops();
        loops.startGame(window);
    }

    public void stopGame() {
        loops.stopGame();
    }


}
