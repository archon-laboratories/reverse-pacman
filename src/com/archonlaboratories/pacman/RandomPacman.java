package com.archonlaboratories.pacman;

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
    Action nextAction()
    {
        return Action.randomAction();
    }
}