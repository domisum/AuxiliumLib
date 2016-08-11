package de.domisum.auxiliumapi.data.container.abstracts;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import de.domisum.auxiliumapi.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.auxiliumapi.util.java.annotations.SetByDeserialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map.Entry;

@APIUsage
public class AbstractPlayerInventory
{

	// PROPERTIES
	@SetByDeserialization
	private HashMap<Integer, AbstractItemStack> items = new HashMap<>();
	@SetByDeserialization
	private int heldItemSlot;


	// -------
	// CONSTRUCTOR
	// -------
	@DeserializationNoArgsConstructor
	public AbstractPlayerInventory()
	{

	}

	@APIUsage
	public AbstractPlayerInventory(Player player)
	{
		PlayerInventory inventory = player.getInventory();

		// items in main inventory
		for(int i = 0; i < inventory.getSize(); i++)
		{
			ItemStack is = inventory.getItem(i);

			if(is == null)
				continue;

			this.items.put(i, new AbstractItemStack(is));
		}

		// armor
		for(int i = 100; i < 104; i++)
		{
			ItemStack is = null;
			if(i == 100)
				is = inventory.getBoots();
			else if(i == 101)
				is = inventory.getLeggings();
			else if(i == 102)
				is = inventory.getChestplate();
			else if(i == 103)
				is = inventory.getHelmet();

			if(is == null)
				continue;

			this.items.put(i, new AbstractItemStack(is));
		}

		this.heldItemSlot = inventory.getHeldItemSlot();
	}


	// -------
	// INVENTORY
	// -------
	@APIUsage
	public void applyTo(Player player)
	{
		PlayerInventory inventory = player.getInventory();

		inventory.clear();
		inventory.setArmorContents(null);

		for(Entry<Integer, AbstractItemStack> entry : this.items.entrySet())
		{
			int slot = entry.getKey();
			ItemStack itemStack = entry.getValue().get();

			if(slot == 100)
				inventory.setBoots(itemStack);
			else if(slot == 101)
				inventory.setLeggings(itemStack);
			else if(slot == 102)
				inventory.setChestplate(itemStack);
			else if(slot == 103)
				inventory.setHelmet(itemStack);
			else
				inventory.setItem(entry.getKey(), entry.getValue().get());
		}

		inventory.setHeldItemSlot(this.heldItemSlot);
		player.updateInventory();
	}

}

