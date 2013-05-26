package com.craftrealms.playerlog;

import com.google.gson.Gson;
import uk.org.whoami.authme.cache.auth.PlayerAuth;
import uk.org.whoami.authme.security.PasswordSecurity;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SocketHandler implements Runnable {
    private Socket server;
    String line,input;
    private PlayerLog p;
    private SocketServer s;
    private PrintStream out;
    public boolean isAuthed = false;
    private String username = null;

    SocketHandler(Socket server, PlayerLog plugin, SocketServer socketserver) {
        this.server=server;
        this.p = plugin;
        this.s = socketserver;
    }

    public void run () {

        input="";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            out = new PrintStream(server.getOutputStream());

            while((line = in.readLine()) != null && !line.equals(".")) {
                input=input + line;
                out.println("I got:" + line);
                String[] req = line.split(":");
                if(req[0].equalsIgnoreCase("login")) {
                    PlayerAuth pAuth = p.auth.database.getAuth(req[1]);
                    try {
                        if(PasswordSecurity.comparePasswordWithHash(req[2], pAuth.getHash(), req[1])) {
                            out.println("ok");
                            username = req[1];
                            isAuthed = true;
                            s.playersockets.put(req[1], this);
                        } else {
                            out.println("bad");
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                } else if(req[0].equalsIgnoreCase("getcommands") && isAuthed) {
                    Gson json = new Gson();
                    ResultSet rs = p.sqlfetch("SELECT date, command FROM command WHERE player = '" + req[1] + "' AND server = '" + p.servername + "' ORDER BY  date ASC ");
                    out.println(json.toJson(rs));
                }
                else if(line.equalsIgnoreCase("close")) {
                    break;
                }else if(isAuthed) {
                    out.println("command not found");
                } else {
                    out.println("no login");
                }
            }

            s.playersockets.remove(username);
            server.close();
        } catch (IOException ioe) {
            System.out.println("IOException on socket listen: " + ioe);
            ioe.printStackTrace();
        }
    }
    public void sendMessage(String message) {
        out.println(message);
    }
}

