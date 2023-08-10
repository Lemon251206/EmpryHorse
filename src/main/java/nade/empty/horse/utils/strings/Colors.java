package nade.empty.horse.utils.strings;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class Colors {
	
    private Colors() {};

	public static String vanilla(String text) {
		if (text != null && !text.isEmpty()) {
			return ChatColor.translateAlternateColorCodes('&', text);
		}
		return text;
	}
	
	public static String deVanilla(String text) {
		if (text != null && !text.isEmpty()) {
			return text.replace('§', '&');
		}
		return text;
	}
	
	public static List<String> vanillaList(List<String> list) {
		List<String> color = new ArrayList<>();
		for (String text : list) {
			color.add(vanilla(text));
		}
		return color;
	}
}
