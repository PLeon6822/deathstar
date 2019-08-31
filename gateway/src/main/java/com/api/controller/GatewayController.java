package main.java.com.api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ThreadLocalRandom;
import main.java.com.api.requests.*;

@RestController
public class GatewayController {

  public final int MIN_SIZE = 20;
  public final int MAX_SIZE = 50;
  public final int STAR_SIZE = ThreadLocalRandom.current().nextInt(MIN_SIZE, MAX_SIZE + 1);

  @RequestMapping(value = "/build/hull", method = RequestMethod.POST)
  String buildHull(@RequestBody int pos){
    
    return "Your request for a regular hull on position " + pos + " was approved.";
  }

  @RequestMapping(value = "/build/reinforced_hull", method = RequestMethod.POST)
  String buildReinforcedHull(@RequestBody int pos){
    
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