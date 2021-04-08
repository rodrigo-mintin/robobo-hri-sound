package com.mytechia.robobo.framework.hri.sound.speechDetection.Vosk.TarsosDSP;

import android.content.res.AssetManager;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.LogLvl;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.hri.sound.soundDispatcherModule.ISoundDispatcherModule;
import com.mytechia.robobo.framework.hri.sound.speechDetection.ASpeechDetectionModule;
import com.mytechia.robobo.framework.hri.sound.speechDetection.ISpeechDetectionModule;
import com.mytechia.robobo.framework.hri.sound.speechDetection.ISpeechListener;
import com.mytechia.robobo.framework.hri.sound.speechDetection.Vosk.VoskSpeechDetectionModule;

import org.json.JSONException;
import org.json.JSONObject;
import org.kaldi.Assets;
import org.kaldi.KaldiRecognizer;
import org.kaldi.Model;
import org.kaldi.RecognitionListener;
import org.kaldi.SpeechService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TarsosDSPVoskSpeechDetectionModule extends ASpeechDetectionModule implements RecognitionListener {

    private String TAG = "TarsosDSPSpeechModule";

    private Model model;
    private TarsosDSPSpeechService speechService;
    private KaldiRecognizer recognizer;
    private ISoundDispatcherModule dispatcherModule;
    private float samplerate = 16000.f;

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

        m.log(LogLvl.DEBUG, TAG,"Properties loaded");

        samplerate = Float.parseFloat(properties.getProperty("model_samplerate"));
        m.log(LogLvl.DEBUG, TAG,":   samplerate");


        try{

            dispatcherModule = manager.getModuleInstance(ISoundDispatcherModule.class);

            ///
            Assets assets = new Assets(m.getApplicationContext());
            File assetDir = assets.syncAssets();

            model = new Model(assetDir.toString() + "/model-android");
            m.log(LogLvl.DEBUG, TAG,":   model");
            ///


            recognizer = new KaldiRecognizer(model, samplerate);
            m.log(LogLvl.DEBUG, TAG,"Recognizer loaded");

            m.log(LogLvl.DEBUG, TAG,"Model loaded");
            soundServiceStartup();
            m.log(LogLvl.DEBUG, TAG,"Sound service loaded");

        } catch(IOException e) {
            e.printStackTrace();
        } catch(ModuleNotFoundException e) {
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
        return "Tarsus DSP Speech detection module";
    }

    @Override
    public String getModuleVersion() {
        return "v0.1";
    }


    private void soundServiceStartup(){
        try {
            speechService = new TarsosDSPSpeechService(recognizer, samplerate, dispatcherModule);
            speechService.addListener(this);
            speechService.startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isWordInModel(String word) {
        return (model.vosk_model_find_word(word) > 0);
    }

    @Override
    public void onPartialResult(String s) {

    }

    @Override
    public void onResult(String s) {
        processResult(s);
    }

    private void processResult(String s){
        //Check better iteration options

        //m.log(LogLvl.DEBUG, TAG, "Json String received: " + s);
        try {

            JSONObject jsonObject = new JSONObject(s);
            if((jsonObject.has("text") && !(jsonObject.getString("text").equals("")))) {
                String message =  jsonObject.getString("text");

                m.log(LogLvl.DEBUG, TAG, "message processed: " + message);

                for (String key : phraselisteners.keySet()) {
                    if (s.contains(key)) {
                        phraselisteners.get(key).onResult(message);
                    }
                }

                for (ISpeechListener l : anyListeners) {
                    l.onResult(message);
                }

            }

        } catch (JSONException e) {
            m.log(LogLvl.ERROR, TAG,"Couldnt Process Vosk JSON object: " + e.getMessage());
        }
    }


    @Override
    public void onError(Exception e) {
        m.log(LogLvl.ERROR,TAG,e.getMessage());
    }

    @Override
    public void onTimeout() {
        //Check for debug

        m.log(LogLvl.TRACE, TAG, "Vosk Recognizer timed out. Restarting. Time: " + System.currentTimeMillis());
        speechService.cancel();
        soundServiceStartup();

    }
}
