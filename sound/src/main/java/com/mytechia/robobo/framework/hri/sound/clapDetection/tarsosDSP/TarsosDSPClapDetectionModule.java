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

package com.mytechia.robobo.framework.hri.sound.clapDetection.tarsosDSP;

import android.content.res.AssetManager;
import android.util.Log;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.commons.util.thread.Threads;
import com.mytechia.robobo.framework.LogLvl;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.hri.sound.clapDetection.AClapDetectionModule;
import com.mytechia.robobo.framework.hri.sound.soundDispatcherModule.ISoundDispatcherModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.onsets.PercussionOnsetDetector;
import be.tarsos.dsp.AudioDispatcher;



/**
 * Implementation of the ROBOBO clap detection module
 */
public class TarsosDSPClapDetectionModule extends AClapDetectionModule {

    //region VAR

    private String TAG = "TarsosClapModule";

    private PercussionOnsetDetector mPercussionDetector;

    private ISoundDispatcherModule dispatcherModule;

    private double threshold = 8;

    private double sensitivity = 20;

    private float samplerate = 44100;

    private int buffersize = 2048;

    //endregion

    //region IModule methods
    @Override
    public void startup(RoboboManager manager) {
        m = manager;
        try {
            dispatcherModule = manager.getModuleInstance(ISoundDispatcherModule.class);
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }
        Properties properties = new Properties();
        AssetManager assetManager = manager.getApplicationContext().getAssets();

        try {
            InputStream inputStream = assetManager.open("sound.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        samplerate = Integer.parseInt(properties.getProperty("samplerate"));
        buffersize = Integer.parseInt(properties.getProperty("buffersize"));
        sensitivity = Integer.parseInt(properties.getProperty("clap_sensitivity"));
        threshold = Integer.parseInt(properties.getProperty("clap_threshold"));
        try {
            remoteModule = manager.getModuleInstance(IRemoteControlModule.class);
        } catch (ModuleNotFoundException e) {
            remoteModule = null;
            e.printStackTrace();
        }

        m.log(TAG,"Properties loaded: "+samplerate+" "+buffersize);




        mPercussionDetector = new PercussionOnsetDetector(samplerate, buffersize,
                new OnsetHandler() {

                    @Override
                    public void handleOnset(double time, double salience) {
                        m.log(LogLvl.TRACE, TAG, "Clap detected!");
                        notifyClap(time);
                    }
                }, sensitivity, threshold);


        dispatcherModule.addProcessor(mPercussionDetector);
    }

    @Override
    public void shutdown() throws InternalErrorException {
        dispatcherModule.removeProcessor(mPercussionDetector);
        mPercussionDetector.processingFinished();

    }

    @Override
    public String getModuleInfo() {
        return "Clap detection Module";
    }

    @Override
    public String getModuleVersion() {
        return "v0.1";
    }
    //endregion


}
