package com.archonlaboratories.pacman.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Defines a belief state over a given world.
 */
public class BeliefState
{
    /**
     * Keeps track of the current beliefs for each tile.
     */
    private Map<World.Tile, Double> beliefs;

    /**
     * Marks if this BeliefState is currently normalized.
     */
    private boolean dirty;

    public BeliefState()
    {
        this(new HashMap<>());
    }

    public BeliefState(Map<World.Tile, Double> beliefs)
    {
        this.beliefs = beliefs;
        dirty = true;
    }

    public BeliefState(Set<World.Tile> tiles, double initValue)
    {
        beliefs = new HashMap<>();

        for (World.Tile tile : tiles)
            beliefs.put(tile, initValue);
    }

    /**
     * Gets the normalized probability of being at the given tile.
     * Normalizes if not done yet.
     *
     * @param tile Tile to get belief state at.
     * @return
     */
    public double getProbability(World.Tile tile)
    {
//        if (dirty)
//            this.normalizeBeliefState(); // handle elegantly later.

        return beliefs.get(tile);
    }

    /**
     * Clears the belief state to prevent any errors with
     * future normalization. Should be called before setProbability
     * for safety.
     */
    public void clearBeliefState()
    {
        beliefs.clear();
    }

    /**
     * Sets the probability of being at the given tile.
     * Doesn't matter if not normalized, as long as all
     * values are replaced and are correct relative to each other.
     *
     * @param tile        Tile to set probability at
     * @param probability Non-normalized probability
     */
    public void setProbability(World.Tile tile, double probability)
    {
        beliefs.put(tile, probability);
        dirty = true;
    }

    /**
     * Normalizes the BeliefState.
     * Should be called after probabilities are updated.
     */
    public void normalizeBeliefState()
    {
        if (!dirty)
            return;
        double sum = 0;
        for (Double prob : beliefs.values())
            sum += prob;

        double normFactor = 1 / sum;

        for (World.Tile tile : beliefs.keySet())
            beliefs.put(tile, beliefs.get(tile) * normFactor);

        dirty = false;
    }

    public Set<World.Tile> getTileSet()
    {
        return beliefs.keySet();
    }
}
