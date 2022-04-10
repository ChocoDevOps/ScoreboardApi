package me.lorenzo.scoreboardapi.listener;

import lombok.Getter;
import me.lorenzo.scoreboardapi.scoreboard.Board;
import me.lorenzo.scoreboardapi.ScoreboardAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Getter
public class JoinListener implements Listener {
    private final ScoreboardAPI scoreboardAPI;

    public JoinListener(ScoreboardAPI scoreboardAPI) {
        this.scoreboardAPI = scoreboardAPI;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.getScoreboardAPI().getBoards().put(event.getPlayer().getUniqueId(), new Board(event.getPlayer(), this.getScoreboardAPI()));
    }
}
