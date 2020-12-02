package com.deliburd.adventofcode;

import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.deliburd.adventofcode.annotations.Day;
import com.deliburd.adventofcode.annotations.EntryPoint;
import com.deliburd.adventofcode.util.ResourceFiles;

@Day(2)
public class Day2PasswordPhilosophy {
	private static final Pattern PASSWORD_PATTERN = Pattern.compile("(?<lowerBound>\\d+)-(?<upperBound>\\d+) (?<letter>.): (?<password>.+)");
	
	@EntryPoint
	public static void start() throws URISyntaxException {
		var passwordLines = ResourceFiles.getResourceLines("day2input.txt");
		System.out.println("Part 1: " + getValidPasswordCount(passwordLines, Day2PasswordPhilosophy::isValidPassword));
		System.out.println("Part 2: " + getValidPasswordCount(passwordLines, Day2PasswordPhilosophy::isActualValidPassword));
	}

	private static long getValidPasswordCount(List<String> passwordLines, Predicate<Matcher> validPasswordPredicate) {
		return passwordLines.stream()
				.map(passwordLine -> PASSWORD_PATTERN.matcher(passwordLine))
				.filter(validPasswordPredicate)
				.count();
	}
	
	private static boolean isValidPassword(Matcher matcher) {
		if(!matcher.matches()) {
			throw new IllegalArgumentException("Passwords and policies must match pattern.");
		}
		
		int letterOccurences = StringUtils.countMatches(matcher.group("password"), matcher.group("letter"));
		int lowerBound = Integer.valueOf(matcher.group("lowerBound"));
		int upperBound = Integer.valueOf(matcher.group("upperBound"));
		
		return letterOccurences >= lowerBound && letterOccurences <= upperBound;
	}
	
	private static boolean isActualValidPassword(Matcher matcher) {
		if(!matcher.matches()) {
			throw new IllegalArgumentException("Passwords and policies must match pattern.");
		}
		
		String password = matcher.group("password");
		char letter = matcher.group("letter").charAt(0);
		int firstIndex = Integer.valueOf(matcher.group("lowerBound"));
		int secondIndex = Integer.valueOf(matcher.group("upperBound"));
		
		return password.charAt(firstIndex - 1) == letter ^ password.charAt(secondIndex - 1) == letter;
	}
}
