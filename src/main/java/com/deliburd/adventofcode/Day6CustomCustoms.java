package com.deliburd.adventofcode;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.deliburd.adventofcode.annotations.Day;
import com.deliburd.adventofcode.annotations.EntryPoint;
import com.deliburd.adventofcode.util.ResourceFiles;

@Day(6)
public class Day6CustomCustoms {
	@EntryPoint
	public static void start() {
		var customsData = ResourceFiles.getResourceString("day6input.txt");

		System.out.println("Part 1: " + yesAnySum(customsData));
		System.out.println("Part 2: " + yesAllSum(customsData));
	}

	private static long yesAllSum(String customsData) {
		return Arrays.stream(customsData.split("\\n\\n"))
				.mapToLong(Day6CustomCustoms::getYesAllAnswers)
				.sum();
	}
	
	private static long getYesAllAnswers(String customsData) {
		var groupData = customsData.replaceAll("\\n", "").chars()
				.boxed()
				.collect(Collectors.toSet());
		
		return customsData.lines()
				.map(line -> line.chars().boxed().collect(Collectors.toSet()))
				.collect(() -> groupData, (differenceSet, lineSet) -> differenceSet.retainAll(lineSet), Set::addAll)
				.size();
	}

	private static long yesAnySum(String customsData) {
		customsData = customsData.replaceAll("\\n(?!\\n)", "");
		customsData = customsData.replaceAll("\\n\\n", "\\n");
		
		return customsData.lines()
				.mapToLong(data -> data.chars().distinct().count())
				.sum();
	}
}
