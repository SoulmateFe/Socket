package com.fe.entity;

/**
 * @Description TankMsg
 * @Author sosuke :-)
 * @Date 2021/8/29 19:09
 */
public class TankMsg {
    private int x, y;

    public TankMsg(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "TankMsg{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
