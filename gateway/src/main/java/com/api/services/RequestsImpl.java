package com.api.services;

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
      this.connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(method);
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setDoOutput(false);

      connection.connect();
      // Check and get content apropriatelly
      response = connection.getContent();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
      return response;
    }
  }

}