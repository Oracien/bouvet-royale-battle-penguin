package com.function;

class Match {
    String matchId;
        int mapWidth;
        int mapHeight;
        int wallDamage;
        int penguinDamage;
        int weaponDamage;
        int visibility;
        int weaponRange;
        You you;
        Enemy[] enemies;
        Wall[] walls;
        Bonustile[] bonusTiles;
        int suddenDeath;
        Fire[] fire;

    public class You
    {
        String direction;
        int x;
        int y;
        int strength;
        int ammo;
        String status;
        int weaponDamage;
        int weaponRange;
        int targetRange;
        String bonus;
    }

    public class Enemy
    {
        String direction;
        int x;
        int y;
        int strength;
        int ammo;
        int weaponDamage;
        int weaponRange;
    }

    public class Wall
    {
        int x;
        int y;
        int strength;
    }

    public class Bonustile
    {
        int x;
        int y;
        String type;
        int value;
    }

    public class Fire
    {
        int x;
        int y;
    }
}