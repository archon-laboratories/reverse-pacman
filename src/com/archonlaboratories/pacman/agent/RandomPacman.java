package com.archonlaboratories.pacman.agent;

import com.archonlaboratories.pacman.simulation.Action;
import com.archonlaboratories.pacman.simulation.World;

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
    public Action nextAction()
    {
        return Action.randomAction();
    }
}