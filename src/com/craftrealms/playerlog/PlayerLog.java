package com.craftrealms.playerlog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import net.db.MySQL;

import org.bukkit.plugin.java.JavaPlugin;
import uk.org.whoami.authme.AuthMe;

import static uk.org.whoami.authme.api.API.hookAuthMe;

public class PlayerLog extends JavaPlugin {
	String sqlhost;
	String sqluser;
	String sqlpass;
	String sqldb;
	String servername;
    int socketport;
    int maxsocketconn;
    public AuthMe auth;
	MySQL MySQL;
	Connection c = null;
    SocketServer server = null;
    boolean usesql = false;

    @Override
    public void onEnable(){
    	saveDefaultConfig();
    	reloadConfig();
    	sqlhost = getConfig().getString("mysql.host");
    	sqluser = getConfig().getString("mysql.user");
    	sqlpass = getConfig().getString("mysql.pass");
    	sqldb = getConfig().getString("mysql.db");
        socketport = getConfig().getInt("socketport");
    	servername = getConfig().getString("server");
        maxsocketconn = getConfig().getInt("maxsocketconnections");
        usesql = getConfig().getBoolean("usesql");
        if(usesql) {
            MySQL = new MySQL(sqlhost, "3306", sqldb, sqluser, sqlpass);
            c = MySQL.open();
        }
        auth = hookAuthMe();
    	new PlayerListener(this);
        new Thread(server = new SocketServer(this, socketport, maxsocketconn)).start();
        getCommand("test").setExecutor(new CommandTest(this));
        this.log("PlayerLog has been enabled!");
    }
 
    @Override
    public void onDisable() {
        if(usesql) {
            try {
                c.close();
            } catch (SQLException e) {
                log("MySQL was never up so we cant close it");
            } catch (NullPointerException e) {
                log("MySQL was not opened properly. Probably the database wasnt responding during server startup");
            }
        }
        this.log("PlayerLog has been disabled!");
    }
    
    public void log(String info) {
    	getLogger().info(info);
    }

    public void warning(String warning) {
        getLogger().warning(warning);
    }
    
    public void sqlinsert(String query) throws SQLException {
		Statement statement = c.createStatement();
		statement.execute(query);
	}
    public ResultSet sqlfetch(String query) {
        ResultSet rs = null;
        try {
            Statement statement = c.createStatement();
            statement.execute(query);
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
