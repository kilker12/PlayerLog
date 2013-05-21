package com.craftrealms.playerlog;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Title:        Sample Server
 * Description:  This utility will accept input from a socket, posting back to the socket before closing the link.
 * It is intended as a template for coders to base servers on. Please report bugs to brad at kieser.net
 * Copyright:    Copyright (c) 2002
 * Company:      Kieser.net
 * @author B. Kieser
 * @version 1.0
 */

public class SocketServer implements Runnable {
    private PlayerLog p;
    private int maxConnections;
    private int port;
    public HashMap<String, SocketHandler> playersockets = new HashMap<String, SocketHandler>();

    public SocketServer(PlayerLog plugin, int po, int maxConnection) {
        p = plugin;
        port = po;
        maxConnections = maxConnection;
    }

    @Override
    public void run() {
        int i=0;
        try{
            ServerSocket listener = new ServerSocket(port);
            Socket server;
            while((i++ < maxConnections) || (maxConnections == 0)){
                SocketHandler connection;

                server = listener.accept();
                SocketHandler conn_c= new SocketHandler(server, p, this);
                Thread t = new Thread(conn_c);
                t.start();
            }
        } catch (IOException ioe) {
            p.warning("IOException on socket listen: " + ioe);
            ioe.printStackTrace();
        }
    }
}

