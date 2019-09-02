package com.api.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.api.services.Requests;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RequestsImpl implements Requests{

  private HttpURLConnection connection = null;
  /***
   * Handles HTTP connections to provided Host and with provided REST method
   * @param url
   * @param method
   * @return response from connection, JSON expected
   */
  public JSONObject request(String url, String method){
    this.connection = null;
    JSONObject response = new JSONObject();
    try {
      URL con_url = new URL(url);
      this.connection = (HttpURLConnection) con_url.openConnection();
      connection.setRequestMethod(method);
      connection.setDoOutput(false);

      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String content = in.readLine();
      in.close();

      JSONParser parser = new JSONParser();
      Object parsed = parser.parse(content);
      response = (JSONObject) parsed;

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (connection != null){
        connection.disconnect();
      }
    }
    return response;
  }

  public JSONObject request(String url, JSONObject obj, String method){
    this.connection = null;
    JSONObject response = new JSONObject();
    try {
      URL con_url = new URL(url);
      this.connection = (HttpURLConnection) con_url.openConnection();
      connection.setRequestMethod(method);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setDoOutput(true);

      DataOutputStream out = new DataOutputStream(connection.getOutputStream());
      out.writeBytes(obj.toJSONString());
      out.flush();
      out.close();

      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String content = in.readLine();
      in.close();

      JSONParser parser = new JSONParser();
      Object parsed = parser.parse(content);
      response = (JSONObject) parsed;

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (connection != null){
        connection.disconnect();
      }
    }
    return response;
  }

}