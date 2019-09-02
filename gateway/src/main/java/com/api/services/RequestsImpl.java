package com.api.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.api.services.Requests;

public class RequestsImpl implements Requests{

  private HttpURLConnection connection = null;
  /***
   * Handles HTTP connections to provided Host and with provided REST method
   * @param url
   * @param method
   * @return response from connection, int expected
   */
  public int request(String url, String method){
    this.connection = null;
    int response = -1;
    try {
      URL con_url = new URL(url);
      this.connection = (HttpURLConnection) con_url.openConnection();
      connection.setRequestMethod(method);
      connection.setRequestProperty("Content-Type", "application/json");  //TODO: update data sent to be json
      connection.setDoOutput(false);

      connection.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String content = in.readLine();
      in.close();
      response = Integer.parseInt(content);

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