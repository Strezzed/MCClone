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

import org.pmw.tinylog.Logger;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameLogicHandler {

    private final List<IGameLogic> gameLogic;

    public GameLogicHandler() {
        gameLogic = new ArrayList<>();
    }

    public void loadLogic() {
        gameLogic.clear();
        Reflections reflections = new Reflections();
        Set<Class<? extends IGameLogic>> logicClass = reflections.getSubTypesOf(IGameLogic.class);
        for (Class<? extends IGameLogic> lc : logicClass) {
            addClass(lc);
        }
        Logger.info("Loaded " + gameLogic.size() + " logic modules.");
    }

    public void foreach(Call each) {
        for (IGameLogic logic : gameLogic) {
            each.call(logic);
        }
    }

    private void addClass(Class<? extends IGameLogic> glc) {
        try {
            IGameLogic iGameLogic = glc.newInstance();
            if (iGameLogic != null && iGameLogic instanceof IGameLogic) {
                addLogic((IGameLogic) iGameLogic);
                return;
            }
            Logger.info("Couldn't instantiate Game Logic: " + glc.getName());
        } catch (Exception e) {
            Logger.info("Couldn't add Game Logic: " + glc.getName());
        }
    }

    private void addLogic(IGameLogic iGameLogic) {
        gameLogic.add(iGameLogic);
    }

    @FunctionalInterface
    public static interface Call {
        void call(IGameLogic logic);
    }

}
