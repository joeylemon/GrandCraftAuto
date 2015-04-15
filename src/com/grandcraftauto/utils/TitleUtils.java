package com.grandcraftauto.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R2.PlayerConnection;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.grandcraftauto.core.Main;

public class TitleUtils {
	
	private static Main main = Main.getInstance();

	public static final void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message){
		sendTitle(player, fadeIn, stay, fadeOut, message, null);
	}
	
	public static final void sendSubtitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message){
		sendTitle(player, fadeIn, stay, fadeOut, null, message);
	}
	
	public static final void sendFullTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle){
		sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
	}
	
	public static final void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle){
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

		PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
		connection.sendPacket(packetPlayOutTimes);

		if (subtitle != null) {
			subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
			subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
			IChatBaseComponent titleSub = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
			PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, titleSub);
			connection.sendPacket(packetPlayOutSubTitle);
		}

		if (title != null) {
			title = title.replaceAll("%player%", player.getDisplayName());
			title = ChatColor.translateAlternateColorCodes('&', title);
			IChatBaseComponent titleMain = ChatSerializer.a("{\"text\": \"" + title + "\"}");
			PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleMain);
			connection.sendPacket(packetPlayOutTitle);
		}
	}
	
	public static final void sendTabTitle(Player player, String header, String footer){
		if (header == null) header = "";
		header = ChatColor.translateAlternateColorCodes('&', header);

		if (footer == null) footer = "";
		footer = ChatColor.translateAlternateColorCodes('&', footer);

		header = header.replaceAll("%player%", player.getDisplayName());
		footer = footer.replaceAll("%player%", player.getDisplayName());

		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		IChatBaseComponent tabTitle = ChatSerializer.a("{\"text\": \"" + header + "\"}");
		IChatBaseComponent tabFoot = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
		PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter(tabTitle);

		try {
			Field field = headerPacket.getClass().getDeclaredField("b");
			field.setAccessible(true);
			field.set(headerPacket, tabFoot);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.sendPacket(headerPacket);
		}
	}
	
	private static Class<?> c1 = null;
	private static Class<?> c2 = null;
	private static Class<?> c3 = null;
	private static Class<?> c4 = null;
	private static Class<?> c5 = null;
	
	public static void sendActionBar(Player player, String message){
		try{
			if(c1 == null){
				c1 = Class.forName("org.bukkit.craftbukkit." + main.nmsver + ".entity.CraftPlayer");
				c4 = Class.forName("net.minecraft.server." + main.nmsver + ".PacketPlayOutChat");
				c5 = Class.forName("net.minecraft.server." + main.nmsver + ".Packet");
			}
			Object p = c1.cast(player);
			Object ppoc = null;
			if((main.nmsver.equalsIgnoreCase("v1_8_R1")) || (!main.nmsver.startsWith("v1_8_"))){
				if(c2 == null){
					c2 = Class.forName("net.minecraft.server." + main.nmsver + ".ChatSerializer");
					c3 = Class.forName("net.minecraft.server." + main.nmsver + ".IChatBaseComponent");
				}
				Method m3 = c2.getDeclaredMethod("a", new Class[] { String.class });
				Object cbc = c3.cast(m3.invoke(c2, new Object[] { "{\"text\": \"" + message + "\"}" }));
				ppoc = c4.getConstructor(new Class[] { c3, Byte.TYPE }).newInstance(new Object[] { cbc, Byte.valueOf((byte) 2) });
			}else{
				if(c2 == null){
					c2 = Class.forName("net.minecraft.server." + main.nmsver + ".ChatComponentText");
					c3 = Class.forName("net.minecraft.server." + main.nmsver + ".IChatBaseComponent");
				}
				Object o = c2.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
				ppoc = c4.getConstructor(new Class[] { c3, Byte.TYPE }).newInstance(new Object[] { o, Byte.valueOf((byte) 2) });
			}
			Method m1 = c1.getDeclaredMethod("getHandle", new Class[0]);
			Object h = m1.invoke(p, new Object[0]);
			Field f1 = h.getClass().getDeclaredField("playerConnection");
			Object pc = f1.get(h);
			Method m5 = pc.getClass().getDeclaredMethod("sendPacket", new Class[] { c5 });
			m5.invoke(pc, new Object[] { ppoc });
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

}
