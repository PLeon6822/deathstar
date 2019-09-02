package com.api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ThreadLocalRandom;
import com.api.services.Requests;
import com.api.services.RequestsImpl;

import org.json.simple.JSONObject;

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

  public Requests request_controller = new RequestsImpl();

  @RequestMapping(value = "/build/hull", method = RequestMethod.POST)
  String buildHull(@RequestBody int pos){
    JSONObject obj = new JSONObject();
    obj.put("position",new Integer(pos));
    JSONObject r_response = request_controller.request(HOST+RESOURCE_PORT+"/resources/titanium", "GET");
    JSONObject b_response = request_controller.request(HOST+BUILD_PORT+"/status/hull/",obj, "GET");
    int resource = Integer.parseInt((String) r_response.get("amount"));
    int build_status = Integer.parseInt((String) b_response.get("status"));
    if (resource >= HULL_COST && build_status == 0){
      JSONObject response = request_controller.request(HOST+BUILD_PORT+"/build/hull/", obj, "POST");
      return "Your request for a regular hull on position " + pos + " was approved.\nResponse: " + response;
    } else if (build_status >= 1) {
      return "The requested position is already requested, under construction or completed\nResponse: " + b_response;
    } else {
      return "Insufficient resources, need more titanium";
    }
  }

  @RequestMapping(value = "/build/reinforced_hull", method = RequestMethod.POST)
  String buildReinforcedHull(@RequestBody int pos){
    JSONObject obj = new JSONObject();
    obj.put("position",new Integer(pos));
    JSONObject r_response = request_controller.request(HOST+RESOURCE_PORT+"/resources/titanium", "GET");
    JSONObject b_response = request_controller.request(HOST+BUILD_PORT+"/status/reinforced_hull/", obj, "GET");
    int resource = Integer.parseInt((String) r_response.get("amount"));
    int build_status = Integer.parseInt((String) b_response.get("status"));
    if (resource >= HULL_COST + R_HULL_COST && build_status == 0) {
      JSONObject response = request_controller.request(HOST+BUILD_PORT+"/build/reinforced_hull/", obj, "POST");
      return "Your request for a reinforced hull on position " + pos + " was approved.\nResponse: " + response;
    } else if (build_status >= 1){
      return "The requested position is already requested, under construction or completed\nResponse " + b_response;
    } else {
      return "Insufficient resources, need more titanium";
    }
  }

  @RequestMapping(value = "/build/cannon", method = RequestMethod.POST)
  String buildCannon(@RequestBody int pos){
    JSONObject obj = new JSONObject();
    obj.put("position",new Integer(pos));
    JSONObject t_response = request_controller.request(HOST+RESOURCE_PORT+"/resources/titanium", "GET");
    JSONObject p_response = request_controller.request(HOST+RESOURCE_PORT+"/resources/plasma", "GET");
    JSONObject b_response = request_controller.request(HOST+BUILD_PORT+"/status/cannon/", obj, "GET");
    int titanium = Integer.parseInt((String) t_response.get("amount"));
    int plasma = Integer.parseInt((String) p_response.get("amount"));
    int build_status = Integer.parseInt((String) b_response.get("status"));
    if (titanium >= CANNON_COST && plasma >= CANNON_COST && build_status == 0){
      JSONObject response = request_controller.request(HOST+BUILD_PORT+"/build/cannon", obj, "POST");
      return "Your request for a regular cannon on position " + pos + " was approved.\nResponse: " + response;
    } else if (build_status >= 1){
      return "The requested position is already requested, under construction or completed\nResponse " + b_response;
    } else if (build_status == -1) {
      return "There is no hull on which to build a cannon on position " + pos + ", please request a hull or wait for its completion";
    } else {
      return "Insufficient resources, need 1 plasma and 1 titanium.\nCurrent resources:\nPlasma: " + p_response + "\nTitanium: " + t_response;
    }
  }

  @RequestMapping(value = "/build/reinforced_cannon", method = RequestMethod.POST)
  String buildReinforcedCannon(@RequestBody int pos){
    JSONObject obj = new JSONObject();
    obj.put("position",new Integer(pos));
    JSONObject t_response = request_controller.request(HOST+RESOURCE_PORT+"/resources/titanium", "GET");
    JSONObject p_response = request_controller.request(HOST+RESOURCE_PORT+"/resources/plasma", "GET");
    JSONObject b_response = request_controller.request(HOST+BUILD_PORT+"/status/reinforced_cannon", obj, "GET");
    int titanium = Integer.parseInt((String) t_response.get("amount"));
    int plasma = Integer.parseInt((String) p_response.get("amount"));
    int build_status = Integer.parseInt((String) b_response.get("status"));
    if (titanium >= 2*CANNON_COST && plasma >= 2*CANNON_COST && build_status == 0){
      JSONObject response = request_controller.request(HOST+BUILD_PORT+"/build/reinforced_cannon", obj, "POST");
      return "Your request for a reinforced cannon on position " + pos + " was approved.\nResponse: " + response;
    } else if (build_status >= 1){
      return "The requested position is already requested, under construction or completed\nResponse " + b_response;
    } else if (build_status == -1) {
      return "There is no hull on which to build a cannon on position " + pos + ", please request a hull or wait for its completion";
    } else {
      return "Insufficient resources, need 1 plasma and 1 titanium.\nCurrent resources:\nPlasma: " + p_response + "\nTitanium: " + t_response;
    }
  }

  @RequestMapping(value = "/resources", method = RequestMethod.GET)
  String getResources(){
    JSONObject t_response = request_controller.request(HOST+RESOURCE_PORT+"/resources/titanium", "GET");
    JSONObject p_response = request_controller.request(HOST+RESOURCE_PORT+"/resources/plasma", "GET");
    int titanium = Integer.parseInt((String) t_response.get("amount"));
    int plasma = Integer.parseInt((String) p_response.get("amount"));
    return "The Empire Reserves currently owns these resources:\nPlasma: " + plasma + "\nTitanium: " + titanium;
  }
}