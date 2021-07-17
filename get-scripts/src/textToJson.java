import java.io.File;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class textToJson {

    private static String[] returnFiles(String filepath){

        String[] pathNames;
        File f = new File(filepath);
        pathNames = f.list();

        return pathNames;

    }

    private static String returnTXTJson(String directory, String[] fileNames){

        String script = "{";
        for (int i = 0; i < fileNames.length; i++){
        //for (int i = 0; i < 20; i++){
            System.out.println(fileNames[i]);

            script += "\"movie" + fileNames[i].replaceAll(".txt","").replace('.','m').replaceAll(" ","").replaceAll("-", "") + "\":[";
            try {
                File myObj = new File(directory + fileNames[i]);
                Scanner myReader = new Scanner(myObj);

                String lineOfTxt = "";
                while (myReader.hasNextLine()) {
                    //script += "\"";
                    lineOfTxt += myReader.nextLine().replaceAll("\"", "").replaceAll("'","").strip();
                    //script += "\",";
                }
                //System.out.println(lineOfTxtSplit.length);
                // have to change to include sentences or the searching thing wont really work - also included commas, but might change
                myReader.close();

                String[] lineOfTxtSplit = lineOfTxt.split("[.|!|?|,]");
                for (int j = 0; j < lineOfTxtSplit.length; j++){
                    script += "\"" + lineOfTxtSplit[j] + "\"";
                    if (j < lineOfTxtSplit.length - 1){
                        script += ",";
                    }

                }


            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            script += "]";
            if (i < fileNames.length - 1){
                script += ",";

            }

        }
        script += "}";

        script = script.replaceAll(",]", "]");

        return script;

    }


    public static void main(String args[]){
        String[] fileNames = returnFiles("scripts");
        String json = returnTXTJson("scripts/", fileNames);

        try{
        FileWriter file = new FileWriter("moviesJson.json");
        file.write(json);
        file.close();
        }
        catch (Exception e){

        }

        System.out.println("JSON written");

    }
}
