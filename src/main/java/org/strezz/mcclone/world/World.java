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

package org.strezz.mcclone.world;

import org.apache.commons.collections4.list.SetUniqueList;
import org.strezz.mcclone.object.GameObject;

import java.util.ArrayList;

public class World {

    private final SetUniqueList<GameObject> objs;

    public World() {
        objs = SetUniqueList.setUniqueList(new ArrayList<>());
    }

    public void addObjectToWorld(GameObject object) {
        objs.add(object);
    }

    public void removeObjectFromWorld(GameObject obj) {
        objs.remove(obj);
    }

    public GameObject[] getObjectsInWorld() {
        return objs.toArray(new GameObject[objs.size()]);
    }

}
