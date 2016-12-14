package com.lag.connectfour.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lag.connectfour.R;
import com.lag.connectfour.game.GameBoardActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LauncherActivity extends AppCompatActivity {

    @BindView(R.id.activity_launcher)
    View mContentView;
    @BindView(R.id.start_image_view)
    ImageView startImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);

        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse_animation);
        startImageView.startAnimation(pulse);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        onWindowFocusChanged(true);
    }

    @OnClick(R.id.start_image_view)
    public void launchGame() {
        Intent intent = new Intent(this, GameBoardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mContentView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }
}
