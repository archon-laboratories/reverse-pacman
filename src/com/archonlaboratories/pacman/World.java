package com.archonlaboratories.pacman;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the world for a simulation.
 */
public class World
{
    public Tile[][] tiles;

    /**
     * Defines a tile in the world.
     * Each tile is a discrete location.
     */
    class Tile
    {
        /**
         * Number of walls bordering this tile.
         */
        private int numWalls = -1; // Error value, triggers calculation at first call.

        /**
         * X Coordinate of this tile in the world.
         */
        private final int xCoord;

        /**
         * Y Coordinate of this tile in the world.
         */
        private final int yCoord;

        /**
         * Mapping of actions to next tiles. Used to speed up lookup time.
         */
        private Map<Action, Tile> nextTileMap;

        public Tile(int numWalls, int xCoord, int yCoord)
        {
            this(xCoord, yCoord);
            this.numWalls = numWalls;
        }

        public Tile(int xCoord, int yCoord)
        {
            this.xCoord = xCoord;
            this.yCoord = yCoord;
        }

        /**
         * Gets the next tile, provided you take action in this tile.
         *
         * @param action Action to be taken in this tile
         * @return Tile that you move to after taking action.
         */
        Tile getNextTile(Action action)
        {
            if (nextTileMap == null)
                initTileMap();

            return nextTileMap.get(action);
        }

        /**
         * Initializes the next-tile map, to allow faster lookups in the future.
         */
        void initTileMap()
        {
            nextTileMap = new HashMap<>(4);

            if (xCoord <= 0)
                nextTileMap.put(Action.LEFT, this);
            else
                nextTileMap.put(Action.LEFT, tiles[xCoord - 1][yCoord]);

            if (xCoord >= tiles.length)
                nextTileMap.put(Action.RIGHT, this);
            else
                nextTileMap.put(Action.RIGHT, tiles[xCoord + 1][yCoord]);

            if (yCoord <= 0)
                nextTileMap.put(Action.UP, this);
            else
                nextTileMap.put(Action.UP, tiles[xCoord][yCoord - 1]);

            if (yCoord >= tiles[xCoord].length)
                nextTileMap.put(Action.DOWN, this);
            else
                nextTileMap.put(Action.DOWN, tiles[xCoord][yCoord + 1]);
        }


        /**
         * @return The number of walls bordering this tile.
         */
        int getNumWalls()
        {
            // Initialize numWalls if not done at construction
            if (numWalls < 0)
            {
                int wallCounter = 0;

                for(Action action : Action.values())
                    if (getNextTile(action) == this)
                        wallCounter++;

                numWalls = wallCounter;
            }

            return numWalls;
        }

        public int getxCoord()
        {
            return xCoord;
        }

        public int getyCoord()
        {
            return yCoord;
        }
    }
}
