package com.archonlaboratories.pacman.agent;

import com.archonlaboratories.pacman.simulation.Action;
import com.archonlaboratories.pacman.simulation.BeliefState;
import com.archonlaboratories.pacman.simulation.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the puppeteer, which controls the ghosts.
 * This simulates perfect communication.
 */
public class Puppeteer
{
    private BeliefState pacmanBelief;
    private Ghost[] ghosts;
    World thisWorld;
    Map<World.Tile, Double> rewardDistribution;

    public Puppeteer(Ghost[] ghosts, World world)
    {
        this.ghosts = ghosts;
        pacmanBelief = new BeliefState(world.getTileSet(), 1);
    }

    // TODO Recommendation: Make an interface for coordinates so that a library can handle them

    /**
     * This function takes two tiles and returns the manhattan distance between them
     *
     * @param a The first tile
     * @param b The second tile
     * @return The manhattan distance between tile a and tile b.
     */
    private int getDistance(World.Tile a, World.Tile b)
    {
        return Math.abs(a.getxCoord() - b.getxCoord()) + Math.abs(a.getyCoord() - b.getyCoord());
    }

    /**
     * Protected division between two numbers, a and b
     *
     * @return a/b if b != 0; 10 if b == 0
     */
    private double pDiv(double a, double b)
    {
        return (b == 0) ? 10 : a / b;
    }

    /**
     * The reward for the tile depends only upon the perceived distance from Pacman.
     * <p>
     * Calculated according to the following formula: b(ghost) * pDiv(1, d(ghost, pacman)) * b(pacman)
     * <p>
     * Where b(x) represents the mapping of the belief states, and d(a, b) is the manhattan distance from the
     * currently tested location of a to the currently tested location of b.
     *
     * @param testing The tile whose value is being calculated
     * @return The reward for that tile
     */
    private double calculateReward(World.Tile testing, BeliefState ghostLocation)
    {
        double result = 0;
        double ghostProb = ghostLocation.getProbability(testing);

        for (World.Tile pacLocation : pacmanBelief.getTileSet())
            result += ghostProb * pDiv(1, getDistance(testing, pacLocation)) * pacmanBelief.getProbability(pacLocation);

        return result;
    }

    /**
     * Gets the action to take, given the current BeliefState of the ghost.
     *
<<<<<<< HEAD
=======
     * This function works by the following algorithm:
     *  * For every tile
     *      * Multiply the belief that the ghost is in that tile by the sum of, for each tile pacman might be,
     *          * The inverse of the manhattan distance (using the method pDiv) times the belief that pacman is in that tile
     *
     *
>>>>>>> bdff5e0f4bcd24e8cd2629e0a92d23287533f82e
     * @param ghostLocation Belief of the ghost's current location
     * @return Action to take.
     */
    Action requestAction(BeliefState ghostLocation)
    {
        rewardDistribution = new HashMap<>();
        double[] bestChoice = new double[Action.values().length];

        for (World.Tile tile : thisWorld.getTileSet())
        {
            rewardDistribution.put(tile, calculateReward(tile, ghostLocation));
        }

        for (World.Tile tile : thisWorld.getTileSet())
        {
            for (int i = 0; i < bestChoice.length; i++)
            {
                bestChoice[i] += rewardDistribution.get(tile.getNextTile(Action.values()[i]));
            }
        }

        int argmax = 0;
        for (int i = 1; i < bestChoice.length; i++)
        {
            if (bestChoice[i] > bestChoice[argmax])
                argmax = i;
        }

        return Action.values()[argmax];
    }

    /**
     * Updates the pacman BeliefState based on the latest location from the ghost.
     *
     * @param ghostLocation Current Estimate of Ghost's location
     */
    void updateReward(BeliefState ghostLocation)
    {
        // TODO
        // I don't know if this needs to be passed the ghost ID or not.
    }
}
