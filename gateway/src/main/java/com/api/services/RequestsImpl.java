package main.java.com.api.requests;

import java.net.HttpURLConnection;
import java.net.URL;

public class RequestsImpl extends Requests{

  private HttpURLConnection connection = null;

  public int request(String url, String method){
    this.connection = null;
    int response = -1;
    try {
      this.connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(method);
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setDoOutput(false);

      connection.connect();

      response = connection.getResponseMessage();
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