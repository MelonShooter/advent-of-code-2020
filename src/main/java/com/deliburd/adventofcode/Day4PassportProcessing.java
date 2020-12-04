package com.deliburd.adventofcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.deliburd.adventofcode.annotations.Day;
import com.deliburd.adventofcode.annotations.EntryPoint;
import com.deliburd.adventofcode.util.ResourceFiles;
import com.google.common.collect.ImmutableSet;

@Day(4)
public class Day4PassportProcessing {
	private static final Pattern heightPattern = Pattern.compile("(\\d+)(cm|in)");
	private static final Pattern hairColorPattern = Pattern.compile("#[a-f0-9]{6}");
	private static final Pattern keyPattern = Pattern.compile("([a-z]{3}):([^\\s]+)");
	private static final Set<String> eyeColors = ImmutableSet.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
	private static final Set<String> fields = ImmutableSet.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
	private static final Map<String, Predicate<String>> fieldsPart2 = new HashMap<>();
	
	
	
	@EntryPoint
	public static void start() {
		fieldsPart2.put("byr", (key) -> key.length() == 4 && Integer.valueOf(key) >= 1920 && Integer.valueOf(key) <= 2002);
		fieldsPart2.put("iyr", (key) -> key.length() == 4 && Integer.valueOf(key) >= 2010 && Integer.valueOf(key) <= 2020);
		fieldsPart2.put("eyr", (key) -> key.length() == 4 && Integer.valueOf(key) >= 2020 && Integer.valueOf(key) <= 2030);
		fieldsPart2.put("hgt", Day4PassportProcessing::isValidHeight);
		fieldsPart2.put("hcl", (key) -> hairColorPattern.matcher(key).matches());
		fieldsPart2.put("ecl", (key) -> eyeColors.contains(key));
		fieldsPart2.put("pid", Day4PassportProcessing::isValidPassportID);
		
		String passportData = ResourceFiles.getResourceString("day4input.txt");
		System.out.println("Part 1: " + getValidPassportCount(passportData, Day4PassportProcessing::isValidPassport));
		System.out.println("Part 2: " + getValidPassportCount(passportData, Day4PassportProcessing::isValidPassport2));
	}

	private static boolean isValidPassportID(String key) {
		try {
			Integer.valueOf(key);
			
			return key.length() == 9;
		} catch(NumberFormatException e) {
			return false;
		}
	}

	private static boolean isValidHeight(String key) {
		var heightMatcher = heightPattern.matcher(key);
		
		if(!heightMatcher.matches()) {
			return false;
		}
		
		int height = Integer.valueOf(heightMatcher.group(1));
		String unit = heightMatcher.group(2);
		
		if(unit.equals("cm")) {
			return height >= 150 && height <= 193;
		} else {
			return height >= 59 && height <= 76;
		}
	}

	private static long getValidPassportCount(String passportData, Predicate<String> validPassportPredicate) {
		passportData = passportData.replaceAll("\\n(?!\\n)", " ");
		passportData = passportData.replaceAll("\\n\\n", "\\n");
		
		return passportData.lines()
			.filter(validPassportPredicate)
			.count();
	}
	
	private static boolean isValidPassport(String passport) {
		return keyPattern.matcher(passport).results()
				.map(result -> result.group(1))
				.collect(Collectors.toSet())
				.containsAll(fields);
	}
	
	private static boolean isValidPassport2(String passport) {
		var keyValueMatcher = keyPattern.matcher(passport);
		boolean areValidFields = keyValueMatcher.results()
				.allMatch(result -> result.group(1).equals("cid") || fieldsPart2.get(result.group(1)).test(result.group(2)));
		
		keyValueMatcher.reset();
		
		return areValidFields && keyValueMatcher.results()
				.map(result -> result.group(1))
				.collect(Collectors.toSet())
				.containsAll(fieldsPart2.keySet());
	}
}
