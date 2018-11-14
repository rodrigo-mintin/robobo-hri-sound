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
import android.util.Log;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.frequency.FrequencyMode;
import com.mytechia.robobo.framework.hri.sound.noiseMetering.ANoiseMeterModule;
import com.mytechia.robobo.framework.hri.sound.noiseMetering.INoiseMeterModule;
import com.mytechia.robobo.framework.hri.sound.soundDispatcherModule.ISoundDispatcherModule;
import com.mytechia.robobo.framework.power.PowerMode;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.Oscilloscope;
import be.tarsos.dsp.SilenceDetector;


public class TarsosDSPNoiseMeterModule extends ANoiseMeterModule {
    //region VAR

    private RoboboManager m;
    private ISoundDispatcherModule dispatcherModule;
    private String TAG = "NoiseMeteringModule";
    private boolean previous = false;

    private float samplerate = 11025;
    private int buffersize = 1024;
    private int overlap = 0;

    private static final long FREQUENCY_LOW = 500;
    private static final long FREQUENCY_NORMAL = 200;
    private static final long FREQUENCY_FAST = 50;
    private static final long FREQUENCY_MAX = 10;
    private Timer t;
    private TimerTask task;
    private long currentFreq = FREQUENCY_NORMAL;
    private SilenceDetector silenceDetector;

    @Override
    public void startup(RoboboManager manager)  {
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

        try {
            remoteControlModule = manager.getModuleInstance(IRemoteControlModule.class);
            dispatcherModule = manager.getModuleInstance(ISoundDispatcherModule.class);

        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }

        final SilenceDetector sd = new SilenceDetector(0, false);
        silenceDetector = sd;
        dispatcherModule.addProcessor(sd);


        t = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                double currentSpl = sd.currentSPL();
                Log.d(TAG, "SPL: "+currentSpl);
                notifyNoiseLevel(currentSpl);
            }
        };

        t.scheduleAtFixedRate(task,3000, FREQUENCY_NORMAL);

    }

    @Override
    public void shutdown() throws InternalErrorException {
        t.cancel();
        t.purge();
        dispatcherModule.removeProcessor(silenceDetector);
    }

    @Override
    public String getModuleInfo() {
        return "Noise Metering Module";
    }

    @Override
    public String getModuleVersion() {
        return "v1";
    }

    @Override
    public void onFrequencyModeChanged(FrequencyMode frequency) {
        switch (frequency){
                        case LOW:
                                this.setMaxRemoteNotificationPeriod(FREQUENCY_LOW);
                                currentFreq = FREQUENCY_LOW;
                            break;
                        case NORMAL:
                                this.setMaxRemoteNotificationPeriod(FREQUENCY_NORMAL);
                                currentFreq = FREQUENCY_NORMAL;
                            break;
                        case FAST:
                                this.setMaxRemoteNotificationPeriod(FREQUENCY_FAST);
                                currentFreq = FREQUENCY_FAST;
                            break;
                        case MAX:
                                this.setMaxRemoteNotificationPeriod(FREQUENCY_MAX);
                                currentFreq = FREQUENCY_MAX;
                            break;
                    }
    }

    private void setMaxRemoteNotificationPeriod(long period){
        t.cancel();
        t.purge();
        t.scheduleAtFixedRate(task,0, period);
    }

    @Override
    public void onPowerModeChange(PowerMode newMode) {
        switch (newMode){
            case NORMAL:
                t.cancel();
                t.purge();
                t.scheduleAtFixedRate(task,0, currentFreq);
                break;
            case LOWPOWER:
                t.cancel();
                t.purge();
                break;
        }
    }
}
