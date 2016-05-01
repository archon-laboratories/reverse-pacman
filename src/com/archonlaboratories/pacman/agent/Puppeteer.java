package com.archonlaboratories.pacman.agent;

import com.archonlaboratories.pacman.simulation.BeliefState;
import com.archonlaboratories.pacman.simulation.World;

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

    void requestAction(World.Tile tile)
    {
        // TODO
        // should this be void?
    }
}
