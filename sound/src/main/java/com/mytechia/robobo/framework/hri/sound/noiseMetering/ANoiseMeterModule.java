package com.mytechia.robobo.framework.hri.sound.noiseMetering;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.frequency.IFrequencyModeListener;
import com.mytechia.robobo.framework.power.IPowerModeListener;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Status;

import java.util.HashSet;

/**
 * Created by luis on 12/11/2018.
 */

public abstract class ANoiseMeterModule implements INoiseMeterModule, IFrequencyModeListener, IPowerModeListener {
    private HashSet<INoiseListener> listeners;

    protected RoboboManager m;
    protected IRemoteControlModule remoteControlModule;

    public ANoiseMeterModule(){
        listeners = new HashSet<INoiseListener>();
    }
    public void suscribe(INoiseListener listener){
        listeners.add(listener);
    }
    public void unsuscribe(INoiseListener listener){
        listeners.remove(listener);
    }
    protected void notifyNoiseLevel(double spl){
        for(INoiseListener listener:listeners){
            listener.onNoise(spl);
        }

        if (remoteControlModule != null){
            Status s = new Status("NOISE");
            s.putContents("level", spl +"");
            remoteControlModule.postStatus(s);
        }
    }
}
