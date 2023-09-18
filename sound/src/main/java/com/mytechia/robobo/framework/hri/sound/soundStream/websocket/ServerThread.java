package com.mytechia.robobo.framework.hri.sound.soundStream.websocket;

import android.os.AsyncTask;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerThread extends AsyncTask<Void, Void, Void> {
    private final int maxQueueLength;
    private volatile SocketChannel channel = null; //The comunication channel
    private LinkedBlockingQueue<byte[]> queue = new LinkedBlockingQueue<byte[]>(); //Queue of images, this is thread-safe

    ServerThread(SocketChannel socketChannel, int queueLength) {
        this.channel = socketChannel;
        this.maxQueueLength=queueLength;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    /**
     * Adds an image to be processed
     *
     * @param image Image to be added
     */
    synchronized void addData(byte[] image) {

        try {
            if (this.queue.size() == maxQueueLength) {
                this.queue.take();
            }
            this.queue.put(image);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * Closes the connection with the client
     */
    synchronized void close() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        channel = null;
        Server.subscribers.remove(this);
    }
}
