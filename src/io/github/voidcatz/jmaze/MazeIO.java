package io.github.voidcatz.jmaze;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MazeIO {
	
	private final static int WALL_COLOR = 0xff000000;
	private final static int AIR_COLOR = 0xffffffff;
	private final static int GOAL_COLOR = 0xffff0000;
	private final static int START_COLOR = 0xff0000ff;
	private final static int PATH_COLOR = 0xff00ff00;
	private final static int MARKER_COLOR = 0xffffff00;
	
	public static Maze createMazeFromImage(BufferedImage image) {
		System.out.println(image.getType());
		Maze.CellType[][] readCells = new Maze.CellType[image.getHeight()][image.getWidth()];
		for(int x = 0; x<image.getWidth(); x++) {
			for(int y = 0; y<image.getHeight(); y++) {
				readCells[x][y] = convertColorToCellType(image.getRGB(x, y));
			}
		}
		return new Maze(readCells);
	}
	
	public static Maze createMazeFromText(File file) {
		Reader reader = null;
		try {
			reader = new FileReader(file);
			BufferedReader bf = new BufferedReader(reader);
			List<char[]> lines = new ArrayList<char[]>();
			
			for (String s; (s = bf.readLine())!=null; ) {
				lines.add(s.toCharArray());				
			}
			
			Maze.CellType[][] readCells = new Maze.CellType[lines.get(0).length][lines.size()];
			
			for(int x = 0; x<lines.get(0).length; x++) {
				for(int y = 0; y<lines.size(); y++) {
					readCells[x][y] = Maze.CellType.values()[Integer.parseInt(lines.get(y)[x]+"")];
				}
			}
			
			return new Maze(readCells);				
		}
		catch ( IOException e ) {
			System.err.println( "Fehler beim Lesen der Datei!" );
		}
		finally {
		  try { reader.close(); } catch ( Exception e ) { }
		}
		return null;
	}
	
	public static Maze createMazeFromFile(File file) throws Exception {
		String ext = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.')+1);
		if(ext.equalsIgnoreCase("png")||ext.equalsIgnoreCase("gif")||ext.equalsIgnoreCase("jpg")||ext.equalsIgnoreCase("bmp")) {
			return createMazeFromImage(ImageIO.read(file));
		}else if(ext.equalsIgnoreCase("maze")||ext.equalsIgnoreCase("txt")) {
			return createMazeFromText(file);
		}else throw new Exception("No valid maze fomat");
		
	}
	
	public static Maze createMazeFromFile() throws Exception {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter mazeImageFilter = new FileNameExtensionFilter("Maze Image", "png", "gif", "jpg", "bmp");
		FileNameExtensionFilter mazeTextFilter = new FileNameExtensionFilter("Maze Text", "txt", "maze");
		fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
		fc.setFileFilter(mazeImageFilter);
		fc.setFileFilter(mazeTextFilter);
		int action = fc.showOpenDialog(null);
		if(action==JFileChooser.APPROVE_OPTION){
			try {
				return createMazeFromFile(fc.getSelectedFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.exit(0);
		}
		return null;
	}
	
	public static void saveMazeToFile(Maze maze) {
		File file = null;
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter mazeImageFilter = new FileNameExtensionFilter("Maze Image", "png", "gif", "jpg", "bmp");
		FileNameExtensionFilter mazeTextFilter = new FileNameExtensionFilter("Maze Text", "txt", "maze");
		fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
		fc.setFileFilter(mazeImageFilter);
		fc.setFileFilter(mazeTextFilter);
		int action = fc.showSaveDialog(null);
		if(action==JFileChooser.APPROVE_OPTION){
			file = fc.getSelectedFile();
		}else{
			System.exit(0);
		}
		
		if(file.getAbsolutePath().endsWith(".maze")||file.getAbsolutePath().endsWith(".txt")) {
			saveMazeAsText(maze, file);
		}else if(file.getAbsolutePath().endsWith("png")||file.getAbsolutePath().endsWith("gif")||file.getAbsolutePath().endsWith("jpg")||file.getAbsolutePath().endsWith("bmp")){
			saveMazeAsImage(maze, file);
		}
	}
	
	public static void saveMazeAsImage(Maze maze, File file) {
		BufferedImage image = new BufferedImage(maze.getWidth(), maze.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		for(int x = 0; x<image.getWidth(); x++) {
			for(int y = 0; y<image.getHeight(); y++) {
				image.setRGB(x, y, convertCellTypeToColor(maze.getCellTypeAt(new Point(x, y))));
			}
		}
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveMazeAsText(Maze maze, File f) {
		File file = null;
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter mazeTextFilter = new FileNameExtensionFilter("Maze Text", "txt", "maze");
		fc.setFileFilter(mazeTextFilter);
		int action = fc.showSaveDialog(null);
		if(action==JFileChooser.APPROVE_OPTION){
			file = fc.getSelectedFile();
		}else{
			System.exit(0);
		}
		
		
		Writer fw = null;
		try {
			if(file.getAbsolutePath().endsWith(".maze")||file.getAbsolutePath().endsWith(".txt")) {
				fw = new FileWriter(file);
			}else {
				fw = new FileWriter(file.getAbsolutePath()+".maze");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int y = 0; y<maze.getHeight(); y++) {
			for(int x = 0; x<maze.getWidth(); x++) {
				try {
					fw.append(maze.getCellTypeAt(new Point(x, y)).ordinal()+"");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				fw.append(System.getProperty("line.separator"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Maze.CellType convertColorToCellType(int color) {
		switch(color) {
		case WALL_COLOR: return Maze.CellType.WALL; 
		case AIR_COLOR: return Maze.CellType.AIR;
		case GOAL_COLOR: return Maze.CellType.GOAL;
		case START_COLOR: return Maze.CellType.START;
		case PATH_COLOR: return Maze.CellType.PATH;
		case MARKER_COLOR: return Maze.CellType.MARKER;
		default: return Maze.CellType.AIR;
		}
	}
	
	public static int convertCellTypeToColor(Maze.CellType type) {
		switch(type) {
		case WALL: return WALL_COLOR; 
		case AIR: return AIR_COLOR;
		case GOAL: return GOAL_COLOR;
		case START: return PATH_COLOR;
		case MARKER: return MARKER_COLOR;
		default: return AIR_COLOR;
		}
	}

}
