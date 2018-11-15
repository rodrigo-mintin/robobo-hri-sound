/*******************************************************************************
 *
 *   Copyright 2016 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 *   Copyright 2016 Luis Llamas <luis.llamas@mytechia.com>
 *
 *   This file is part of Robobo HRI Modules.
 *
 *   Robobo HRI Modules is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Robobo HRI Modules is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Robobo HRI Modules.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package com.mytechia.robobo.framework.hri.sound.clapDetection;

import android.util.Log;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Status;

import java.util.HashSet;



/**
 * Abstract class that manages listeners and posting status
 */
public abstract class AClapDetectionModule implements IClapDetectionModule {
    private HashSet<IClapListener> listeners;
    protected IRemoteControlModule remoteModule = null;
    protected RoboboManager m;

    public AClapDetectionModule(){
        // Initialize listener list
        listeners = new HashSet<IClapListener>();
    }

    @Override
    public void suscribe(IClapListener listener) {
        m.log("FD_module", "Suscribed:"+listener.toString());
        listeners.add(listener);
    }

    @Override
    public void unsuscribe(IClapListener listener) {
        listeners.remove(listener);
    }


    /**
     * Notify the listeners of a clap
     * @param time Moment of the detection
     */
    protected void notifyClap(double time){
        // Notify all the listeners of the event
        for(IClapListener listener:listeners){
            listener.onClap(time);
        }

        // Send status via remote module
        if (remoteModule!=null) {
            Status status = new Status("CLAP");
            remoteModule.postStatus(status);
        }
    }
}
