package com.mytechia.robobo.framework.hri.sound.noiseMetering;

/**
 * Created by luis on 12/11/2018.
 */

public interface INoiseListener {

    void  onNoise(double spl);

    public void suscribe(INoiseListener listener);

    public void unsuscribe(INoiseListener listener);
}
