package me.lorenzo.scoreboardapi.scoreboard;

import lombok.Getter;
import me.lorenzo.scoreboardapi.ScoreboardAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Board {

	private final ScoreboardAPI scoreboardAPI;

	private final List<ScoreboardEntry> entries = new ArrayList<>();
	private final List<String> identifiers = new ArrayList<>();

	private final UUID uuid;

	
	public Board(Player player, ScoreboardAPI scoreboardAPI) {
		this.uuid = player.getUniqueId();
		this.scoreboardAPI = scoreboardAPI;
		this.setup(player);
	}

	
	public Scoreboard getScoreboard() {
		Player player = Bukkit.getPlayer(getUuid());
		if (this.getScoreboardAPI().isHook() || player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
			return player.getScoreboard();
		} else {
			return Bukkit.getScoreboardManager().getNewScoreboard();
		}
	}

	
	public Objective getObjective() {
		Scoreboard scoreboard = getScoreboard();
		if (scoreboard.getObjective("ScoreboardAPI") == null) {
			Objective objective = scoreboard.registerNewObjective("ScoreboardAPI", "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName(this.getScoreboardAPI().getAdapter().getTitle(Bukkit.getPlayer(getUuid())));
			return objective;
		} else {
			return scoreboard.getObjective("ScoreboardAPI");
		}
	}

	
	private void setup(Player player) {
		Scoreboard scoreboard = getScoreboard();
		player.setScoreboard(scoreboard);
		getObjective();
	}

	
	public ScoreboardEntry getEntryAtPosition(int pos) {
		return pos >= this.entries.size() ? null : this.entries.get(pos);
	}

	
	public String getUniqueIdentifier(int position) {
		String identifier = getRandomChatColor(position) + ChatColor.WHITE;

		while (this.identifiers.contains(identifier)) {
			identifier = identifier + getRandomChatColor(position) + ChatColor.WHITE;
		}


		if (identifier.length() > 16) {
			return this.getUniqueIdentifier(position);
		}


		this.identifiers.add(identifier);

		return identifier;
	}

	
	private String getRandomChatColor(int position) {
		return scoreboardAPI.getChatColorCache()[position].toString();
	}

}
