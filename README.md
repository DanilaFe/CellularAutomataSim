# Cellular Automata Sim [![Build Status](https://travis-ci.org/DanilaFe/CellularAutomataSim.svg?branch=master)](https://travis-ci.org/DanilaFe/CellularAutomataSim)
## About
This is a simple project I threw together in about 30 minutes in order to simplify testing of different cellular automata rules. 
Written in Java, CellularAutomataSim uses LibGDX for rendering and input as well as Java 8's lambda as a means of providing rules
to each individual cell. 

And it comes in nice pastel colors.
## How to run
If you don't want to tweak anything, it should be sufficient to run
```
./gradlew desktop:run
```
if you're on Linux or OS X, and 

```
gradlew.bat desktop:run
```

If you're on Windows. 

## How to control
There are a few controls you can use:
* R clears the field
* F fills the field
* E toggles execution
* C generates a new color
* T toggles mode between delete and create
* Click creates or deletes cells, based on the mode.
* I inverts the field. All dead cell become alive and all living cells die.
* Z randomizes the field.

## How to tweak
Adding new rules is simple, and uses Lambda: 

```Java
CellBehaviorInterface equality = (x, y) -> x == y;
```

The above code will make all cells whose x is equal to their y alive, and kill the rest.

To make the program run the rule, set the *cellBehavior* to the new rule you've created, for example:

```Java
cellBehavior = equality;
```

You may also tweak the size of the cells by changing the *cellWidth*, or the speed of the simulation by changing the *secDelay* variable.

