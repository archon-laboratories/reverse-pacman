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

    int count = 0;

    public Puppeteer(Ghost[] ghosts, World world)
    {
        this.ghosts = ghosts;
        pacmanBelief = new BeliefState(world.getTileSet(), 1);
        pacmanBelief.normalizeBeliefState();
        thisWorld = world;
    }

    // TODO Recommendation: Make an interface for coordinates so that a library can handle them

    /**
     * This function takes two tiles and returns the manhattan distance between them
     *
     * @param a The first tile
     * @param b The second tile
     * @return The manhattan distance between tile a and tile b.
     */
    static int getDistance(World.Tile a, World.Tile b)
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
        return (b == 0) ? 0 : a / b;
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
    private double calculateReward(World.Tile testing, Action action)
    {
        double result = 0;

        for (World.Tile pacLocation : pacmanBelief.getTileSet())
            result += pDiv(1, getDistance(testing.getNextTile(action), pacLocation)) * pacmanBelief.getProbability(pacLocation);

        return result;
    }

    /**
     * Gets the action to take, given the current BeliefState of the ghost.
     *
     * This function works by the following algorithm:
     *  * For every tile
     *      * Multiply the belief that the ghost is in that tile by the sum of, for each tile pacman might be in,
     *          * The inverse of the manhattan distance (using the method pDiv) times the belief that pacman is in that tile
     *
     *
     * @param ghostLocation Belief of the ghost's current location
     * @return Action to take.
     */
    Action requestAction(BeliefState ghostLocation)
    {
        double[] bestChoice = new double[Action.values().length];

        for (int i = 0; i < bestChoice.length; i++)
            for (World.Tile tile : thisWorld.getTileSet())
                bestChoice[i] += ghostLocation.getProbability(tile) * calculateReward(tile, Action.values()[i]);

        int argmax = 0;
        for (int i = 1; i < bestChoice.length; i++)
            if (bestChoice[i] > bestChoice[argmax])
                argmax = i;

        return Action.values()[argmax];
    }

    /**
     * Updates the pacman BeliefState based on the latest location from the ghost.
     *
     * @param ghostLocation Current Estimate of Ghost's location
     */
    void updateReward(BeliefState ghostLocation)
    {
        if (++count != ghosts.length)
            return;

        count = 0;

        BeliefState beliefNotPacman = new BeliefState();

        for (World.Tile tile : thisWorld.getTileSet())
        {
            beliefNotPacman.setProbability(tile, 1 - pacmanBelief.getProbability(tile));
        }
        // This for loop actually calculates the probability that Pacman isn't there
        for (Ghost ghost : ghosts)
        {
            boolean sensedPacman = ghost.triggerPacmanSensor();
            for (World.Tile tile : thisWorld.getTileSet())
            {
                beliefNotPacman.setProbability(tile, beliefNotPacman.getProbability(tile) + 1./ghosts.length * ghost.getBeliefState().getProbability(tile));
                for (Action action : Action.values())
                {
                    World.Tile afterMove = tile.getNextTile(action);
                    if (!sensedPacman)
                        beliefNotPacman.setProbability(afterMove, beliefNotPacman.getProbability(afterMove) + 1./ghosts.length * .9*ghost.getBeliefState().getProbability(tile));
                    else
                        beliefNotPacman.setProbability(afterMove, beliefNotPacman.getProbability(afterMove) + 1./ghosts.length * .1*ghost.getBeliefState().getProbability(tile));
                }
            }
        }

        beliefNotPacman.normalizeBeliefState();

        for (World.Tile tile : thisWorld.getTileSet())
        {
            pacmanBelief.setProbability(tile, 1 - beliefNotPacman.getProbability(tile));
        }

        pacmanBelief.normalizeBeliefState();
    }
}
