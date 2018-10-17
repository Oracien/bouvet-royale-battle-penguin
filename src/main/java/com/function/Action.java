package com.function;

import java.util.HashMap;
import java.util.Map;

public class Action {

    private static final  String ROTATE_RIGHT = "rotate-right";
    private static final  String ROTATE_LEFT = "rotate-left";
    private static final  String ADVANCE = "advance";
    private static final  String RETREAT = "retreat";
    private static final  String SHOOT = "shoot";
    private static final  String PASS = "pass";
    private static final  Map<String, String> MOVE_UP;
    private static final  Map<String, String> MOVE_DOWN;
    private static final  Map<String, String> MOVE_RIGHT;
    private static final  Map<String, String> MOVE_LEFT;

    static {
        MOVE_UP = new HashMap<>();
        MOVE_UP.put("top", ADVANCE);
        MOVE_UP.put("bottom", ROTATE_LEFT);
        MOVE_UP.put("left", ROTATE_RIGHT);
        MOVE_UP.put("right", ROTATE_LEFT);

        MOVE_DOWN = new HashMap<>();
        MOVE_DOWN.put("top", ROTATE_LEFT);
        MOVE_DOWN.put("bottom", ADVANCE);
        MOVE_DOWN.put("left", ROTATE_LEFT);
        MOVE_DOWN.put("right", ROTATE_RIGHT);

        MOVE_RIGHT = new HashMap<>();
        MOVE_RIGHT.put("top", ROTATE_RIGHT);
        MOVE_RIGHT.put("bottom", ROTATE_LEFT);
        MOVE_RIGHT.put("left", ROTATE_LEFT);
        MOVE_RIGHT.put("right", ADVANCE);

        MOVE_LEFT = new HashMap<>();
        MOVE_LEFT.put("top", ROTATE_LEFT);
        MOVE_LEFT.put("bottom", ROTATE_RIGHT);
        MOVE_LEFT.put("left", ADVANCE);
        MOVE_LEFT.put("right", ROTATE_RIGHT);
    }

    private Match match;

    private String moveTowardsCenterOfMap()
    {
        int centerPointx = match.mapWidth / 2;
        int centerPointy = match.mapHeight / 2;
        return moveTowardsPoint(centerPointx, centerPointy);
    }

    private String moveTowardsPoint(int pointx, int pointy)
    {
        int  penguinPositionx = match.you.x;
        int penguinPositiony = match.you.y;
        String plannedAction = PASS;

        if (penguinPositionx < pointx)
        {
            plannedAction = MOVE_RIGHT.get(match.you.direction);
        }
        else if (penguinPositionx > pointx)
        {
            plannedAction = MOVE_LEFT.get(match.you.direction);
        }
        else if (penguinPositiony < pointy)
        {
            plannedAction = MOVE_DOWN.get(match.you.direction);
        }
        else if (penguinPositiony > pointy)
        {
            plannedAction = MOVE_UP.get(match.you.direction);
        }
        if (plannedAction.equals(ADVANCE) && wallInFrontOfPenguin())
        {
            return SHOOT;
        }
        return plannedAction;
    }

    private boolean doesCellContainWall(int x, int y)
    {
        for (int i = 0; i < match.walls.length; i++) {
            if (match.walls[i].x == x && match.walls[i].y == y) {
                return true;
            }
        }
        return false;
    }

    private boolean wallInFrontOfPenguin()
    {
        switch (match.you.direction)
        {
            case "top":
                return doesCellContainWall(match.you.x, --match.you.y);
            case "bottom":
                return doesCellContainWall(match.you.x, ++match.you.y);
            case "left":
                return doesCellContainWall(--match.you.x, match.you.y);
            case "right":
                return doesCellContainWall(++match.you.x, match.you.y);
            default:
                return true;
        }
    }

    Action(Match match) {
        this.match = match;
    }

    String chooseAction() {
        String response;
        response = moveTowardsCenterOfMap();
        return response;
    }
}