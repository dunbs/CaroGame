/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 * Players in the current game
 *
 * @author conqu
 */
public class Player implements Serializable {

    public static final boolean PLAYER_FIRST = true;
    public static final boolean PLAYER_SECOND = false;
    public static final String PLAYER_X = "X";
    public static final String PLAYER_O = "O";

    private boolean initiate;

    // X | O
    private String ours;
    private String theirs;

    // Name
    private String myName;
    private String opponentName;

    public Player switchSide() {
        Player opponent = new Player(myName, opponentName, !initiate);
        opponent.setOurs(ours.equals(PLAYER_O));
        return opponent;
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
        this.theirs = ours ? PLAYER_O : PLAYER_X;
    }

    public String getTheirs() {
        return theirs;
    }

    public Player(String myName, String opponentName, boolean initiate) {
        this.myName = myName;
        this.opponentName = opponentName;
        this.initiate = initiate;
    }

    public String getMyName() {
        return myName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public boolean isInitiate() {
        return initiate;
    }
}
