package com.example.pmu_projekat.game_loop;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;

import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.views.BattleView;

public class GameLoop extends Thread {

    private BattleView battleView;
    public static final int MAX_FPS = 80;
    private boolean running;

    public void setGameView(BattleView gv) {
        this.battleView = gv;
    }

    @Override
    public void run() {
        super.run();
        while (running && !isInterrupted()) {
            synchronized (battleView)
            {
                battleView.update();
                battleView.invalidate();
            }
            SystemClock.sleep(1000 / MAX_FPS);
            Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "run(): " + this.getId());
        }

        Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "id: " + this.getId());
        Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "interrupted: " + isInterrupted());
        Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "running flag: " + running);
    }

    public void setRunning(boolean running) {
        this.running = running;
        Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "setRunning(): " + this.running);
    }
}
