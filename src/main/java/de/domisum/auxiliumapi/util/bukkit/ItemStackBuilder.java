package de.domisum.auxiliumapi.util.bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
	}


	// -------
	// SETTERS
	// -------
	public ItemStackBuilder material(Material material)
	{
		this.material = material;

		return this;
	}

	public ItemStackBuilder amount(int amount)
	{
		this.amount = amount;

		return this;
	}

	public ItemStackBuilder durability(int durability)
	{
		this.durability = (short) durability;

		return this;
	}


	public ItemStackBuilder displayName(String displayName)
	{
		this.displayName = ChatColor.WHITE + displayName;

		return this;
	}

	public ItemStackBuilder lore(String... lore)
	{
		this.lore = new ArrayList<>();
		for(String l : lore)
			this.lore.add(ChatColor.WHITE + l);

		return this;
	}

	public ItemStackBuilder addLore(List<String> lore)
	{
		if(this.lore == null)
			this.lore = new ArrayList<>();

		for(String l : lore)
			this.lore.add(ChatColor.WHITE + l);

		return this;
	}

	public ItemStackBuilder flags(ItemFlag... flags)
	{
		this.flags = flags;

		return this;
	}


	public ItemStackBuilder enchantment(Enchantment enchantment, int level)
	{
		this.enchantments.put(enchantment, level);

		return this;
	}


	// -------
	// BUILDING
	// -------
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

		return itemStack;
	}

}
