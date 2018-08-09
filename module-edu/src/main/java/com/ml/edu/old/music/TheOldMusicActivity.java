package com.ml.edu.old.music;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

import com.ml.edu.R;

public class TheOldMusicActivity extends AppCompatActivity {

    public static Intent intent(Context context) {
        Intent intent = new Intent(context, TheOldMusicActivity.class);
        return intent;
    }

    private RadioGroup rgOldMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_old_music);
        rgOldMusic = findViewById(R.id.old_rg_tabs_music);
        rgOldMusic.setOnCheckedChangeListener(onCheckedChangeListener);
        rgOldMusic.check(R.id.old_rb_music);
        findViewById(R.id.old_tv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (R.id.old_rb_music == checkedId) {
                SheetListFragment.addOrShow(getSupportFragmentManager(), R.id.old_music_fl_content);
                return;
            }
            if (R.id.old_rb_favorite == checkedId) {
                rgOldMusic.check(R.id.old_rb_music);
            }
        }
    };

}
