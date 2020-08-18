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
public class InvitationResponse implements Serializable {

    public final static String INVITATION_REFUSE_MESSAGE = "%s has refused to play with you.";

    private final boolean accepted;
    private String opponent;
    private String accepter;
    private int size;

    public boolean isAccepted() {
        return accepted;
    }

    public String getOpponent() {
        return opponent;
    }

    public String getAccepter() {
        return accepter;
    }

    public int getSize() {
        return size;
    }

    public InvitationResponse(String accepter, String opponent, int size, boolean accepted) {
        this.accepter = accepter;
        this.opponent = opponent;
        this.size = size;
        this.accepted = accepted;
    }
}
