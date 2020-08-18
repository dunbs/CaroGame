/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author conqu
 */
public class Coordinate implements Serializable {
    private int x;
    private int y;
    private final Player player;

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

    public Coordinate(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "Coordinate{" + "x=" + x + ", y=" + y + ", player=" + player + '}';
    }

}
