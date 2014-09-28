package io.github.voidcatz.jmaze;
import java.awt.Point;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Maze {
	private CellType[/*column x width*/][/*row y height*/] cells;
	
	public enum CellType {
		
		WALL,
		AIR,
		GOAL,
		START,
		PATH,
		MARKER;

	}
	
	public Maze(CellType[][] cells) {
		this.cells = cells;
	}
	
	public Maze(int width, int height) {
		this.cells = new CellType[width][height];
		System.out.println("Width: " + this.getWidth() + ", Height: "+ this.getHeight());
	}
	
	public CellType getCellTypeAt(Point p) {
		if(p.x<0||p.y<0||p.x>=getWidth()||p.y>=getHeight())return CellType.WALL;
		return cells[p.x][p.y];
	}
	
	public void setCellTypeAt(Point p, CellType type) {
		if(p.x<0||p.y<0||p.x>=getWidth()||p.y>=getHeight())return;
		cells[p.x][p.y] = type;
	}
	
	public int getHeight() {
		return cells[0].length;
	}
	
	public int getWidth() {
		return cells.length;
	}
	
	public Point getStart() {		
		for(int x = 0; x<getWidth(); x++) {
			for(int y = 0; y<getHeight(); y++) {
				if(getCellTypeAt(new Point(x, y))==CellType.START) {
					return new Point(x, y);
				}
			}
		}
		
		return null;
	}
	
	public Point getGoal() {		
		for(int x = 0; x<getWidth(); x++) {
			for(int y = 0; y<getHeight(); y++) {
				if(getCellTypeAt(new Point(x, y))==CellType.GOAL) {
					return new Point(x, y);
				}
			}
		}
		
		return null;
	}
	
	
	
	public int getSurroundingWalls(Point p) {
		if(getCellTypeAt(p)==CellType.AIR) {
			int walls = 0;
			if(getCellTypeAt(new Point(p.x+1, p.y))==CellType.WALL)walls++;
			if(getCellTypeAt(new Point(p.x-1, p.y))==CellType.WALL)walls++;
			if(getCellTypeAt(new Point(p.x, p.y+1))==CellType.WALL)walls++;
			if(getCellTypeAt(new Point(p.x, p.y-1))==CellType.WALL)walls++;
			return walls;
		}else return -1;
	}
	
	public Point[] getNeighbors(Point p) {
		Point[] neighbors = new Point[4];
		neighbors[0] = new Point(p.x+2, p.y);
		neighbors[1] = new Point(p.x-2, p.y);
		neighbors[2] = new Point(p.x, p.y+2);
		neighbors[3] = new Point(p.x, p.y-2);
		return neighbors;
	}
	
	public void connect(Point a, Point b) {
		if((a.x==b.x-2)&&(a.y==b.y))setCellTypeAt(new Point(a.x+1, a.y), CellType.AIR);
		else if((a.x==b.x+2)&&(a.y==b.y))setCellTypeAt(new Point(a.x-1, a.y), CellType.AIR);
		else if((a.x==b.x)&&(a.y==b.y-2))setCellTypeAt(new Point(a.x, a.y+1), CellType.AIR);
		else if((a.x==b.x)&&(a.y==b.y+2))setCellTypeAt(new Point(a.x, a.y-1), CellType.AIR);
	}
	
	public Point getRandomAirCell(Random ran) {
		List<Point> airCells = new LinkedList<Point>();
		for(int x = 0; x<getWidth(); x++) {
			for(int y = 0; y<getHeight(); y++){
				Point p = new Point(x, y);
				if(getCellTypeAt(p)==Maze.CellType.AIR)airCells.add(p);
			}
		}
		return airCells.get(ran.nextInt(airCells.size()));
	}
	
	public int getTotalAirCells() {
		int airCells = 0;
		for(int x = 0; x<getWidth(); x++) {
			for(int y = 0; y<getHeight(); y++){
				if(getCellTypeAt(new Point(x, y))==Maze.CellType.AIR)airCells++;
			}
		}
		return airCells;
	}

}
