# Blind Reverse Pacman

Final Advanced AI Project for Sam Beckmann and Nate Beckemeyer.

## Problem

**n** ghosts are trying to catch pacman. However, it's dark in the maze, so the ghosts only have a vague idea of where they are. (noisy sensors) They know pacman is somewhere else in the maze, but can only see pacman if they are next to him. (The pacman sensor is also noisy) Luckily for the ghosts, however, they have perfect communication with each other, and know the world they are in perfectly. The goal is to catch pacman in as few time steps as possible. Nothing is to be assumed about pacman's behavior.

## Implemented Solution

We viewed the problem as a modification of a POMDP, and treated it as such. The POMDP aspects of it were solved by implementing a dynamic decision network. The reward function for the decision network, usually static, was instead replaced by a dynamic bayesian network. A diagram detailing how we used this combination, is presented below: 

![diagram](https://github.com/archon-laboratories/reverse-pacman/wiki/images/diagram.png)

## Running

To run reverse-pacman, simply clone this repository and compile the Java code (relies on Java 7 or higher). The running class is `Simulation`. Simply pass the relative paths to any datasets you want to run as arguments.

## Future Improvements

There are several further improvements that we believe could be taken to enhance the ability of the ghosts to find pacman in various mazes.

### Pacman Belief State optimization

The algorithm used to simulate communication between the ghosts by providing a common belief state for where pacman was located, which is updated by all ghosts. This could be optimized, both in complexity, and in effectiveness. For instance, a planning algorithm could be implemented to encourage multiple ghosts to split up.

### Multi-step lookahead

Currently, the ghosts only look one step ahead went decided what action to take. Although looking ahead multiple steps is computationally intensive, it would be interesting to see how effective it is at improving the ghosts' rate of catching pacman.
