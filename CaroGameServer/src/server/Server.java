/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Caro game server management
 *
 * @author conqu
 */
public class Server implements Runnable {

    private final int port = 999;

    private ServerSocket serverSocket;
    private HashMap<String, ClientHandler> clients;

    public Server() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
        }
    }

    /**
     * Waiting player join
     */
    @Override
    public void run() {
        try {
            while (!serverSocket.isClosed()) {
                // Accept client
                Socket client = serverSocket.accept();

                ObjectOutputStream out
                        = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream inp
                        = new ObjectInputStream(client.getInputStream());

            }
        } catch (Exception e) {
        }
    }
}
