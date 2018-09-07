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

package org.strezz.mcclone.object;

import lombok.Getter;
import org.joml.Vector3f;

/**
 * Placeholder
 */
public final class GameObject {

    @Getter
    private final Vector3f position = new Vector3f();
    @Getter
    private final Vector3f rotation = new Vector3f();
    @Getter
    private float scale;
}
