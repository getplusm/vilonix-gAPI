package plusm.vilonix.gapi.module.playertime;

/*
import org.bukkit.Bukkit;
import plusm.vilonix.gapi.loader.DartaAPI;

public class Playtime {

    private int id;
    private String server;
    private long time;
    private int playerId;

    public Playtime(int playerId) {
        this.playerId = playerId;
    }

    public Playtime(int id, int playerId, String server, long time) {
        this.id = id;
        this.playerId = playerId;
        this.server = server;
        this.time = time;
    }

    public void create(String server, long time) {
        Bukkit.getScheduler().runTaskAsynchronously(DartaAPI.getInstance(), () -> {
            try {
                PreparedStatement statement = Database.getConnection().prepareStatement("INSERT INTO playtime(player, server, time) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, server);
                statement.setLong(3, time);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void setTime(long time, String server) {
        Bukkit.getScheduler().runTaskAsynchronously(Time.getInstance(), () -> {
            try {
                PreparedStatement statement = Database.getConnection().prepareStatement("UPDATE playtime SET time = ? WHERE player = ? AND server = ?");
                statement.setLong(1, time);
                statement.setString(2, uuid.toString());
                statement.setString(3, server);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void reset() {
        Bukkit.getScheduler().runTaskAsynchronously(Time.getInstance(), () -> {
            try {
                PreparedStatement statement = Database.getConnection().prepareStatement("DELETE FROM playtime WHERE player = ?");
                statement.setString(1, uuid.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void resetAllMinutes() {
        Bukkit.getScheduler().runTaskAsynchronously(Time.getInstance(), () -> {
            try {
                PreparedStatement statement = Database.getConnection().prepareStatement("DELETE FROM playtime");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean hasTime(String server) {
        try {
            PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM playtime WHERE player = ? AND server = ?");
            statement.setString(1, uuid.toString());
            statement.setString(2, server);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getTotalTime() {
        try {
            PreparedStatement statement = Database.getConnection().prepareStatement("SELECT sum(time) as total FROM playtime WHERE player = ?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return rs.getLong("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    public long getTime(String server) {
        try {
            PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM playtime WHERE player = ? AND server = ?");
            statement.setString(1, uuid.toString());
            statement.setString(2, server);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return rs.getLong("time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    public List<Playtime> getTimes() {
        List<Playtime> time = new ArrayList<Playtime>();
        try {
            PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM playtime WHERE player = ?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                time.add(new Playtime(rs.getInt("ID"), UUID.fromString(rs.getString("player")), rs.getString("server"), rs.getLong("time")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return time;
    }

    public int getID() {
        return id;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getServer() {
        return server;
    }

    public long getTime() {
        return time;
    }


}*/