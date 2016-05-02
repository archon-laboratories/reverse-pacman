package com.archonlaboratories.pacman.simulation;

import com.archonlaboratories.pacman.agent.Ghost;
import com.archonlaboratories.pacman.agent.Pacman;
import com.archonlaboratories.pacman.agent.Puppeteer;
import com.archonlaboratories.pacman.agent.RandomPacman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Performs the simulation of reverse-pacman.
 */
public class Simulation
{

    private static final boolean PRINT_OUTPUT = false;
    private static final int NUM_TRIALS = 1000;

    public World world;

    private Ghost[] ghosts;

    /**
     * Keeps track of the action locations of the ghosts.
     */
    private World.Tile[] ghostLocations;

    private Pacman pacman;

    private Puppeteer puppeteer;

    public Pacman getPacman()
    {
        return pacman;
    }

    public Ghost[] getGhosts()
    {
        return ghosts;
    }

    public World.Tile[] getGhostLocations()
    {
        return ghostLocations;
    }

    public Puppeteer getPuppeteer()
    {
        return puppeteer;
    }

    public static void main(String[] args)
    {
        for (String arg : args)
        {
            int totalTime = 0;
            File data = new File(arg);
            Simulation sim = new Simulation();

            for (int i = 0; i < NUM_TRIALS; i++)
            {
                int timeSteps = sim.performSimulation(data);
                totalTime += timeSteps;

                if (PRINT_OUTPUT)
                    System.out.printf("Simulation %s completed in %d timesteps.\n\n\n", arg, timeSteps);
            }

            double averageTime = (double) totalTime / NUM_TRIALS;

            System.out.printf("Simulation %s average over %d trials: %f\n\n", arg, NUM_TRIALS, averageTime);
        }
    }

    /**
     * Performs the simulation
     *
     * @param dataset File that contains the information for this simulation.
     * @return Number of time-steps until the ghosts found pacman.
     */
    private int performSimulation(File dataset)
    {
        initSimulation(dataset);

        boolean gameOverFlag = false;
        int timeSteps = 0;

        while (!gameOverFlag)
        {
            timeSteps++;

            if (PRINT_OUTPUT) System.out.printf("TIMESTEP: %d\n", timeSteps);

            for (Ghost ghost : ghosts)
            {
                ghost.performUpdate();
            }

            for (int i = 0; i < ghosts.length; i++)
            {
                Action ghostAction = ghosts[i].performAction();
                ghostLocations[i] = ghostLocations[i].getNextTile(ghostAction);
                if (PRINT_OUTPUT) System.out.printf("Ghost %d Action: %s\n", i, ghostAction);
            }
            gameOverFlag = isEndConditionMet();

            Action pacmanMovement = pacman.nextAction();
            pacman.setLocation(pacman.getLocation().getNextTile(pacmanMovement));
            if (PRINT_OUTPUT) System.out.printf("Pacman Action: %s\n", pacmanMovement);

            gameOverFlag = gameOverFlag || isEndConditionMet();
        }

        return timeSteps;
    }

    /**
     * Initializes the simulation.
     */
    private void initSimulation(File dataset)
    {
        Ghost.puppeteer = null;

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

                for (int index = 0; index < line.length(); index++)
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

        puppeteer = new Puppeteer(ghosts, world);
    }

    /**
     * Determines if a ghost is in the same Tile as pacman.
     *
     * @return True if a ghost is at pacman's location, else false.
     */
    private boolean isEndConditionMet()
    {
        for (World.Tile tile : ghostLocations)
            if (tile.equals(pacman.getLocation()))
                return true;

        return false;
    }
}
