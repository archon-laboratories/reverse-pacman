package com.archonlaboratories.pacman;

/**
 * Defines the world for a simulation.
 */
public class World
{
    public Tile[][] tiles;

    class Tile
    {
        int numWalls;

        Tile getNextTile(Action action)
        {
            // TODO
            return null;
        }

        int getNumWalls()
        {
            // TODO: Initialize numWalls
            return numWalls;
        }
    }
}
