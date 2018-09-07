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

package org.strezz.mcclone.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.strezz.mcclone.window.Window;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memFree;

public class IconUtils {

    public static void setIcon(String path, Window window) throws Exception {
        IntBuffer w = memAllocInt(1);
        IntBuffer h = memAllocInt(1);
        IntBuffer comp = memAllocInt(1);

        ByteBuffer icon16;
        ByteBuffer icon32;
        try {
            icon16 = ioResourceToByteBuffer(path, 2048);
            icon32 = ioResourceToByteBuffer(path, 4096);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (GLFWImage.Buffer icons = GLFWImage.malloc(2)) {
            ByteBuffer pixels16 = STBImage.stbi_load_from_memory(icon16, w, h, comp, 4);
            icons.position(0).width(w.get(0)).height(h.get(0)).pixels(pixels16);

            ByteBuffer pixels32 = STBImage.stbi_load_from_memory(icon32, w, h, comp, 4);
            icons.position(1).width(w.get(0)).height(h.get(0)).pixels(pixels32);
            icons.position(0);
            GLFW.glfwSetWindowIcon(window.getWindowID(), icons);

            STBImage.stbi_image_free(pixels32);
            STBImage.stbi_image_free(pixels16);
        }

        memFree(comp);
        memFree(h);
        memFree(w);

    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    private static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) ;
            }
        } else {
            try (InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource); ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1)
                        break;
                    if (buffer.remaining() == 0)
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                }
            }
        }

        buffer.flip();
        return buffer;
    }

}
