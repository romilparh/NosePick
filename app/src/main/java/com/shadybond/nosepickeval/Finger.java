package com.shadybond.nosepickeval;

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Rect;
public class Finger {

    final int fingerUp = 1;
    int xPosition;
    int yPosition;
    int direction;              // -1 = not moving, 0 = left, 1 = right
    Bitmap playerImage;
    int speed = 15;

    private Rect hitBox;

    public Finger(Context context, int x, int y) {
        this.playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.finger);
        this.xPosition = x;
        this.yPosition = y;
        this.hitBox = new Rect(this.xPosition, this.yPosition, this.xPosition + this.playerImage.getWidth(), this.yPosition + this.playerImage.getHeight());

    }

    public void updatePlayerPosition() {
        if (this.direction == 0) {
            // move left
            this.xPosition = this.xPosition - this.speed;
        }
        else if (this.direction == 1) {
            // move right
            this.xPosition = this.xPosition + this.speed;
        }
        else if(this.direction == 2){
        }

        // update the position of the hitbox
        this.updateHitbox();
    }

    public void updateFingerPosition(int x){
        if(this.yPosition!=0){
            this.yPosition = this.yPosition - fingerUp;
        }

    }

    public int getDirection(){
        return this.direction;
    }
    public void updateHitbox() {
        // update the position of the hitbox
        this.hitBox.top = this.yPosition;
        this.hitBox.left = this.xPosition;
        this.hitBox.right = this.xPosition + this.playerImage.getWidth();
        this.hitBox.bottom = this.yPosition + this.playerImage.getHeight();
    }


    public Rect getHitbox() {
        return this.hitBox;
    }

    public int getXPosition() {
        return this.xPosition;
    }
    public int getYPosition() {
        return this.yPosition;
    }

    public void setDirection(int i) {
        this.direction = i;
    }
    public Bitmap getBitmap() {
        return this.playerImage;
    }

    public void setXPosition(int x) {
        this.xPosition = x;
        this.updateHitbox();
    }
    public void setYPosition(int y) {
        this.yPosition = y;
        this.updateHitbox();
    }
}
