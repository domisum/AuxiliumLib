package io.domisum.lib.auxiliumlib.work.reserver;

import io.domisum.lib.auxiliumlib.util.Compare;
import io.domisum.lib.auxiliumlib.work.reserver.s.InsertWorkReserver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class ThrottlingWorkReserverTest
{
	
	// TEST
	@Test
	public void fractional()
	{
		double perMinute = 0.5;
		var minInterval = Duration.ofMinutes(2).minusSeconds(5);
		var testDuration = Duration.ofMinutes(30);
		
		
		var b = new InsertWorkReserver<String>();
		for(int i = 0; i < 100; i++)
			b.insert(i + "");
		
		var clock = new OffsetClock(Instant.parse("2026-04-01T14:00:00Z"));
		var t = new ThrottlingWorkReserver<>(b, clock, perMinute);
		
		Instant last = null;
		while(Compare.lessThan(clock.getOffset(), testDuration))
		{
			var workOpt = t.getWorkOptional();
			workOpt.ifPresent(ReservedWork::close);
			if(workOpt.isPresent())
			{
				var now = clock.get();
				if(last != null)
				{
					var since = Duration.between(last, now);
					System.out.println(since);
					if(Compare.greaterThan(clock.getOffset(), Duration.ofMinutes(10)))
						Assertions.assertTrue(Compare.greaterThan(since, minInterval));
				}
				System.out.println(now);
				last = now;
			}
			
			clock.advance(Duration.ofMillis(383));
		}
	}
	
	
	// UTIL
	@RequiredArgsConstructor
	private static class OffsetClock
		implements Supplier<Instant>
	{
		
		private final Instant base;
		@Getter @Setter private Duration offset = Duration.ZERO;
		
		
		// INTERFACE
		@Override
		public Instant get() {return base.plus(offset);}
		
		public void advance(Duration delta) {this.offset = this.offset.plus(delta);}
		
	}
	
}
