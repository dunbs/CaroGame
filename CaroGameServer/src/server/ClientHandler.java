/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Coordinate;
import model.Invitation;
import model.InvitationResponse;
import model.User;
import model.UserDisconnected;

/**
 *
 * @author conqu
 */
public class ClientHandler implements Runnable {
    private User user;
    private ObjectOutputStream out;
    private ObjectInputStream inp;
    private Server server;

    public ClientHandler(ObjectOutputStream out, ObjectInputStream inp, Server server) {
        this.out = out;
        this.inp = inp;
        this.server = server;
        setUser();
    }

    private void setUser() {
        try {
            String name = null;

            System.out.println("[Get Name] BEGIN");

            // Read until get an unused name;
            do {
                if (name != null) {
                    out.writeObject(null);
                }

                System.out.println("[Get Name] READ UTF");

                name = (String) inp.readObject();
                System.out.println("[Get Name]" + name);
            } while (this.server.isUsedName(name));

            // Send accepted user to client
            User user = new User(name);
            out.writeObject(user);
            this.user = user;

            // Start listening
            startListen();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            this.user = new User("");
        } catch (ClassNotFoundException ex) {
            this.user = new User("");
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Online users information
     */
    protected void sendOnlineUsers(Set<User> onlineUsers) {
        try {
            out.writeObject(onlineUsers);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Listen for client response
     */
    private void startListen() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        String message;
        Invitation invitation;
        Coordinate coordinate;
        InvitationResponse invitationResponse;
        UserDisconnected userDisconnected;

        Object data;

        // repeatedly read message from client
        try {
            while (!this.server.isClosed()) {
                data = inp.readObject();
                System.out.println("[" + this.user.getName() + " RECEIVE] " + data);

                // Message type
                if (data instanceof String) {
                    message = (String) data;
                }

                // Invitation type
                if (data instanceof Invitation) {
                    invitation = (Invitation) data;
                    this.server.sendObject(invitation, invitation.getTo());
                }

                if (data instanceof InvitationResponse) {
                    invitationResponse = (InvitationResponse) data;
                    this.server.sendObject(
                            invitationResponse, invitationResponse.getOpponent());
                }

                // Move to coordinate
                if (data instanceof Coordinate) {
                    coordinate = (Coordinate) data;
                    String opponent = coordinate.getPlayer().getOpponentName();
                    this.server.sendObject(data, opponent);
                }

                if (data instanceof UserDisconnected) {
                    userDisconnected = (UserDisconnected) data;
                    String opponent = userDisconnected.getTo();
                    this.server.sendObject(data, opponent);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void sendObject(Object data) {
        try {
            System.out.println("[" + this.user.getName() + " SEND] " + data);
            out.writeObject(data);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User getUser() {
        return user;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public ObjectInputStream getInp() {
        return inp;
    }

    public void setInp(ObjectInputStream inp) {
        this.inp = inp;
    }

}
