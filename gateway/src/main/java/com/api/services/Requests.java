package com.api.services;

import org.json.simple.JSONObject;

public interface Requests{
  
  public JSONObject request(String url, JSONObject obj, String method);

  public JSONObject request(String url, String method);

}