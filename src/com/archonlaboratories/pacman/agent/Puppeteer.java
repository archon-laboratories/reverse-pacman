package com.archonlaboratories.pacman.agent;

import com.archonlaboratories.pacman.simulation.Action;
import com.archonlaboratories.pacman.simulation.BeliefState;
import com.archonlaboratories.pacman.simulation.World;

/**
 * Defines the puppeteer, which controls the ghosts.
 * This simulates perfect communication.
 */
public class Puppeteer
{
    private BeliefState pacmanBelief;
    private Ghost[] ghosts;

    public Puppeteer(Ghost[] ghosts, World world)
    {
        this.ghosts = ghosts;
        pacmanBelief = new BeliefState(world.getTileSet(), 1);
    }

    double calculateReward()
    {
        // TODO
        return 0;
    }

    /**
     * Gets the action to take, given the current BeliefState of the ghost.
     *
     * @param ghostLocation Belief of the ghost's current location
     * @return Action to take.
     */
    Action requestAction(BeliefState ghostLocation)
    {
        // TODO
        return null;
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
