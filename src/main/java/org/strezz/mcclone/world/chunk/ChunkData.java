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

import lombok.Getter;
import org.joml.Vector3i;
import org.pmw.tinylog.Logger;
import org.strezz.mcclone.block.Block;

public class ChunkData {

    public static final int CHUNK_SIZE = 16;

    @Getter
    private final Vector3i chunkPos;
    private final Block[][][] blocks;

    public ChunkData(Vector3i pos) {
        chunkPos = new Vector3i(pos);
        blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
    }

    public static boolean inChunk(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < CHUNK_SIZE && y < CHUNK_SIZE && z < CHUNK_SIZE;
    }

    public void setBlockAt(int x, int y, int z, Block block) {
        if (inChunk(x, y, z)) {
            blocks[x][y][z] = block;
            return;
        }
        Logger.warn("Can't set block: outside of chunk: " + x + ", " + y + ", " + z);
    }

    public Block getBlockAt(int x, int y, int z) {
        if (inChunk(x, y, z)) {
            return blocks[x][y][z];
        }
        return null;
    }


}
