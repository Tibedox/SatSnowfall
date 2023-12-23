package com.mygdx.snowfall;

import static com.mygdx.snowfall.Snowfall.SCR_HEIGHT;
import static com.mygdx.snowfall.Snowfall.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Snowflake {
    float x, y;
    float vx, vy;
    float width, height;
    float angle, speedRotation;

    public Snowflake() {
        respawn();
    }

    void move() {
        x += vx;
        y += vy;
        if(y<0-height){
            respawn();
        }
        angle += speedRotation;
    }

    void respawn() {
        x = MathUtils.random(0, SCR_WIDTH);
        y = MathUtils.random(SCR_HEIGHT, SCR_HEIGHT*2);
        vy = -MathUtils.random(1f, 3f);
        vx = MathUtils.random(-0.5f, 0.5f);
        float k = 477f/413; // коэффициент пропорциональности изображения снежинки
        width = MathUtils.random(20f, 40f);
        height = width*k;
        speedRotation = MathUtils.random(-3f, 3f);
    }

    boolean hit(float tx, float ty) {
        return x<tx & tx<x+width & y<ty & ty<y+height;
    }
}
