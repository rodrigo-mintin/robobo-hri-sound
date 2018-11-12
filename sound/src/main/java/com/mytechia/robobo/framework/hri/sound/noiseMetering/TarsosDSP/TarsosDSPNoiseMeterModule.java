/*******************************************************************************
 *
 *   Copyright 2018 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 *   Copyright 2018 Luis Llamas <luis.llamas@mytechia.com>
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
package com.mytechia.robobo.framework.hri.sound.noiseMetering.TarsosDSP;

import android.content.res.AssetManager;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.hri.sound.noiseMetering.INoiseMeterModule;
import com.mytechia.robobo.framework.hri.sound.soundDispatcherModule.ISoundDispatcherModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.Oscilloscope;


public class TarsosDSPNoiseMeterModule implements INoiseMeterModule {
    //region VAR

    private RoboboManager m;
    private ISoundDispatcherModule dispatcherModule;
    private String TAG = "PitchDetectionModule";
    private boolean previous = false;

    private float samplerate = 11025;
    private int buffersize = 1024;
    private int overlap = 0;


    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {
        m = manager;

        Properties properties = new Properties();
        AssetManager assetManager = m.getApplicationContext().getAssets();

        try {
            InputStream inputStream = assetManager.open("sound.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        samplerate = Integer.parseInt(properties.getProperty("samplerate"));
        buffersize = Integer.parseInt(properties.getProperty("buffersize"));
        overlap = Integer.parseInt(properties.getProperty("overlap"));
        m.log( TAG,"Properties loaded: "+samplerate+" "+buffersize+" "+overlap);

        dispatcherModule = manager.getModuleInstance(ISoundDispatcherModule.class);

        Oscilloscope o = new Oscilloscope(new Oscilloscope.OscilloscopeEventHandler() {
            @Override
            public void handleEvent(float[] floats, AudioEvent audioEvent) {

            }
        });

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
