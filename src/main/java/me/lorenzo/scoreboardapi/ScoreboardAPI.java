package me.lorenzo.scoreboardapi;

import lombok.Getter;
import lombok.Setter;
import me.lorenzo.scoreboardapi.listener.JoinListener;
import me.lorenzo.scoreboardapi.listener.QuitListener;
import me.lorenzo.scoreboardapi.scoreboard.Board;
import me.lorenzo.scoreboardapi.scoreboard.ScoreboardAdapter;
import me.lorenzo.scoreboardapi.scoreboard.Style;
import me.lorenzo.scoreboardapi.thread.SThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class ScoreboardAPI {

    private final JavaPlugin plugin;

    private ScoreboardAdapter adapter;
    private SThread thread;
    private JoinListener joinListener;
    private QuitListener quitListener;
    private Style style = Style.CLASSIC;

    private Map<UUID, Board> boards;

    private long ticks = 2;
    private boolean hook = false, debugMode = true, callEvents = true;

    private final ChatColor[] chatColorCache = ChatColor.values();


    public ScoreboardAPI(JavaPlugin plugin, ScoreboardAdapter adapter) {
        if (plugin == null) {
            throw new RuntimeException("ScoreboardAPI can not be instantiated without a plugin instance!");
        }

        this.plugin = plugin;
        this.adapter = adapter;
        this.boards = new ConcurrentHashMap<>();

        this.setup();
    }


    public void setup() {
        this.joinListener = new JoinListener(this);
        this.quitListener = new QuitListener(this);

        this.plugin.getServer().getPluginManager().registerEvents(joinListener, this.plugin);
        this.plugin.getServer().getPluginManager().registerEvents(quitListener, this.plugin);


        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }


        for (Player player : this.getPlugin().getServer().getOnlinePlayers()) {
            getBoards().putIfAbsent(player.getUniqueId(), new Board(player, this));
        }


        this.thread = new SThread(this);
    }


    public void cleanup() {
        if (joinListener != null) {
            HandlerList.unregisterAll(joinListener);
            joinListener = null;
        }

        if (quitListener != null) {
            HandlerList.unregisterAll(quitListener);
            quitListener = null;
        }


        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }


        for (UUID uuid : getBoards().keySet()) {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null || !player.isOnline()) {
                continue;
            }

            getBoards().remove(uuid);
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
    }

}
