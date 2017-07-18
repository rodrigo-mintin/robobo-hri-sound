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

package com.mytechia.robobo.framework.hri.sound.noteGeneration.android;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.LogLvl;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.hri.sound.noteDetection.INoteDetectionModule;
import com.mytechia.robobo.framework.hri.sound.noteGeneration.ANoteGeneratorModule;
import com.mytechia.robobo.framework.hri.sound.noteGeneration.INoteGeneratorModule;
import com.mytechia.robobo.framework.hri.sound.noteGeneration.Note;
import com.mytechia.robobo.framework.remote_control.remotemodule.Command;
import com.mytechia.robobo.framework.remote_control.remotemodule.ICommandExecutor;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Implementation of the robobo Note generation module
 */
public class AndroidNoteGenerationModule extends ANoteGeneratorModule {
    private String TAG = "NoteGeneratorModule";
    private LinkedList<SeqNote> sequence;
    private Timer timer;
    private TimerTask waitTask;
    private boolean playingNote;
    AudioTrack lasttone = null;

    @Override
    public void playNote(Note note, int timems) {

        m.log(LogLvl.TRACE, TAG,"Playing note: "+note.toString());
        if (lasttone!=null){
            lasttone.release();
        }

        AudioTrack tone = generateTone(noteToFreq(note),timems);
        tone.play();
        lasttone = tone;



    }

    private void playNoteByIndex(int index, int timems){

        m.log(LogLvl.TRACE, TAG,"Playing note, index: "+index);
        if (lasttone!=null){
            lasttone.release();
        }

        AudioTrack tone = generateTone(indexToFreq(index),timems);
        tone.play();
        lasttone = tone;
    }

    @Override
    public void addNoteToSequence(Note note, int timems) {

            sequence.add(new SeqNote(note,timems));


    }

    @Override
    public void playSequence() {

        try {
            SeqNote sn = sequence.pop();
            m.log(LogLvl.TRACE, TAG,sn.toString());
            playNote(sn.note,sn.timems);
            waitTask = new WaitTask();

            timer.schedule(waitTask,sn.timems);
        }catch (NoSuchElementException e){
            m.log(LogLvl.TRACE, TAG,"END SEQUENCE");
            notifySequenceEnd();
        }
    }

    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {
        sequence = new LinkedList<>();
        m = manager;
        timer = new Timer();

        m.getModuleInstance(IRemoteControlModule.class).registerCommand("PLAYNOTE", new ICommandExecutor() {
            @Override
            public void executeCommand(Command c, IRemoteControlModule rcmodule) {
                playNoteByIndex(Integer.parseInt(c.getParameters().get("index"))-57,Integer.parseInt(c.getParameters().get("time")));
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

    //region AudioGeneration

    private AudioTrack generateTone(double freqHz, int durationMs)
    {
        int count = (int)(44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];
        for(int i = 0; i < count; i += 2){
            short sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            samples[i + 0] = sample;
            samples[i + 1] = sample;
        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);
        track.write(samples, 0, count);
        return track;
    }


    private double noteToFreq(Note note){
        double freq = 220* Math.pow(2,(note.index/12.0));
        m.log(LogLvl.TRACE, TAG,"Index =" +note.index+"Freq: " +freq);
        return freq;

    }

    private double indexToFreq(int index){
        double freq = 220* Math.pow(2,(index/12.0));
        m.log(LogLvl.TRACE, TAG,"Index =" +index+"Freq: " +freq);
        return freq;

    }
    //endregion

    //region SequenceManager

    private class SeqNote{
        Note note;
        int timems;
        SeqNote(Note n,int time ){
            note = n;
            timems = time;
        }

        @Override
        public String toString() {
            return "SeqNote{" +
                    "note=" + note +
                    ", timems=" + timems +
                    '}';
        }
    }

    private class WaitTask extends TimerTask {



        @Override
        public void run() {
            playSequence();
        }
    }

    //endregion
}
