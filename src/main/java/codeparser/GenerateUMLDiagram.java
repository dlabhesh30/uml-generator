package codeparser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GenerateUMLDiagram {

    public static Boolean generateImage(String grammar, String outputPath) {
    	
        try {
            String api = "https://yuml.me/diagram/class/" + grammar
                    + ".png";
            URL url = new URL(api);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException(
                        "Failure : HTTP error code : " + connection.getResponseCode());
            }
            OutputStream outputStream = new FileOutputStream(new File(outputPath));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = connection.getInputStream().read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
