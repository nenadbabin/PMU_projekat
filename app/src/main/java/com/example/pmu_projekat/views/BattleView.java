package com.example.pmu_projekat.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.activities.BattleActivity;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.game_loop.GameLoop;
import com.example.pmu_projekat.objects.CarElement;
import com.example.pmu_projekat.objects.ChassisElement;
import com.example.pmu_projekat.objects.Missile;
import com.example.pmu_projekat.objects.weapon.Blade;

import java.util.ArrayList;
import java.util.List;

public class BattleView extends View {

    private boolean userControl = false;

    private ChassisElement carP1;
    private ChassisElement carP2;

    private int bottomLine = (int) (Constants.SCREEN_HEIGHT * 0.8);

    private boolean isYCalculated = false;

    private int damageToP1 = 0;
    private int damageToP2 = 0;

    private int timeToRocketLaunchP1 = 0;
    private int timeToRocketLaunchP2 = 0;

    private BattleActivity battleActivity;

    private List<Missile> p1MissileList;
    private List<Missile> p2MissileList;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean attack = false;

    private Drawable wallLeft;
    private Drawable wallRight;
    private int wallLeftX = -800 - 20;
    private int wallLeftY = 350;
    private int wallRightX = Constants.SCREEN_WIDTH + 20;
    private int wallRightY = 350;
    private static int WALL_HEIGHT_WIDTH = 800;

    private Context context;

    private boolean moveWalls = false;
    private int wallCounter = 0;

    private int contactWithWallTimeP1 = 0;
    private int contactWithWallTimeP2 = 0;
    private static final int WALL_DAMAGE = 40;
    private static final int WALL_START_TIME_SEC = 10;

    private int p1Health, p2Health;

    private GameLoop gameLoop;

    public BattleView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BattleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public BattleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init()
    {
        p1MissileList = new ArrayList<>();
        p2MissileList = new ArrayList<>();

        wallLeft = context.getResources().getDrawable(R.drawable.wall, null);
        wallRight = context.getResources().getDrawable(R.drawable.wall, null);
    }

    public void setUserControl(boolean userControl) {
        this.userControl = userControl;
    }

    public void setBattleActivity(BattleActivity battleActivity) {
        this.battleActivity = battleActivity;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (carP1 != null)
        {
            carP1.draw(canvas);
        }

        if (carP2 != null)
        {
            carP2.draw(canvas);
        }

        for (int i = 0; i < p1MissileList.size(); i++)
        {
            Missile missile = p1MissileList.get(i);
            missile.draw(canvas);
        }

        for (int i = 0; i < p2MissileList.size(); i++)
        {
            Missile missile = p2MissileList.get(i);
            missile.draw(canvas);
        }

        wallLeft.setBounds(wallLeftX, wallLeftY, wallLeftX + WALL_HEIGHT_WIDTH, wallLeftY + WALL_HEIGHT_WIDTH);
        wallRight.setBounds(wallRightX, wallRightY, wallRightX + WALL_HEIGHT_WIDTH, wallRightY + WALL_HEIGHT_WIDTH);
        wallLeft.draw(canvas);
        wallRight.draw(canvas);

        if (!isYCalculated)
        {
            setY();
        }

        Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "onDraw()");
    }

    public void setCarP1(ChassisElement car) {
        this.carP1 = car;
        carP1.setX((int) (Constants.SCREEN_WIDTH * 0.1));
        carP1.setY(bottomLine - carP1.getHeight());
    }

    public void setCarP2(ChassisElement car) {
        this.carP2 = car;
        carP2.setX((int) (Constants.SCREEN_WIDTH * 0.8));
        carP2.setY(bottomLine - carP2.getHeight());
    }

    public void update()
    {
        if (isYCalculated)
        {
            int oldP1Health = p1Health;
            int oldP2Health = p2Health;

            Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "update()");
            if (wallCounter < GameLoop.MAX_FPS * WALL_START_TIME_SEC)
            {
                wallCounter++;
            }

            if ((wallCounter >= GameLoop.MAX_FPS * WALL_START_TIME_SEC) && moveWalls == false)
            {
                moveWalls = true;
            }

            if (moveWalls)
            {
                wallLeftX++;
                wallRightX--;
            }

            if (doP1TouchLeftWall() && doP2TouchRightWall() && doCarsCollide())
            {
                // poseban slucaj
            }

            if (doP1TouchLeftWall())
            {
                carP1.setX(carP1.getX() + 1);
                if (doCarsCollide())
                {
                    carP2.setX(carP2.getX() + 1); // 1 je brzina kretanja zida
                }
            }

            if (doP2TouchRightWall())
            {
                carP2.setX(carP2.getX() - 1);
                if (doCarsCollide())
                {
                    carP1.setX(carP1.getX() - 1); // 1 je brzina kretanja zida
                }
            }

            if (!doCarsCollide())
            {
                if (!userControl)
                {
                    if (carP1.getWheelLeft() != null && carP1.getWheelRight() != null) // mora da ima tockove da bi se kretao
                    {
                        carP1.setX(carP1.getX() + moveCar(carP1));
                    }
                }
                else
                {
                    if (carP1.getWheelLeft() != null && carP1.getWheelRight() != null)
                    {
                        if (moveLeft && canMoveP1Left())
                        {
                            carP1.setX(carP1.getX() - moveCar(carP1));
                        }

                        if (moveRight)
                        {
                            carP1.setX(carP1.getX() + moveCar(carP1));
                        }
                    }
                }

                if (carP2.getWheelLeft() != null && carP2.getWheelRight() != null)
                {
                    carP2.setX(carP2.getX() - moveCar(carP2));
                }
            }
            else
            {
                if (carP1.getWheelLeft() != null && carP1.getWheelRight() != null)
                {
                    if (moveLeft && canMoveP1Left())
                    {
                        carP1.setX(carP1.getX() - moveCar(carP1));
                    }
                }

                /*if (moveRight)
                {
                    carP1.setX(carP1.getX() + 2);
                }*/

                if ((!userControl && carP1.getEnergy() > carP2.getEnergy() && canMoveP2Right() && canMoveP2Right() && carP1.getWheelLeft() != null && carP1.getWheelRight() != null) || (userControl && moveRight && carP1.getEnergy() > carP2.getEnergy() && canMoveP2Right() && carP1.getWheelLeft() != null && carP1.getWheelRight() != null))
                {
                    int step = /*(carP1.getEnergy() - carP2.getEnergy()) / 2*/ 1;
                    carP1.setX(carP1.getX() + step);
                    carP2.setX(carP2.getX() + step);
                }

                if (carP1.getEnergy() < carP2.getEnergy() && canMoveP1Left() && carP2.getWheelLeft() != null && carP2.getWheelRight() != null)
                {
                    int step = /*(carP2.getEnergy() - carP1.getEnergy()) / 2*/ 1;
                    carP1.setX(carP1.getX() - step);
                    carP2.setX(carP2.getX() - step);
                }
            }

            if (carP1.getWeapon() != null)
            {
                if (carP1.getWeapon().getElementIdentity() == Constants.WPN_ROCKET)
                {
                    timeToRocketLaunchP1++;

                    if (timeToRocketLaunchP1 >= GameLoop.MAX_FPS && ((userControl && attack) || !userControl))
                    {
                        timeToRocketLaunchP1 = 0;

                        Missile missile = new Missile(carP1.getWeapon().getX() + carP1.getWeapon().getWidth(), carP1.getWeapon().getY() + 25);
                        p1MissileList.add(missile);
                    }
                }
                else if (carP1.getWeapon().getElementIdentity() == Constants.WPN_BLADE)
                {
                    if ((userControl && attack) || !userControl)
                    {
                        Blade p1Blade = (Blade) carP1.getWeapon();
                        p1Blade.setDegree(p1Blade.getDegree() + 3);
                        if (p1Blade.getDegree() >= 360)
                        {
                            p1Blade.setDegree(p1Blade.getDegree() % 360);

                            if (wasCarDamagedByAnother(carP2, carP1))
                            {
                                p2Health -= carP1.getWeapon().getPower();
                            }
                        }
                    }
                }
                else
                {
                    if (wasCarDamagedByAnother(carP2, carP1) && ((userControl && attack) || !userControl))
                    {
                        damageToP2++;
                        if (damageToP2 == GameLoop.MAX_FPS)
                        {
                            damageToP2 = 0;

                            // lower health of p2
                            if (carP1.getWeapon() != null)
                            {
                                p2Health -= carP1.getWeapon().getPower();
                            }
                        }
                    }
                }
            }

            if (carP2.getWeapon() != null) {
                if (carP2.getWeapon().getElementIdentity() == Constants.WPN_ROCKET) {
                    timeToRocketLaunchP2++;

                    if (timeToRocketLaunchP2 == GameLoop.MAX_FPS) {
                        timeToRocketLaunchP2 = 0;

                        Missile missile = new Missile(carP2.getWeapon().getX(), carP2.getWeapon().getY() + 25);
                        p2MissileList.add(missile);
                    }
                }
                else if (carP2.getWeapon().getElementIdentity() == Constants.WPN_BLADE)
                {
                    Blade p2Blade = (Blade) carP2.getWeapon();
                    p2Blade.setDegree(p2Blade.getDegree() + 3);
                    if (p2Blade.getDegree() >= 360)
                    {
                        p2Blade.setDegree(p2Blade.getDegree() % 360);

                        if (wasCarDamagedByAnother(carP1, carP2))
                        {
                            p1Health -= carP2.getWeapon().getPower();
                        }
                    }
                }
                else
                {
                    if (wasCarDamagedByAnother(carP1, carP2))
                    {
                        damageToP1++;
                        if (damageToP1 == GameLoop.MAX_FPS)
                        {
                            damageToP1 = 0;
                            //int currentHealth = battleActivity.getP1Health();
                            if (carP2.getWeapon() != null)
                            {
                                p1Health -= carP2.getWeapon().getPower();
                            }

                            //battleActivity.setP1Health(currentHealth);
                        }
                    }
                }
            }

            for (int i = 0; i < p1MissileList.size(); i++)
            {
                Missile missile = p1MissileList.get(i);
                missile.setX(missile.getX() + 5);
            }

            for (int i = 0; i < p2MissileList.size(); i++)
            {
                Missile missile = p2MissileList.get(i);
                missile.setX(missile.getX() - 5);
            }

            if (carP1.getWeapon() != null && carP1.getWeapon().getElementIdentity() == Constants.WPN_ROCKET)
            {
                if (wasCarHitByMissile(carP2, p1MissileList))
                {
                    p2Health -= carP1.getWeapon().getPower();
                }
            }

            if (carP2.getWeapon() != null && carP2.getWeapon().getElementIdentity() == Constants.WPN_ROCKET)
            {
                if (wasCarHitByMissile(carP1, p2MissileList))
                {
                    p1Health -= carP2.getWeapon().getPower();
                }
            }

            checkIfMissilesOffScreen();

            if (doP1TouchLeftWall())
            {
                contactWithWallTimeP1++;
            }

            if (doP2TouchRightWall())
            {
                contactWithWallTimeP2++;
            }

            if (contactWithWallTimeP1 >= GameLoop.MAX_FPS / 2)
            {
                contactWithWallTimeP1 = 0;
                p1Health -= WALL_DAMAGE;
            }

            if (contactWithWallTimeP2 >= GameLoop.MAX_FPS / 2)
            {
                contactWithWallTimeP2 = 0;;
                p2Health -= WALL_DAMAGE;
            }

            if (p1Health < 0)
            {
                p1Health = 0;
            }

            if (p2Health < 0)
            {
                p2Health = 0;
            }

            if (p1Health != oldP1Health)
            {
                battleActivity.setP1Health(p1Health);
            }

            if (p2Health != oldP2Health)
            {
                battleActivity.setP2Health(p2Health);
            }

            if (p1Health == 0)
            {
                gameLoop.setRunning(false);
                battleActivity.toast("Opponent won the battle!");
                battleActivity.p2Win();
            }

            if (p2Health == 0)
            {
                gameLoop.setRunning(false);
                battleActivity.toast("User won the battle!");
                battleActivity.p1Win();
            }
        }
    }

    private boolean doCarsCollide()
    {
        List<Rect> carElemList1 = new ArrayList<>();
        List<Rect> carElemList2 = new ArrayList<>();

        Rect chassisRect1 = new Rect(carP1.getX(), carP1.getY(), carP1.getX() + carP1.getWidth(), carP1.getY() + carP1.getHeight());
        Rect chassisRect2 = new Rect(carP2.getX(), carP2.getY(), carP2.getX() + carP2.getWidth(), carP2.getY() + carP2.getHeight());

        carElemList1.add(chassisRect1);
        carElemList2.add(chassisRect2);


        /*if (carP1.getWeapon() != null)
        {
            Rect weaponRect1 = new Rect(carP1.getWeapon().getX(), carP1.getWeapon().getY(), carP1.getWeapon().getX() + carP1.getWeapon().getWidth(), carP1.getWeapon().getY() + carP1.getWeapon().getHeight());
            carElemList1.add(weaponRect1);
        }

        if (carP2.getWeapon() != null)
        {
            Rect weaponRect2 = new Rect(carP2.getWeapon().getX(), carP2.getWeapon().getY(), carP2.getWeapon().getX() + carP2.getWeapon().getWidth(), carP2.getWeapon().getY() + carP2.getWeapon().getHeight());
            carElemList2.add(weaponRect2);
        }*/



        if (carP1.getWheelLeft() != null)
        {
            Rect wheelLeftRect1 = new Rect(carP1.getWheelLeft().getX(), carP1.getWheelLeft().getY(), carP1.getWheelLeft().getX() + carP1.getWheelLeft().getWidth(), carP1.getWheelLeft().getY() + carP1.getWheelLeft().getHeight());
            carElemList1.add(wheelLeftRect1);
        }

        if (carP2.getWheelLeft() != null)
        {
            Rect wheelLeftRect2 = new Rect(carP2.getWheelLeft().getX(), carP2.getWheelLeft().getY(), carP2.getWheelLeft().getX() + carP2.getWheelLeft().getWidth(), carP2.getWheelLeft().getY() + carP2.getWheelLeft().getHeight());
            carElemList2.add(wheelLeftRect2);
        }



        if (carP1.getWheelRight() != null)
        {
            Rect wheelRightRect1 = new Rect(carP1.getWheelRight().getX(), carP1.getWheelRight().getY(), carP1.getWheelRight().getX() + carP1.getWheelRight().getWidth(), carP1.getWheelRight().getY() + carP1.getWheelRight().getHeight());
            carElemList1.add(wheelRightRect1);
        }

        if (carP2.getWheelRight() != null)
        {
            Rect wheelRightRect2 = new Rect(carP2.getWheelRight().getX(), carP2.getWheelRight().getY(), carP2.getWheelRight().getX() + carP2.getWheelRight().getWidth(), carP2.getWheelRight().getY() + carP2.getWheelRight().getHeight());
            carElemList2.add(wheelRightRect2);
        }


        for (int i = 0; i < carElemList1.size(); i++)
        {
            for (int j = 0; j < carElemList2.size(); j++)
            {
                Rect r1 = carElemList1.get(i);
                Rect r2 = carElemList2.get(j);

                if (r1.intersect(r2))
                {
                    return true;
                }

                if (r2.intersect(r1))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean wasCarDamagedByAnother(ChassisElement defender, ChassisElement attacker)
    {
        List<Rect> carElemList1 = new ArrayList<>();

        Rect chassisRect1 = new Rect(defender.getX(), defender.getY(), defender.getX() + defender.getWidth(), defender.getY() + defender.getHeight());

        carElemList1.add(chassisRect1);

        if (defender.getWeapon() != null)
        {
            Rect weaponRect1 = new Rect(defender.getWeapon().getX(), defender.getWeapon().getY(), defender.getWeapon().getX() + defender.getWeapon().getWidth(), defender.getWeapon().getY() + defender.getWeapon().getHeight());
            carElemList1.add(weaponRect1);
        }

        if (defender.getWheelLeft() != null)
        {
            Rect wheelLeftRect2 = new Rect(defender.getWheelLeft().getX(), defender.getWheelLeft().getY(), defender.getWheelLeft().getX() + defender.getWheelLeft().getWidth(), defender.getWheelLeft().getY() + defender.getWheelLeft().getHeight());
            carElemList1.add(wheelLeftRect2);
        }

        if (defender.getWheelRight() != null)
        {
            Rect wheelRightRect2 = new Rect(defender.getWheelRight().getX(), defender.getWheelRight().getY(), defender.getWheelRight().getX() + defender.getWheelRight().getWidth(), defender.getWheelRight().getY() + defender.getWheelRight().getHeight());
            carElemList1.add(wheelRightRect2);
        }

        if (attacker.getWeapon() != null)
        {
            CarElement weapon = attacker.getWeapon();
            Rect weaponRect = new Rect(weapon.getX(), weapon.getY(), weapon.getX() + weapon.getWidth(), weapon.getY() + weapon.getHeight());

            for (int i = 0; i < carElemList1.size(); i++)
            {
                Rect r2 = carElemList1.get(i);

                if (weaponRect.intersect(r2))
                {
                    return true;
                }

                if (r2.intersect(weaponRect))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private void checkIfMissilesOffScreen()
    {
        List<Missile> toRemove = new ArrayList<>();
        for (int i = 0; i < p1MissileList.size(); i++)
        {
            Missile missile = p1MissileList.get(i);
            if (missile.getX() >= Constants.SCREEN_WIDTH)
            {
                toRemove.add(missile);
            }
        }

        p1MissileList.remove(toRemove);

        toRemove = new ArrayList<>();
        for (int i = 0; i < p2MissileList.size(); i++)
        {
            Missile missile = p2MissileList.get(i);
            if (missile.getX() <= 0)
            {
                toRemove.add(missile);
            }
        }

        p2MissileList.remove(toRemove);
    }

    private boolean wasCarHitByMissile(ChassisElement car, List<Missile> missileList)
    {
        List<Rect> carElemList2 = new ArrayList<>();

        Rect chassisRect2 = new Rect(car.getX(), car.getY(), car.getX() + car.getWidth(), car.getY() + car.getHeight());

        carElemList2.add(chassisRect2);

        /*if (car.getWeapon() != null)
        {
            Rect weaponRect1 = new Rect(car.getWeapon().getX(), car.getWeapon().getY(), car.getWeapon().getX() + car.getWeapon().getWidth(), car.getWeapon().getY() + car.getWeapon().getHeight());
            carElemList2.add(weaponRect1);
        }*/

        if (car.getWheelLeft() != null)
        {
            Rect wheelLeftRect2 = new Rect(car.getWheelLeft().getX(), car.getWheelLeft().getY(), car.getWheelLeft().getX() + car.getWheelLeft().getWidth(), car.getWheelLeft().getY() + car.getWheelLeft().getHeight());
            carElemList2.add(wheelLeftRect2);
        }

        if (car.getWheelRight() != null)
        {
            Rect wheelRightRect2 = new Rect(car.getWheelRight().getX(), car.getWheelRight().getY(), car.getWheelRight().getX() + car.getWheelRight().getWidth(), car.getWheelRight().getY() + car.getWheelRight().getHeight());
            carElemList2.add(wheelRightRect2);
        }

        for (int i = 0; i < missileList.size(); i++)
        {
            Missile missile = missileList.get(i);
            for (int j = 0; j < carElemList2.size(); j++)
            {
                Rect rectCE = carElemList2.get(j);
                if (missile.getRect().intersect(rectCE))
                {
                    missileList.remove(missile);
                    return true;
                }
            }
        }

        return false;
    }

    public void setMoveLeft (boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public void setAttack (boolean attack) {
        this.attack = attack;
    }

    public void setY()
    {
        /*Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "set Y: " + carP1.getY() + " " + carP1.getHeight());
        Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "set Y: " + carP2.getY() + " " + carP2.getHeight());
        Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "set Y: " + bottomLine + " " + carP1.getBoundBottom() + " " + carP1.getBoundTop());
        Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "set Y: " + bottomLine + " " + carP2.getBoundBottom() + " " + carP2.getBoundTop());*/

        if (carP1 != null)
        {
            if (carP1.getBoundBottom() != 0)
            {
                carP1.setY(bottomLine - (carP1.getBoundBottom() - carP1.getBoundTop()));
                isYCalculated = true;
            }
            //Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "set Y: " + carP1.getY());
        }

        if (carP2 != null)
        {
            if (carP2.getBoundBottom() != 0)
            {
                carP2.setY(bottomLine - (carP2.getBoundBottom() - carP2.getBoundTop()));
                isYCalculated = true;
            }
            //Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "set Y: " + carP2.getY());
        }

        //Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "bottom line: " + bottomLine);
    }

    private boolean canMoveP1Left()
    {
        if (carP1.getBoundLeft() <= 0 || carP1.getBoundLeft() <= wallLeftX + WALL_HEIGHT_WIDTH)
        {
            return false;
        }

        return true;
    }

    private boolean canMoveP2Right()
    {
        if (carP2.getBoundRight() >= Constants.SCREEN_WIDTH || carP2.getBoundRight() >= wallRightX)
        {
            return false;
        }

        return true;
    }

    private boolean doP1TouchLeftWall()
    {
        if (carP1.getBoundLeft() <= wallLeftX + WALL_HEIGHT_WIDTH)
        {
            return true;
        }

        return false;
    }

    private boolean doP2TouchRightWall()
    {
        if (carP2.getBoundRight() >= wallRightX)
        {
            return true;
        }

        return false;
    }

    public void setP1Health(int p1Health) {
        this.p1Health = p1Health;
    }

    public void setP2Health(int p2Health) {
        this.p2Health = p2Health;
    }

    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    private int moveCar(ChassisElement chassisElement)
    {
        switch (chassisElement.getElementIdentity())
        {
            case Constants.C_BOULDER : return 2;
            case Constants.C_CLASSIC : return 3;
            case Constants.C_WHALE : return 4;
            default : return 0;
        }
    }
}
