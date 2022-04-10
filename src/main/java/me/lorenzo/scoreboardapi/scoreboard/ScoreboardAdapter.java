package me.lorenzo.scoreboardapi.scoreboard;

import java.util.List;
import org.bukkit.entity.Player;

public interface ScoreboardAdapter {

	
	String getTitle(Player player);

	
	List<String> getLines(Player player);

}
