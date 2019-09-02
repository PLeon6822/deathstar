package com.api.services;

import org.json.simple.JSONObject;

public interface Requests{
  
  public int request(String url, JSONObject obj, String method);

  public int request(String url, String method);

}