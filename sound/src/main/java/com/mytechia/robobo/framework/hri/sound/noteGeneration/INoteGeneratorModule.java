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

package com.mytechia.robobo.framework.hri.sound.noteGeneration;

import com.mytechia.robobo.framework.IModule;



public interface INoteGeneratorModule extends IModule {

    /** Subscribes the listener to new note detection events
     *
     * @param listener the listener to receive new note detection events
     */
    void suscribe(INotePlayListener listener);

    /** Unsubscribes the listener from note detection events
     *
     * @param listener the listener to unsubscribe
     */
    void unsuscribe(INotePlayListener listener);

    /**
     * Plays a note
     * @param note the note to be played
     * @param timems the duration of the note
     */
    void playNote(Note note,int timems);

    /**
     * Add a note to the internal sequence
     * @param note the note to be added to the sequence
     * @param timems the duration of the note
     */
    void addNoteToSequence(Note note,int timems);

    /**
     * Plays the internal sequence
     */
    void playSequence();

}
