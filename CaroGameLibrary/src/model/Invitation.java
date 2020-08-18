/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Invitation of player to play a game
 *
 * @author conqu
 */
public class Invitation {
    private String name;
    private int size;
    private Player player;

    public Invitation(String name, int size, Player player) {
        this.name = name;
        this.size = size;
        this.player = player;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
