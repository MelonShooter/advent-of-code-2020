package com.deliburd.adventofcode.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.io.Resources;

public class ResourceFiles {
	private ResourceFiles() {}
	
	public static List<String> getResourceLines(String inputFileName) {
		try {
			return FileUtils.readLines(FileUtils.toFile(Resources.getResource(inputFileName)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
