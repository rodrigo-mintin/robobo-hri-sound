
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
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.hri.sound.R;
import com.mytechia.robobo.framework.hri.sound.emotionSound.IEmotionSoundModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Command;
import com.mytechia.robobo.framework.remote_control.remotemodule.ICommandExecutor;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * Implementation of the emotion sound module
 */
public class AndroidSoundPoolEmotionSoundModule implements IEmotionSoundModule {

    private IRemoteControlModule remoteControlModule = null;
    private MediaPlayer mediaPlayer;
    SoundPool.Builder sp_builder = new SoundPool.Builder();
    SoundPool soundPool;
    private Context context;
    private String TAG = "AndroidEmotionSound";
    private static Random r = new Random();
    private RoboboManager m;
    private LinkedList<Integer> loadedSounds = new LinkedList<Integer>();
    private HashMap<Integer,Integer> soundMap = new HashMap<>();
    private int maxSoundsLoaded = 10;


    private static int generateRandom(int min, int max) {
        // max - min + 1 will create a number in the range of min and max, including max. If you don´t want to include it, just delete the +1.
        // adding min to it will finally create the number in the range between min and max
        return r.nextInt(max-min+1) + min;
    }
    
    private void playSound(int resId,int id,int cat_id){
        if (manageQueue(id+cat_id)){
            int soundId = soundPool.load(context,resId,1);

            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    soundPool.play(sampleId,1f,1f,1,0,1);
                }
            });
            soundMap.put(id+cat_id,soundId);
        }else {
            soundPool.play(soundMap.get(id + cat_id), 1f, 1f, 1, 0, 1);
        }
    }


    private boolean manageQueue(Integer sound){
        if (loadedSounds.contains(sound)){
            loadedSounds.remove(sound);
            loadedSounds.addFirst(sound);
            return false;
        }else{
            if (loadedSounds.size() < maxSoundsLoaded){
                loadedSounds.add(sound);

            }else {
                int removeid = loadedSounds.removeLast();
                soundPool.unload(soundMap.get(removeid));
                soundMap.put(removeid, null);
                loadedSounds.addFirst(sound);
            }
            return true;
        }

    }

    @Override
    public void playSound(final int sound) {
        int soundId = 0;
        int id = generateRandom(0,2);
        int cat_id = 0;
        switch (sound){

            //00
            case ANGRY_SOUND:
                id = generateRandom(0,2);
                cat_id = 0;
                switch (id){
                    case 0:
                        playSound(R.raw.angry01_r01,id,cat_id);
                        break;
                    case 1:
                        playSound(R.raw.angry02_r01,id,cat_id);
                        break;
                    case 2:
                        playSound(R.raw.angry03_r01,id,cat_id);
                        break;

                }

                break;
            //01
            case APPROVE_SOUND:
                id = generateRandom(0,2);
                cat_id = 100;
                switch (id){
                    case 0:
                        playSound(R.raw.approve01_r01,id,cat_id);
                        break;
                    case 1:
                        playSound(R.raw.approve02_r01,id,cat_id);
                        break;
                    case 2:
                        playSound(R.raw.approve03_r01,id,cat_id);
                        break;

                }

                break;
            //02
            case DISAPPROVE_SOUND:
                id = generateRandom(0,5);
                cat_id = 200;
                switch (id){
                    case 0:
                        playSound(R.raw.disapprove01_r01,id,cat_id);
                        break;
                    case 1:
                            playSound(R.raw.disapprove02_r01,id,cat_id);

                        break;
                    case 2:
                            playSound(R.raw.disapprove03_r01,id,cat_id);

                        break;
                    case 3:
                         playSound(R.raw.disapprove04_r01,id,cat_id);

                        break;
                    case 4:
                         playSound(R.raw.disapprove05_r01,id,cat_id);
                        break;
                    case 5:
                         playSound(R.raw.disapprove06_r01,id,cat_id);

                        break;
                }

                break;
            //03
            case DISCOMFORT_SOUND:
                id = generateRandom(0,1);
                cat_id = 300;
                switch (id){
                    case 0:
                         playSound(R.raw.discomfort01_r01,id,cat_id);
                        break;
                    case 1:
                         playSound(R.raw.discomfort02_r01,id,cat_id);
                        break;
                }

                break;
            //04
            case DOUBTFUL_SOUND:
                id = generateRandom(0,1);
                cat_id = 400;
                switch (id){
                    case 0:
                         playSound(R.raw.doubtful01_r01,id,cat_id);
                        break;
                    case 1:
                         playSound(R.raw.doubtful02_r01,id,cat_id);
                        break;
                }

                break;
            //05
            case LAUGH_SOUND:
                id = generateRandom(0,7);
                cat_id = 500;
                switch (id){
                    case 0:
                         playSound(R.raw.laugh01_r01,id,cat_id);
                        break;
                    case 1:
                         playSound(R.raw.laugh02_r01,id,cat_id);
                        break;
                    case 2:
                         playSound(R.raw.laugh03_r01,id,cat_id);
                        break;
                    case 3:
                         playSound(R.raw.laugh04_r01,id,cat_id);
                        break;
                    case 4:
                         playSound(R.raw.laugh05_r01,id,cat_id);
                        break;
                    case 5:
                         playSound(R.raw.laugh06_r01,id,cat_id);
                        break;
                    case 6:
                         playSound(R.raw.laugh07_r01,id,cat_id);
                        break;
                    case 7:
                         playSound(R.raw.laugh08_r01,id,cat_id);
                        break;
                }

                break;
            //06
            case LIKES_SOUND:
                id = 0;
                cat_id = 600;
                 playSound(R.raw.likes01_r01,id,cat_id);
                break;
            //07
            case MOAN_SOUND:
                id = generateRandom(0,3);
                cat_id = 700;
                switch (id){
                    case 0:
                         playSound(R.raw.moan01_r01,id,cat_id);
                        break;
                    case 1:
                         playSound(R.raw.moan02_r01,id,cat_id);
                        break;
                    case 2:
                         playSound(R.raw.moan03_r01,id,cat_id);
                        break;
                    case 3:
                         playSound(R.raw.moan04_r01,id,cat_id);
                        break;
                }

                break;
            //08
            case MUMBLE_SOUND:
                id = generateRandom(0,6);
                cat_id = 800;
                switch (id){
                    case 0:
                         playSound(R.raw.mumble01_r01,id,cat_id);
                        break;
                    case 1:
                         playSound(R.raw.mumble02_r01,id,cat_id);
                        break;
                    case 2:
                         playSound(R.raw.mumble03_r01,id,cat_id);
                        break;
                    case 3:
                         playSound(R.raw.mumble04_r01,id,cat_id);
                        break;
                    case 4:
                         playSound(R.raw.mumble05_r01,id,cat_id);
                        break;
                    case 5:
                         playSound(R.raw.mumble06_r01,id,cat_id);
                        break;
                    case 6:
                         playSound(R.raw.mumble07_r01,id,cat_id);
                        break;

                }

                break;
            //09
            case OUCH_SOUND:
                id = generateRandom(0,3);
                cat_id = 900;
                switch (id){
                    case 0:
                         playSound(R.raw.ouch01_r01,id,cat_id);
                        break;
                    case 1:
                         playSound(R.raw.ouch02_r01,id,cat_id);
                        break;
                    case 2:
                         playSound(R.raw.ouch03_r01,id,cat_id);
                        break;
                    case 3:
                         playSound(R.raw.ouch04_r01,id,cat_id);
                        break;
                }

                break;
            //10
            case PURR_SOUND:
                id = generateRandom(0,2);
                cat_id = 1000;
                switch (id){
                    case 0:
                         playSound(R.raw.purring01_r01,id,cat_id);
                        break;
                    case 1:
                         playSound(R.raw.purring02_r01,id,cat_id);
                        break;
                    case 2:
                         playSound(R.raw.purring03_r01,id,cat_id);
                        break;

                }
                break;
            //11
            case THINKING_SOUND:
                id = generateRandom(0,3);
                cat_id = 1100;
                switch (id){
                    case 0:
                         playSound(R.raw.thinking_01_r01,id,cat_id);
                        break;
                    case 1:
                         playSound(R.raw.thinking_02_r01,id,cat_id);
                        break;
                    case 2:
                         playSound(R.raw.thinking_ok01_r01,id,cat_id);
                        break;
                    case 3:
                         playSound(R.raw.thinking_ok02_r01,id,cat_id);
                        break;

                }

                break;
        }

    }

    @Override
    public void startup(RoboboManager manager) {
        m = manager;
        sp_builder.setMaxStreams(5);
        soundPool = sp_builder.build();
        try {
            remoteControlModule = manager.getModuleInstance(IRemoteControlModule.class);
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }

        if (remoteControlModule!=null){
            remoteControlModule.registerCommand("PLAY-SOUND", new ICommandExecutor() {
                @Override
                public void executeCommand(Command c, IRemoteControlModule rcmodule) {
                    m.log(TAG,c.toString());
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
