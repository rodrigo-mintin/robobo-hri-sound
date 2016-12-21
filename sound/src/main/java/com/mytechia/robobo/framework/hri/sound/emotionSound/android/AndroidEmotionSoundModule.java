
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

import java.util.TimerTask;

/**
 * Implementation of the emotion sound module
 */
public class AndroidEmotionSoundModule implements IEmotionSoundModule {

    private IRemoteControlModule remoteControlModule = null;
    private MediaPlayer mediaPlayer;
    private Context context;
    private String TAG = "AndroidEmotionSound";

    @Override
    public void playSound(int sound) {
        switch (sound){
            case ALERT_SOUND:

                mediaPlayer = MediaPlayer.create(context, R.raw.alerta);
                mediaPlayer.start();
                break;
            case CLAPS_SOUND:

                mediaPlayer = MediaPlayer.create(context, R.raw.clapping);
                mediaPlayer.start();
                break;
            case BOOOOO_SOUND:

                mediaPlayer = MediaPlayer.create(context, R.raw.boo);
                mediaPlayer.start();
                break;
            case LAUGH_SOUND:

                mediaPlayer = MediaPlayer.create(context, R.raw.laugh);
                mediaPlayer.start();
                break;
            case ALARM_SOUND:

                mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
                mediaPlayer.start();
                break;
            case RIMSHOT_SOUND:

                mediaPlayer = MediaPlayer.create(context, R.raw.rimshot);
                mediaPlayer.start();
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
                        case "alert":
                            playSound(ALERT_SOUND);
                            break;
                        case "claps":
                            playSound(CLAPS_SOUND);
                            break;
                        case "booooo":
                            playSound(BOOOOO_SOUND);
                            break;
                        case "laugh":
                            playSound(LAUGH_SOUND);
                            break;
                        case "alarm":
                            playSound(ALARM_SOUND);
                            break;
                        case "rimshot":
                            playSound(RIMSHOT_SOUND);
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
