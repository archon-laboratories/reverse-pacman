package com.archonlaboratories.pacman;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Defines the actions that can be taken by Pacman or the ghosts.
 */
public enum Action
{
    UP, RIGHT, DOWN, LEFT;

    private static final List<Action> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    /**
     * Returns a random value from this enum.
     *
     * @return A random Action
     */
    public static Action randomAction()
    {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
