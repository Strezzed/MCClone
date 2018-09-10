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

package org.strezz.mcclone.render;

import de.matthiasmann.twl.utils.PNGDecoder;
import lombok.Getter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.strezz.mcclone.io.Resources;

import java.io.InputStream;
import java.nio.ByteBuffer;

public final class Texture {

    private String location;

    @Getter
    private int id;

    @Getter
    private boolean loaded = false;

    public Texture(String location) {
        this.location = location;
    }

    public void loadTexture() {
        if (!loaded) {
            loaded = true;
            try {
                InputStream stream = Resources.getResource(location);
                if (stream != null) {
                    PNGDecoder decoder = new PNGDecoder(stream);

                    ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
                    decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
                    buffer.flip();

                    id = GL11.glGenTextures();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
                    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
                    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
