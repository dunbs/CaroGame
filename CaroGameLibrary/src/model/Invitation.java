/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 * Invitation of player to play a game
 *
 * @author conqu
 */
public class Invitation implements Serializable {

    public final static String INVITATION_RECEIVED_MESSAGE = "%s has challenge you to a %dx%d Carogame \n Do you accept?";
    public final static String INVITATION_RECEIVED_TITLE = "You are being challenged";

    private String from;
    private String to;
    private int size;

    public Invitation(String from, String to, int size) {
        this.to = to;
        this.from = from;
        this.size = size;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getSize() {
        return size;
    }
}
