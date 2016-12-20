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
package com.mytechia.robobo.framework.hri.sound.emotionSound;

import com.mytechia.robobo.framework.IModule;


/**
 * Interface of the emotion sound module
 */
public interface IEmotionSoundModule extends IModule {

    int ALERT_SOUND = 0;
    int CLAPS_SOUND = 1;
    int BOOOOO_SOUND = 2;
    int LAUGH_SOUND = 3;
    int ALARM_SOUND = 4;
    int RIMSHOT_SOUND = 5;

    /**
     * Plays a prefixed emotion sound
     * @param Sound  The sound to be played
     */
    void playSound(int Sound);
}
