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

            script += "movie" + fileNames[i].replaceAll(".txt","").replace('.','m').replaceAll(" ","").replaceAll("-", "") + ":[";
            try {
                File myObj = new File(directory + fileNames[i]);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    script += "\"" + myReader.nextLine().replaceAll("\"", "").replaceAll("'","") + "\",";
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            script += "],";

        }
        script += "}";

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

        System.out.println(json);

    }
}
