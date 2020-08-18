/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import constant.Constant;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.User;

/**
 * Caro game server management
 *
 * @author conqu
 */
public class Server implements Runnable {

    private ServerSocket serverSocket;
    private HashMap<String, ClientHandler> clients;

    public Server() {
        clients = new HashMap<>();
        try {
            serverSocket = new ServerSocket(Constant.SERVER_PORT);
            this.startListen();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Server port occupied, please check port " + Constant.SERVER_PORT);
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public boolean isClosed() {
        return serverSocket.isClosed();
    }

    /**
     * New Player Management
     */
    protected boolean isUsedName(String name) {
        return this.clients.containsKey(name);
    }

    /**
     * Waiting player join
     */
    private void startListen() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            while (!serverSocket.isClosed()) {
                // Accept client
                Socket client = serverSocket.accept();

                System.out.println("[NEW USER ACCESSED]");

                ObjectOutputStream out
                        = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream inp
                        = new ObjectInputStream(client.getInputStream());

                ClientHandler clientHandler = new ClientHandler(out, inp, this);

                // Send all current online users except fresher
                out.writeObject(new ArrayList<>(clients.keySet()));
                User newUser = clientHandler.getUser();
                newPlayerJoined(newUser);
                clients.put(
                        newUser.getName(),
                        clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newPlayerJoined(User user) {
        clients.values().forEach((client) -> {
            client.sendObject(user);
        });
    }

    /**
     * Send data to user
     *
     * @param data
     * @param name
     */
    protected void sendObject(Object data, String name) {
        this.clients.get(name).sendObject(data);
    }
}
