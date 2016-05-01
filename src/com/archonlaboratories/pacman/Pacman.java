package com.archonlaboratories.pacman;

/**
 * Defines a pacman agent in the world.
 *
 * Abstract since the movement strategy should be
 * left up to the implementation of the pacman.
 */
public abstract class Pacman
{
    /**
     * Keeps track of which tile pacman is currently in.
     */
    private World.Tile location;

    public Pacman(World.Tile initLocation)
    {
        this.location = initLocation;
    }

    /**
     * Get the current location of pacman.
     * Note: Ghosts should not call this method.
     *
     * @return current Tile pacman is in.
     */
    public World.Tile getLocation()
    {
        return location;
    }

    /**
     * Updates the location of pacman.
     *
     * @param location New Tile pacman is in.
     */
    public void setLocation(World.Tile location)
    {
        this.location = location;
    }

    /**
     * Asks pacman for the next action he will perform.
     * Location updating is handled by the Simulation.
     *
     * @return The action pacman will perform.
     */
    abstract Action performAction();

    /**
     * Implementation of Pacman where he moves randomly.
     */
    public class RandomPacman extends Pacman
    {

        public RandomPacman(World.Tile initLocation)
        {
            super(initLocation);
        }

        @Override
        Action performAction()
        {
            return Action.randomAction();
        }
    }
}
