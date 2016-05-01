package com.archonlaboratories.pacman.agent;

import com.archonlaboratories.pacman.simulation.Action;
import com.archonlaboratories.pacman.simulation.BeliefState;

/**
 * Defines the puppeteer, which controls the ghosts.
 * This simulates perfect communication.
 */
public class Puppeteer
{
    BeliefState pacmanBelief;

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
