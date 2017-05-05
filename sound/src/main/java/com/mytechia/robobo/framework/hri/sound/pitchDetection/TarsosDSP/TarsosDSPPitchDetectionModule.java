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

package com.mytechia.robobo.framework.hri.sound.pitchDetection.TarsosDSP;

import android.content.res.AssetManager;
import android.util.Log;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.LogLvl;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.hri.sound.pitchDetection.APitchDetectionModule;
import com.mytechia.robobo.framework.hri.sound.soundDispatcherModule.ISoundDispatcherModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

/**
 * Implementation of the Robobo pitch detection module using the TarsosDSP library
 */
public class TarsosDSPPitchDetectionModule extends APitchDetectionModule{

    //region VAR

    private PitchProcessor.PitchEstimationAlgorithm algo;
    private PitchProcessor pitchProcessor;
    private ISoundDispatcherModule dispatcherModule;
    private String TAG = "PitchDetectionModule";
    private boolean previous = false;

    private float samplerate = 44100;
    private int buffersize = 2048;
    private int overlap = 0;


    //endregion

    //region IModule methods
    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {
        m = manager;

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
        overlap = Integer.parseInt(properties.getProperty("overlap"));
        m.log( TAG,"Properties loaded: "+samplerate+" "+buffersize+" "+overlap);

        dispatcherModule = manager.getModuleInstance(ISoundDispatcherModule.class);

        algo = PitchProcessor.PitchEstimationAlgorithm.YIN;



        PitchDetectionHandler handler = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                    AudioEvent audioEvent) {
                double pitch = pitchDetectionResult.getPitch();
                //Log.d(TAG,(audioEvent.getTimeStamp() + " " +pitch));

                if (pitch>0){
                    previous = true;

                    notifyPitch(pitch*2);
                }else {
                    if (previous){
                        //Si se deja de detectar mandar un -1 a modo de cierre
                        previous = false;
                        notifyPitch(-1);
                    }
                }
            }
        };

        pitchProcessor =new PitchProcessor(algo, samplerate, buffersize,handler);

        dispatcherModule.addProcessor(pitchProcessor);


    }

    @Override
    public void shutdown() throws InternalErrorException {
        dispatcherModule.removeProcessor(pitchProcessor);
        pitchProcessor.processingFinished();

    }

    @Override
    public String getModuleInfo() {
        return "Pitch detection module";
    }

    @Override
    public String getModuleVersion() {
        return "v0.1";
    }
    //endRegion
}
