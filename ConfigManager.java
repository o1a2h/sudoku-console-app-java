import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class ConfigManager {
  
  String file_path;
  Map<String, String> data;

  
  
  public ConfigManager() {
  
  }

  public ConfigManager(String file_path) {
    this.file_path = file_path;
    this.data = new HashMap<>();
    read();
  }


  public static void createConfigFile(String file_name) {
      try {
        FileWriter file_writer = new FileWriter(file_name);
        System.out.println("Configuration file created successfully!");
      } catch (IOException e) {
        System.out.println(e);
      }
  }

  public void read() {
    try (BufferedReader buffer_reader = new BufferedReader(new FileReader(file_path))) {
      String line;
      while ((line = buffer_reader.readLine()) != null) {
        String[] line_splited = line.split("=");
        if (line_splited.length == 2) {
          String key = line_splited[0].strip();
          String value = line_splited[1].strip();
          data.put(key, value);
        }
      }
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public void save() {
    try (BufferedWriter buffer_writer = new BufferedWriter(new FileWriter(file_path))) {
      for (Map.Entry<String, String> entry: data.entrySet()) {
        buffer_writer.write(entry.getKey() + "=" + entry.getValue());
        buffer_writer.newLine();
      }
    } catch (IOException e) {
      System.out.println(e);
    }
  }


  public String get(String key) {
    return data.get(key);
  }

  public int getInteger(String key) {
    return Integer.parseInt(data.get(key));
  }

  public float getFloat(String key) {
    return Float.parseFloat(data.get(key));
  }

  public boolean getBoolean(String key) {
    return Boolean.parseBoolean(key);
  }


  public void write(String key, String value) {
    data.put(key, value);
    save();
  }

  public void write(String key, int value) {
    data.put(key, "" + value);
    save();
  }

  public void write(String key, float  value) {
    data.put(key, "" + value);
    save();
  }

  public void write(String key, boolean value) {
    data.put(key, "" + value);
    save();
  }

  

  
  

}
