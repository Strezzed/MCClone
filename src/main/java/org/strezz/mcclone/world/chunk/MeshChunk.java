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

package org.strezz.mcclone.world.chunk;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.strezz.mcclone.block.Block;
import org.strezz.mcclone.render.Texture;
import org.strezz.mcclone.render.object.Mesh;

import java.util.ArrayList;
import java.util.List;

public class MeshChunk {

    private static final Vector3f up = new Vector3f(0, 1, 0);
    private static final Vector3f right = new Vector3f(1, 0, 0);
    private static final Vector3f forward = new Vector3f(0, 0, 1);

    public static Mesh buildChunkMesh(ChunkData chunk) {
        List<Vector3f> verts = new ArrayList<>();
        List<Integer> tris = new ArrayList<>();
        List<Vector2f> uvs = new ArrayList<>();

        for (int x = 0; x < ChunkData.CHUNK_SIZE; x++) {
            for (int y = 0; y < ChunkData.CHUNK_SIZE; y++) {
                for (int z = 0; z < ChunkData.CHUNK_SIZE; z++) {
                    Block at = chunk.getBlockAt(x, y, z);
                    if (at == null) {
                        continue;
                    }
                    addBlock(at, x, y, z, chunk, verts, tris, uvs);
                }
            }
        }

        float[] outVerts = new float[verts.size() * 3];
        int[] outTris = new int[tris.size()];
        float[] outUvs = new float[uvs.size() * 2];

        int i = 0;
        for (Vector3f f : verts) {
            outVerts[i * 3] = f.x;
            outVerts[i * 3 + 1] = f.y;
            outVerts[i * 3 + 2] = f.z;
            i++;
        }

        i = 0;
        for (Integer f : tris) {
            outTris[i] = f;
            i++;
        }

        i = 0;
        for (Vector2f f : uvs) {
            outUvs[i * 2] = f.x;
            outUvs[i * 2 + 1] = f.y;
            i++;
        }

        return new Mesh(outVerts, outTris, outUvs, new Texture("minecraft:textures/blocks/block_stone.png"));
    }

    private static void addBlock(Block block, int x, int y, int z, ChunkData chunk, List<Vector3f> verts, List<Integer> tris, List<Vector2f> uvs) {
        if (fullBlockAt(chunk, x - 1, y, z)) {
            addFace(block, new Vector3f(x, y, z), up, forward, false, verts, tris, uvs);        // Left
        }
        if (fullBlockAt(chunk, x + 1, y, z)) {
            addFace(block, new Vector3f(x + 1, y, z), up, forward, true, verts, tris, uvs);        // Right
        }

        if (fullBlockAt(chunk, x, y - 1, z)) {
            addFace(block, new Vector3f(x, y, z), forward, right, false, verts, tris, uvs);        // Bottom
        }
        if (fullBlockAt(chunk, x, y + 1, z)) {
            addFace(block, new Vector3f(x, y + 1, z), forward, right, true, verts, tris, uvs);    // Top
        }

        if (fullBlockAt(chunk, x, y, z - 1)) {
            addFace(block, new Vector3f(x, y, z), up, right, true, verts, tris, uvs);            // Front
        }
        if (fullBlockAt(chunk, x, y, z + 1)) {
            addFace(block, new Vector3f(x, y, z + 1), up, right, false, verts, tris, uvs);        // Back
        }
    }


    private static void addFace(Block block, Vector3f corner, Vector3f up, Vector3f right, boolean rev, List<Vector3f> verts, List<Integer> tris, List<Vector2f> uvs) {
        int index = verts.size();

        Vector3f v0 = new Vector3f(corner);
        Vector3f v1 = new Vector3f(corner).add(up);
        Vector3f v2 = new Vector3f(corner).add(up).add(right);
        Vector3f v3 = new Vector3f(corner).add(right);

        verts.add(v0);
        verts.add(v1);
        verts.add(v2);
        verts.add(v3);

        if (rev) {
            tris.add(index + 0);
            tris.add(index + 1);
            tris.add(index + 2);
            tris.add(index + 2);
            tris.add(index + 3);
            tris.add(index + 0);
        } else {
            tris.add(index + 1);
            tris.add(index + 0);
            tris.add(index + 2);
            tris.add(index + 3);
            tris.add(index + 2);
            tris.add(index + 0);
        }

        uvs.add(new Vector2f(0.0f, 0.0f));
        uvs.add(new Vector2f(0.0f, 1.0f));
        uvs.add(new Vector2f(1.0f, 1.0f));
        uvs.add(new Vector2f(1.0f, 0.0f));
    }

    private static boolean fullBlockAt(ChunkData chunk, int x, int y, int z) {
        if (!ChunkData.inChunk(x, y, z)) {
            return true;
        }
        Block b = chunk.getBlockAt(x, y, z);

        if (b == null) {
            return true;
        }
        return b.isFullBlock();
    }

}
