import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Main {
    public static void main(String[] args) {
        String inputFileName = args[0];
        String outputFileNamePrefix = args[1];
        int num = Integer.parseInt(args[2]);
        String outputFileExtension = ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            List<String> currentVolume = new ArrayList<>();
            int currentVolumeNumber = -1;
            int fileCount = 1;
            int volumeCount = 0;

            Pattern volumePattern = Pattern.compile("(\\d+) Том\\. Глава \\d+\\..*");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = volumePattern.matcher(line);
                if (matcher.matches()) {
                    int newVolumeNumber = Integer.parseInt(matcher.group(1));
                    if (newVolumeNumber != currentVolumeNumber) {
                        currentVolumeNumber = newVolumeNumber;
                        volumeCount++;

                        // If we have two volumes, write to file and start a new one
                        if (volumeCount == num) {
                            writeToFile(outputFileNamePrefix + fileCount + outputFileExtension, currentVolume);
                            currentVolume.clear();
                            volumeCount = 0;
                            fileCount++;
                        }
                    }
                }

                currentVolume.add(line);
            }

            // Write any remaining content to the last file
            if (!currentVolume.isEmpty()) {
                writeToFile(outputFileNamePrefix + fileCount + outputFileExtension, currentVolume);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(String fileName, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
