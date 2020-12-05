package com.deliburd.adventofcode;

import java.util.List;

import com.deliburd.adventofcode.annotations.Day;
import com.deliburd.adventofcode.annotations.EntryPoint;
import com.deliburd.adventofcode.util.ResourceFiles;

@Day(5)
public class Day5BinaryBoarding {
	@EntryPoint
	public static void start() {
		var boardingPasses = ResourceFiles.getResourceLines("day5input.txt");
		System.out.println("Part 1: " + getHighestSeatID(boardingPasses));
		System.out.println("Part 2: " + getOwnSeat(boardingPasses));
	}
	
	private static int getOwnSeat(List<String> boardingPasses) {
		var seatIDs = boardingPasses.stream()
				.mapToInt(Day5BinaryBoarding::boardingPassToSeatID)
				.sorted()
				.toArray();
		
		int gap = seatIDs[0];
		
		for(int i = 1; i < seatIDs.length - 1; i++) {
			gap++;
			
			int seatID = seatIDs[i];
			
			if(seatID != gap) {
				return gap;
			}
		}
		
		return -1;
	}
	
	private static int getHighestSeatID(List<String> boardingPasses) {
		return boardingPasses.stream()
				.mapToInt(Day5BinaryBoarding::boardingPassToSeatID)
				.max()
				.getAsInt();
	}
	
	private static int boardingPassToSeatID(String boardingPass) {
		int rowNumber = processNumber(boardingPass.substring(0, 7), 128, 'F', 'B');
		int columnNumber = processNumber(boardingPass.substring(7), 8, 'L', 'R');
		
		return rowNumber * 8 + columnNumber;
	}
	
	private static int processNumber(String search, int totalNumber, char lowerHalf, char upperHalf) {
		var searchCharacters = search.chars()
				.toArray();

		int lowerBound = 0;
		int upperBound = totalNumber - 1;
		
		for(int character : searchCharacters) {
			int incrementer = (int) Math.ceil((upperBound - lowerBound + 1) / 2);

			if(character == lowerHalf) {
				upperBound -= incrementer;
			} else {
				lowerBound += incrementer;
			}
		}
		
		char lastCharacter = search.charAt(search.length() - 1);
		
		if(lastCharacter == lowerHalf) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
