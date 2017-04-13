package mstecsoft.marufsharia.game.DxBall;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by marufsharia on 12/24/2016..
 */

public class Brick {
    Canvas canvas;
    Paint paint;
    float distanceBwtweenBricks = 30;
    float numberOfBricsPerRow = 5;
    float upperBarHeight;
    int bricID = 0;
    Rect[] brick = new Rect[50];

    Brick(Canvas canvsa) {
        this.canvas = canvsa;
        upperBarHeight = canvsa.getHeight() / 7;

    }

    public void drawBrics() {


        float padding = 25;
        float bricWidth = (canvas.getWidth() - (padding * 2 + distanceBwtweenBricks * numberOfBricsPerRow)) / numberOfBricsPerRow;
        float brickHeight = 50;
        float x = 50, y = upperBarHeight + 30;
        bricID = 0;

        paint.setColor(Color.rgb(178, 34, 34));
        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < numberOfBricsPerRow; j++) {
                if (brick[bricID] != null) {
                    brick[bricID].set((int) x, (int) y, (int) (x + bricWidth), (int) (y + brickHeight));
                    canvas.drawRect(brick[bricID], paint);
                }
                bricID++;
                x += bricWidth + distanceBwtweenBricks;
            }
            y += brickHeight + 50;
            x = 50;
        }
    }
}
