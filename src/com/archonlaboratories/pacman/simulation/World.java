package com.archonlaboratories.pacman.simulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Defines the world for a simulation.
 */
public class World
{
    public final Tile[][] tiles;
    private final Set<Tile> tileSet;

    World(boolean[][] activations)
    {
        tiles = new Tile[activations.length][activations[0].length];
        tileSet = new HashSet<>();

        for (int x = 0; x < activations.length; x++)
            for (int y = 0; y < activations[x].length; y++)
            {
                if (activations[x][y])
                    tiles[x][y] = new Tile(x, y);
                else
                    tiles[x][y] = null;

                tileSet.add(tiles[x][y]);
            }
    }

    public Set<Tile> getTileSet()
    {
        return tileSet;
    }

    /**
     * Defines a tile in the world.
     * Each tile is a discrete location.
     */
    public class Tile
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

        Tile(int xCoord, int yCoord)
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
        public Tile getNextTile(Action action)
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

            if (xCoord <= 0 || tiles[xCoord - 1][yCoord] == null)
                nextTileMap.put(Action.LEFT, this);
            else
                nextTileMap.put(Action.LEFT, tiles[xCoord - 1][yCoord]);

            if (xCoord >= tiles.length - 1 || tiles[xCoord + 1][yCoord] == null)
                nextTileMap.put(Action.RIGHT, this);
            else
                nextTileMap.put(Action.RIGHT, tiles[xCoord + 1][yCoord]);

            if (yCoord <= 0 || tiles[xCoord][yCoord - 1] == null)
                nextTileMap.put(Action.UP, this);
            else
                nextTileMap.put(Action.UP, tiles[xCoord][yCoord - 1]);

            if (yCoord >= tiles[xCoord].length - 1 || tiles[xCoord][yCoord + 1] == null)
                nextTileMap.put(Action.DOWN, this);
            else
                nextTileMap.put(Action.DOWN, tiles[xCoord][yCoord + 1]);
        }


        /**
         * @return The number of walls bordering this tile.
         */
        public int getNumWalls()
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

        @Override
        public boolean equals(Object obj)
        {
            if (obj == this)
                return true;

            if (obj == null)
                return false;

            if (getClass() != obj.getClass())
                return false;

            Tile tile = (Tile) obj;

            return (xCoord == tile.xCoord && yCoord == tile.yCoord);
        }

        @Override
        public int hashCode()
        {
            return xCoord * 31 ^ yCoord * 37;
        }
    }
}
