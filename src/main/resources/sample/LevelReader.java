package fr.ubx.poo.ubomb.resources.sample;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LevelReader {

    public EntityCode[][] readfile(String filePath, String arg){
        BufferedReader br = null;
        FileReader fr = null;
        BufferedReader br1 = null;

        FileReader fr1 = null;
        try{
            fr = new FileReader(filePath + arg);
            br = new BufferedReader(fr);

            fr1 = new FileReader(filePath + arg);
            br1 = new BufferedReader(fr);
            int lines = 1;
            br1.readLine();
            while((br1.readLine()) != null){
                lines++;
            }
            fr1.close();

            // Reads the first line and puts everything in the String lineFile
            String lineFile = br.readLine();
            // Splits lineFile in several Strings using the space separator
            String[] splitLine = lineFile.split("");

            EntityCode[][] level = new EntityCode[splitLine.length][lines];

            return level;

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
}
