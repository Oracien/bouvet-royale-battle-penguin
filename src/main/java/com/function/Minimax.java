package com.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private boolean awayFromEnemy(Match gameState) {
        int enemyX = gameState.enemies[0].x;
        int enemyY = gameState.enemies[0].y;
        int myX = gameState.you.x;
        int myY = gameState.you.y;
        return Math.sqrt(Math.pow(enemyX - myX, 2) + Math.pow(enemyY - myY, 2)) > 3;
    }

    private boolean closeToFire(Match gameState) {
        return gameState.fire.length > 0;
    }

    private boolean isInFire(Match gameState) {
        return Arrays.stream(gameState.fire)
                .map(flame -> flame.x * gameState.mapWidth + flame.y)
                .anyMatch(flamecoord -> flamecoord == gameState.you.x * gameState.mapWidth + gameState.you.y);
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
	value += (50 - distanceFromCenter(gameState));
	return value;
    }

    private int distanceFromCenter(Match gameState){
	return Math.abs(gameState.you.x-gameState.mapWidth/2)+Math.abs(gameState.you.y-gameState.mapHeight/2);
    }

    private String[] getLegalActions(Match gameState, int agentIndex) {
	List<String> actions = new ArrayList<>(Arrays.asList("pass", "rotate-right", "rotate-left"));
	Action action = new Action(gameState);
	if(agentIndex==0){
	    if(gameState.you.ammo>0 && gameState.enemies[0].direction != null){
			actions.add("shoot");
	    }
	    switch (gameState.you.direction)
	    {
		case "top":
		    if(!action.doesCellContainWall(gameState.you.x, --gameState.you.y)){
			actions.add("advance");
		    }
		    if(!action.doesCellContainWall(gameState.you.x, ++gameState.you.y)){
			actions.add("retreat");
		    }
		    break;
		case "bottom":
		    if(!action.doesCellContainWall(gameState.you.x, ++gameState.you.y)){
			actions.add("advance");
		    }
		    if(!action.doesCellContainWall(gameState.you.x, --gameState.you.y)){
			actions.add("retreat");
		    }
		    break;
		case "left":
		    if(!action.doesCellContainWall(--gameState.you.x, gameState.you.y)){
			actions.add("advance");
		    }
		    if(!action.doesCellContainWall(++gameState.you.x, gameState.you.y)){
			actions.add("retreat");
		    }
		    break;
		case "right":
		    if(!action.doesCellContainWall(++gameState.you.x, gameState.you.y)){
			actions.add("advance");
		    }
		    if(!action.doesCellContainWall(--gameState.you.x, gameState.you.y)){
			actions.add("retreat");
		    }
		    break;
	    }
	} else {
	    if(gameState.enemies[0].ammo>0 && gameState.enemies[0].direction != null){
			actions.add("shoot");
	    }
	    if (gameState.enemies[0].direction == null) {
	    	actions.clear();
	    	return actions.toArray(new String[0]);
		}
	    switch (gameState.enemies[0].direction)
	    {
		case "top":
		    if(!action.doesCellContainWall(gameState.enemies[0].x, --gameState.enemies[0].y)){
			actions.add("advance");
		    }
		    if(!action.doesCellContainWall(gameState.enemies[0].x, ++gameState.enemies[0].y)){
			actions.add("retreat");
		    }
		    break;
		case "bottom":
		    if(!action.doesCellContainWall(gameState.enemies[0].x, ++gameState.enemies[0].y)){
			actions.add("advance");
		    }
		    if(!action.doesCellContainWall(gameState.enemies[0].x, --gameState.enemies[0].y)){
			actions.add("retreat");
		    }
		    break;
		case "left":
		    if(!action.doesCellContainWall(--gameState.enemies[0].x, gameState.enemies[0].y)){
			actions.add("advance");
		    }
		    if(!action.doesCellContainWall(++gameState.enemies[0].x, gameState.enemies[0].y)){
			actions.add("retreat");
		    }
		    break;
		case "right":
		    if(!action.doesCellContainWall(++gameState.enemies[0].x, gameState.enemies[0].y)){
			actions.add("advance");
		    }
		    if(!action.doesCellContainWall(--gameState.enemies[0].x, gameState.enemies[0].y)){
			actions.add("retreat");
		    }
		    break;
	    }
	}
	return actions.toArray(new String[0]);
    }

    private final static String[] DIRECTIONS = new String[]{"top", "right", "bottom", "left"};

    private boolean isAdjacent(Match gameState) {
        return Math.abs(gameState.you.x - gameState.enemies[0].x) + (Math.abs(gameState.you.y - gameState.enemies[0].y)) == 1;
    }

    private Match generateSuccessorState(Match gameState, int agentIndex, String action) {
        if (agentIndex == 0) {
            if(action.equals("pass")) {
                return gameState;
            }
            if(action.equals("rotate-right")) {
                gameState.you.direction = DIRECTIONS[(Arrays.asList(DIRECTIONS).indexOf(gameState.you.direction) + 1) %4];
                return gameState;

            }
            if(action.equals("rotate-left")) {
                gameState.you.direction = DIRECTIONS[(Arrays.asList(DIRECTIONS).indexOf(gameState.you.direction) - 1) %4];
                return gameState;
            }
            if(action.equals("advance")) {
                if(isAdjacent(gameState)) {
                    switch (gameState.you.direction) {
                        case "top":
                            if (gameState.you.y - gameState.enemies[0].y == 1) {
                                gameState.you.strength -= gameState.penguinDamage;
                                gameState.enemies[0].strength -= gameState.penguinDamage;
                                return gameState;
                            }
                            break;
                        case "bottom":
                            if (gameState.you.y - gameState.enemies[0].y == -1) {
                                gameState.you.strength -= gameState.penguinDamage;
                                gameState.enemies[0].strength -= gameState.penguinDamage;
                                return gameState;
                            }
                            break;
                        case "left":
                            if (gameState.you.x - gameState.enemies[0].x == 1) {
                                gameState.you.strength -= gameState.penguinDamage;
                                gameState.enemies[0].strength -= gameState.penguinDamage;
                                return gameState;
                            }
                            break;
                        case "right":
                            if (gameState.you.x - gameState.enemies[0].x == -1) {
                                gameState.you.strength -= gameState.penguinDamage;
                                gameState.enemies[0].strength -= gameState.penguinDamage;
                                return gameState;
                            }
                            break;
                    }
                }
                else{
                    switch (gameState.you.direction) {
                        case "top":
                            gameState.you.y--;
                            return gameState;
                        case "bottom":
                            gameState.you.y++;
                            return gameState;
                        case "left":
                            gameState.you.x--;
                            return gameState;
                        case "right":
                            gameState.you.x++;
                            return gameState;
                    }
                }
            }
            if(action.equals("retreat")) {
                if(isAdjacent(gameState)) {
                    switch (gameState.you.direction) {
                        case "bottom":
                            if (gameState.you.y - gameState.enemies[0].y == 1) {
                                gameState.you.strength -= gameState.penguinDamage;
                                gameState.enemies[0].strength -= gameState.penguinDamage;
                                return gameState;
                            }
                            break;
                        case "top":
                            if (gameState.you.y - gameState.enemies[0].y == -1) {
                                gameState.you.strength -= gameState.penguinDamage;
                                gameState.enemies[0].strength -= gameState.penguinDamage;
                                return gameState;
                            }
                            break;
                        case "right":
                            if (gameState.you.x - gameState.enemies[0].x == 1) {
                                gameState.you.strength -= gameState.penguinDamage;
                                gameState.enemies[0].strength -= gameState.penguinDamage;
                                return gameState;
                            }
                            break;
                        case "left":
                            if (gameState.you.x - gameState.enemies[0].x == -1) {
                                gameState.you.strength -= gameState.penguinDamage;
                                gameState.enemies[0].strength -= gameState.penguinDamage;
                                return gameState;
                            }
                            break;
                    }
                }
                else{
                    switch (gameState.you.direction) {
                        case "bottom":
                            gameState.you.y--;
                            return gameState;
                        case "top":
                            gameState.you.y++;
                            return gameState;
                        case "right":
                            gameState.you.x--;
                            return gameState;
                        case "left":
                            gameState.you.x++;
                            return gameState;
                    }
                }
            }
            }
            if(action.equals("shoot")) {
                if(willYouHit(gameState)) {
                    gameState.enemies[0].strength -= gameState.you.weaponDamage;
                    return gameState;
            }
        }
        else {
                if(action.equals("pass")) {
                    return gameState;
                }
                if(action.equals("rotate-right")) {
                    gameState.enemies[0].direction = DIRECTIONS[(Arrays.asList(DIRECTIONS).indexOf(gameState.enemies[0].direction) + 1) %4];
                    return gameState;

                }
                if(action.equals("rotate-left")) {
                    gameState.enemies[0].direction = DIRECTIONS[(Arrays.asList(DIRECTIONS).indexOf(gameState.enemies[0].direction) - 1) %4];
                    return gameState;
                }
                if(action.equals("advance")) {
                    if(isAdjacent(gameState)) {
                        switch (gameState.enemies[0].direction) {
                            case "top":
                                if (gameState.enemies[0].y - gameState.you.y == 1) {
                                    gameState.enemies[0].strength -= gameState.penguinDamage;
                                    gameState.you.strength -= gameState.penguinDamage;
                                    return gameState;
                                }
                                break;
                            case "bottom":
                                if (gameState.enemies[0].y - gameState.you.y == -1) {
                                    gameState.enemies[0].strength -= gameState.penguinDamage;
                                    gameState.you.strength -= gameState.penguinDamage;
                                    return gameState;
                                }
                                break;
                            case "left":
                                if (gameState.enemies[0].x - gameState.you.x == 1) {
                                    gameState.enemies[0].strength -= gameState.penguinDamage;
                                    gameState.you.strength -= gameState.penguinDamage;
                                    return gameState;
                                }
                                break;
                            case "right":
                                if (gameState.enemies[0].x - gameState.you.x == -1) {
                                    gameState.enemies[0].strength -= gameState.penguinDamage;
                                    gameState.you.strength -= gameState.penguinDamage;
                                    return gameState;
                                }
                                break;
                        }
                    }
                    else{
                        switch (gameState.enemies[0].direction) {
                            case "top":
                                gameState.enemies[0].y--;
                                return gameState;
                            case "bottom":
                                gameState.enemies[0].y++;
                                return gameState;
                            case "left":
                                gameState.enemies[0].x--;
                                return gameState;
                            case "right":
                                gameState.enemies[0].x++;
                                return gameState;
                        }
                    }
                }
                if(action.equals("retreat")) {
                    if(isAdjacent(gameState)) {
                        switch (gameState.enemies[0].direction) {
                            case "bottom":
                                if (gameState.enemies[0].y - gameState.you.y == 1) {
                                    gameState.enemies[0].strength -= gameState.penguinDamage;
                                    gameState.you.strength -= gameState.penguinDamage;
                                    return gameState;
                                }
                                break;
                            case "top":
                                if (gameState.enemies[0].y - gameState.you.y == -1) {
                                    gameState.enemies[0].strength -= gameState.penguinDamage;
                                    gameState.you.strength -= gameState.penguinDamage;
                                    return gameState;
                                }
                                break;
                            case "right":
                                if (gameState.enemies[0].x - gameState.you.x == 1) {
                                    gameState.enemies[0].strength -= gameState.penguinDamage;
                                    gameState.you.strength -= gameState.penguinDamage;
                                    return gameState;
                                }
                                break;
                            case "left":
                                if (gameState.enemies[0].x - gameState.you.x == -1) {
                                    gameState.enemies[0].strength -= gameState.penguinDamage;
                                    gameState.you.strength -= gameState.penguinDamage;
                                    return gameState;
                                }
                                break;
                        }
                    }
                    else{
                        switch (gameState.enemies[0].direction) {
                            case "bottom":
                                gameState.enemies[0].y--;
                                return gameState;
                            case "top":
                                gameState.enemies[0].y++;
                                return gameState;
                            case "right":
                                gameState.enemies[0].x--;
                                return gameState;
                            case "left":
                                gameState.enemies[0].x++;
                                return gameState;
                        }
                    }
                }
            }
        if(action.equals("shoot")) {
            if(WillEnemyHit(gameState)) {
                gameState.you.strength -= gameState.enemies[0].weaponDamage;
                return gameState;
            }
        }
        return gameState;
    }

    private boolean isWithinLimit(int position1, int position2, int range) {
        return position1 - position2 <= range && position1 - position2 >= 0;
    }

    private boolean isSameField(int position1, int position2) {
        return position1 == position2;
    }

    private boolean willYouHit(Match gameState) {
        switch(gameState.you.direction) {
            case "bottom":
                return (isSameField(gameState.you.x, gameState.enemies[0].x) &&
                        isWithinLimit(gameState.enemies[0].y, gameState.you.y, gameState.you.weaponRange));
            case "top":
                return (isSameField(gameState.you.x, gameState.enemies[0].x) &&
                        isWithinLimit(gameState.you.y, gameState.enemies[0].y, gameState.you.weaponRange));
            case "right":
                return (isSameField(gameState.you.y, gameState.enemies[0].y) &&
                        isWithinLimit(gameState.enemies[0].x, gameState.you.x, gameState.you.weaponRange));
            case "left":
                return (isSameField(gameState.you.y, gameState.enemies[0].y) &&
                        isWithinLimit(gameState.you.x, gameState.enemies[0].x, gameState.you.weaponRange));
        }
        return false;
    }

    private boolean WillEnemyHit(Match gameState) {
        switch(gameState.enemies[0].direction) {
            case "top":
                return (isSameField(gameState.enemies[0].x, gameState.you.x) &&
                        isWithinLimit(gameState.you.y, gameState.enemies[0].y, gameState.enemies[0].weaponRange));
            case "bottom":
                return (isSameField(gameState.enemies[0].x, gameState.you.x) &&
                        isWithinLimit(gameState.enemies[0].y, gameState.you.y, gameState.enemies[0].weaponRange));
            case "left":
                return (isSameField(gameState.enemies[0].y, gameState.you.y) &&
                        isWithinLimit(gameState.you.x, gameState.enemies[0].x, gameState.you.weaponRange));
            case "right":
                return (isSameField(gameState.enemies[0].y, gameState.you.y) &&
                        isWithinLimit(gameState.enemies[0].x, gameState.you.y, gameState.you.weaponRange));
        }
        return false;
    }

    public double getValue(Match gameState, int depth, int agentIndex) {
        if(depth == 0 || getLegalActions(gameState, agentIndex).length == 0) {
            return evaluateState(gameState);
        }
        if (agentIndex == 0) {
            double value = -1e100;
            String[] possibleActions = getLegalActions(gameState, agentIndex);
            for (String action : possibleActions) {
                value = Math.max(value, getValue(generateSuccessorState(gameState, agentIndex, action), depth-1, (agentIndex+1) % 2));
            }
            return value;
        }
        double value = 1e100;
        String[] possibleActions = getLegalActions(gameState, agentIndex);
        for (String action : possibleActions) {
            value = Math.min(value, getValue(generateSuccessorState(gameState, agentIndex, action), depth-1, (agentIndex+1) %2));
        }
        return value;
    }

    public String chooseAction(Match gameState) {
        String[] possibleActions = getLegalActions(gameState, 0);
        System.out.println(possibleActions.length);
        double[] values = new double[possibleActions.length];
        for (int i = 0; i < possibleActions.length; i++) {
            values[i] = getValue(generateSuccessorState(gameState, 0, possibleActions[i]), 3, 1);
        }
        double maxValue = -1e100;
        int where = -1;
        for (int i=0; i<values.length; i++) {
            if(values[i]>maxValue){
                maxValue = values[i];
                where = i;
            }
        }
        return possibleActions[where];
    }


}
