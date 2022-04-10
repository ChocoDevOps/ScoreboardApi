package me.lorenzo.scoreboardapi.thread;

import me.lorenzo.scoreboardapi.ScoreboardAPI;
import me.lorenzo.scoreboardapi.scoreboard.ScoreboardEntry;
import me.lorenzo.scoreboardapi.scoreboard.Board;
import me.lorenzo.scoreboardapi.scoreboard.exception.ScoreboardAPIException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.List;

public class SThread extends Thread {

    private final ScoreboardAPI scoreboardAPI;

    public SThread(ScoreboardAPI scoreboardAPI) {
        this.scoreboardAPI = scoreboardAPI;
        this.start();
    }

    @Override
    public void run() {
        while(true) {
            try {
                tick();
                sleep(scoreboardAPI.getTicks() * 50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    
    private void tick() {
        for (Player player : this.scoreboardAPI.getPlugin().getServer().getOnlinePlayers()) {
            try {
                Board board = this.scoreboardAPI.getBoards().get(player.getUniqueId());


                if (board == null) {
                    continue;
                }

                Scoreboard scoreboard = board.getScoreboard();
                Objective objective = board.getObjective();

                if (scoreboard == null || objective == null) {
                    continue;
                }



                String title = ChatColor.translateAlternateColorCodes('&', this.scoreboardAPI.getAdapter().getTitle(player));


                if (!objective.getDisplayName().equals(title)) {
                    objective.setDisplayName(title);
                }

                List<String> newLines = this.scoreboardAPI.getAdapter().getLines(player);


                if (newLines == null || newLines.isEmpty()) {
                    board.getEntries().forEach(ScoreboardEntry::remove);
                    board.getEntries().clear();
                } else {
                    if (newLines.size() > 15) {
                        newLines = newLines.subList(0, 15);
                    }


                    if (!this.scoreboardAPI.getStyle().isDescending()) {
                        Collections.reverse(newLines);
                    }


                    if (board.getEntries().size() > newLines.size()) {
                        for (int i = newLines.size(); i < board.getEntries().size(); i++) {
                            ScoreboardEntry entry = board.getEntryAtPosition(i);

                            if (entry != null) {
                                entry.remove();
                            }
                        }
                    }


                    int cache = this.scoreboardAPI.getStyle().getStartNumber();
                    for (int i = 0; i < newLines.size(); i++) {
                        ScoreboardEntry entry = board.getEntryAtPosition(i);


                        String line = ChatColor.translateAlternateColorCodes('&', newLines.get(i));




                        if (entry == null) {
                            entry = new ScoreboardEntry(board, line, i);
                        }


                        entry.setText(line);
                        entry.setup();
                        entry.send(
                                this.scoreboardAPI.getStyle().isDescending() ? cache-- : cache++
                        );
                    }
                }

                if (player.getScoreboard() != scoreboard && !scoreboardAPI.isHook()) {
                    player.setScoreboard(scoreboard);
                }
            } catch(Exception e) {
                e.printStackTrace();
                throw new ScoreboardAPIException("There was an error updating " + player.getName() + "'s scoreboard.");
            }
        }
    }

}
