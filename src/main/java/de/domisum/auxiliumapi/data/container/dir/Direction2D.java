package de.domisum.auxiliumapi.data.container.dir;

public enumDirection2D
		{

		// @formatter:off
	NORTH(0, -1),
	SOUTH(0, 1),
	EAST(1, 0),
	WEST(-1, 0);
	// @formatter:on


public final int dX;
public final int dZ;


// -------
// CONSTRUCTOR
// -------
private Direction2D(int dX,int dZ)
		{
		this.dX=dX;
		this.dZ=dZ;
		}


// -------
// GETTERS
// -------
public static Direction2D getRandom(Random r)
		{
		return values()[r.nextInt(values().length)];
		}

public Direction2D getRandomOther(Random r)
		{
		Direction2D dir=null;
		do dir=getRandom(r);
		while(dir==this);

		return dir;
		}

public Direction2D getOpposite()
		{
		if(this==NORTH)
		return SOUTH;
		if(this==SOUTH)
		return NORTH;
		if(this==EAST)
		return WEST;
		if(this==WEST)
		return EAST;

		return null;
		}


public static List<Direction2D> getValuesAsList()
		{
		return new ArrayList<>(Arrays.asList(values()));
		}

		}
