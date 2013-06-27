package org.twobuilderstwotools.tristan.TwoBuildersTwoToolsCommands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TwoBuildersTwoToolsCommands extends JavaPlugin implements Listener {

    File configFile;
    FileConfiguration config;
    List<String> blacklist;
    String blacklistMessage;

    @Override
    public void onEnable() {

        configFile = new File(getDataFolder(), "config.yml");

        try {
            firstRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
        config = new YamlConfiguration();
        loadYamls();

        getServer().getPluginManager().registerEvents(this, this);
        blacklist = config.getStringList("blacklisted-commands");
        blacklistMessage = config.getString("blacklisted-message");

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage();
        Player p = e.getPlayer();
        if (!p.isOp() || !p.hasPermission("2b2t.noblacklist")) {
            if (blacklist.contains(command.toLowerCase())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', blacklistMessage));
            }
        }
    }

    private void firstRun() throws Exception {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            copy(getResource("config.yml"), configFile);
        }
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadYamls() {
        try {
            config.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
