package net.cubespace.geSuit.configs;

import net.cubespace.geSuit.geSuit;
import net.cubespace.geSuit.configs.SubConfig.AnnouncementEntry;
import net.cubespace.Yamler.Config.YamlConfig;

import java.io.File;
import java.util.HashMap;

public class Announcements extends YamlConfig {
    public Announcements() {
        CONFIG_FILE = new File(geSuit.instance.getDataFolder(), "announcements.yml");
    }

    public Boolean Enabled = true;
    public HashMap<String, AnnouncementEntry> Announcements = new HashMap<>();
}


