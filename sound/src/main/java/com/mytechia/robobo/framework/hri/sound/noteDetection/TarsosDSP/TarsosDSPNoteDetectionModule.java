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

package com.mytechia.robobo.framework.hri.sound.noteDetection.TarsosDSP;

import android.content.res.AssetManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.LogLvl;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.hri.sound.noteDetection.ANoteDetectionModule;
import com.mytechia.robobo.framework.hri.sound.noteDetection.Note;
import com.mytechia.robobo.framework.hri.sound.pitchDetection.IPitchDetectionModule;
import com.mytechia.robobo.framework.hri.sound.pitchDetection.IPitchListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Implementation of the Robobo note detection module
 */
public class TarsosDSPNoteDetectionModule extends ANoteDetectionModule implements IPitchListener{

    private IPitchDetectionModule pitchDetectionModule;

    private String TAG = "NoteModule";

    //The thresholds for considering a pitch a well tuned note
    private double minThreshold = 0.1;
    private double maxThreshold = 0.9;

    //Start and end of the note play
    private long startTime;
    private long endTime;

    private Note lastNote;

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
        minThreshold = Double.parseDouble(properties.getProperty("minThreshold"));
        maxThreshold = Double.parseDouble(properties.getProperty("maxThreshold"));


        pitchDetectionModule = manager.getModuleInstance(IPitchDetectionModule.class);
        pitchDetectionModule.suscribe(this);
    }

    @Override
    public void shutdown() throws InternalErrorException {
       pitchDetectionModule.unsuscribe(this);
    }

    @Override
    public String getModuleInfo() {
        return "TarsosDsp Notedetection Module";
    }

    @Override
    public String getModuleVersion() {
        return "v0.1";
    }


    //endregion

    //region PitchListener methods
    @Override
    public void onPitchdetected(double freq) {
        //If freq == -1 and the last note is not null, that means that note stopped playing
        if (freq==-1){

            if (lastNote!=null){
                endTime = System.currentTimeMillis();
                notifyNoteEnd(lastNote,endTime-startTime);
                m.log(LogLvl.TRACE, TAG,"ENDNOTE "+lastNote.toString()+" Time elapsed:"+(endTime-startTime)+" ms");
                lastNote = null;
            }


        }else{

            double doubleNoteIndex = freqToNote(freq);
            int noteindex = (int) Math.round(doubleNoteIndex);

            //The difference to the nearest note index
            double diffValue = Math.abs(Math.abs(doubleNoteIndex)-Math.abs(Math.round(doubleNoteIndex)));

            //Log.d(TAG,"Diff: "+diffValue+" Index: "+doubleNoteIndex);
            //Filter by threshold
            if ((diffValue<minThreshold)||(diffValue>maxThreshold)) {
                //Log.d(TAG, "Index: " + doubleNoteIndex + " freq: " + freq);
                Note note = null;
                for (Note note1 : Note.values()) {
                    //Get the note from the index
                    if (note1.index == noteindex) {

                        if(note1 != lastNote){

                            if (lastNote!=null){
                                //m.log(LogLvl.TRACE, TAG,"lastNote!=null");
                                endTime = System.currentTimeMillis();
                                notifyNoteEnd(lastNote,endTime-startTime);
                                m.log(LogLvl.TRACE, TAG,"ENDNOTE "+lastNote.toString()+" Time elapsed:"+(endTime-startTime)+" ms");
                            }
                            m.log(LogLvl.TRACE, TAG,"NEWNOTE "+note1.toString());
                            notifyNewNote(note1);
                            startTime = System.currentTimeMillis();

                            lastNote = note1;
                        }
                        notifyNote(note1);
                    }
                }
            }
//            }else{
//                //TODO Probar esto
//                Log.d(TAG,"Fallo en diffvalue Lastnote: "+lastNote.toString());
//                endTime = System.currentTimeMillis();
//                notifyNoteEnd(lastNote,endTime-startTime);
//                lastNote = null;
//
//            }
        }
    }
    //endregion
}
