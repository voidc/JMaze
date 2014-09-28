package io.github.voidcatz.jmaze;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;




public class MazeGenerator {
	Random ran;
	
	public MazeGenerator(Random ran) {
		this.ran = ran;
	}
	
	public MazeGenerator() {
		ran = new Random();
	}
		
	
	public Maze getRawMaze(int width, int height) {
		Maze maze = new Maze(width, height);
		
		for(int y = 0; y<height; y++) {
			for(int x = 0; x<width; x++){
				maze.setCellTypeAt(new Point(x, y), Maze.CellType.WALL);
			}
		}
		
		for(int y = 1; y<height-1; y++) {
			for(int x = 1; x<width-1; x++){
				if(y%2!=0) {
					if(x%2!=0)maze.setCellTypeAt(new Point(x, y), Maze.CellType.AIR);
				}					
			}
		}
		return maze;
	}
	
	public Maze generate(int width, int height) {
		Maze maze = getRawMaze(width, height);
		
		List<Point> cellStack = new LinkedList<Point>();
		int totalAirCells = maze.getTotalAirCells();
		Point currentCell = maze.getRandomAirCell(ran);
		maze.setCellTypeAt(currentCell, Maze.CellType.START);		
		int visitedCells = 1;
		
		while(visitedCells<totalAirCells) {
			Point[] unvisitedNeighbors = getUnvisitedNeighbors(maze, currentCell);
			if(unvisitedNeighbors.length!=0) {
				Point newCell = unvisitedNeighbors[new Random().nextInt(unvisitedNeighbors.length)];
				maze.connect(newCell, currentCell);
				cellStack.add(currentCell);
				currentCell = newCell;
				visitedCells++;
			}else {
				Point recentCell = cellStack.get(cellStack.size()-1);
				cellStack.remove(cellStack.size()-1);
				currentCell = recentCell;
			}
		}
		
		maze.setCellTypeAt(currentCell, Maze.CellType.GOAL);
		
		return maze;
	}
	
	public Maze generateTest(int width, int height) {
		Maze maze = getRawMaze(width, height);
		int total = maze.getTotalAirCells();
		
		for(int i = 0; i<total*2; i++) {
			Point p = maze.getRandomAirCell(ran);
			removeRandomWall(maze, p);
		}
		
		return maze;
		
	}
	
	public Point[] getUnvisitedNeighbors(Maze maze, Point p) {
		List<Point> unvisitedNeighbors = new ArrayList<Point>();
		Point[] neighbors = maze.getNeighbors(p);
		for(Point n : neighbors) {
			if(maze.getSurroundingWalls(n)==4)unvisitedNeighbors.add(n);
		}
		return unvisitedNeighbors.toArray(new Point[unvisitedNeighbors.size()]);
	}
	
	public void removeRandomWall(Maze maze, Point p) {
		int s = ran.nextInt(4);
		switch(s) {
		case 0: maze.setCellTypeAt(new Point(p.x+1, p.y), Maze.CellType.AIR); break;
		case 1: maze.setCellTypeAt(new Point(p.x-1, p.y), Maze.CellType.AIR); break;
		case 2: maze.setCellTypeAt(new Point(p.x, p.y+1), Maze.CellType.AIR); break;
		case 3: maze.setCellTypeAt(new Point(p.x, p.y-1), Maze.CellType.AIR); break;
		}
	}

}
