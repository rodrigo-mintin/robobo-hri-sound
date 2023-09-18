package com.mytechia.robobo.framework.hri.sound.soundStream.websocket;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.hri.sound.soundStream.ASoundStreamModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Command;
import com.mytechia.robobo.framework.remote_control.remotemodule.ICommandExecutor;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO: Maybe needs to implement some kind of microphone listener
public class AndroidMicrophoneSoundStreamModule extends ASoundStreamModule  {
    private static final String TAG = "AndroidMicrophoneSoundStreamModule";
    static final int QUEUE_LENGTH = 60;

    IRemoteControlModule rcmodule;
    //Queue

    // TODO: We aren't working in frames now, but maybe sound has some kind of analogous packet
    // FPS control variables
    private long lastFrameTime = 0;
    private long deltaTimeThreshold = 17;

    private int sync_id = -1;

    private boolean processing = false;

    ExecutorService executor;
    Server server;

    @Override
    public void setBitrate(int bitrate) {

    }

    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {
        m = manager;
        executor = Executors.newFixedThreadPool(1);

        // TODO: We don't need frame events as with camera, just get the sound stream and react to thresholds

        server = new Server(QUEUE_LENGTH);
        server.start();
        // Uncomment to start with the module active
        //cameraModule.suscribe(this);
    }

    @Override
    public void shutdown() throws InternalErrorException {

    }

    @Override
    public String getModuleInfo() {
        return null;
    }

    @Override
    public String getModuleVersion() {
        return null;
    }
}
