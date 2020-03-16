package io.domisum.lib.auxiliumlib.run;

import io.domisum.lib.auxiliumlib.util.java.ThreadUtil;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ExecuteOnCommandLineInput
{

	private final Consumer<String> onInput;


	// INIT
	public void start()
	{
		ThreadUtil.createAndStartDaemonThread(()->
		{
			try(Scanner scanner = new Scanner(System.in))
			{
				if(scanner.hasNextLine())
					onInput.accept(scanner.nextLine());
			}
		}, "onCliInput");
	}

}
