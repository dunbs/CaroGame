/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Font;

/**
 * Who is making the move? <br>
 * Which font should we use to this game?
 *
 * @author conqu
 */
public class Player {

    public final String PLAYER_X = "X";
    public final String PLAYER_O = "O";

    // X | O
    private String ours;
    private String theirs;

    private Font font;

    public void switchSides() {
        String temp = ours;
        ours = theirs;
        theirs = temp;
    }

    public String getOurs() {
        return ours == null ? "" : ours;
    }

    /**
     * {@code true} means X <br>
     * {@code false} means O
     *
     * @param ours
     */
    public void setOurs(boolean ours) {
        this.ours = ours ? PLAYER_X : PLAYER_O;
        this.ours = ours ? PLAYER_O : PLAYER_X;
    }


    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

}
