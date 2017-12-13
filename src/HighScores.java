import java.io.*;
import java.util.HashMap;

public class HighScores {
    private static HashMap<String, Integer> scores;

    public static HashMap<String, Integer> load() {
        scores = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("highscores.txt")));

            while (reader.ready()) {
                String[] row = reader.readLine().trim().split(",");
                scores.put(row[0], Integer.parseInt(row[1]));
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            //TODO: dont do this
        }
        return scores;
    }

    public static void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("highscores.txt"), "UTF-8"));
            for (String key : scores.keySet()) {
                writer.write(key + "," + String.valueOf(scores.get(key) + "\n"));
            }
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            //TODO: again, don't do this
        }
    }

    public static void addScore(String name, int score) {
        if(name.equals("")) {
            throw new IllegalArgumentException("Empty name");
        }

        scores.put(name, score);
        save();
    }
}
