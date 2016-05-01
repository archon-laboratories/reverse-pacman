package com.archonlaboratories.pacman;

import java.util.Random;

/**
 * Defines a ghost in the simulation.
 */
public class Ghost
{
    private BeliefState location;
    private WallSensor sensor;

    public Ghost(Simulation current)
    {
        sensor = new WallSensor(current);
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
    void performUpdate()
    {
        // TODO
        int numWalls = performSensing();

        // Should this return void?
    }

    private int performSensing()
    {
        return sensor.getSurroundingWalls();
    }

    void getPacmanSensor()
    {
        // TODO
        // Should this return void?
    }

    /**
     * Gets the next action from the ghost, given it's current belief state and the reward distribution. Also updates
     * the belief state in accordance with the action.
     *
     * @return Action to be taken for this ghost from the simulation.
     */
    Action performAction()
    {
        // TODO
        return null;
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
         * @param container
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
    }


}
