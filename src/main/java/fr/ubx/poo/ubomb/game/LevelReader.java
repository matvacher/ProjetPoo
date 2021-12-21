package fr.ubx.poo.ubomb.game;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static fr.ubx.poo.ubomb.game.EntityCode.*;
import fr.ubx.poo.ubomb.game.GridRepoSample;

public class LevelReader {

    /*public EntityCode[][] readfile(String filePath, String arg){
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

            String[][] level = new String[lines][splitLine.length];

            for(int i = 0 ; i < lines ; i++){
                for(int j = 0 ; j < splitLine.length ; j++){
                    level[i][j] = splitLine[j];
                }
                br.readLine();
            }

            EntityCode[][] tab = new EntityCode[lines][splitLine.length];
            tab.load(1, level);

            return level;

        } catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException();

        } finally {
        	if (br != null) {
        		try {
        			br.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }
    }*/
}
