package de.domisum.lib.auxilium.util.bukkit;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
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

/**
 * A helper class to easily construct ItemStacks with custom properties.
 */
@APIUsage
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


	/*
	// CONSTRUCTOR
	*/

	/**
	 * Creates an ItemStackBuilder and sets the material to the material given by the parameter.
	 * <p>
	 * The values besides the material are unchanged from the default values.
	 *
	 * @param material the material
	 */
	@APIUsage
	public ItemStackBuilder(Material material)
	{
		this.material = material;
	}

	/**
	 * Creates an ItemStackBuilder from an already existing ItemStack,
	 * copying all its attributes to this ItemStackBuilder.
	 *
	 * @param itemStack the ItemStack
	 */
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

		// misc
		glow:
		if(this.enchantments.size() == 0)
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


	/*
	// SETTERS
	*/
	// BASE VALUES

	/**
	 * Sets the material of this ItemStackBuilder.
	 *
	 * @param material the material
	 * @return this
	 */
	@APIUsage
	public ItemStackBuilder material(Material material)
	{
		this.material = material;

		return this;
	}

	/**
	 * Sets the amount of this ItemStackBuilder.
	 * <p>
	 * Negative values or values out of the usual range of the ItemStack are also allowed,
	 * but at a certain point the value won't be displayed on the ItemStack or may lead to errors.
	 *
	 * @param amount the amount
	 * @return this
	 */
	@APIUsage
	public ItemStackBuilder amount(int amount)
	{
		this.amount = amount;

		return this;
	}

	/**
	 * Sets the durability of this ItemStackBuilder.
	 * <p>
	 * This modifies the durability bar for tools and armor,
	 * changes the subId of certain items like wool or color or
	 * for some Materials has no effect at all.
	 *
	 * @param durability the durability
	 * @return this
	 */
	@APIUsage
	public ItemStackBuilder durability(int durability)
	{
		this.durability = (short) durability;

		return this;
	}


	// ITEM META

	/**
	 * Sets the displayName of this ItemStackBuilder.
	 * <p>
	 * This method automatically prepends the ChatColor white to the displayName,
	 * avoiding the default italic font of the displayName.
	 * This doesn't inhibit the use of ChatColors or specifically ChatColor.ITALIC, it only changes
	 * the format without any ChatColor.
	 *
	 * @param displayName the displayName
	 * @return this
	 */
	@APIUsage
	public ItemStackBuilder displayName(String displayName)
	{
		this.displayName = ChatColor.WHITE+displayName;

		return this;
	}

	/**
	 * Sets the lore of this ItemStackBuilder.
	 * <p>
	 * If any lore has been set or appended before, this overwrites it, just leaving the lore from the parameter.
	 * <p>
	 * Before the lore of the ItemStackBuilder is set to the lore from the parameter,
	 * it is processed, following these steps:
	 * <ol>
	 * <li>Lines containing {@code \n} are split into two lines</li>
	 * <li>Every line is prepended with ChatColor.WHITE, to avoid the default purple lines</li>
	 * </ol>
	 *
	 * @param loreArray varargs/array of lore line Strings
	 * @return this
	 */
	@APIUsage
	public ItemStackBuilder lore(String... loreArray)
	{
		this.lore = processLore(new ArrayList<>(Arrays.asList(loreArray)));

		return this;
	}

	/**
	 * @param additionalLoreArray the new lines to add to the lore
	 * @return this
	 * @see #addLore(List)
	 */
	@APIUsage
	public ItemStackBuilder addLore(String... additionalLoreArray)
	{
		return addLore(new ArrayList<>(Arrays.asList(additionalLoreArray)));
	}

	/**
	 * Adds the lore lines from the parameter to the already existing lore lines.
	 * <p>
	 * If no lore existed before this metode is used, it works like {@code lore(String...)}
	 *
	 * @param additionalLore the new lines to add to the lore
	 * @return this
	 */
	@APIUsage
	public ItemStackBuilder addLore(List<String> additionalLore)
	{
		additionalLore = processLore(additionalLore);

		if(this.lore == null)
			this.lore = new ArrayList<>();

		this.lore.addAll(additionalLore);

		return this;
	}

	/**
	 * Sets the ItemFlags of this ItemStackBuilder.
	 * <p>
	 * If some flags were set before using this method, they are overwritten.
	 *
	 * @param flags the flags
	 * @return this
	 */
	@APIUsage
	public ItemStackBuilder flags(ItemFlag... flags)
	{
		this.flags = flags;

		return this;
	}


	// ENCHANTMENTS

	/**
	 * Adds an enchantment to this ItemStackBuilder.
	 * <p>
	 * The level of the enchantment is the level displayed in roman numerals in the game.
	 * <p>
	 * Multiple enchantments can be added to this ItemStackBuilder without overwriting the previous one.
	 * If this ItemStackBuilder already contains an Enchantment of the same type (at any level), it will
	 * be overwritten.
	 *
	 * @param enchantment the enchantment type
	 * @param level       the level of the enchantment
	 * @return this
	 */
	@APIUsage
	public ItemStackBuilder enchantment(Enchantment enchantment, int level)
	{
		// TODO check if the level set here is the same ingame or if 0 here means I ingame
		this.enchantments.put(enchantment, level);

		return this;
	}


	// MISC

	/**
	 * Set the ItemStackBuilder to glow. Glowing simulates the glowing effect of enchanted ItemStacks,
	 * but doesn't display any Enchantment.
	 *
	 * @param glowing whether the ItemStackBuilder should glow
	 * @return this
	 */
	@APIUsage
	public ItemStackBuilder glowing(boolean glowing)
	{
		this.glowing = glowing;

		return this;
	}

	/**
	 * Sets the skullOwner of this ItemStackBuilder. This sets the skin of the skull item to the player,
	 * whose name is given with the {@code skullOwner} parameter.
	 * <p>
	 * This method can be called regardles of the Material of the ItemStack builder.
	 * If the skullOwner is set, but the Material of the ItemStackBuilder doesn't allow a skullOwner
	 * when {@code #build()} is called, an Exception will be thrown.
	 *
	 * @param skullOwner the name of the skullOwner
	 * @return this
	 */
	@APIUsage
	public ItemStackBuilder skullOwner(String skullOwner)
	{
		this.skullOwner = skullOwner;

		return this;
	}


	/*
	// BUILDING
	*/

	/**
	 * Builds an ItemStack from the properties of the ItemStackBuilder.
	 * <p>
	 * This method can be called multiple times on a single ItemStackBuilder.
	 * After building the ItemStack, changes can be made to the ItemStackBuilder
	 * (they won't impact the already built ItemStack) in order to build another ItemStack with altered
	 * properties.
	 * <p>
	 * If no material was set before calling this method, an Exception will be thrown.
	 *
	 * @return the built ItemStack
	 * @throws IllegalStateException    if material is not set
	 * @throws IllegalArgumentException if skullOwner is set (not null),
	 *                                  but the material does not support SkullOwner
	 */
	@APIUsage
	public ItemStack build()
	{
		if(this.material == null)
			throw new IllegalStateException("No material was specified for the itemstack");

		ItemStack itemStack = new ItemStack(this.material, this.amount, this.durability);

		ItemMeta itemMeta = itemStack.getItemMeta();
		if(this.displayName != null)
			itemMeta.setDisplayName(this.displayName);
		if(this.lore != null)
			itemMeta.setLore(this.lore);
		if(this.flags != null)
			itemMeta.addItemFlags(this.flags);

		itemStack.addUnsafeEnchantments(this.enchantments);

		if(this.glowing)
			if(this.enchantments.size() == 0)
				itemStack = makeGlow(itemStack);

		if(this.skullOwner != null)
		{
			if(!(itemMeta instanceof SkullMeta) || this.durability != 3)
				throw new IllegalArgumentException("Skull owner cannot be applied to a non-skull item!");

			SkullMeta skullMeta = (SkullMeta) itemMeta;
			skullMeta.setOwner(this.skullOwner);
		}

		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	/**
	 * Helper method that makes the copy of an ItemStack glow by editing its NMS properties.
	 * <p>
	 * The ItemStack from the parameter remains unchanged.
	 *
	 * @param itemStack the ItemStack to make glow
	 * @return a glowing copy of the ItemStack
	 */
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


	/*
	// UTIL
	*/

	/**
	 * Helper method to process lore lines.
	 * <p>
	 * It splits lines containing {@code \n} into multiple lines
	 * and prepends {@code ChatColor.WHITE} to every line.
	 * <p>
	 * The lore from the parameter remains unchanged.
	 *
	 * @param oldLore the lines of lore to be processed
	 * @return the processed lore
	 */
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
