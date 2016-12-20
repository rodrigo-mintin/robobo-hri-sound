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

package com.mytechia.robobo.framework.hri.sound.noteDetection;

import com.mytechia.robobo.framework.hri.sound.pitchDetection.IPitchListener;

import java.util.HashSet;

/**
 * Abstract class that manages the listener notifications
 */
public abstract class ANoteDetectionModule implements INoteDetectionModule {
    private HashSet<INoteListener> listeners;



    public ANoteDetectionModule(){
        listeners = new HashSet<INoteListener>();
    }
    public void suscribe(INoteListener listener){
        listeners.add(listener);
    }
    public void unsuscribe(INoteListener listener){
        listeners.remove(listener);
    }

    /**
     * Notifies when an note is being played
     * @param note The note being played
     */
    protected void notifyNote(Note note){
        for(INoteListener listener:listeners){
            listener.onNoteDetected(note);

        }
    }

    /**
     * Notifies when a note stops playing
     * @param note The note
     * @param time The time elapsed by the note
     */
    protected void notifyNoteEnd(Note note, long time){
        for(INoteListener listener:listeners){
            listener.onNoteEnd(note, time);

        }
    }

    /**
     * Notifies qhen a note starts playing
     * @param note The note
     */
    protected void notifyNewNote(Note note){
        for(INoteListener listener:listeners){
            listener.onNewNote(note);

        }
    }

    /**
     * Converts frequency into note index
     * @param freq The frequency to be converted
     * @return the index of the note
     */
    protected double freqToNote(double freq){

        //freq = 440* 2^(n/12)
        //http://www.intmath.com/trigonometric-graphs/music.php
        double note = 0.0;

        double noteaprox = (Math.log(freq/220.0)/Math.log(2))*12.0;

        return noteaprox;
    }
}
