package com.mytechia.robobo.framework.sound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.hri.sound.emotionSound.IEmotionSoundModule;
import com.mytechia.robobo.framework.hri.sound.emotionSound.android.AndroidEmotionSoundModule;
import com.mytechia.robobo.framework.service.RoboboServiceHelper;

public class TestActivity extends AppCompatActivity {

    private IEmotionSoundModule soundModule;
    private RoboboManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RoboboServiceHelper serviceHelper = new RoboboServiceHelper(this, new RoboboServiceHelper.Listener() {
            @Override
            public void onRoboboManagerStarted(RoboboManager roboboManaer) {
                manager = roboboManaer;
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
            soundModule = manager.getModuleInstance(IEmotionSoundModule.class);
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }

        soundModule.playSound(IEmotionSoundModule.PURR_SOUND);
        soundModule.playSound(IEmotionSoundModule.PURR_SOUND);

    }
}
