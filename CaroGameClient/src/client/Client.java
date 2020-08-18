/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import GUI.GameGUI;
import constant.Constant;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import model.Coordinate;
import model.Invitation;
import model.InvitationResponse;
import model.Player;
import model.User;
import model.UserDisconnected;

/**
 *
 * @author conqu
 */
public class Client implements Runnable {
    private Socket client;
    private User user;
    private ObjectOutputStream out;
    private ObjectInputStream inp;
    private DefaultListModel<String> onlineList;
    private HashMap<String, GameGUI> games;

    public User getUser() {
        return user;
    }

    /**
     * Connect to server.<br>
     * Get User name.<br>
     * Get Current online List <br>
     *
     * @param onlineList
     */
    public Client(DefaultListModel<String> onlineList) {
        games = new HashMap<>();

        try {
            System.out.println("[INITITATE] Trying to connect");
            client = new Socket(Constant.SERVER_HOST, Constant.SERVER_PORT);

            // Init input output
            System.out.println("[INITITATE] Connect success");
            out = new ObjectOutputStream(client.getOutputStream());
            inp = new ObjectInputStream(client.getInputStream());

            // Get User Name
            getUserName();


            // Get online people
            this.onlineList = onlineList;
            onlineList.add(0, user.getName() + " (me)");

            // Add all current online users
            ArrayList<String> name = (ArrayList<String>) inp.readObject();
            name.forEach((e) -> onlineList.addElement(e));

            // Listen for information
            startListening();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cannot connect to server");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Server error");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    private void getUserName() throws IOException, ClassNotFoundException {
        String name = JOptionPane.showInputDialog("Enter your desired name");

        // Send name to server
        out.writeObject(name);

            Object data = inp.readObject();

            while (!(data instanceof User)) {
                name = JOptionPane.showInputDialog("An user has already chosen that name :< \nPlease choose a different name");

                // Send name to server
                out.writeObject(name);

                data = inp.readObject();
        }

        this.user = (User) data;
    }

    private void addNewUser(User user) {
        onlineList.addElement(user.getName());
    }

    public void sendObject(Object data) {
        try {
            out.writeObject(data);
            System.out.println("[" + this.user.getName() + " SEND] " + data);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Caro function
     */
    private void createNewGame(Player player, int size) {
        GameGUI gameUI = new GameGUI(player, size, this);
        java.awt.EventQueue.invokeLater(() -> {
            gameUI.setVisible(true);
        });

        // Add game to map
        String opponent = player.getMyName();
        opponent = opponent.equals(this.user.getName())
                ? player.getOpponentName() : opponent;

        games.put(opponent, gameUI);
    }

    private void move(Coordinate coordinate) {
        String opponent = coordinate.getPlayer().getMyName();
        GameGUI gameGUI = games.get(opponent);
        gameGUI.opponentMove(coordinate);
    }

    /**
     * Invite others
     */
    /**
     *
     * @param opponent
     * @param size
     */
    public void invite(String opponent, int size) {
        if (opponent.equals(this.user.getName())) {
            // Cannot invite yourself
            return;
        }
        Invitation invitation = new Invitation(this.user.getName(), opponent, size);
        sendObject(invitation);

    }

    /**
     *
     * @return {@code true} Opponent accepted your invitation <br> {@code false}
     * Opponent denied your invitation
     */
    private void receivedInvitationResponse(InvitationResponse invitationResponse) {
        if (invitationResponse.isAccepted()) {
            Player player = new Player(this.user.getName(), invitationResponse.getAccepter(), true);
            player.setOurs(Player.PLAYER_FIRST);
            System.out.println("[INVITE] " + player.getMyName());
            createNewGame(player, invitationResponse.getSize());
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    String.format(
                            InvitationResponse.INVITATION_REFUSE_MESSAGE,
                            invitationResponse.getOpponent()));
        }
    }

    /**
     * Listening
     */
    private void startListening() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        String message;
        Invitation invitation;
        InvitationResponse invitationResponse;
        Coordinate coordinate;
        User newUser;
        UserDisconnected userDisconnected;

        Object data;

        // repeatedly read message from client
        try {
            while ((data = inp.readObject()) != null) {
                System.out.println("[" + this.user.getName() + " RECEIVE] " + data);
                // Message type
                if (data instanceof String) {
                    message = (String) data;
                }

                // Invitation received
                if (data instanceof Invitation) {
                    invitation = (Invitation) data;
                    receivedInvitation(invitation);
                }

                // InvitationResponse
                if (data instanceof InvitationResponse) {
                    invitationResponse = (InvitationResponse) data;
                    receivedInvitationResponse(invitationResponse);
                }

                // Move to coordinate
                if (data instanceof Coordinate) {
                    coordinate = (Coordinate) data;
                    this.move(coordinate);
                }

                // New user joined
                if (data instanceof User) {
                    newUser = (User) data;
                    this.addNewUser(newUser);
                }

                if (data instanceof UserDisconnected) {
                    userDisconnected = (UserDisconnected) data;
                    this.playerDisconnect(userDisconnected);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Invitation received
     */
    private void receivedInvitation(Invitation invitation) {
        String opponent = invitation.getFrom();
        int size = invitation.getSize();

        String message = Invitation.INVITATION_RECEIVED_MESSAGE;
        message = String.format(message, opponent, size, size);

        if (JOptionPane.showConfirmDialog(null, message, Invitation.INVITATION_RECEIVED_TITLE, JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION) {
            invitationAccepted(invitation);
            return;
        };

        invitationRefused(invitation);
    }

    private void invitationAccepted(Invitation invitation) {
        // Send accept
        this.sendObject(new InvitationResponse(
                this.getUser().getName(),
                invitation.getFrom(),
                invitation.getSize(),
                true));

        Player player = new Player(this.user.getName(), invitation.getFrom(), false);
        player.setOurs(Player.PLAYER_SECOND);
        System.out.println("[ACCEPT] " + player.getMyName());
        createNewGame(player, invitation.getSize());
    }

    private void invitationRefused(Invitation invitation) {
        // Send refused
        this.sendObject(new InvitationResponse(
                this.getUser().getName(),
                invitation.getFrom(),
                invitation.getSize(),
                false));
    }

    /**
     * Player disconnect
     */
    private void playerDisconnect(UserDisconnected userDisconnected) {
        String opponent = userDisconnected.getFrom();
        this.games.get(opponent).opponentDisconnect();
        this.games.remove(opponent);
    }
}
