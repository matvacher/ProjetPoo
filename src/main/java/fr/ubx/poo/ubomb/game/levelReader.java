package fr.ubx.poo.ubomb.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import fr.ubx.poo.ubomb.ressources.sample.*;

public class levelReader {

    public EntityCode[][] readfile(String filePath, String arg){
        BufferedReader br = null;
        FileReader fr = null;
        try{
            fr = new FileReader(filepath + arg);
            br = new BufferedReader(fr);

            fr1 = new FileReader(filepath + arg);
            br1 = new BufferedReader(fr);
            int lines = 1;
            br1.readLine();
            while((br1.readLine()) != null){
                lines++;
            }
            fr1.close

            // Reads the first line and puts everything in the String lineFile
            String lineFile = br.readLine();
            // Splits lineFile in several Strings using the space separator
            String[] splitLine = lineFile.split("");

            EntityCode[][] level = new EntityCode[splitLine.length][lines];

            return level;

        } catch (Exception e) {
        			e.printStackTrace();
        			throw new IllegalStateException(
        					"Exception during the read of the file " + absolutePathToFolder + " at line " + lineNumber);
        		} finally {
        			if (br != null) {
        				try {
        					br.close();
        				} catch (IOException e) {
        					e.printStackTrace();
        				}
        			}
    }
}
