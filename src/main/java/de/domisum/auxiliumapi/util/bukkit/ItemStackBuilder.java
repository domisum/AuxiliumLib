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
import org.bukkit.inventory.meta.SkullMeta;

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
	private Material material;
	private int amount = 1;
	private short durability = 0;

	// itemmeta
	private String displayName;
	private List<String> lore;
	private ItemFlag[] flags;
	private Map<Enchantment, Integer> enchantments = new HashMap<>();

	private boolean glowing = false;
	private String skullOwner;


	// -------
	// CONSTRUCTOR
	// -------
	public ItemStackBuilder(Material material)
	{
		this.material = material;
	}

	@APIUsage
	public ItemStackBuilder(ItemStack itemStack)
	{
		// base values
		this.material = itemStack.getType();
		this.amount = itemStack.getAmount();
		this.durability = itemStack.getDurability();

		// item meta
		ItemMeta itemMeta = itemStack.getItemMeta();
		if(itemMeta.hasDisplayName())
			this.displayName = itemMeta.getDisplayName();
		if(itemMeta.hasLore())
			this.lore = new ArrayList<>(itemMeta.getLore());

		Set<ItemFlag> itemFlags = itemMeta.getItemFlags();
		if(!itemFlags.isEmpty())
			this.flags = itemFlags.toArray(new ItemFlag[itemFlags.size()]);

		// enchantments
		this.enchantments = itemMeta.getEnchants();
		if(this.enchantments.size() == 0)
			this.enchantments = null;

		// misc
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

		if(itemMeta instanceof SkullMeta)
			this.skullOwner = ((SkullMeta) itemMeta).getOwner();
	}


	// -------
	// SETTERS
	// -------
	// BASE VALUES
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


	// ITEM META
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


	// ENCHANTMENTS
	@APIUsage
	public ItemStackBuilder enchantment(Enchantment enchantment, int level)
	{
		this.enchantments.put(enchantment, level);

		return this;
	}


	// MISC
	@APIUsage
	public ItemStackBuilder glowing(boolean glowing)
	{
		this.glowing = glowing;

		return this;
	}

	@APIUsage
	public ItemStackBuilder skullOwner(String skullOwner)
	{
		this.skullOwner = skullOwner;

		return this;
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

		if(this.skullOwner != null)
		{
			if(!(itemMeta instanceof SkullMeta) || this.durability != 3)
				throw new IllegalArgumentException("Skull owner cannot be applied to a non-skull item!");

			SkullMeta skullMeta = (SkullMeta) itemMeta;
			skullMeta.setOwner(this.skullOwner);
		}

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


	// -------
	// UTIL
	// -------
	private List<String> processLore(List<String> oldLore)
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

}
