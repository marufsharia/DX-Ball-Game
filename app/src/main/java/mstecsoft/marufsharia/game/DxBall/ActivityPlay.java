package mstecsoft.marufsharia.game.DxBall;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ActivityPlay extends Activity {
    String newString;
    TextView tv1 = null;
    MediaPlayer player;
    Button btnGoNext;
    ScoreList scoreList = new ScoreList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        // Toast.makeText(getApplicationContext(),"Score: "+scoreList.getScore(),Toast.LENGTH_SHORT).show();
        tv1 = (TextView) findViewById(R.id.txtView);
        btnGoNext= (Button) findViewById(R.id.buttonNEXT);
        player = MediaPlayer.create(getApplicationContext(), R.raw.winningmenu);
        player.setLooping(true); // Set looping
        player.start();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("LEVEL");
            }
        } else {
            newString = (String) savedInstanceState.getSerializable("LEVEL");
        }
        //Toast.makeText(getApplicationContext(),"level "+newString,Toast.LENGTH_SHORT).show();
        if (newString.equals("lev2")) {
            tv1.setTextColor(Color.GREEN);
            tv1.setText("Congratulation to Level 2");
        } else if (newString.equals("lev3")) {
            tv1.setTextColor(Color.MAGENTA);
            tv1.setText("Congratulation !!! Game Over !!!");
            btnGoNext.setVisibility(View.GONE);

        }
        else if (newString.equals("error")) {
            tv1.setTextColor(Color.MAGENTA);
            tv1.setText("Looser !!! Game Over !!!");
            btnGoNext.setVisibility(View.GONE);

        }

    }

    public void onClickGoHome(View view) {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        player.stop();
        this.finish();
    }

    public void onclickGoNextLevel(View view) {
        player.stop();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new Level2(this));
    }
}
