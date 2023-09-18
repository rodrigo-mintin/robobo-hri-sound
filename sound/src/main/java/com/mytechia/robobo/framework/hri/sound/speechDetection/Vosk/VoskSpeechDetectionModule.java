package com.mytechia.robobo.framework.hri.sound.speechDetection.Vosk;

import android.content.res.AssetManager;
import android.net.Uri;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.LogLvl;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.hri.sound.speechDetection.ASpeechDetectionModule;
import com.mytechia.robobo.framework.hri.sound.speechDetection.ISpeechListener;
import com.mytechia.robobo.framework.remote_control.remotemodule.Command;
import com.mytechia.robobo.framework.remote_control.remotemodule.ICommandExecutor;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Status;

import org.json.JSONException;
import org.json.JSONObject;
import org.kaldi.*;

import java.io.File;
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
    private float samplerate = 16000.f;

    private String TAG = "SpeechModule";

    private boolean doDetection = true;

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

        // Load remote module
        try {
            remoteModule = manager.getModuleInstance(IRemoteControlModule.class);
        } catch (ModuleNotFoundException e) {
            remoteModule = null;
            e.printStackTrace();
        }

        if (remoteModule!=null){
            registerCommands();
        }


        m.log(LogLvl.DEBUG, TAG,"Properties loaded");

        samplerate = Float.parseFloat(properties.getProperty("model_samplerate"));
        m.log(LogLvl.DEBUG, TAG,":   samplerate");


        try{

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
        }
    }

    private void soundServiceStartup(){
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
        if (!doDetection)
            return;

        try {

            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.has("text")) {
                String message =  jsonObject.getString("text");

                m.log(LogLvl.DEBUG, TAG, "message processed: " + message);

                for (String key : phraselisteners.keySet()) {
                    if (s.contains(key)) {
                        phraselisteners.get(key).onResult(message);
                    }
                }

                for (   ISpeechListener l : anyListeners) {
                    l.onResult(message);
                }

                // Send status via remote module
                if (remoteModule!=null) {
                    Status status = new Status("SpeechDetector::"+message);
                    remoteModule.postStatus(status);
                }


            }

        }catch (JSONException e) {
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

    @Override
    public boolean isWordInModel(String word) {
        return (model.vosk_model_find_word(word) > 0);
    }
}
