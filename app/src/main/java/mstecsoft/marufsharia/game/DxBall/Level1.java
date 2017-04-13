package mstecsoft.marufsharia.game.DxBall;
/*
Created by marufsharia on 12/24/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by marufsharia on 12/24/2016.
 */

public class Level1 extends View {
    //for canvas and draw
    Paint paint;
    boolean firstTime = true;
    float x = 0, y = 0, dx = 5, dy = 5;
    float canvasWidth, canvasHeight;

    //for Life counting
    int lifeTime = 3;
    ///for bar
    float barX = 0;
    float barHeight = 30;
    float barWidth;
    Rect Bar = new Rect();

    float radius = 25;
    //for brick
    Rect[] brick = new Rect[50];
    int totalBricks = 15;
    float perRowNumberOfBrick = 5;
    float brickDistance = 20;
    int btickIDNumber = 0;

    //for statusbar
    float statusBarHeight;
    int score = 0;

    /////Scorelist,Thread & Media player
    ScoreList scoreList = new ScoreList();
    Runnable r;
    Timer timer;
    MediaPlayer player;
    MediaPlayer mp;


    /*
    Constructor

    */
    public Level1(Context context) {
        super(context);
        playBgMusic();
        paint = new Paint();

        //create  brick object
        for (int i = 0; i < totalBricks; i++) {
            brick[i] = new Rect();
        }


    }



    /*
    calculate and draw canvas
    */


    protected void calculateNextPos(Canvas canvas) {
        //sidewall (x) collision detectation
        if (x < radius || x > (canvasWidth - radius)) {
            //sidewalll (x)collide sound
            mp = MediaPlayer.create(getContext(), R.raw.sound6);
            mp.start();
            dx = -dx;

        }
        //upwall (y) collision detectation
        else if (y < radius + statusBarHeight || y > (canvasHeight - radius)) {
            //Upwalll (y)collide sound
            mp = MediaPlayer.create(getContext(), R.raw.sound1);
            mp.start();
            dy = -dy;
        }

        // bar & ball collision detectation
        if (Bar.intersect((int) (x - radius), (int) (y - radius), (int) (x + radius), (int) (y + radius))) {
            mp = MediaPlayer.create(getContext(), R.raw.sound4);
            mp.start();
            dy = -dy;

        }


        if (y > canvasHeight - radius - barHeight + 5) {
            resetGame();
            lifeTime--;

            if (lifeTime == 0) {
                newGame();
                dx = 0;
                dy = 0;
            }
        }
        x += dx;
        y += dy;
        collisionDetection(); //call collosion Detection Method
        levelComplete();//check levelComplete is complated or not

    }


    /*
    for draw brick
    */

    public void drawBricks(Canvas canvas) {


        float padding = 25;
        float bricWidth = (canvasWidth - (padding * 2 + brickDistance * perRowNumberOfBrick)) / perRowNumberOfBrick;
        float brickHeight = 50;
        float x = 50, y = statusBarHeight + 30;
        btickIDNumber = 0;

        paint.setColor(Color.BLUE);
        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < perRowNumberOfBrick; j++) {
                if (brick[btickIDNumber] != null) {
                    brick[btickIDNumber].set((int) x, (int) y, (int) (x + bricWidth), (int) (y + brickHeight));
                    canvas.drawRect(brick[btickIDNumber], paint);
                }
                btickIDNumber++;
                x += bricWidth + brickDistance;
            }
            y += brickHeight + 50;
            x = 50;
        }
    }


    /*
    for draw ball

     */

    public void drawBall(Canvas canvas) {

        paint.setColor(Color.RED);
        canvas.drawCircle(x, y, radius, paint);
    }

/*
for draw bar
*
**/

    public void drawBar(Canvas canvas) {
        paint.setColor(Color.BLUE);
        Bar.set((int) barX, (int) (canvasHeight - barHeight), (int) (barX + barWidth), (int) (canvasHeight));
        canvas.drawRect(Bar, paint);
    }


    /*
    for touch event
    */


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        barX = (int) e.getX();
        barX -= barWidth / 2;

        if ((int) e.getX() <= barWidth / 2)
            barX = 0;
        if (((int) e.getX() + barWidth / 2) >= canvasWidth)
            barX = canvasWidth - barWidth;
        return true;
    }

    /*
    for draw canvus
    */

    @Override
    protected void onDraw(Canvas canvas) {

        if (firstTime) {
            firstTime = false;
            canvasHeight = canvas.getHeight();
            canvasWidth = canvas.getWidth();
            x = canvasWidth / 2;
            y = canvasHeight - radius - barHeight;
            barWidth = canvasWidth / 5;
            statusBarHeight = canvasHeight / 10;
            barX = canvasWidth / 2 - barWidth / 2;
            //Toast.makeText(getContext(),""+canvasWidth,Toast.LENGTH_SHORT).show();
        }
        calculateNextPos(canvas);
        canvas.drawRGB(255, 255, 255);
        drawBall(canvas);
        drawStatusBar(canvas);
        drawBricks(canvas);
        drawBar(canvas);

        //// bk=new Brick(canvas);
        /////  bk.drawBrics();


        invalidate();//its a view class method & Invalidate the whole view. If the view is visible.
    }



    /*
    for detect collision
    *
    *
    */
    private void collisionDetection() {
        for (int i = 0; i < totalBricks; i++) {
            if (brick[i] != null) {
                if (brick[i].intersect((int) (x - radius), (int) (y - radius), (int) (x + radius), (int) (y + radius))) {

                    brick[i] = null;
                    dy = -dy;
                    score += 10;
                    mp = MediaPlayer.create(getContext(), R.raw.sound2);
                    mp.start();
                }
            }
        }
    }


    /*

   Draw status bar for display life and score

   */
    public void drawStatusBar(Canvas canvas) {
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, canvasWidth, statusBarHeight, paint);
        drawLifeText(canvas);
        drawScore(canvas);
    }



    /*
    display Life Text only
    */
    public void drawLifeText(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        paint.setTypeface(Typeface.create(Typeface.create("casual", 2), Typeface.BOLD));
        canvas.drawText("life: ", canvasWidth / 18, statusBarHeight / 2 + 40 / 2, paint);
        drawLifes(canvas);
    }


    /*
    display Life number
     */

    public void drawLifes(Canvas canvas) {
        float cx = canvasWidth / 4 - 20;
        float cy = statusBarHeight / 2 + 10;

        if (lifeTime == 0) {

            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
            canvas.drawText("Dead", cx - 10, cy + 10, paint);


        } else if (lifeTime == 1) {


            paint.setColor(Color.RED);
            paint.setTextSize(50);
            paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
            canvas.drawText("1", cx - 10, cy + 10, paint);


        } else if (lifeTime == 2) {

            paint.setColor(Color.MAGENTA);
            paint.setTextSize(50);
            paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
            canvas.drawText("2", cx - 10, cy + 10, paint);

        } else if (lifeTime == 3) {
            paint.setColor(Color.GREEN);
            paint.setTextSize(50);
            paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
            canvas.drawText("3", cx - 10, cy + 10, paint);

        }
    }

    /*

    draw score text

    */

    public void drawScore(Canvas canvas) {

        paint.setColor(Color.LTGRAY);
        paint.setTextSize(40);
        paint.setTypeface(Typeface.create(Typeface.create("casual", 2), Typeface.BOLD));
        canvas.drawText("Score: ", canvasWidth - canvasWidth / 2 + 50, statusBarHeight / 2 + 40 / 2, paint);//hree 40 is font size
        canvas.drawText(String.valueOf(score), canvasWidth - canvasWidth / 4, statusBarHeight / 2 + 40 / 2, paint);//hree 40 is font size
    }

    /*

    Initilized component & start new game

    */

    public void newGame() {
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(getContext());
        alertDlg.setTitle("Game Over");
        alertDlg.setMessage("Do you want to Play again ?");
        alertDlg.setIcon(R.drawable.help);
        alertDlg.setCancelable(false);
        alertDlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Yes button
                resetGame();
                resetBricks();
                score = 0;
                lifeTime = 3;
            }
        });
        alertDlg.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                player.stop();
                System.exit(0);
            }
        });
        alertDlg.create().show();
    }

    public void resetBricks() {
        for (int i = 0; i < totalBricks; i++) {
            brick[i] = new Rect();
        }
    }

        /*
        for restar game

         */

    public void resetGame() {
        x = canvasWidth / 2;
        y = canvasHeight - radius - barHeight;
        barWidth = canvasWidth / 5;
        statusBarHeight = canvasHeight / 10;
        barX = canvasWidth / 2 - barWidth / 2;
        dx = 5;
        dy = 5;
    }

    /*
    for check level complede
    */

    public void levelComplete() {
        if (score == 150) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 10 seconds
                    scoreList.setScore(150);
                    Intent i = new Intent(getContext(), ActivityPlay.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    String strName = "lev2";
                    i.putExtra("LEVEL", strName);
                    getContext().startActivity(i);
                    System.exit(0);

                }
            }, 10);

        }
    }


    /*for play background music*/
    public void playBgMusic() {
        final Handler handler = new Handler();
        timer = new Timer();
        TimerTask doAsyncTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {

                    public void run() {

                        player = MediaPlayer.create(getContext(), R.raw.level1bg);
                        player.setLooping(true);
                        player.setVolume(1.0f, 1.0f);
                        player.start();

                    }
                });
            }
        };
        timer.schedule(doAsyncTask, 0, 9100000);
    }

    /*
     * do something
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
            player.stop();
            System.exit(0);

        }
        return super.onKeyDown(keyCode, event);

    }
}
