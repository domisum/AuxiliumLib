package de.domisum.auxiliumapi.data.container.abstracts;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import de.domisum.auxiliumapi.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.auxiliumapi.util.java.annotations.SetByDeserialization;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@APIUsage
public class AbstractPotionEffect
{

	// PROPERTIES
	@SetByDeserialization
	private String type;
	@SetByDeserialization
	private int amplifier;
	@SetByDeserialization
	private int duration;

	// STATUS
	private PotionEffectType type_;


	// -------
	// CONSTRUCTOR
	// -------
	@DeserializationNoArgsConstructor
	public AbstractPotionEffect()
	{

	}


	// -------
	// GETTERS
	// -------
	private PotionEffectType getType()
	{
		if(type_ == null)
			determinePotionEffectType();

		return type_;
	}

	private void determinePotionEffectType()
	{
		type_ = PotionEffectType.getByName(type);
		if(type == null)
			throw new IllegalArgumentException("The potion effect type '"+type+"' does not exist");
	}


	// -------
	// APPLICATION
	// -------
	@APIUsage
	public void applyTo(PotionMeta potionMeta)
	{
		PotionEffect effect = new PotionEffect(getType(), duration, amplifier);
		potionMeta.addCustomEffect(effect, true);
	}

}
