package com.mytechia.robobo.framework.hri.sound.soundStream.websocket;

import java.nio.ByteBuffer;

// TODO: Probably the byte utils here are different for sound than for imgs
public class ByteUtils {
    private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}
