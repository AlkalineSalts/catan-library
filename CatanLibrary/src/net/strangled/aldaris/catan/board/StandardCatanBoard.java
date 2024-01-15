package net.strangled.aldaris.catan.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.board.CatanHexagon.CatanHexagonBuilder;
import net.strangled.aldaris.catan.math.Line;
import net.strangled.aldaris.catan.math.Point;

public class StandardCatanBoard extends CatanBoard {
	public static int ID = 1;
	public StandardCatanBoard() {
		super(ID);
		//creates the catan board in three steps: step one is making the hexagons
		//make the 1st and 5th rows, then the second and fourth, and finally the third
	{
		List<Integer> potentialNumbers = new LinkedList<>(Stream.of(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12).toList());
		Collections.shuffle(potentialNumbers);
		
		List<Resource> resourceList = new LinkedList<>();
		for (int i = 0; i < 3; i++) {
			resourceList.add(Resource.BRICK);
			resourceList.add(Resource.ORE); 
			resourceList.add(Resource.WHEAT);
			resourceList.add(Resource.SHEEP);
			resourceList.add(Resource.WOOD);
		}
		resourceList.add(Resource.SHEEP);
		resourceList.add(Resource.WHEAT);
		resourceList.add(Resource.WOOD);
		resourceList.add(null);
		Collections.shuffle(resourceList);
		
		
		BiConsumer<Integer, Integer> generatorFunction = (y, toX) -> {
			for (int x = 1; x <= toX; x++) {
				Point p = new Point(x, y);
				CatanHexagonBuilder builder = new CatanHexagonBuilder();
				builder.setX(x);
				builder.setY(y);
				Resource resource = resourceList.remove(0);
				builder.setResourceType(resource);
				if (resource != null)
					builder.setResourceNumber(potentialNumbers.remove(0));
				coordsToDataHexagons.put(p, builder.build());
			}
		};
		generatorFunction.accept(1, 3);	
		generatorFunction.accept(5, 3);	
		generatorFunction.accept(2, 4);	
		generatorFunction.accept(4, 4);
		generatorFunction.accept(3, 5);	
		
	}
		
	
	
	//step two is getting the points and making the data points
	for (CatanHexagon hexagon: coordsToDataHexagons.values()) {
		for (Point point: hexagon.getMathematicalPoints()) {
			pointsToDataPoints.putIfAbsent(point, new CatanPoint(point));
		}
	}
	
	
	//step three is making the lines
	for (CatanHexagon hexagon: coordsToDataHexagons.values()) {
		for (Line line: hexagon.getMathematicalLines()) {
			lineToDataLine.putIfAbsent(line, new CatanLine(line));
		}
	}
	
	}

}
