package com.function;

import java.util.Arrays;

public class Minimax {

    private int evaluateState(Match gameState) {
        int negativeValue = getNegativeValue(gameState);
        int positiveValue = getPositiveValue(gameState);
        return negativeValue + positiveValue;
    }

    private int getNegativeValue(Match gameState) {
        int value = 0;
        if(gameState.you.strength == 0) {
            value -= 10000;
        }
        value -= (500 - gameState.you.strength) * 20;
        if(isInFire(gameState)) {
            value -= 500;
        }
        if(closeToFire(gameState)) {
            value -= 50;
        }
        if(awayFromEnemy(gameState)) {
            value -= 50;
        }
        return value;
    }

    private int getPositiveValue(Match gameState) {
        int value = 0;
        if(gameState.enemies[0].strength == 0) {
            value += 10000;
        }
        value += (500 - gameState.enemies[0].strength) * 20;
        switch(gameState.you.bonus) {
            case "weapon-range":
                value += 100;
                break;
            case "weapon-damage":
                value += 100;
                break;
            case "strength":
                value += 100;
                break;
            default:
                break;
        }
        value += (50 - distanceFromCenter);
        return value;
    }

    private Action[] getLegalActions(Match gameState, int agentIndex) {

    }

    private Match generateSuccessorState(Match gameState, int agentIndex, Action action) {

    }

    public double getValue(Match gameState, int depth, int agentIndex) {
        if(depth == 0 || getLegalActions(gameState, agentIndex).length == 0) {
            return evaluateState(gameState);
        }
        if (agentIndex == 0) {
            double value = -1e100;
            Action[] possibleActions = getLegalActions(gameState, agentIndex);
            for (Action action : possibleActions) {
                value = Math.max(value, getValue(generateSuccessorState(gameState, agentIndex, action), depth-1, (agentIndex+1) % 2));
            }
            return value;
        }
        double value = 1e100;
        Action[] possibleActions = getLegalActions(gameState, agentIndex);
        for (Action action : possibleActions) {
            value = Math.min(value, getValue(generateSuccessorState(gameState, agentIndex, action), depth-1, (agentIndex+1) %2));
        }
        return value;
    }

    public Action chooseAction(Match gameState) {
        Action[] possibleActions = getLegalActions(gameState, 0);
        double[] values = new double[possibleActions.length];
        for (int i = 0; i < possibleActions.length; i++) {
            values[i] = getValue(generateSuccessorState(gameState, 0, possibleActions[i]), 3, 1);
        }
        double maxValue = -1e100;
        for (double value : values) {
            maxValue = Math.max(maxValue, value);
        }
        return possibleActions[Arrays.asList(values).indexOf(maxValue)];
    }

}
