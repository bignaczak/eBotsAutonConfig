package com.example.ebotsautonconfigapp;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class eBotsConfigIO {

    private static final boolean debugOn = true;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    private static ArrayList<String> configInstance = new ArrayList<>();

    public static String writeConfigFile(String configString){
        //Get the configuration file

        String logTag = "BTI_writeConfigFile";
        if(debugOn) Log.d(logTag, "Entering BTI_writeConfigFile");

        File file = initializeConfigFileIO();
        //writeFileHeader(file, generateFileHeader());
        try{
            createNewFile(generateFileHeader(), file);
            if(debugOn) Log.d (logTag, "Created new file");

        } catch (IOException e){
            if(debugOn) Log.d (logTag, "Error creating file");
            if(debugOn) Log.d(logTag, e.getMessage());
        }

        //Write the config parameters
        try{
            writeToFile(configString, file);
            if(debugOn) Log.d (logTag, "Successfully wrote config : " + configString);

        } catch (IOException e){
            if(debugOn) Log.d (logTag, "Error writing configuration: " + configString);
            if(debugOn) Log.d(logTag, e.getMessage());
        }

        String returnString;
        if(file != null){
            returnString = file.getName();
        } else {
            returnString = "No file written";
        }
        return returnString;
    }

    private static void generateConfigOptions(){
        String logTag = "BTI_generateConfigOps";
        if(debugOn) Log.d(logTag, "Entering generate Config Options");
        if (configInstance.size() > 0) configInstance.clear();

        configInstance.add("FAST");
        configInstance.add("BLUE");
        configInstance.add("QUARRY");
        configInstance.add("EVERY_LOOP");

        if(debugOn) Log.d(logTag, "Number of Config items = " + configInstance.size());

    }

    private static File initializeConfigFileIO() {
        //This check verifies that storage permissions are granted
        String logTag = "BTI_initConfigFileIO";
        if(debugOn) Log.d(logTag, "Entering initializeConfigFileIO...");

        File newFile = null;
        //Verify that storage is writable
        if (isExternalStorageWritable()) {
            if(debugOn) Log.d(logTag, "Yes, it is writable");

            //Generate a filename using timestamp
            //Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
            //Log.d(logTag, sdf.format(timeStamp));
            //String fileName = sdf.format(timeStamp);

            String fileName = "eBotsAutonConfig.txt";
            newFile = getPublicFileStorageDir(fileName);
        } else {
            if(debugOn) Log.d(logTag, "No, not writable.  Returning Null");
        }


        //Note:  this may return null
        if(debugOn) {
            if (newFile == null) {
                Log.d(logTag, "File is null!!!");
            } else {
                Log.d(logTag, "filename is " + newFile.getName());
            }
        }
        return newFile;
    }

    private static String generateFileHeader(){
        //Generate file header text using timestamp
        String logTag = "BTI_generateFileHeader";

        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        if(debugOn) Log.d(logTag, sdf.format(timeStamp));
        return "Auton Configuration -- " + sdf.format(timeStamp);

    }
    private static void writeFileHeader(File newFile, String headerText){
        String logTag = "BTI_writeFileHeader";
        try{
            createNewFile(headerText, newFile);

        } catch (IOException e){
            if(debugOn) Log.d (logTag, "Error writing driveStep");
            if(debugOn) Log.d(logTag, e.getMessage());
        }
    }
    private static void writeToFile( String textLine, File file ) throws IOException {
        FileWriter write = new FileWriter( file.getAbsolutePath(), true);
        PrintWriter printLine = new PrintWriter( write );
        printLine.printf("%s" + "%n" , textLine);
        printLine.close();
    }

    private static void createNewFile( String textLine, File file ) throws IOException {
        FileWriter write = new FileWriter( file.getAbsolutePath(), false);
        PrintWriter printLine = new PrintWriter( write );
        printLine.printf("%s" + "%n" , textLine);
        printLine.close();
    }



    /*
    public void writeDriveStepClicksToFile(File newFile, ArrayList<String> driveSteps,
                                           boolean skipMotorInit){

        try{
            DriveStep driveStep = new DriveStep(skipMotorInit);
            writeToFile(driveStep.toString(), newFile);
            driveSteps.add(driveStep.toString());

        } catch (IOException e){
            if(debugOn) Log.d ("Helper", "Error writing driveStep");
            if(debugOn) Log.d("Helper", e.getMessage());
        }
    }

     */


    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private static File getPublicFileStorageDir(String fileName) {
        // Get the directory for the user's public pictures directory.
        String logTag = "BTI_getPublic...Dir";
        if(debugOn) Log.d(logTag, "Entering getPublicFileStorageDir");
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS),"");
        if(debugOn) Log.d(logTag, directory.getPath());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!directory.mkdirs()) {
            if(debugOn) Log.d(logTag, "Directory not created");
        }

        File writeFile = new File(directory.getAbsolutePath() + "/" + fileName);
        return writeFile;
    }



    private static String readFileContents(String fileName) {
        String readLine="";
        try {
            File myDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS),"");
            BufferedReader br = new BufferedReader(new FileReader(myDir + "/"+fileName));
            readLine = br.readLine();

            // Set TextView text here using tv.setText(s);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readLine;
    }

    private static void getDriveStepInstructions(String fileName, ArrayList<String> driveStepInstructions) {
        try {

            File myDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS),"");
            BufferedReader br = new BufferedReader(new FileReader(myDir + "/"+fileName));
            String line = "";
            while ((line = br.readLine()) != null) {
                driveStepInstructions.add(line);
            }

            // Set TextView text here using tv.setText(s);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private static void parseMotorEncoderTargets(String inputLine, int[] motorEncoderTargets){
        String[] splitText = inputLine.split(",", 4);
        for(int i=0; i<splitText.length; i++){
            int stringLength = splitText[i].length();
            String firstCharacter = splitText[i].substring(0,1);
            String lastCharacter = splitText[i].substring(stringLength-1,stringLength);

            if(firstCharacter.equals("[")) splitText[i] = splitText[i].substring(1,stringLength);
            if (lastCharacter.equals("]")) splitText[i] = splitText[i].substring(0,stringLength-1);
            motorEncoderTargets[i] = Integer.parseInt(splitText[i].trim());
        }
    }

}
