package com.mygdx.snowfall;

public class MyButton {
    float x, y;
    float width, height;

    public MyButton(float x, float y, float size) {
        this.x = x;
        this.y = y;
        width = height = size;
    }

    boolean hit(float tx, float ty) {
        return x<tx & tx<x+width & y<ty & ty<y+height;
    }
}
