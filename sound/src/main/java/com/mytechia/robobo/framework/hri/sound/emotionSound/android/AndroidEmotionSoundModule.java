
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

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.hri.sound.R;
import com.mytechia.robobo.framework.hri.sound.emotionSound.IEmotionSoundModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Command;
import com.mytechia.robobo.framework.remote_control.remotemodule.ICommandExecutor;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;

import java.util.Random;

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


            case ANGRY_SOUND:
                switch (generateRandom(0,2)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.angry01_r01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.angry02_r01);
                        mediaPlayer.start();
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.angry03_r01);
                        mediaPlayer.start();
                        break;

                }

                break;

            case APPROVE_SOUND:
                switch (generateRandom(0,2)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.approve01_r01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.approve02_r01);
                        mediaPlayer.start();
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.approve03_r01);
                        mediaPlayer.start();
                        break;

                }

                break;

            case DISAPPROVE_SOUND:
                switch (generateRandom(0,5)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.disapprove01_r01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.disapprove02_r01);
                        mediaPlayer.start();
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.disapprove03_r01);
                        mediaPlayer.start();
                        break;
                    case 3:
                        mediaPlayer = MediaPlayer.create(context, R.raw.disapprove04_r01);
                        mediaPlayer.start();
                        break;
                    case 4:
                        mediaPlayer = MediaPlayer.create(context, R.raw.disapprove05_r01);
                        mediaPlayer.start();
                        break;
                    case 5:
                        mediaPlayer = MediaPlayer.create(context, R.raw.disapprove06_r01);
                        mediaPlayer.start();
                        break;
                }

                break;

            case DISCOMFORT_SOUND:
                switch (generateRandom(0,1)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.discomfort01_r01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.discomfort02_r01);
                        mediaPlayer.start();
                        break;
                }

                break;
            case DOUBTFUL_SOUND:
                switch (generateRandom(0,1)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.doubtful01_r01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.doubtful02_r01);
                        mediaPlayer.start();
                        break;
                }

                break;

            case LAUGH_SOUND:
                switch (generateRandom(0,7)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.laugh01_r01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.laugh02_r01);
                        mediaPlayer.start();
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.laugh03_r01);
                        mediaPlayer.start();
                        break;
                    case 3:
                        mediaPlayer = MediaPlayer.create(context, R.raw.laugh04_r01);
                        mediaPlayer.start();
                        break;
                    case 4:
                        mediaPlayer = MediaPlayer.create(context, R.raw.laugh05_r01);
                        mediaPlayer.start();
                        break;
                    case 5:
                        mediaPlayer = MediaPlayer.create(context, R.raw.laugh06_r01);
                        mediaPlayer.start();
                        break;
                    case 6:
                        mediaPlayer = MediaPlayer.create(context, R.raw.laugh07_r01);
                        mediaPlayer.start();
                        break;
                    case 7:
                        mediaPlayer = MediaPlayer.create(context, R.raw.laugh08_r01);
                        mediaPlayer.start();
                        break;
                }

                break;

            case LIKES_SOUND:

                    mediaPlayer = MediaPlayer.create(context, R.raw.likes01_r01);
                    mediaPlayer.start();

            case MOAN_SOUND:
                switch (generateRandom(0,3)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.moan01_r01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.moan02_r01);
                        mediaPlayer.start();
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.moan03_r01);
                        mediaPlayer.start();
                        break;
                    case 3:
                        mediaPlayer = MediaPlayer.create(context, R.raw.moan04_r01);
                        mediaPlayer.start();
                        break;
                }

                break;

            case MUMBLE_SOUND:
                switch (generateRandom(0,6)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.mumble01_r01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.mumble02_r01);
                        mediaPlayer.start();
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.mumble03_r01);
                        mediaPlayer.start();
                        break;
                    case 3:
                        mediaPlayer = MediaPlayer.create(context, R.raw.mumble04_r01);
                        mediaPlayer.start();
                        break;
                    case 4:
                        mediaPlayer = MediaPlayer.create(context, R.raw.mumble05_r01);
                        mediaPlayer.start();
                        break;
                    case 5:
                        mediaPlayer = MediaPlayer.create(context, R.raw.mumble06_r01);
                        mediaPlayer.start();
                        break;
                    case 6:
                        mediaPlayer = MediaPlayer.create(context, R.raw.mumble07_r01);
                        mediaPlayer.start();
                        break;

                }

                break;

            case OUCH_SOUND:
                switch (generateRandom(0,3)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.ouch01_r01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.ouch02_r01);
                        mediaPlayer.start();
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.ouch03_r01);
                        mediaPlayer.start();
                        break;
                    case 3:
                        mediaPlayer = MediaPlayer.create(context, R.raw.ouch04_r01);
                        mediaPlayer.start();
                        break;
                }

                break;

            case PURR_SOUND:
                switch (generateRandom(0,2)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.purring01_r01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.purring02_r01);
                        mediaPlayer.start();
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.purring03_r01);
                        mediaPlayer.start();
                        break;

                }
                break;

            case THINKING_SOUND:
                switch (generateRandom(0,3)){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.thinking_01_r01);
                        mediaPlayer.start();
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.thinking_02_r01);
                        mediaPlayer.start();
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.thinking_ok01_r01);
                        mediaPlayer.start();
                        break;
                    case 3:
                        mediaPlayer = MediaPlayer.create(context, R.raw.thinking_ok02_r01);
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
                        case "angry":
                            playSound(ANGRY_SOUND);
                            break;
                        case "approve":
                            playSound(APPROVE_SOUND);
                            break;
                        case "disapprove":
                            playSound(DISAPPROVE_SOUND);
                            break;
                        case "discomfort":
                            playSound(DISCOMFORT_SOUND);
                            break;
                        case "doubtful":
                            playSound(DOUBTFUL_SOUND);
                            break;
                        case "laugh":
                            playSound(LAUGH_SOUND);
                            break;
                        case "likes":
                            playSound(LIKES_SOUND);
                            break;
                        case "mumble":
                            playSound(MUMBLE_SOUND);
                            break;
                        case "ouch":
                            playSound(OUCH_SOUND);
                            break;
                        case "thinking":
                            playSound(THINKING_SOUND);
                            break;
                        case "various":
                            playSound(VARIOUS_SOUND);
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
