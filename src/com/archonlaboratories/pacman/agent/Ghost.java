package com.archonlaboratories.pacman.agent;

import com.archonlaboratories.pacman.simulation.Action;
import com.archonlaboratories.pacman.simulation.BeliefState;
import com.archonlaboratories.pacman.simulation.Simulation;
import com.archonlaboratories.pacman.simulation.World;

import java.util.Random;
import java.util.Set;

/**
 * Defines a ghost in the simulation.
 */
public class Ghost
{
    private BeliefState location;
    private WallSensor sensor;
    private PacmanSensor pacmanSensor;
    private static Puppeteer puppeteer;
    private World world;
    private Simulation current;

    public Ghost(Simulation current)
    {
        this.current = current;
        world = current.world;
        location = new BeliefState(world.getTileSet(), 1);
    }

    /**
     * Gets the ghost's current belief state about its own location.
     *
     * @return The Belief State of the ghost's location.
     */
    BeliefState getBeliefState()
    {
        return location;
    }

    /**
     * Updates the ghost's belief state in accordance with its sensors.
     */
    public void performUpdate()
    {
        int evidence = performSensing();
        Set<World.Tile> tiles = world.getTileSet();

        for (World.Tile tile : tiles)
        {
            double updatedProb = location.getProbability(tile);
            updatedProb *= sensor.getErrorFactor(Math.abs(evidence - tile.getNumWalls()));
            location.setProbability(tile, updatedProb);
        }

        location.normalizeBeliefState();

        if (puppeteer == null)
            puppeteer = current.getPuppeteer();

        puppeteer.updateReward(location);
        // Should this return void?
    }

    private int performSensing()
    {
        if (sensor == null)
            sensor = new WallSensor(current);

        return sensor.getSurroundingWalls();
    }

    boolean triggerPacmanSensor()
    {
        if (pacmanSensor == null)
            pacmanSensor = new PacmanSensor(current);

        return pacmanSensor.sense();
    }

    /**
     * Gets the next action from the ghost, given it's current belief state and the reward distribution. Also updates
     * the belief state in accordance with the action.
     *
     * @return Action to be taken for this ghost from the simulation.
     */
    public Action performAction()
    {
        if (puppeteer == null)
            puppeteer = current.getPuppeteer();

        Action actionToTake = puppeteer.requestAction(location);
        BeliefState nextLocation = new BeliefState(world.getTileSet(), 0);

        for (World.Tile tile : world.getTileSet())
        {
            World.Tile nextTile = tile.getNextTile(actionToTake);
            double updatedProb = nextLocation.getProbability(nextTile) + location.getProbability(tile);
            nextLocation.setProbability(nextTile, updatedProb);
        }

        nextLocation.normalizeBeliefState();
        location = nextLocation;

        return actionToTake;
    }

    /**
     * Created for Projects by @author Nate Beckemeyer on 2016-05-01.
     */
    private class WallSensor
    {
        /**
         * The pseudo-random generator used by the wall sensor to create noise.
         */
        private Random rnd = new Random();

        /**
         * The index (in the array returned by the getGhostLocations in Simulation) of the ghost using this sensor.
         */
        private int ghostIndex;

        /**
         * The simulation that's currently running. This object is necessary so that the sensor can get the actual
         * locations of the ghost and walls in order to return a noisy number.
         */
        private Simulation current;

        /**
         * Initializes the wall sensor
         *
         * @param container current Simulation the ghost is contained in.
         */
        WallSensor(Simulation container)
        {
            current = container;
            Ghost[] ghosts = container.getGhosts();
            for (int i = 0; i < ghosts.length; i++)
            {
                if (ghosts[i] == Ghost.this)
                {
                    ghostIndex = i;
                    return;
                }
            }

            System.err.println("Could not find this ghost in current simulation.");
            System.exit(10);
        }

        // TODO make the result random
        int getSurroundingWalls()
        {
            double error = rnd.nextDouble();

            int actual = current.getGhostLocations()[ghostIndex].getNumWalls();

            if (error < .8)
                return actual;
            if (error < .95)
                return Math.abs(actual - 1);
            if (error < .99)
                if (actual == 1)
                    return 3;
                else
                    return Math.abs(actual - 2);

            if (actual < 2)
                return 3;
            else
                return 0;
        }

        /**
         * Gets the probability of the given difference between evidence and reality at a tile.
         *
         * @param difference absolute value of actual - evidence
         * @return Probability of this difference occurring.
         */
        private double getErrorFactor(int difference)
        {
            switch (difference)
            {
                case 0:
                    return .8;
                case 1:
                    return .15;
                case 2:
                    return .04;
                case 3:
                    return .01;
                default:
                    System.exit(10);
                    return -1; // Never reached
            }
        }
    }

    private class PacmanSensor
    {
        /**
         * The pseudo-random generator used by the wall sensor to create noise.
         */
        private Random rnd = new Random();

        /**
         * The index (in the array returned by the getGhostLocations in Simulation) of the ghost using this sensor.
         */
        private int ghostIndex;

        /**
         * The simulation that's currently running. This object is necessary so that the sensor can get the actual
         * locations of the ghost and walls in order to return a noisy number.
         */
        private Simulation current;

        /**
         * Initializes the wall sensor
         *
         * @param container current Simulation the ghost is contained in.
         */
        PacmanSensor(Simulation container)
        {
            current = container;
            Ghost[] ghosts = container.getGhosts();
            for (int i = 0; i < ghosts.length; i++)
            {
                if (ghosts[i] == Ghost.this)
                {
                    ghostIndex = i;
                    return;
                }
            }

            System.err.println("Could not find this ghost in current simulation.");
            System.exit(10);
        }

        boolean sense()
        {
            World.Tile pacLocation = current.getPacman().getLocation();
            World.Tile ghostLocation = current.getGhostLocations()[ghostIndex];
            if (Math.abs(pacLocation.getyCoord() - ghostLocation.getyCoord()) == 1 && (Math.abs(
                    pacLocation.getxCoord() - ghostLocation.getxCoord())) == 1)
                if (rnd.nextDouble() < getAccuracyFactor())
                    return true;
                else return false;
            else if (rnd.nextDouble() < getAccuracyFactor())
                return false;
            else return true;
        }

        private double getAccuracyFactor()
        {
            return .9;
        }
    }

}
