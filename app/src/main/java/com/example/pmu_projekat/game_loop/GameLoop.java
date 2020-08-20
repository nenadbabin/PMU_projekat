package com.example.pmu_projekat.game_loop;

import android.graphics.Canvas;
import android.os.SystemClock;

import com.example.pmu_projekat.views.BattleView;

public class GameLoop extends Thread {

    private BattleView battleView;
    public static final int MAX_FPS = 80;
    private boolean running;

    public void setGameView(BattleView gv) {
        this.battleView = gv;
        running = true;
    }

    @Override
    public void run() {
        super.run();
        while (running) {
            synchronized (battleView)
            {
                battleView.update();
                battleView.invalidate();
            }
            SystemClock.sleep(1000 / MAX_FPS);
        }
    }

    public void stopLoop() {
        this.running = false;
    }
    public void interruptThread()
    {
        this.interrupt();
    }
}
