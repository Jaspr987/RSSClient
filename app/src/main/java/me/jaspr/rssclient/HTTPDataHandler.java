package me.jaspr.rssclient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class HTTPDataHandler {
    private static String stream = null;

    HTTPDataHandler() {

    }

    String GetHTTPData(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                stream = sb.toString();
                urlConnection.disconnect();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream;
    }
}
