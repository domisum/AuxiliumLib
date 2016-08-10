package de.domisum.auxiliumapi.util.bukkit;

import de.domisum.auxiliumapi.util.java.ReflectionUtil;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayerUtil
{

	// MESSAGING
	public static void sendStatusMessage(Player player, String message)
	{
		player.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"["+message+"]");
	}

	// MANIPULATION
	public static void clear(Player player)
	{
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);

		player.setLevel(0);
		player.setExp(0);

		player.setHealth(player.getMaxHealth());
		player.setSaturation(5);
		player.setFoodLevel(20);

		for(PotionEffect pe : player.getActivePotionEffects())
			player.removePotionEffect(pe.getType());

		removeArrows(player);
	}

	public static void removeArrows(Player player)
	{
		// reflection makes this plugin compatible with multiple versions

		try
		{
			// reflection code for this:
			// ((CraftPlayer) player).getHandle().k(0);

			Class<?> craftPlayerClazz = Class.forName(ReflectionUtil.getCBPath()+"entity.CraftPlayer");
			Method getHandle = craftPlayerClazz.getMethod("getHandle");
			Object entityPlayer = getHandle.invoke(player);

			Method setArrows = entityPlayer.getClass().getMethod("k", int.class);
			setArrows.invoke(entityPlayer, 0);
		}
		catch(ClassNotFoundException|NoSuchMethodException|SecurityException|IllegalArgumentException|IllegalAccessException|InvocationTargetException reflectionException)
		{
			reflectionException.printStackTrace();
		}
	}

	public static void respawn(Player player)
	{
		try
		{
			// reflection makes this plugin compatible with multiple versions, but is really complicated

			// reflection code for this:
			// PacketPlayInClientCommand respawnPacket = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);

			Class<?> enumClazz = Class.forName(ReflectionUtil.getNMSPath()+"PacketPlayInClientCommand$EnumClientCommand");
			Object enumValue = enumClazz.getField("PERFORM_RESPAWN").get(null);

			Class<?> packetClazz = Class.forName(ReflectionUtil.getNMSPath()+"PacketPlayInClientCommand");
			Constructor<?> packetConstructor = packetClazz.getConstructor(enumClazz);
			Object packet = packetConstructor.newInstance(enumValue);

			// reflection code for this:
			// ((CraftPlayer) player).getHandle().playerConnection.a(respawnPacket);

			Class<?> craftPlayerClazz = Class.forName(ReflectionUtil.getCBPath()+"entity.CraftPlayer");
			Method getHandle = craftPlayerClazz.getMethod("getHandle");
			Object entityPlayer = getHandle.invoke(player);

			Class<?> entityPlayerClazz = Class.forName(ReflectionUtil.getNMSPath()+"EntityPlayer");
			Field entityPlayerPlayerConnectionField = entityPlayerClazz.getField("playerConnection");

			Object playerConnection = entityPlayerPlayerConnectionField.get(entityPlayer);

			Class<?> playerConnectionClass = Class.forName(ReflectionUtil.getNMSPath()+"PlayerConnection");
			Method a = playerConnectionClass.getMethod("a", packetClazz);
			a.invoke(playerConnection, packet);
		}
		catch(ClassNotFoundException|NoSuchMethodException|SecurityException|NoSuchFieldException|IllegalArgumentException|IllegalAccessException|InstantiationException|InvocationTargetException reflectionException)
		{
			reflectionException.printStackTrace();
		}
	}

	public static void heal(Player player, double health)
	{
		if(health <= 0)
			return;

		Damageable da = player;

		if((da.getHealth()+health) < da.getMaxHealth())
			player.setHealth(da.getHealth()+health);
		else
			player.setHealth(da.getMaxHealth());
	}

	// DAMAGING
	public static void causeDamage(Player player, LivingEntity target, double damage)
	{
		causeDamage(player, ((CraftLivingEntity) target).getHandle(), damage);
	}

	public static void causeDamage(Player player, EntityLiving target, double damage)
	{
		// unsure if this works
		// have to do this in this manner, because
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		target.lastDamager = entityPlayer;
		target.killer = entityPlayer;
		((LivingEntity) target.getBukkitEntity()).damage(damage);
	}

}
