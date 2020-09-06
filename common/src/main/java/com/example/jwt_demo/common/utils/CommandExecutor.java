package com.example.jwt_demo.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandExecutor {

	private CommandExecutor() {
	}

	public static CommandExecutor getInstace() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static final CommandExecutor INSTANCE = new CommandExecutor();
	}

	public boolean execute(String[] command) {
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream(true);

		return invokeProcess(builder, true);
	}

	public boolean execute(String[] command, String execDir) {
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(new File(execDir));
		builder.redirectErrorStream(true);

		return invokeProcess(builder, true);
	}

	private boolean invokeProcess(ProcessBuilder builder, boolean isLogging) {
		Process process;
		try {
			process = builder.start();
			try (InputStream itsOutput = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(itsOutput);
				BufferedReader br = new BufferedReader(isr)) {
				String line = null;
				if (isLogging) {
					log.info("STDOUT/STDERR");
					while ((line = br.readLine()) != null) {
						log.info(line);
					}
				} else {
					while ((line = br.readLine()) != null) {
					}
				}
				int ret = process.waitFor();
				if (ret != 0) {
					log.error("FATAL ERROR execute failed RET CODE: {}", ret);
					return false;
				}
			} catch (Exception e) {
				log.error("execute failed {}", e.getMessage());
				return false;
			}
		} catch (IOException e1) {
			log.error("execute failed {}", e1.getMessage());
			return false;
		}
		return true;
	}
}
