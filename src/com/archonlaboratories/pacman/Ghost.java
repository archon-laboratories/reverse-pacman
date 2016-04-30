package com.archonlaboratories.pacman;

/**
 * Defines a ghost in the simulation.
 */
public class Ghost
{
    private BeliefState location;

    /**
     * Gets the current belief state from the ghost
     * as to where it is.
     *
     * @return The Belief State of the ghost's location.
     */
    BeliefState getBeliefState()
    {
        return location;
    }

    void performUpdate()
    {
        // TODO
        // Should this return void?
    }

    void getPacmanSensor()
    {
        // TODO
        // Should this return void?
    }

    /**
     * Gets the next action from the ghost, given it's current
     * belief state and the reward distribution
     *
     * @return Action to be taken for this ghost from the simulation.
     */
    Action performAction()
    {
        // TODO
        return null;
    }

}
