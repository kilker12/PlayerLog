package com.craftrealms.playerlog;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.db.MySQL;

import org.bukkit.plugin.java.JavaPlugin;

public class PlayerLog extends JavaPlugin {
	private String sqlhost;
	private String sqluser;
	private String sqlpass;
	private String sqldb;
	public String server;
	MySQL MySQL;
	Connection c = null;
	
    @Override
    public void onEnable(){
    	saveDefaultConfig();
    	reloadConfig();
    	sqlhost = getConfig().getString("mysql.host");
    	sqluser = getConfig().getString("mysql.user");
    	sqlpass = getConfig().getString("mysql.pass");
    	sqldb = getConfig().getString("mysql.db");
    	server = getConfig().getString("server");
    	MySQL = new MySQL(sqlhost, "3306", sqldb, sqluser, sqlpass);
    	c = MySQL.open();
    	new PlayerListener(this);
        this.log("PlayerLog has been enabled!");
    }
 
    @Override
    public void onDisable() {
        try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        this.log("PlayerLog has been disabled!");
    }
    
    public void log(String info) {
    	getLogger().info(info);
    }
    
    public void sqlinsert(String query) throws SQLException {
		Statement statement = c.createStatement();
		statement.execute(query);
	}
}
