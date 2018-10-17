package com.function;

import com.google.gson.Gson;
import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

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
}
