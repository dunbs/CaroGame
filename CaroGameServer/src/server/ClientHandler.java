/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Coordinate;
import model.Invitation;

/**
 *
 * @author conqu
 */
public class ClientHandler implements Runnable {
    private String name;
    private ObjectOutputStream out;
    private ObjectInputStream inp;
    private Server server;

    public ClientHandler(ObjectOutputStream out, ObjectInputStream inp, Server server) {
        this.out = out;
        this.inp = inp;
        this.server = server;
        setName();
    }

    private void setName() {
        try {
            String name = inp.readUTF();
            this.name = name;
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            this.name = "";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    /**
     * Listen for client response
     */
    @Override
    public void run() {
        String message;
        Invitation invitation;
        Coordinate coordinate;

        Object data;

        // repeatedly read message from client
        try {
            while ((data = inp.readObject()) != null) {

                // Message type
                if (data instanceof String) {
                    message = (String) data;
                }

                // Invitation type
                if (data instanceof Invitation) {
                    invitation = (Invitation) data;
                }

                // Move to coordinate
                if (data instanceof Coordinate) {
                    coordinate = (Coordinate) data;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        }
    }

}
