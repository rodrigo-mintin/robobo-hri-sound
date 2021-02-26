package com.mytechia.robobo.framework.hri.sound.speechDetection.Vosk;

import android.content.res.AssetManager;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.LogLvl;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.hri.sound.speechDetection.ASpeechDetectionModule;
import com.mytechia.robobo.framework.hri.sound.speechDetection.ISpeechListener;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import org.kaldi.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Implementation of the Robobo speech detection module using the Vosk-Android library
 */
public class VoskSpeechDetectionModule extends ASpeechDetectionModule implements RecognitionListener {
    private Model model;
    private SpeechService speechService;
    private KaldiRecognizer recognizer;
    private int samplerate = 11025;

    private String TAG = "SpeechModule";

    @Override
    public void startup(RoboboManager manager) throws InternalErrorException {

        m = manager;
        // Load propreties from file
        Properties properties = new Properties();
        AssetManager assetManager = manager.getApplicationContext().getAssets();

        try {
            InputStream inputStream = assetManager.open("sound.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        samplerate = Integer.parseInt(properties.getProperty("samplerate"));
        model = new Model(properties.getProperty("speechModelPath"));      // ?
        recognizer = new KaldiRecognizer(model, samplerate);

        try {
            speechService = new SpeechService(recognizer, samplerate);
            speechService.addListener(this);
            speechService.startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void shutdown() throws InternalErrorException {
        if (speechService != null) {
            speechService.cancel();
            speechService.shutdown();
        }
    }

    @Override
    public String getModuleInfo() {
        return "Speech detection module";
    }

    @Override
    public String getModuleVersion() {
        return "v0.1";
    }

    @Override
    public void onPartialResult(String s) {
        //processResult(s);
    }

    @Override
    public void onResult(String s) {
        processResult(s);
    }

    private void processResult(String s){
        //Check better iteration options
        m.log(LogLvl.DEBUG, TAG,"message processed: " + s);
        for (String key : phraselisteners.keySet()) {
            if(s.contains(key)){
                phraselisteners.get(key).onResult(s);
            }
        }

       for (ISpeechListener l: anyListeners){
           l.onResult(s);
       }
    }

    @Override
    public void onError(Exception e) {
        m.log(LogLvl.ERROR,TAG,e.getMessage());
    }

    @Override
    public void onTimeout() {
        //Check for debug

        m.log(LogLvl.TRACE, TAG,"Vosk Recognizer timed out. Restarting. Time: " + System.currentTimeMillis());
        speechService.cancel();
        try {
            speechService = new SpeechService(recognizer, samplerate);
            speechService.addListener(this);
            speechService.startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
