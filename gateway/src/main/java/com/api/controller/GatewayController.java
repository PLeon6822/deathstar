package com.api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ThreadLocalRandom;
import com.api.services.Requests;

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
    int r_response = request_controller.request(HOST+RESOURCE_PORT+"/resource/titanium", "GET");
    int b_response = request_controller.request(HOST+BUILD_PORT+"/get?structure=hull&position="+pos, "GET");
    if (r_response >= HULL_COST && b_response == 0){
      int response = request_controller.request(HOST+BUILD_PORT+"/build?structure=hull&reinforced=false&position="+pos, "POST");
      return "Your request for a regular hull on position " + pos + " was approved.\nResponse: " + response;
    } else if (b_response >= 1) {
      return "The requested position is already requested, under construction or completed\nResponse: " + b_response;
    } else {
      return "Insufficient resources, need more titanium";
    }
  }

  @RequestMapping(value = "/build/reinforced_hull", method = RequestMethod.POST)
  String buildReinforcedHull(@RequestBody int pos){
    int r_response = request_controller.request(HOST+RESOURCE_PORT+"/resource/titanium", "GET");
    int b_response = request_controller.request(HOST+BUILD_PORT+"/get?structure=hull&reinforced=true&position="+pos, "GET");
    if (r_response >= HULL_COST + R_HULL_COST && b_response == 0) {
      int response = request_controller.request(HOST+BUILD_PORT+"/build?structure=hull&reinforced=true&position="+pos, "POST");
      return "Your request for a reinforced hull on position " + pos + " was approved.\nResponse: " + response;
    } else if (b_response >= 1){
      return "The requested position is already requested, under construction or completed\nResponse " + b_response;
    } else {
      return "Insufficient resources, need more titanium";
    }
  }

  @RequestMapping(value = "/build/cannon", method = RequestMethod.POST)
  String buildCannon(@RequestBody int pos){
    int t_response = request_controller.request(HOST+RESOURCE_PORT+"/resource/titanium", "GET");
    int p_response = request_controller.request(HOST+RESOURCE_PORT+"/resource/plasma", "GET");
    int b_response = request_controller.request(HOST+BUILD_PORT+"/get?structure=cannon&&position="+pos, "GET");
    if (t_response >= CANNON_COST && p_response >= CANNON_COST && b_response == 0){
      int response = request_controller.request(HOST+BUILD_PORT+"/build?structure=cannon&reinforced=false&position="+pos, "POST");
      return "Your request for a regular cannon on position " + pos + " was approved.\nResponse: " + response;
    } else if (b_response >= 1){
      return "The requested position is already requested, under construction or completed\nResponse " + b_response;
    } else if (b_response == -1) {
      return "There is no hull on which to build a cannon on position " + pos + ", if there are sufficient resources, it will be built";
    } else {
      return "Insufficient resources, need 1 plasma and 1 titanium.\nCurrent resources:\nPlasma: " + p_response + "\nTitanium: " + t_response;
    }
  }

  @RequestMapping(value = "/build/reinforced_cannon", method = RequestMethod.POST)
  String buildReinforcedCannon(@RequestBody int pos){
    
    return "Your request for a reinforced cannon on position " + pos + " was approved.";
  }

  @RequestMapping(value = "/resources", method = RequestMethod.GET)
  String getResources(){
    int t_response = request_controller.request(HOST+RESOURCE_PORT+"/resource/titanium", "GET");
    int p_response = request_controller.request(HOST+RESOURCE_PORT+"/resource/plasma", "GET");
    return "There are currently these resources:\nPlasma: " + p_response + "\nTitanium: " + t_response;
  }
}