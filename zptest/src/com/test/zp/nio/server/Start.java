package com.test.zp.nio.server;

import java.io.*;
import java.io.IOException;

/**
 * @author Taylor Cowan
 */
public class Start {
	public static void main(String[] args) throws IOException {
		if(args.length == 0) args = new String[2];

		args[0] = "8887";
		args[1] = "d:\\";
		int port = Integer.parseInt(args[0]);
		final Server s = new Server(port, new ServerEventHandler(args[1],4));
		System.out.println("Starting server on port " + port);
		System.out.println("Web root folder: " + new File(args[1]).getAbsolutePath());
		while (true)
			s.listen();
	}
}
