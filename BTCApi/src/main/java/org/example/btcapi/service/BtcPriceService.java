package org.example.btcapi.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class BtcPriceService {

    private static final String URL = "https://api.coindesk.com/v1/bpi/currentprice.json";


    public String getDataFromUrl() {
        try {
            URI uri = new URI(URL);

            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                return parseJsonInString(content.toString());
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String parseJsonInString(String json) {
        JSONObject obj = new JSONObject(json);
        JSONObject bpi = obj.getJSONObject("bpi");
        JSONObject usd = bpi.getJSONObject("USD");
        return usd.getString("rate");
    }
}
