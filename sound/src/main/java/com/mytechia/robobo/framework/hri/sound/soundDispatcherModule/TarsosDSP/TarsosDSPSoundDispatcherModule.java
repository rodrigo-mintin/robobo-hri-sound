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
package com.mytechia.robobo.framework.hri.sound.soundDispatcherModule.TarsosDSP;

import android.content.res.AssetManager;
import android.util.Log;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.hri.sound.soundDispatcherModule.ISoundDispatcherModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;


/**
 * Implementation of the Robobo sound dispatcher module using the TarsosDSP library
 */
public class TarsosDSPSoundDispatcherModule implements ISoundDispatcherModule {

    private String TAG = "TarsosDispatcherModule";
    private AudioDispatcher dispatcher;
    private Thread dispatcherThread;

    private int samplerate = 44100;

    private int buffersize = 2048;

    private int overlap = 0;

    private RoboboManager m;

    //region SoundDispatcherModule methods
    @Override
    public void addProcessor(AudioProcessor processor) {
        dispatcher.addAudioProcessor(processor);

    }

    @Override
    public void removeProcessor(AudioProcessor processor) {
        dispatcher.removeAudioProcessor(processor);
    }

    @Override
    public void runDispatcher() {
        dispatcherThread = new Thread(dispatcher);
        dispatcherThread.run();
    }

    @Override
    public void stopDispatcher() {

        dispatcher.stop();
        dispatcherThread.interrupt();
    }

    @Override
    public AudioDispatcher getDispatcher() {
        return dispatcher;
    }
    //endregion

    //region IModule Methods
    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {
        Properties properties = new Properties();
        AssetManager assetManager = manager.getApplicationContext().getAssets();
        m = manager;
        try {
            InputStream inputStream = assetManager.open("sound.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        samplerate = Integer.parseInt(properties.getProperty("samplerate"));
        buffersize = Integer.parseInt(properties.getProperty("buffersize"));
        overlap = Integer.parseInt(properties.getProperty("overlap"));
        m.log(TAG,"Properties loaded: "+samplerate+" "+buffersize+" "+overlap);
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(samplerate,buffersize,overlap);

    }

    @Override
    public void shutdown() throws InternalErrorException {
        stopDispatcher();
    }

    @Override
    public String getModuleInfo() {
        return "Audio dispatcher module";
    }

    @Override
    public String getModuleVersion() {
        return "v0.1";
    }
    //endregion
}
