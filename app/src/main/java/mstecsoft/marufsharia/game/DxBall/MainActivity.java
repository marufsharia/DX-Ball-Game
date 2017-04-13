package mstecsoft.marufsharia.game.DxBall;
/*
Created by marufsharia on 12/24/2016.
 */

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        player = MediaPlayer.create(getApplicationContext(), R.raw.menubg);
        player.setLooping(true); // Set looping
        player.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.stop();
    }

    public void onClickPlay(View view) {
        player.stop();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new Level1(this));
    }

    public void onClickAbout(View view) {
        Toast.makeText(getApplicationContext(), "Dx-Ball Game\nversion: 1.0.0\nMarufSharia", Toast.LENGTH_SHORT).show();
    }

    public void onClickExit(View view) {
        player.stop();
        this.finish();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.stop();
        this.finish();
        System.exit(0);

    }
}
