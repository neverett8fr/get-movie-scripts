import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.*;

public class main {

    private static List<String> getWebpage(String urlInput){

        try{
            URL url = new URL(urlInput);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            List<String> output = new ArrayList<String>();

            InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
            BufferedReader buff = new BufferedReader(in);

            String line = buff.readLine(); // e.g. <html>, etc.
            // then read the line for the tags e.g. <html><div>table</div> = html, div, table, /div


            Boolean tagOpen = false;
            String smallerPart = "";
            while (line != null){

                for (int i = 0; i < line.length(); i++){

                    if (line.charAt(i) == '<') {
                        tagOpen = true;

                        int currentPos = i;
                        while (tagOpen){
                            if (line.charAt(currentPos) == '>'){
                                tagOpen = false;
                            }
                            smallerPart += line.charAt(currentPos);
                            currentPos += 1;
                        }
                        i = currentPos;
                        output.add(smallerPart);
                        smallerPart = "";

                    }
                    else {
                        smallerPart += line.charAt(i);
                        try {
                            if (line.charAt(i + 1) == '<'){
                                output.add(smallerPart);
                                smallerPart = "";
                            }
                        }
                        catch (Exception e){}

                    }

                }

                line = buff.readLine();

            }

            return output;

        }
        catch (Exception e){
            return null;

        }

    }

    private static List<String> getAllMoviesURL(List<String> movies){

        List<String> output = new ArrayList<String>();

        for (int i = 0; i < movies.size(); i++){
            String line = movies.get(i);
            if (line.contains("<a href=\"/Movie Scripts/")) {
                String urlLine = "";
                outerloop:
                for (int j = 0; j < line.length(); j++){
                    if (line.charAt(j) == '"'){
                        for (int k = j + 1; k < line.length(); k++){
                            if (line.charAt(k) == '"') break outerloop;
                            urlLine += line.charAt(k);

                        }

                    }
                }
                output.add(urlLine);
            }

        }

        return output;
    }

    public static void main(String args[]){
        String urlInput = "https://imsdb.com/all-scripts.html";

        System.out.println(getAllMoviesURL(getWebpage(urlInput)));

    }

}
