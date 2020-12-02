package com.deliburd.adventofcode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.deliburd.adventofcode.annotations.Day;
import com.deliburd.adventofcode.annotations.EntryPoint;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class Main {
	private static class DayData {
		private final Integer day;
		private final List<Method> entryPoints;
		
		private DayData(Integer day, List<Method> entryPoints) {
			this.day = day;
			this.entryPoints = entryPoints;
		}
		
		public Integer getDay() {
			return day;
		}
		
		public List<Method> getEntryPoints() {
			return entryPoints;
		}
	}

	public static void main(String[] args) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		var systemClassPath = ClassPath.from(ClassLoader.getSystemClassLoader());
		var packageClasses = systemClassPath.getTopLevelClasses(Main.class.getPackageName());
		var optionalDayData = packageClasses.stream()
				.filter(info -> !info.getName().equals(Main.class.getName()))
				.map(Main::getDayDataFromClassInfo)
				.filter(dayData -> dayData.getDay() != null && !dayData.getEntryPoints().isEmpty())
				.max((dayData1, dayData2) -> Integer.compare(dayData1.getDay(), dayData2.getDay()));
		
		if(optionalDayData.isEmpty()) {
			System.out.println("No class with a day and an entrypoint found.");
			return;
		}
		
		var methodAnnotationsWithDay = optionalDayData.get();
		
		System.out.println("Day " + methodAnnotationsWithDay.getDay() + " is the most recent. Finding entrypoint...");
		
		var entryPoints = methodAnnotationsWithDay.getEntryPoints();
		
		if(entryPoints.size() > 1) {
			throw new IllegalStateException("More than one entry point found. Stopping...");
		}
		
		System.out.println("Entry point found...");
		
		var entryPoint = entryPoints.get(0);
		
		if(!Modifier.isStatic(entryPoint.getModifiers())) {
			throw new IllegalStateException("The entry point must be static. Stopping...");
		} else if(entryPoint.getParameters().length != 0) {
			throw new IllegalStateException("The entry point cannot have any parameters. Stopping...");
		}
		
		entryPoint.setAccessible(true);
		
		System.out.println("Executing entry point...");
		
		entryPoint.invoke(null);
	}
	
	private static DayData getDayDataFromClassInfo(ClassInfo classInfo) {
		Class<?> clazz;
		
		try {
			clazz = Class.forName(classInfo.getName());
		} catch (ClassNotFoundException e) { // Should never happen
			throw new IllegalStateException("Couldn't find one of the classes from ClassPath.", e);
		}

		var day = Arrays.stream(clazz.getAnnotations())
				.filter(annotation -> annotation.annotationType() == Day.class)
				.mapToInt(annotation -> ((Day) annotation).value())
				.findFirst();
		
		var entryPoints = Arrays.stream(clazz.getMethods())
				.filter(method -> method.getAnnotation(EntryPoint.class) != null)
				.collect(Collectors.toList());
		
		return new DayData(day.isPresent() ? Integer.valueOf(day.getAsInt()) : null, entryPoints);
	}

}
