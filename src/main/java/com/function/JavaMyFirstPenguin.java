package com.function;

import com.function.data.objects.Penguin;
import com.google.gson.Gson;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class JavaMyFirstPenguin {

    @FunctionName("JavaMyFirstPenguin")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS}, route = "JavaMyFirstPenguin/{queryString}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context, @BindingName("queryString") String queryString) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        Gson gson = new Gson();
        if (request.getHttpMethod() == HttpMethod.GET && queryString.equals("info")) {
            Penguin penguin = new Penguin("Java", "Tech 2");
            return  request.createResponseBuilder(HttpStatus.OK).header("content-type", "application/json").body(gson.toJson(penguin)).build();
        } else if (request.getHttpMethod() == HttpMethod.POST && queryString.equals("command")){
            String json = request.getBody().toString();
            json = json.substring(9, json.length()-1);
            Match match = gson.fromJson(json, Match.class);
            Action action = new Action(match);
            String chosenAction = action.chooseAction();
            return request.createResponseBuilder(HttpStatus.OK).header("content-type", "application/json").body("{\"command\": \"" + chosenAction + "\"}").build();
        }

        return request.createResponseBuilder(HttpStatus.OK).body("Hello").build();
    }

    public static void main(String[] args){
        Match gameState = new Match();
        Match.You you = gameState.new You();
        you.x=10;
        you.y=10;
        you.strength=500;
        you.direction="right";
        you.weaponRange=5;
        you.ammo=50;
        you.bonus="";
        you.weaponDamage=100;
        you.status="";
        you.targetRange=5;
        Match.Enemy enemy = gameState.new Enemy();
        enemy.x=50;
        enemy.y=50;
        enemy.strength=500;
        enemy.direction="top";
        enemy.ammo=0;
        enemy.weaponDamage=100;
        enemy.weaponRange=5;
        gameState.matchId="test";
        gameState.mapWidth=100;
        gameState.mapHeight=100;
        gameState.suddenDeath=100;
        gameState.wallDamage=100;
        gameState.penguinDamage=100;
        gameState.weaponDamage=100;
        gameState.visibility=5;
        gameState.weaponRange=5;
        gameState.you=you;
        gameState.enemies= new Match.Enemy[]{enemy};
        gameState.bonusTiles=new Match.Bonustile[0];
        gameState.walls=new Match.Wall[0];
        gameState.fire= new Match.Fire[0];

        Action action = new Action(gameState);
        String chosenAction = action.chooseAction();
        System.out.println(chosenAction);
    }
}
