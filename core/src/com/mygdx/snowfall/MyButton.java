package com.mygdx.snowfall;

public class MyButton {
    float x, y;
    float width, height;

    public MyButton(float x, float y, float size) {
        this.x = x;
        this.y = y;
        width = height = size;
    }

    public MyButton(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    boolean hit(float tx, float ty) {
        return x<tx & tx<x+width & y<ty & ty<y+height;
    }
}