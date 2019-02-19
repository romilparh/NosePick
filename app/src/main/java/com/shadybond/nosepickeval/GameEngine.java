package com.shadybond.nosepickeval;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameEngine extends SurfaceView implements Runnable {

    // screen size
    int screenHeight;
    int screenWidth;
    int counter = 0;

    final int fps = 17;
    // game state
    boolean gameIsRunning;

    boolean showBoxes = true;

    boolean collided = false;
    boolean collidedFalse = false;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;


    int picked = 0;
    int missed = 0;

    Finger finger;
    Nose nose;

    // check if player win
    boolean win = false;



    final int FINGER_VERTICAL_SPEED = 5;

    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();



        // @TODO: Add your sprites
        this.spawnFinger();
        this.spawnNose();
        // @TODO: Any other game setup

    }

    private void printScreenInfo() {

        Log.d("Nose Pick", "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }


    public void updateFingerPosition(){
        finger.setYPosition((this.screenHeight)-500);
    }
    // To Run the Game
    @Override
    public void run() {
        while (gameIsRunning == true) {
            counter = 0;
            //this.updateFingerPosition();
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
            Log.d("FingerDrawn","Finger Drawn");
        }
    }

    final int FINGER_STARTING_Y = 580; // (this.screenHeight)-500;
    private void spawnFinger() {
        // put player in middle of screen --> you may have to adjust the Y position
        // depending on your device / emulator
        finger = new Finger(this.getContext(), 0, FINGER_STARTING_Y);
        finger.setDirection(1);

    }

    private void spawnNose() {
        // put player in middle of screen --> you may have to adjust the Y position
        // depending on your device / emulator
        this.nose = new Nose(this.getContext(), (this.screenWidth/2), 0);

    }

    // On Touch Event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //@TODO: What should happen when person touches the screen? Update Finger Touch

        //finger.setYPosition(finger.yPosition + FINGER_VERTICAL_SPEED);\
        //finger.setYPosition(0);

        // change the finger direction to up
//        if (finger.getYPosition() >= FINGER_STARTING_Y) {
//            finger.setYPosition(FINGER_STARTING_Y);
//        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("ROMIL", "CALLING THE TOUCH EVENT");
            finger.setDirection(2);
        }






        return true;
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.WHITE);


            //@TODO: Draw the finger
            canvas.drawBitmap(this.finger.getBitmap(), this.finger.getXPosition(), this.finger.getYPosition(), paintbrush);
            canvas.drawBitmap(this.nose.getBitmap(), this.nose.getXPosition(), this.nose.getYPosition(), paintbrush);

            //@TODO: Draw the nose


            // Show the hitboxes on player and enemy
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);
            Rect playerHitbox = finger.getHitbox();


            Rect noseHitboxOne = nose.getHitboxOne();


            Rect noseHitboxTwo = nose.getHitboxTwo();


            if(showBoxes == true){
                canvas.drawRect(playerHitbox.left, playerHitbox.top, playerHitbox.right, playerHitbox.bottom, paintbrush);
                canvas.drawRect(noseHitboxOne.left, 0, noseHitboxOne.right, noseHitboxOne.bottom, paintbrush);
                canvas.drawRect(noseHitboxTwo.left, 0, noseHitboxTwo.right, noseHitboxTwo.bottom, paintbrush);
            }
            // draw game stats
            paintbrush.setTextSize(60);
            paintbrush.setColor(Color.BLACK);

            canvas.drawText("Hits: " + picked, 100, 100, paintbrush);
            canvas.drawText("Misses: " + missed, 100, 200, paintbrush);


            if (win) {
                // say YOU WIN
                canvas.drawText("YOU WIN!!!", 100, 300, paintbrush);
            }
            else {
                // erase previous win text
                paintbrush.setColor(Color.WHITE);
                canvas.drawText("YOU WIN!!!", 100, 300, paintbrush);

                paintbrush.setColor(Color.BLACK);

                canvas.drawText("YOU LOSE!!!", 100, 300, paintbrush);
            }
            // write if person wins or loses

            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(fps);
        }
        catch (Exception e) {

        }
    }



    public void updatePositions() {
        // @TODO: Update position of finger






        System.out.println(finger.getXPosition());
//        if (finger.getXPosition() <= 0) {
//
//            //reset the enemy's starting position to right side of screen
//            // you may need to adjust this number according to your device/emulator
//            finger.setDirection(1);
//        } else if (finger.getXPosition() >= screenWidth) {
//            finger.setDirection(0);
//        } else if(finger.getDirection()== 2){
//            try {
//                gameThread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            finger.setXPosition(0);
//            finger.setDirection(0);
//        }


        Log.d("ROMIL", "Finger position: " + finger.getXPosition() + "," + finger.getYPosition() + ", Direction: " + finger.getDirection());



        finger.updatePlayerPosition();

        // collision detection
        // -------------------
        if (finger.getXPosition() <= 300) {
            // if finger is at left side, change directions
            finger.setDirection(1); // 1 = rigth
        }
        else if (finger.getXPosition() >= this.screenWidth -300) {
            // if finger is at right side, change directions
            finger.setDirection(0); // 0 = left

        }
        else if (finger.getYPosition() <= 0) {
            // if finger is at top of screen, move it back down

            Log.d("ROMIL", "++++++++++=FINGER IS ABOVE THE SCREEN");

            this.missed++;
            this.resetGame();


//            // move finger back to original starting position
//            finger.setYPosition(580);
//            finger.setXPosition(0);
//
//            // start to move the finger left and right
//            finger.setDirection(0);

        }


        if (finger.getHitbox().intersect(nose.getHitboxOne())) {
//            // reduce lives
//
//            // reset player to original position
            collided = true;
            this.picked++;

            this.win = true;
            this.resetGame();

        } else if (finger.getHitbox().intersect(nose.getHitboxTwo())) {
//            // reduce lives

            // reset player to original position
            collided = true;
            this.picked++;
            this.win = true;
            this.resetGame();
        }

    }

    public void resetGame() {
        // spawn finger back in starting position
        try {
            Thread.sleep(1000);
            this.win = false;
            this.spawnFinger();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
