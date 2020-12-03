package com.deliburd.adventofcode;

import java.util.List;

import com.deliburd.adventofcode.annotations.Day;
import com.deliburd.adventofcode.annotations.EntryPoint;
import com.deliburd.adventofcode.util.ResourceFiles;

@Day(3)
public class Day3TobogganTrajectory {
	@EntryPoint
	public static void start() {
		var resourceLines = ResourceFiles.getResourceLines("day3input.txt");
		System.out.println("Part 1: " + findTreeAmount(resourceLines, 3, 1));
		
		var slopeList = List.of(1, 1, 3, 1, 5, 1, 7, 1, 1, 2);
		
		System.out.println("Part 2: " + findTreeAmounts(resourceLines, slopeList));
	}
	
	private static long findTreeAmounts(List<String> resourceLines, List<Integer> treeSlopes) {
		long product = 1;
		
		for(int i = 0; i < treeSlopes.size(); i += 2) {
			product *= findTreeAmount(resourceLines, treeSlopes.get(i), treeSlopes.get(i + 1));
		}
		
		return product;
	}

	private static int findTreeAmount(List<String> resourceLines, int right, int down) {
		int treeCount = 0;
		
		for (int i = 0; i < resourceLines.size(); i += down) {
			int treeXCoordinate = i * right * down;
			String treeLine = resourceLines.get(i);
			
			if(treeLine.charAt(treeXCoordinate % treeLine.length()) == '#') {
				treeCount++;
			}
		}
		
		return treeCount;
	}
}
