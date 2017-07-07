package com.mytechia.robobo.framework.sound;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.hri.sound.emotionSound.IEmotionSoundModule;
import com.mytechia.robobo.framework.hri.sound.noteDetection.INoteDetectionModule;
import com.mytechia.robobo.framework.hri.sound.pitchDetection.IPitchDetectionModule;
import com.mytechia.robobo.framework.hri.sound.soundDispatcherModule.ISoundDispatcherModule;
import com.mytechia.robobo.framework.service.RoboboServiceHelper;

public class TestActivity extends AppCompatActivity {

    private IEmotionSoundModule soundModule;
    private ISoundDispatcherModule dispatcherModule;
    private IPitchDetectionModule pitchDetectionModule;
    private INoteDetectionModule noteDetectionModule;
    private RoboboManager manager;

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
            public void onError(String errorMsg) {

            }
        });
        Bundle options = new Bundle();
        serviceHelper.bindRoboboService(options);

    }


    public void startapp(){
        try {

                dispatcherModule = manager.getModuleInstance(ISoundDispatcherModule.class);
//                pitchDetectionModule = manager.getModuleInstance(IPitchDetectionModule.class);
//                noteDetectionModule = manager.getModuleInstance(INoteDetectionModule.class);
                dispatcherModule.runDispatcher();



        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }


    }
}
