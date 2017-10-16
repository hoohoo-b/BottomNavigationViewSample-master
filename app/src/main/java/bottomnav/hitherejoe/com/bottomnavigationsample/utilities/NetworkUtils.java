package bottomnav.hitherejoe.com.bottomnavigationsample.utilities;

/**
 * Created by Allets on 9/10/2017.
 */

import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    public static String getResponseFromHttpUrl(String urlstring, String requestMethod, String tokenValue) throws IOException {

        URL url = new URL(urlstring);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(requestMethod);

        if (tokenValue != ""){
            urlConnection.setRequestProperty("Authorization", "Token " + tokenValue);
        }

        urlConnection.connect();
        int status = urlConnection.getResponseCode();
        System.out.println("HTTP Client" + "HTTP status code : " + status);

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
            return "A problem was encountered";
        }
        finally {
            urlConnection.disconnect();
        }
    }

    public static String getResponseFromHttpUrl(String urlstring, String requestMethod, String json, String tokenValue) throws IOException {

        URL url = new URL(urlstring);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(requestMethod);
        urlConnection.setRequestProperty("Content-Type", "application/json");

        if (tokenValue != ""){
            urlConnection.setRequestProperty("Authorization", "Token " + tokenValue);
        }

        OutputStream outputStream = urlConnection.getOutputStream();
        outputStream.write(json.getBytes("UTF-8"));
        outputStream.close();

        urlConnection.connect();
        int status = urlConnection.getResponseCode();
        System.out.println("HTTP Client" + "HTTP status code : " + status);

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
            return "A problem was encountered";
        }
        finally {
            urlConnection.disconnect();
        }
    }

    public static String getAuthToken(String authJson) throws IOException, LoginException {

        URL url = new URL("https://hidden-springs-80932.herokuapp.com/api/v1.0/login2");

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");

        OutputStream outputStream = urlConnection.getOutputStream();
        outputStream.write(authJson.getBytes("UTF-8"));
        outputStream.close();

        urlConnection.connect();
        int status = urlConnection.getResponseCode();
        System.out.println("HTTP Client" + "HTTP status code : " + status);
        if(status != 200){
            throw new LoginException("Invalid login");
        }

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
            return "A problem was encountered";
        }
        finally {
            urlConnection.disconnect();
        }
    }

}