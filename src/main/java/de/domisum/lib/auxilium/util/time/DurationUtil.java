package de.domisum.lib.auxilium.util.time;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DurationUtil
{

	@API public static String format(Duration duration)
	{
		if(duration.isNegative())
			return "-"+format(duration.abs());

		String format = "s's'";

		if(duration.compareTo(Duration.ofMinutes(1)) >= 0)
			format = "m'm':"+format;

		if(duration.compareTo(Duration.ofHours(1)) >= 0)
			format = "H'h':"+format;


		String string = DurationFormatUtils.formatDuration(duration.toMillis(), format);

		// done like this because "S" gives leading zeroes, don't want that
		if((duration.toMillis()%1000) != 0)
			string += ":"+(duration.toMillis()%1000)+"ms";

		return string;
	}


	// NOW
	@API public static Duration toNow(Temporal from)
	{
		return Duration.between(from, Instant.now());
	}

}
