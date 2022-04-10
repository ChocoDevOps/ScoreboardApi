package me.lorenzo.scoreboardapi.listener;

import lombok.Getter;
import me.lorenzo.scoreboardapi.ScoreboardAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
public class QuitListener implements Listener {
    private final ScoreboardAPI scoreboardAPI;

    public QuitListener(ScoreboardAPI scoreboardAPI) {
        this.scoreboardAPI = scoreboardAPI;
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        this.getScoreboardAPI().getBoards().remove(event.getPlayer().getUniqueId());
        event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }
}
