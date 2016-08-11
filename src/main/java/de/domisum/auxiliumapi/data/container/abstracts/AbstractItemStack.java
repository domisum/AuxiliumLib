package de.domisum.auxiliumapi.data.container.abstracts;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import de.domisum.auxiliumapi.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.auxiliumapi.util.java.annotations.SetByDeserialization;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.List;

@APIUsage
public class AbstractItemStack
{

	// GAME
	@SetByDeserialization
	private Material material;
	@SetByDeserialization
	private int amount;
	@SetByDeserialization
	private short durability;

	@SetByDeserialization
	private String displayName;
	@SetByDeserialization
	private List<String> lore;

	@SetByDeserialization
	private AbstractPotionEffect potionEffect;


	// -------
	// CONSTRUCTOR
	// -------
	@DeserializationNoArgsConstructor
	public AbstractItemStack()
	{

	}

	@APIUsage
	public AbstractItemStack(ItemStack itemStack)
	{
		this.material = itemStack.getType();
		this.amount = itemStack.getAmount();
		this.durability = itemStack.getDurability();

		if(itemStack.hasItemMeta())
		{
			ItemMeta itemMeta = itemStack.getItemMeta();

			if(itemMeta.hasDisplayName())
				this.displayName = itemMeta.getDisplayName();

			if(itemMeta.hasLore())
				this.lore = itemMeta.getLore();
		}
	}

	@APIUsage
	public AbstractItemStack(Material material)
	{
		this.material = material;
		this.amount = 1;
	}


	// -------
	// GETTERS
	// -------
	@APIUsage
	public Material getMaterial()
	{
		return material;
	}

	@APIUsage
	public int getAmount()
	{
		return amount;
	}

	@APIUsage
	public short getDurability()
	{
		return durability;
	}

	@APIUsage
	public ItemStack get()
	{
		ItemStack itemStack = new ItemStack(this.material, this.amount, this.durability);

		ItemMeta itemMeta = itemStack.getItemMeta();
		if(this.displayName != null)
			itemMeta.setDisplayName(this.displayName);
		if(this.lore != null)
			if(!this.lore.isEmpty())
				itemMeta.setLore(this.lore);

		if(material == Material.POTION && potionEffect != null)
			potionEffect.applyTo((PotionMeta) itemMeta);

		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}


	// -------
	// SETTERS
	// -------
	@APIUsage
	public void setAmount(int amount)
	{
		this.amount = amount;
	}

}

