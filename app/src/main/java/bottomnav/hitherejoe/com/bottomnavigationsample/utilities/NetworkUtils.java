package bottomnav.hitherejoe.com.bottomnavigationsample.utilities;

/**
 * Created by Allets on 9/10/2017.
 */

import android.content.ContentResolver;
import android.net.Uri;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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

    public static String imageUploadPost(String urlstring, String tokenValue, Uri imageUri, ContentResolver cr, String imageFileName) throws IOException{

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        String imageContentType = imageFileName.substring(imageFileName.lastIndexOf(".") + 1);
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 5000 * 5000;

        URL url = new URL(urlstring);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Allow Inputs &amp; Outputs.
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        // Set HTTP method to POST.
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Token " + tokenValue);
//        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Postman-Token", "4c55b432-fb89-0c29-214a-87e8a756bd91");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        InputStream inputStream;
        DataOutputStream outputStream;
        outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(twoHyphens + boundary + lineEnd);

        outputStream.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"" + imageFileName +"\"" + lineEnd);

        outputStream.writeBytes("Content-Type: image/" + imageContentType  + lineEnd + lineEnd);

        inputStream = cr.openInputStream(imageUri);
        bytesAvailable = inputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        // Read file
        bytesRead = inputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            outputStream.write(buffer, 0, bufferSize);
            bytesAvailable = inputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = inputStream.read(buffer, 0, bufferSize);
        }

        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

        // Responses from the server (code and message)
        int status = connection.getResponseCode();
        System.out.println("HTTP Client" + "HTTP status code : " + status);
        System.out.println(connection.getResponseMessage());

        try {
            InputStream in = connection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
            return "A problem was encountered";
        }
        finally {
            connection.disconnect();
        }

    }

}