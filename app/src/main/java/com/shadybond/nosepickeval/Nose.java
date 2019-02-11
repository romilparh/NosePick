package com.shadybond.nosepickeval;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Nose{

    int xPosition;
    int yPosition;
    Bitmap noseImage;
    int noseImageWidth;

    private Rect hitBoxOne;
    private Rect hitBoxTwo;

    public Nose(Context context, int x, int y) {
        this.noseImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.nose);
        this.xPosition = x;
        this.yPosition = y;

        this.hitBoxOne = new Rect(this.xPosition+ (this.noseImage.getWidth()/4)-50, (this.noseImage.getHeight()*3)/4, this.xPosition + this.noseImage.getWidth()/2-40, this.noseImage.getHeight());
        this.hitBoxTwo = new Rect(this.xPosition+ ((this.noseImage.getWidth()*3)/4)-110, (this.noseImage.getHeight()*3)/4, this.noseImage.getWidth()+this.xPosition-110, this.noseImage.getHeight());

    }

    public Rect getHitboxOne() {
        return this.hitBoxOne;
    }
    public Rect getHitboxTwo() {
        return this.hitBoxTwo;
    }

    public int getXPosition() {
        return this.xPosition;
    }
    public int getYPosition() {
        return this.yPosition;
    }

    public Bitmap getBitmap() {
        return this.noseImage;
    }

    public void setXPosition(int x) {
        this.xPosition = x;
    }
    public void setYPosition(int y) {
        this.yPosition = y;
    }


}
