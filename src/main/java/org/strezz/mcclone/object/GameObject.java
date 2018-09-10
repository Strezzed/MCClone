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
import lombok.Setter;
import org.joml.Vector3f;
import org.strezz.mcclone.render.object.Mesh;

/**
 * Placeholder
 */
public final class GameObject {

    @Getter
    @Setter
    private Vector3f position = new Vector3f();

    @Getter
    @Setter
    private Vector3f rotation = new Vector3f();

    @Getter
    @Setter
    private float scale;

    @Getter
    @Setter
    private Mesh mesh;

    public GameObject(Vector3f position, Vector3f rotation, float scale, Mesh mesh) {
        if (position != null) setPosition(position);
        if (rotation != null) setRotation(rotation);
        setScale(scale);
        setMesh(mesh);
    }

    public GameObject(Vector3f position, Vector3f rotation, Mesh mesh) {
        this(position, rotation, 1.0f, mesh);
    }

    public GameObject(Vector3f position, Mesh mesh) {
        this(position, null, 1.0f, mesh);
    }

    public GameObject(Mesh mesh) {
        this(null, null, 1.0f, mesh);
    }

    public void render() {
        if (mesh != null) {
            if (!mesh.isBuilt()) {
                mesh.build();
            }
            mesh.render();
        }
    }

    public void renderCleanup() {
        if (mesh != null) {
            mesh.cleanup();
        }
    }


}
