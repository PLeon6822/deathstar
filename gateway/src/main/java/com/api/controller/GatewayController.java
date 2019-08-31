package main.java.com.api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ThreadLocalRandom;
import main.java.com.api.requests.*;
import main.java.com.api.services.Requests;

@RestController
public class GatewayController {

  public final int MIN_SIZE = 20;
  public final int MAX_SIZE = 50;
  public final int STAR_SIZE = ThreadLocalRandom.current().nextInt(MIN_SIZE, MAX_SIZE + 1);

  public final String HOST = "http://localhost:";
  public final int RESOURCE_PORT = 5000;
  public final int BUILD_PORT = 5001;

  public final int HULL_COST = 5;
  public final int R_HULL_COST = 4;
  public final int CANNON_COST = 1;
  public final int R_CANNON_COST = 2;

  public Requests request_controller;

  @RequestMapping(value = "/build/hull", method = RequestMethod.POST)
  String buildHull(@RequestBody int pos){
    int response = request_controller.request(HOST+RESOURCE_PORT+"/resource/titanium", "GET");
    if (response >= HULL_COST){
      response = request_controller.request(HOST+BUILD_PORT+"/build?hull&reinforced=false&position="+pos, "POST");
      return "Your request for a regular hull on position " + pos + " was approved.\nResponse: " + response;
    } else {
      return "Insufficient resources, need more titanium";
    }
  }

  @RequestMapping(value = "/build/reinforced_hull", method = RequestMethod.POST)
  String buildReinforcedHull(@RequestBody int pos){
    int response = request_controller.request(HOST+RESOURCE_PORT+"/resource/titanium", "GET");
    if (response >= HULL_COST + R_HULL_COST) {
      response = request_controller.request(HOST+BUILD_PORT+"/build?hull&reinforced=false&position="+pos, "POST");
    }
    return "Your request for a reinforced hull on position " + pos + " was approved.";
  }

  @RequestMapping(value = "/build/cannon", method = RequestMethod.POST)
  String buildCannon(@RequestBody int pos){
    
    return "Your request for a regular cannon on position " + pos + " was approved.";
  }

  @RequestMapping(value = "/build/reinforced_cannon", method = RequestMethod.POST)
  String buildReinforcedCannon(@RequestBody int pos){
    
    return "Your request for a reinforced cannon on position " + pos + " was approved.";
  }

  @RequestMapping(value = "/resources", method = RequestMethod.GET)
  String getResources(){
    
    return "There are currently these resources:\nPlasma: " + 0 + "\nTitanium: " + 0;
  }
}