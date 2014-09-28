JMaze
=====

Java maze generation application

# How to use
1. create a new MazeGenerator:
    MazeGenerator mazeGen = new MazeGenerator();
2. generate a new maze
    Maze maze = mazeGen.generate(width, height);
3. save your maze
    MazeIO.saveMazeToFile(maze);
