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

package com.mytechia.robobo.framework.hri.sound.soundDispatcherModule;

import com.mytechia.robobo.framework.IModule;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;


/**
 * A sound dispatcher module is in charge of capturing audio and dispatching
 * it to all the modules that require audio input.
 *
 * Any sound dispatcher module must implement this interface.
 *
 */
public interface ISoundDispatcherModule extends IModule {
    /**
     * Adds a sound processor to the list of active processors
     * @param processor The processor
     */
    public void addProcessor(AudioProcessor processor);

    /**
     * Removes a sound processor from the list of active processors
     * @param processor The processor
     */
    public void removeProcessor(AudioProcessor processor);

    /**
     * Starts the sound dispatcher
     */
    public void runDispatcher();

    /**
     * Stops the sound dispatcher
     */
    public void stopDispatcher();

    /**
     * Returns the dispatcher object
     * @return the dispatcher
     */
    public AudioDispatcher getDispatcher();
}
