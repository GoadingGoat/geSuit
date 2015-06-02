package net.cubespace.geSuit.moderation;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import com.google.common.collect.Lists;

import net.cubespace.geSuit.geSuit;
import net.cubespace.geSuit.core.GlobalPlayer;
import net.cubespace.geSuit.core.objects.TimeRecord;
import net.cubespace.geSuit.core.objects.Track;
import net.cubespace.geSuit.core.storage.StorageException;
import net.cubespace.geSuit.database.DatabaseManager;
import net.cubespace.geSuit.database.repositories.OnTime;
import net.cubespace.geSuit.database.repositories.Tracking;
import net.cubespace.geSuit.remote.moderation.TrackingActions;

public class TrackingManager implements TrackingActions {
    private Tracking trackingRepo;
    private OnTime ontimeRepo;
    
    public TrackingManager(DatabaseManager database) {
        trackingRepo = database.getTracking();
        ontimeRepo = database.getOntime();
    }
    
    public void updateTracking(GlobalPlayer player) {
        try {
            trackingRepo.insertTracking(player);
        } catch (SQLException e) {
            geSuit.getLogger().log(Level.SEVERE,  "A database exception occured while attempting to update tracking history for " + player.getDisplayName(), e);
        }
    }
    
    @Override
    public List<Track> getHistory(UUID id) throws StorageException {
        try {
            return trackingRepo.getTrackingForUUID(id);
        } catch (SQLException e) {
            geSuit.getLogger().log(Level.SEVERE,  "A database exception occured while attempting to get tracking history for " + id, e);
            throw new StorageException("Unable to retrieve tracking history");
        }
    }
    
    @Override
    public List<Track> getHistory(String name) throws StorageException {
        try {
            return trackingRepo.getTrackingForName(name);
        } catch (SQLException e) {
            geSuit.getLogger().log(Level.SEVERE,  "A database exception occured while attempting to get tracking history for " + name, e);
            throw new StorageException("Unable to retrieve tracking history");
        }
    }
    
    @Override
    public List<Track> getHistory(InetAddress ip) throws StorageException {
        try {
            return trackingRepo.getTrackingForIP(ip);
        } catch (SQLException e) {
            geSuit.getLogger().log(Level.SEVERE,  "A database exception occured while attempting to get tracking history for " + ip.getHostAddress(), e);
            throw new StorageException("Unable to retrieve tracking history");
        }
    }
    
    @Override
    public List<Track> getNameHistory(GlobalPlayer player) throws StorageException {
        try {
            return trackingRepo.getNameHistory(player.getUniqueId());
        } catch (SQLException e) {
            geSuit.getLogger().log(Level.SEVERE,  "A database exception occured while attempting to get name history for " + player.getDisplayName(), e);
            throw new StorageException("Unable to retrieve name history");
        }
    }
    
    @Override
    public TimeRecord getOntime(GlobalPlayer player) throws StorageException {
        try {
            return ontimeRepo.getPlayerOnTime(player.getUniqueId());
        } catch (SQLException e) {
            geSuit.getLogger().log(Level.SEVERE,  "A database exception occured while attempting to get ontime for " + player.getDisplayName(), e);
            throw new StorageException("Unable to retrieve ontime");
        }
    }
    
    @Override
    public List<TimeRecord> getOntimeTop(int offset, int size) throws StorageException {
        try {
            Map<UUID, Long> times = ontimeRepo.getOnTimeTop(offset, size);
            List<TimeRecord> results = Lists.newArrayListWithCapacity(times.size());
            for (Entry<UUID, Long> time : times.entrySet()) {
                results.add(new TimeRecord(time.getKey(), time.getValue()));
            }
            
            return results;
        } catch (SQLException e) {
            geSuit.getLogger().log(Level.SEVERE,  "A database exception occured while attempting to get ontime top", e);
            throw new StorageException("Unable to retrieve ontime top");
        }
    }
}