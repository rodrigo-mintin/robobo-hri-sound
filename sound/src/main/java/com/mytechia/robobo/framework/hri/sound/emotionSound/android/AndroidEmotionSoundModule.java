
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

package com.mytechia.robobo.framework.hri.sound.emotionSound.android;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceView;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.hri.sound.R;
import com.mytechia.robobo.framework.hri.sound.emotionSound.IEmotionSoundModule;
import com.mytechia.robobo.framework.hri.sound.soundDispatcherModule.ISoundDispatcherModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Command;
import com.mytechia.robobo.framework.remote_control.remotemodule.ICommandExecutor;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;

import java.util.Random;
import java.util.TimerTask;

/**
 * Implementation of the emotion sound module
 */
public class AndroidEmotionSoundModule implements IEmotionSoundModule {

    private IRemoteControlModule remoteControlModule = null;
    private MediaPlayer mediaPlayer;
    private Context context;
    private String TAG = "AndroidEmotionSound";
    private static Random r = new Random();


    private static int generateRandom(int min, int max) {
        // max - min + 1 will create a number in the range of min and max, including max. If you don´t want to include it, just delete the +1.
        // adding min to it will finally create the number in the range between min and max
        return r.nextInt(max-min+1) + min;
    }

    @Override
    public void playSound(int sound) {
        switch (sound){


            case MOAN_SOUND:
                switch (generateRandom(0,3)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.moan01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.moan02);
                        mediaPlayer.start();
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.moan03);
                        mediaPlayer.start();
                        break;
                    case 3:
                        mediaPlayer = MediaPlayer.create(context, R.raw.moan04);
                        mediaPlayer.start();
                        break;
                }

                break;

            case PURR_SOUND:
                switch (generateRandom(0,2)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.purring01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.purring02);
                        mediaPlayer.start();
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.purring03);
                        mediaPlayer.start();
                        break;

                }
                break;
        }

    }

    @Override
    public void startup(RoboboManager manager) {

        try {
            remoteControlModule = manager.getModuleInstance(IRemoteControlModule.class);
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }

        if (remoteControlModule!=null){
            remoteControlModule.registerCommand("SOUND", new ICommandExecutor() {
                @Override
                public void executeCommand(Command c, IRemoteControlModule rcmodule) {
                    Log.d(TAG,c.toString());
                    switch (c.getParameters().get("sound")) {
                        case "moan":
                            playSound(MOAN_SOUND);
                            break;
                        case "purr":
                            playSound(PURR_SOUND);
                            break;
                    }
                }
            });
        }
        context = manager.getApplicationContext();

    }

    @Override
    public void shutdown() throws InternalErrorException {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

    }

    @Override
    public String getModuleInfo() {
        return "Emotion Sound Module";
    }

    @Override
    public String getModuleVersion() {
        return "v0.1";
    }



}
