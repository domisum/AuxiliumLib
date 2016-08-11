package de.domisum.auxiliumapi.util.bukkit;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.NBTTagList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemStackBuilder
{

	// PROPERTIES
	// basic
	protected Material material;
	protected int amount = 1;
	protected short durability = 0;

	// itemmeta
	protected String displayName;
	protected List<String> lore;
	protected ItemFlag[] flags;
	protected Map<Enchantment, Integer> enchantments = new HashMap<>();

	protected boolean glowing = false;


	// -------
	// CONSTRUCTOR
	// -------
	public ItemStackBuilder(Material material)
	{
		this.material = material;
	}

	public ItemStackBuilder(ItemStack itemStack)
	{
		this.material = itemStack.getType();
		this.amount = itemStack.getAmount();
		this.durability = itemStack.getDurability();

		ItemMeta itemMeta = itemStack.getItemMeta();
		if(itemMeta.hasDisplayName())
			this.displayName = itemMeta.getDisplayName();
		if(itemMeta.hasLore())
			this.lore = new ArrayList<>(itemMeta.getLore());

		Set<ItemFlag> itemFlags = itemMeta.getItemFlags();
		if(!itemFlags.isEmpty())
			this.flags = itemFlags.toArray(new ItemFlag[itemFlags.size()]);

		this.enchantments = itemMeta.getEnchants();
		if(this.enchantments.size() == 0)
			this.enchantments = null;

		// glowing
		glow:
		if(this.enchantments == null)
		{
			net.minecraft.server.v1_9_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
			if(!nmsStack.hasTag())
				break glow;

			NBTTagCompound tag = nmsStack.getTag();
			NBTTagList enchantments = (NBTTagList) tag.get("ench");
			if(enchantments == null)
				break glow;

			this.glowing = true;
		}
	}


	// -------
	// SETTERS
	// -------
	@APIUsage
	public ItemStackBuilder material(Material material)
	{
		this.material = material;

		return this;
	}

	@APIUsage
	public ItemStackBuilder amount(int amount)
	{
		this.amount = amount;

		return this;
	}

	@APIUsage
	public ItemStackBuilder durability(int durability)
	{
		this.durability = (short) durability;

		return this;
	}


	@APIUsage
	public ItemStackBuilder displayName(String displayName)
	{
		this.displayName = ChatColor.WHITE+displayName;

		return this;
	}

	@APIUsage
	public ItemStackBuilder lore(String... loreArray)
	{
		this.lore = processLore(new ArrayList<>(Arrays.asList(loreArray)));

		return this;
	}

	@APIUsage
	public ItemStackBuilder addLore(List<String> additionalLore)
	{
		additionalLore = processLore(additionalLore);

		if(this.lore == null)
			this.lore = new ArrayList<>();

		this.lore.addAll(additionalLore);

		return this;
	}

	@APIUsage
	public ItemStackBuilder flags(ItemFlag... flags)
	{
		this.flags = flags;

		return this;
	}


	@APIUsage
	public ItemStackBuilder enchantment(Enchantment enchantment, int level)
	{
		this.enchantments.put(enchantment, level);

		return this;
	}

	@APIUsage
	public ItemStackBuilder setGlowing(boolean glowing)
	{
		this.glowing = glowing;

		return this;
	}

	@APIUsage
	protected List<String> processLore(List<String> oldLore)
	{
		List<String> lore = new ArrayList<>();

		for(String l : oldLore)
		{
			String[] splitLine = l.split("\\n");
			for(String s : splitLine)
				lore.add(ChatColor.WHITE+s);
		}

		return lore;
	}


	// -------
	// BUILDING
	// -------
	@APIUsage
	public ItemStack build()
	{
		if(this.material == null)
			throw new IllegalArgumentException("No material was specified for the itemstack");

		ItemStack itemStack = new ItemStack(this.material, this.amount, this.durability);

		ItemMeta itemMeta = itemStack.getItemMeta();
		if(this.displayName != null)
			itemMeta.setDisplayName(this.displayName);
		if(this.lore != null)
			itemMeta.setLore(this.lore);
		if(this.flags != null)
			itemMeta.addItemFlags(this.flags);
		itemStack.setItemMeta(itemMeta);

		if(this.enchantments != null)
			itemStack.addUnsafeEnchantments(this.enchantments);

		if(this.glowing)
			if(this.enchantments == null ? true : this.enchantments.size() == 0)
				itemStack = makeGlow(itemStack);

		return itemStack;
	}

	@APIUsage
	protected static ItemStack makeGlow(ItemStack itemStack)
	{
		net.minecraft.server.v1_9_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag = null;
		if(!nmsStack.hasTag())
		{
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		}

		if(tag == null)
			tag = nmsStack.getTag();

		NBTTagList ench = new NBTTagList();
		tag.set("ench", ench);
		nmsStack.setTag(tag);

		return CraftItemStack.asCraftMirror(nmsStack);
	}

}
