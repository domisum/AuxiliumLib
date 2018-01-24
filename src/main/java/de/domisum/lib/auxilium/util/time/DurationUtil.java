package de.domisum.lib.auxilium.util.time;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DurationUtil
{

	// FORMAT
	@API public static String formatMMSS(Duration duration)
	{
		long durationMs = duration.toMillis();

		if(duration.isNegative())
			durationMs *= -1;

		String sign = duration.isNegative() ? "-" : "";
		return sign+DurationFormatUtils.formatDuration(durationMs, "m:ss");
	}

}
