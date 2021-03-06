package com.craftrealms.playerlog;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {
	private PlayerLog plugin;
	
	public PlayerListener(PlayerLog plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	@EventHandler
	public void PlayerChat(AsyncPlayerChatEvent e) {
        String chan;
        String message;
        if(e.getMessage().length() > 4) {
            chan = e.getMessage().substring(e.getMessage().length() - 4);
            message = e.getMessage().substring(0, e.getMessage().length() -4);
        } else {
            chan = "LOCL";
            message = e.getMessage();
        }
		if(chan.equalsIgnoreCase("GLOB")) {
            e.setMessage(message);
            try {
                this.plugin.sqlinsert("INSERT INTO  `playerlog`.`chat` (`id` ,`player` ,`date` ,`message` ,`server`) VALUES (NULL ,  '" + e.getPlayer().getName().toLowerCase() + "', CURRENT_TIMESTAMP ,  'GLOBAL: " + message.replace("'", "") + "', '" + this.plugin.servername + "')");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } else if(chan.equalsIgnoreCase("HELP")) {
            e.setMessage(message);
            try {
                this.plugin.sqlinsert("INSERT INTO  `playerlog`.`chat` (`id` ,`player` ,`date` ,`message` ,`server`) VALUES (NULL ,  '" + e.getPlayer().getName().toLowerCase() + "', CURRENT_TIMESTAMP ,  'HELP: " + message.replace("'", "") + "', '" + this.plugin.servername + "')");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } else if(chan.equalsIgnoreCase("TRAD")) {
            e.setMessage(message);
            try {
                this.plugin.sqlinsert("INSERT INTO  `playerlog`.`chat` (`id` ,`player` ,`date` ,`message` ,`server`) VALUES (NULL ,  '" + e.getPlayer().getName().toLowerCase() + "', CURRENT_TIMESTAMP ,  'TRADE: " + message.replace("'", "") + "', '" + this.plugin.servername + "')");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } else {
            try {
                this.plugin.sqlinsert("INSERT INTO  `playerlog`.`chat` (`id` ,`player` ,`date` ,`message` ,`server`) VALUES (NULL ,  '" + e.getPlayer().getName().toLowerCase() + "', CURRENT_TIMESTAMP ,  'LOCAL: " + e.getMessage().replace("'", "") + "', '" + this.plugin.servername + "')");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
	}
	
	@EventHandler
	public void PlayerCommand(PlayerCommandPreprocessEvent c) {
        String[] cmd = c.getMessage().substring(1).split(" ");
		if(!cmd[0].equalsIgnoreCase("login")) {
            if(cmd[0].equalsIgnoreCase("g")) {
                c.setMessage(c.getMessage() + "GLOB");
            } else if(cmd[0].equalsIgnoreCase("h")) {
                c.setMessage(c.getMessage() + "HELP");
            } else if(cmd[0].equalsIgnoreCase("trade")) {
                c.setMessage(c.getMessage() + "TRAD");
            } else {
			    try {
				    this.plugin.sqlinsert("INSERT INTO  `playerlog`.`command` (`id` ,`player` ,`date` ,`command` ,`server`) VALUES (NULL ,  '" + c.getPlayer().getName().toLowerCase() + "', CURRENT_TIMESTAMP ,  '" + c.getMessage().replace("'", "") + "', '" + this.plugin.servername + "')");
			    } catch (SQLException e1) {
				    e1.printStackTrace();
			    }
            }
		}
	}
	
	@EventHandler
	public void PlayerChestOpen(PlayerInteractEvent c) {
		if(c.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(c.getClickedBlock().getType().getId() == 54) {
				try {
					this.plugin.sqlinsert("INSERT INTO  `playerlog`.`chest` (`id` ,`player` ,`date` ,`x` ,`y` ,`z` ,`server`) VALUES (NULL ,  '" + c.getPlayer().getName().toLowerCase() + "', CURRENT_TIMESTAMP ,  '" + Integer.toString(c.getClickedBlock().getX()) + "', '" + Integer.toString(c.getClickedBlock().getY()) + "', '" + Integer.toString(c.getClickedBlock().getZ()) + "', '" + this.plugin.servername + "')");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@EventHandler
	public void PlayerTeleport(PlayerTeleportEvent t) {
		Integer fromx = t.getFrom().getBlockX();
		Integer fromy = t.getFrom().getBlockY();
		Integer fromz = t.getFrom().getBlockZ();
		Integer tox = t.getTo().getBlockX();
		Integer toy = t.getTo().getBlockY();
		Integer toz = t.getTo().getBlockZ();
		if(!tox.equals(fromx) && !tox.equals(fromx + 5) && !tox.equals(fromx - 5)) {
			try {
				this.plugin.sqlinsert("INSERT INTO  `playerlog`.`teleport` (`id` ,`player` ,`date` ,`fromx` ,`fromy` ,`fromz` ,`tox` ,`toy` ,`toz` ,`server`) VALUES (NULL ,  '" + t.getPlayer().getName().toLowerCase() + "', CURRENT_TIMESTAMP ,  '" + Integer.toString(fromx) + "', '" + Integer.toString(fromy) + "', '" + Integer.toString(fromz) + "', '" + Integer.toString(tox) + "', '" + Integer.toString(toy) + "', '" + Integer.toString(toz) + "', '" + this.plugin.servername + "')");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void PlayerLogin(PlayerLoginEvent l) {
		try {
			this.plugin.sqlinsert("INSERT INTO  `playerlog`.`loginlogout` (`id` ,`player` ,`date` ,`log` ,`server`) VALUES (NULL ,  '" + l.getPlayer().getName().toLowerCase() + "', CURRENT_TIMESTAMP ,  'login', '" + this.plugin.servername + "')");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent event) {
        try {
            this.plugin.sqlinsert("INSERT INTO  `playerlog`.`loginlogout` (`id` ,`player` ,`date` ,`log` ,`server`) VALUES (NULL ,  '" + event.getPlayer().getName().toLowerCase() + "', CURRENT_TIMESTAMP ,  'logout', '" + this.plugin.servername + "')");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
	
	@EventHandler
	public void PlayerDeath(PlayerDeathEvent d) {
		if(d.getEntity().getKiller() != null) {
			try {
				this.plugin.sqlinsert("INSERT INTO  `playerlog`.`killdeath` (`id` ,`date` ,`victim` ,`killer` ,`server`) VALUES (NULL ,CURRENT_TIMESTAMP , '" + d.getEntity().getName().toLowerCase() + "' ,'" + d.getEntity().getKiller().getName().toLowerCase() + "', '" + this.plugin.servername + "')");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
    @EventHandler
    public void PlayerRightClick(PlayerInteractEntityEvent event) {
        if(event.getRightClicked() instanceof Player) {
            if(event.getPlayer().hasPermission("playerlog.client")) {
                Player clicked = ((Player) event.getRightClicked()).getPlayer();
                Player clicker = event.getPlayer();
                plugin.server.playersockets.get(clicker.getName().toLowerCase()).sendMessage("show:" + clicked.getName().toLowerCase() + ":" + plugin.servername);
            }
        }
    }
}
