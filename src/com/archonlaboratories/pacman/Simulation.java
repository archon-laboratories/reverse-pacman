package com.archonlaboratories.pacman;

/**
 * Performs the simulation of reverse-pacman.
 */
public class Simulation
{
    /**
     * Keeps track of the action locations of the ghosts.
     */
    private World.Tile[] ghostLocations;

    public World world;

    private Pacman pacman;

    private Ghost[] ghosts;

    public static void main(String[] args)
    {
        // TODO
    }

    void performSimulation()
    {
        initSimulation();

        boolean gameOverFlag = false;

        while (!gameOverFlag)
        {
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

            pacman.nextAction();

            gameOverFlag = gameOverFlag || isEndConditionMet();
        }
    }

    private void initSimulation()
    {

    }

    private boolean isEndConditionMet()
    {
        // TODO
        return false;
    }
}
