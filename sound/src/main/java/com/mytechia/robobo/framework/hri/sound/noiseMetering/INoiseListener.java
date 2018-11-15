package com.mytechia.robobo.framework.hri.sound.noiseMetering;


/**
 * Interface to be notified on noise level data
 */
public interface INoiseListener {

    /**
     * Called on noise level data available
     * @param spl Sound pressure level
     */
    public void  onNoise(double spl);


}
