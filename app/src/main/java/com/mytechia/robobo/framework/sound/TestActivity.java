package com.mytechia.robobo.framework.sound;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.hri.sound.emotionSound.IEmotionSoundModule;
import com.mytechia.robobo.framework.hri.sound.noiseMetering.INoiseMeterModule;
import com.mytechia.robobo.framework.hri.sound.noteDetection.INoteDetectionModule;
import com.mytechia.robobo.framework.hri.sound.pitchDetection.IPitchDetectionModule;
import com.mytechia.robobo.framework.hri.sound.soundDispatcherModule.ISoundDispatcherModule;
import com.mytechia.robobo.framework.hri.sound.speechDetection.ISpeechDetectionModule;
import com.mytechia.robobo.framework.hri.sound.speechDetection.ISpeechListener;
import com.mytechia.robobo.framework.service.RoboboServiceHelper;

import org.w3c.dom.Text;

public class TestActivity extends AppCompatActivity implements ISpeechListener {

    //private IEmotionSoundModule soundModule;
    private ISoundDispatcherModule dispatcherModule;
    private ISpeechDetectionModule speechDetectionModule;
    //private INoteDetectionModule noteDetectionModule;
    //private INoiseMeterModule noiseMeterModule;
    private RoboboManager manager;
    private EditText phraseInput;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RoboboServiceHelper serviceHelper = new RoboboServiceHelper(this, new RoboboServiceHelper.Listener() {
            @Override
            public void onRoboboManagerStarted(RoboboManager roboboManager) {
                manager = roboboManager;
                startapp();


            }

            @Override
            public void onError(Throwable ex) {

            }
        });
        Bundle options = new Bundle();
        serviceHelper.bindRoboboService(options);

    }


    public void startapp(){
        try {

            dispatcherModule = manager.getModuleInstance(ISoundDispatcherModule.class);
            //pitchDetectionModule = manager.getModuleInstance(IPitchDetectionModule.class);
            speechDetectionModule = manager.getModuleInstance(ISpeechDetectionModule.class);
            //noteDetectionModule = manager.getModuleInstance(INoteDetectionModule.class);

            //noiseMeterModule = manager.getModuleInstance(INoiseMeterModule.class);
            //dispatcherModule.runDispatcher();
            phraseInput = (EditText) findViewById(R.id.detectorTextView);
            textView = (TextView) findViewById(R.id.textView);;
            Log.d("TEST","TEST");



        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void onListenToAnyBtn(View v) {
        speechDetectionModule.suscribeAny(this);
        Toast.makeText(manager.getApplicationContext(), "Listening to ALL phrases (Reset to stop)", Toast.LENGTH_SHORT).show();
    }

    public void onAddToPhrasesBtn(View v){
        boolean success = true;
        String phrase = phraseInput.getText().toString().toLowerCase();
        String[] wordlist = phrase.split(" ");
        for(String word : wordlist) {
            if(!speechDetectionModule.isWordInModel(word)){
               Toast.makeText(manager.getApplicationContext(), "Word " + word + " is not included in the Speech Model", Toast.LENGTH_SHORT).show();
                success = false;
                break;
            }
        }
        if(success){
            speechDetectionModule.suscribePhrase(this, phrase);
        }
    }

    public void onResetBtn(View v) {
        speechDetectionModule.unsuscribeAll(this);
        textView.setText("");
        Toast.makeText(manager.getApplicationContext(), "Subscriptions reset!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResult(String s) {
        String phrase = s;
        textView.append("Phrase detected: "+ phrase + "\n");
    }
}
