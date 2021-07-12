import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class main {

    private static List<String> getWebpage(String urlInput) throws IOException{

        try{
            URL url = new URL(urlInput);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            List<String> output = new ArrayList<String>();

            InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
            BufferedReader buff = new BufferedReader(in);

             // e.g. <html>, etc.
            // then read the line for the tags e.g. <html><div>table</div> = html, div, table, /div


            String line = "";

            String smallerPart = "";
            while (line != null){
                line = buff.readLine();

                output.add(line);

            }


            return output;

        }
        catch (Exception e){
            return null;

        }

    }

    private static List<String> getAllMoviesProfileURL(List<String> movies){

        List<Integer> indexes = new ArrayList<Integer>();
        int index = 0;
        while (index != -1){
            index = movies.get(196).indexOf("<a href=\"/Movie Scripts", index);
            if (index != -1) {
                indexes.add(index);
                index++;
            }
        }

        List<String> output = new ArrayList<String>();
        for (int i = 0; i < indexes.size(); i++){

            String link = "";
            for (int j = indexes.get(i); j < movies.get(196).length(); j++){
                link += movies.get(196).charAt(j);
                if(movies.get(196).charAt(j) == '>'){
                    output.add(link);
                    break;
                }

            }

        }

        for (int i = 0; i < output.size(); i++){
            String urlLine = "";
            outerloop:
            for (int j = 0; j < output.get(i).length(); j++){
                if (output.get(i).charAt(j) == '"'){
                    for (int k = j + 1; k < output.get(i).length(); k++){
                        if (output.get(i).charAt(k) == '"') break outerloop;
                        urlLine += output.get(i).charAt(k);

                    }

                }
            }
            output.set(i, urlLine.replaceAll(" ", "%20"));

        }


        return output;
    }

    private static List<String> getAllMoviesScriptURL(List<String> urls){

        List<String> output = new ArrayList<String>();


        for (int x = 0; x < urls.size(); x++){

            try {
                List<String> profilePage = getWebpage("https://imsdb.com" + urls.get(x));

            for (int i = 0; i < profilePage.size(); i++){

                String line = profilePage.get(i);
                if (line.contains("<a href=\"/scripts/")) {
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
                    output.add(urlLine.replaceAll(" ", "%20"));
                }

            }
            }
            catch (Exception e){}

        }


        return output;
    }

    private static String getScript(String URL) {
        String output = "";




            boolean readingScript = false;
            try{
                List<String> website = getWebpage(URL);
            for (int i = 0; i < website.size(); i++) {


                //if (website.get(i).contains("<td class=\"scrtext\">")) readingScript = true;
                if (website.get(i).equals("<pre>")) readingScript = true;
                else if (website.get(i).contains("</pre>")) readingScript = false;

                if (readingScript) {output += website.get(i).replaceAll("<b>|</b>|<pre>","") + "\n";}

            }
            }
            catch (Exception e){}
        //System.out.println(website);


        return output;

    }

    private static List<String> getAllScripts(List<String> scriptURLs){
        List<String> output = new ArrayList<String>();


        for (int i = 0; i < scriptURLs.size(); i++){
            if (!scriptURLs.get(i).equals("center")){

            output.add(getScript("https://imsdb.com" + scriptURLs.get(i)));
            System.out.println("https://imsdb.com" + scriptURLs.get(i));

            try{
            System.out.println("Written: " + writeToFile(getScript("https://imsdb.com" + scriptURLs.get(i)), scriptURLs.get(i)));
            }
            catch (Exception e){}
                System.out.println("Written: false");
            }

        }

        return output;
    }

    private static boolean writeToFile(String input, String urlTitle){

        try{
            String title = "";

            if (input.length() == 0){
                System.out.println("org: " + input);
                input = getScript("https://imsdb.com" + urlTitle);
                System.out.println("redo: " + input);
            }

            //System.out.println(input);
            System.out.println("Title: " + urlTitle.replaceAll("/scripts/", "").replaceAll(".html", ""));
            FileWriter file = new FileWriter(urlTitle.replaceAll("/scripts/", "").replaceAll(".html", "") + ".txt");
            file.write(input);
            file.close();

            return true;

        }
        catch (Exception e){
            System.out.println("An error occurred.");
            System.out.println(input);
            return false;
        }

    }

    public static void main(String args[]){
        String urlInput = "https://imsdb.com";

        List<String> webpageList = new ArrayList<String>();
        try{
            webpageList = getWebpage(urlInput + "/all-scripts.html");
        }catch (Exception e){}

        List<String> movieProfiles = getAllMoviesProfileURL(webpageList);

        List<String> scriptURL = getAllMoviesScriptURL(movieProfiles);
        System.out.println("Gotten script URLs");

        List<String> allScripts = getAllScripts(scriptURL);
        System.out.println("Gotten scripts");

        //for (int i = 0; i < allScripts.size(); i++) writeToFile(allScripts.get(i));
        System.out.println("Scripts written");

    }

}
