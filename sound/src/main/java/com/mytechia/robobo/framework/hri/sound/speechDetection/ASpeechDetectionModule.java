package com.mytechia.robobo.framework.hri.sound.speechDetection;


import com.mytechia.robobo.framework.RoboboManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public abstract class ASpeechDetectionModule implements ISpeechDetectionModule {

    public HashSet<ISpeechListener> anyListeners;
    public HashMap<String, ISpeechListener> phraselisteners;
    protected RoboboManager m;
    public ASpeechDetectionModule(){
        anyListeners = new HashSet<ISpeechListener>();
        phraselisteners = new HashMap<String, ISpeechListener>();
    }

    @Override
    public void suscribePhrase(ISpeechListener listener, String phrase) {
        phraselisteners.put(phrase, listener);
    }

    @Override
    public void unsuscribePhrase(ISpeechListener listener, String phrase) {
        phraselisteners.remove(phrase, listener);
    }

    @Override
    public void suscribeAny(ISpeechListener listener) {
        anyListeners.add(listener);
    }

    @Override
    public void unsuscribeAll(final ISpeechListener listener) {
        anyListeners.remove(listener);
        if(phraselisteners.containsValue(listener)) {
            phraselisteners.values().removeAll(Collections.singleton(listener)); // remove(listener) would only remove the first one
        }
    }

    //public void suscribe(ISpeechListener listener){ listeners.add(listener); }
    //public void unsuscribe(ISpeechListener listener){ listeners.remove(listener); }

}
