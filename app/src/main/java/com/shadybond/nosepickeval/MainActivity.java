package com.shadybond.nosepickeval;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.graphics.Point;

public class MainActivity extends AppCompatActivity {

    GameEngine nosePick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        nosePick = new GameEngine(this, size.x, size.y);

        setContentView(nosePick);
    }
    @Override
    protected void onResume() {
        super.onResume();
        nosePick.startGame();
    }

    // Stop the thread in snakeEngine
    @Override
    protected void onPause() {
        super.onPause();
        nosePick.pauseGame();
    }
}
