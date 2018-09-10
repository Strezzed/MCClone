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

import org.strezz.mcclone.block.Blocks;

public class ChunkGenerator {

    public static void generate(ChunkData chunk) {
        for (int y = 0; y < ChunkData.CHUNK_SIZE; y++) {
            for (int z = 0; z < ChunkData.CHUNK_SIZE; z++) {
                chunk.setBlockAt(y, 1, z, Blocks.blockStone);

            }
        }

    }

}
