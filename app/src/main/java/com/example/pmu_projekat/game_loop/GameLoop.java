package com.example.pmu_projekat.game_loop;

import android.graphics.Canvas;
import android.os.SystemClock;

import com.example.pmu_projekat.views.BattleView;

public class GameLoop extends Thread {

    private BattleView battleView;
    private static final int MAX_FPS = 60;
    private boolean running;

    public void setGameView(BattleView gv) {
        this.battleView = gv;
        this.running = true;
    }

    @Override
    public void run() {
        super.run();
        while (running) {
            battleView.update();
            battleView.invalidate();
            SystemClock.sleep(1000 / MAX_FPS);
        }
    }

    public void stopLoop() {
        this.running = false;
    }
}
