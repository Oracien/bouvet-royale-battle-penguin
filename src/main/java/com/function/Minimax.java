package com.function;

public class Minimax {

    private int evaluateState(Match gameState) {
        int negativeValue = getNegativeValue(Match gameState);
        return value;
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
                value = Math.max(value, getValue(generateSuccessorState(gameState, agentIndex, action), depth-1, agentIndex+1));
            }
            return value;
        }
        double value = 1e100;
        Action[] possibleActions = getLegalActions(gameState, agentIndex);
        for (Action action : possibleActions) {
            value = Math.min(value, getValue(generateSuccessorState(gameState, agentIndex, action), depth-1, agentIndex+1));
        }
        return value;
    }

    public Action chooseAction() {

    }

}
