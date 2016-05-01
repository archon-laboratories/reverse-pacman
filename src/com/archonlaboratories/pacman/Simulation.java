package com.archonlaboratories.pacman;

import java.io.*;

/**
 * Performs the simulation of reverse-pacman.
 */
public class Simulation
{
    /**
     * Keeps track of the action locations of the ghosts.
     */
    private World.Tile[] ghostLocations;

    World.Tile[] getGhostLocations()
    {
        return ghostLocations;
    }

    public World world;

    private Pacman pacman;

    private Ghost[] ghosts;

    Ghost[] getGhosts()
    {
        return ghosts;
    }

    public static void main(String[] args)
    {
        for (String arg : args)
        {
            File data = new File(arg);
            Simulation sim = new Simulation();
            sim.performSimulation(data);
        }
    }

    /**
     * Performs the simulation
     *
     * @param dataset File that contains the information for this simulation.
     * @return Number of time-steps until the ghosts found pacman.
     */
    int performSimulation(File dataset)
    {
        initSimulation(dataset);

        boolean gameOverFlag = false;
        int timeSteps = 0;

        while (!gameOverFlag)
        {
            timeSteps++;

            for (Ghost ghost : ghosts)
            {
                ghost.performUpdate();
            }

            for (int i = 0; i < ghosts.length; i++)
            {
                Action ghostAction = ghosts[i].performAction();
                ghostLocations[i] = ghostLocations[i].getNextTile(ghostAction);
            }
            gameOverFlag = isEndConditionMet();

            Action pacmanMovement = pacman.nextAction();
            pacman.setLocation(pacman.getLocation().getNextTile(pacmanMovement));

            gameOverFlag = gameOverFlag || isEndConditionMet();
        }

        return timeSteps;
    }

    /**
     * Initializes the simulation.
     */
    private void initSimulation(File dataset)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(dataset));

            // Read size of maze
            String[] size = reader.readLine().split(",");
            int x = Integer.parseInt(size[0]);
            int y = Integer.parseInt(size[1]);

            boolean[][] map = new boolean[x][y];

            for (int row = 0; row < y; row++)
            {
                String line = reader.readLine();

                for(int index = 0; index < line.length(); index++)
                    map[index][row] = line.charAt(index) != 0;
            }

            world = new World(map); // init world

            String[] pacmanLoc = reader.readLine().split(",");
            int pacX = Integer.parseInt(pacmanLoc[0]);
            int pacY = Integer.parseInt(pacmanLoc[1]);

            pacman = new RandomPacman(world.tiles[pacX][pacY]);

            int numGhosts = Integer.parseInt(reader.readLine());

            ghostLocations = new World.Tile[numGhosts];
            ghosts = new Ghost[numGhosts];


            for (int ghostID = 0; ghostID < numGhosts; ghostID++)
            {
                String[] location = reader.readLine().split(",");
                int xCoord = Integer.parseInt(location[0]);
                int yCoord = Integer.parseInt(location[1]);

                ghostLocations[ghostID] = world.tiles[xCoord][yCoord];
                ghosts[ghostID] = new Ghost(this);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Determines if a ghost is in the same Tile as pacman.
     *
     * @return True if a ghost is at pacman's location, else false.
     */
    private boolean isEndConditionMet()
    {
        for(World.Tile tile : ghostLocations)
            if (tile.equals(pacman.getLocation()))
                return true;

        return false;
    }
}
