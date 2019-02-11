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

    final int fps = 10;
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
            this.updateFingerPosition();
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
            Log.d("FingerDrawn","Finger Drawn");
        }
    }

    private void spawnFinger() {
        // put player in middle of screen --> you may have to adjust the Y position
        // depending on your device / emulator
        finger = new Finger(this.getContext(), 0, (this.screenHeight)-500);
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
        finger.setYPosition(this.screenHeight-700);


        updatePositions();
        if(collided){
            if(counter == 0){
                picked++;
                counter++;
                collided = false;
            }

        }
        if(!collided){
            if(counter == 0){
                if(finger.getYPosition()!=0){
                    missed++;
                    counter++;
                }
            }

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

        finger.updatePlayerPosition();
        System.out.println(finger.getXPosition());
        if (finger.getXPosition() <= 0) {

            //reset the enemy's starting position to right side of screen
            // you may need to adjust this number according to your device/emulator
            finger.setDirection(1);
        } else if (finger.getXPosition() >= screenWidth) {
            finger.setDirection(0);
        } else if(finger.getDirection()== 2){
            try {
                gameThread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finger.setXPosition(0);
            finger.setDirection(0);
        }


        if (finger.getHitbox().intersect(nose.getHitboxOne())) {
            // reduce lives

            // reset player to original position
            collided = true;
            finger.setDirection(2);
        } else if (finger.getHitbox().intersect(nose.getHitboxTwo())) {
            // reduce lives

            // reset player to original position
            collided = true;
            finger.setDirection(2);
        }

    }


}
