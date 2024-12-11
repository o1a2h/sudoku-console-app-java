
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

  static ConfigManager default_config;
  static ConfigManager difficulty_config;
  static ConfigManager language;

  static String current_language = "en";
  static String current_difficulty = "easy";
  static String current_size = "small";

  static long start_play_time;
  static long end_play_time;

  static int min = 4;
  static int max = 4;

  static User user;
  static ConfigManager user_data;

  public static void main(String[] args) {

    default_config = new ConfigManager("config/default.cfg");
    difficulty_config = new ConfigManager("config/difficulty.cfg");

    current_size = default_config.get("size");
    current_difficulty = default_config.get("difficulty");
    max = difficulty_config.getInteger(current_difficulty+"-"+current_size+"-max");
    min = difficulty_config.getInteger(current_difficulty+"-"+current_size+"-min");

    selectLanguageAtStart();
    language = new ConfigManager("language/" + current_language + ".cfg");
    account();
  }

  public static void account() {

    log_in_username = "";
    log_in_password = "";
    log_in_password_hidden = "";

    sign_in_username = "";
    sign_in_public_username = "";
    sign_in_password = "";
    sign_in_password_hidden = "";
    sign_in_repeat_password = "";
    sign_in_repeat_password_hidden = "";

    Console.clear();

    String[] v = {
      language.get("login"),
      language.get("signin"),
    };

    String selected_tab = Console.ask(
      language.get("account"), v);

    if (new ArrayList<>(List.of("1", "l")).contains(selected_tab)) {
      login();
    } else if (new ArrayList<>(List.of("2", "s")).contains(selected_tab)) {
      signin();
    } else {
      account();
    }
  }

  static String log_in_username = "";
  static String log_in_password = "";
  static String log_in_password_hidden = "";

  public static void login() {
    Console.clear();

    String[] v = {
      language.get("usernamelogin") + ": " + log_in_username,
      language.get("passwordlogin") + ": " + log_in_password_hidden,
      language.get("continue"),
      language.get("back"),
    };

    String selected_tab = Console.ask(
      language.get("account"), v);

    if (new ArrayList<>(List.of("1", "u")).contains(selected_tab)) {
      Console.clear();

      String[] q = {
        language.get("account"),
        language.get("enterusername")
      };
  
      log_in_username = Console.ask(q);

      login();

    } else if (new ArrayList<>(List.of("2", "p")).contains(selected_tab)) {
      Console.clear();

      String[] q = {
        language.get("account"),
        language.get("enterpassword")
      };
  
      log_in_password = Console.ask(q);
      log_in_password_hidden = "";
      for (int i = 0; i < log_in_password.length(); i++) {
        log_in_password_hidden += "*";
      }

      login();

    } else if (new ArrayList<>(List.of("3", "c")).contains(selected_tab)) {
      user = new User(log_in_username, "Unknown", log_in_password);
      ConfigManager user_data = new ConfigManager("userdata/" + Cypher.SHA256(user.username + "username") + ".cfg");
      String stored_password = user_data.get(Cypher.SHA256(user.username + "password"));
      if (stored_password != null && stored_password.equals(user.password_hashed)) {
        user.public_username = user_data.get("publicusername");
        String score_hashed = user_data.get(Cypher.SHA256(user.public_username + "score"));
        user.score = Integer.parseInt(Cypher.AESDecrypt(score_hashed, user.public_username + "score"));
        menu();
      } else {
        login();
      }
    } else if (new ArrayList<>(List.of("4", "b")).contains(selected_tab)) {
      account();
    } else {
      login();
    }
  }

  static String sign_in_username = "";
  static String sign_in_public_username = "";
  static String sign_in_password = "";
  static String sign_in_password_hidden = "";
  static String sign_in_repeat_password = "";
  static String sign_in_repeat_password_hidden = "";


  public static void signin() {

    Console.clear();

    String[] v = {
      language.get("username") + ": " + sign_in_username,
      language.get("publicusername") + ": " + sign_in_public_username,
      language.get("password") + ": " + sign_in_password_hidden,
      language.get("repeatpassword") + ": " + sign_in_repeat_password_hidden,
      language.get("continue"),
      language.get("back"),
    };

    String selected_tab = Console.ask(
      language.get("account"), v);

    if (new ArrayList<>(List.of("1", "u")).contains(selected_tab)) {
      Console.clear();

      String[] q = {
        language.get("account"),
        language.get("enterusername")
      };
  
      sign_in_username = Console.ask(q);

      signin();
      
    } else if (new ArrayList<>(List.of("2", "pu")).contains(selected_tab)) {
      Console.clear();

      String[] q = {
        language.get("account"),
        language.get("enterpublicusername")
      };
  
      sign_in_public_username = Console.ask(q);

      signin();
    } else if (new ArrayList<>(List.of("3", "p")).contains(selected_tab)) {
      Console.clear();

      String[] q = {
        language.get("account"),
        language.get("enterpassword")
      };
  
      sign_in_password = Console.ask(q);
      sign_in_password_hidden = "";
      for (int i = 0; i < sign_in_password.length(); i++) {
        sign_in_password_hidden += "*";
      }

      signin();
      
    } else if (new ArrayList<>(List.of("4", "rp")).contains(selected_tab)) {
      Console.clear();

      String[] q = {
        language.get("account"),
        language.get("passwordagain")
      };
  
      sign_in_repeat_password = Console.ask(q);
      sign_in_repeat_password_hidden = "";
      for (int i = 0; i < sign_in_repeat_password.length(); i++) {
        sign_in_repeat_password_hidden += "*";
      }

      signin();

    } else if (new ArrayList<>(List.of("5", "c")).contains(selected_tab)) {
      if (!sign_in_password_hidden.equals(sign_in_repeat_password_hidden)) {
        signin();
      } else {
        user = new User(sign_in_username, sign_in_public_username, sign_in_password);
        ConfigManager user_data = new ConfigManager("userdata/" + Cypher.SHA256(user.username + "username") + ".cfg");
        user_data.write("publicusername", user.public_username);
        user_data.write(Cypher.SHA256(user.username + "password"), user.password_hashed);
        user_data.write(Cypher.SHA256(user.public_username + "score"), Cypher.AESEncrypt("0", user.public_username + "score"));
        menu();
      }
    } else if (new ArrayList<>(List.of("6", "b")).contains(selected_tab)) {
      account();
    } else {
      signin();
    }
  }

  public static void menu() {
    
    Console.clear();
    
    String[] v = {
      language.get("start"),
      language.get("tutorial"),
      language.get("statistics"),
      language.get("leaderboard"),
      language.get("difficulty"),
      language.get("language"),
      language.get("logout"),
      language.get("exit")
    };

    String selected_tab = Console.ask(
      language.get("menu"), v);

    if (new ArrayList<>(List.of("1", "s")).contains(selected_tab)) {
      start();
    } else if (new ArrayList<>(List.of("2", "t")).contains(selected_tab)) {
      tutorial();
    } else if (new ArrayList<>(List.of("3", "st")).contains(selected_tab)) {
      statistics();
    } else if (new ArrayList<>(List.of("4", "lb")).contains(selected_tab)) {
      leaderboard();
    } else if (new ArrayList<>(List.of("5", "d")).contains(selected_tab)) {
      difficulty();
    } else if (new ArrayList<>(List.of("6", "la")).contains(selected_tab)) {
      changeLanguage();
    } else if (new ArrayList<>(List.of("7", "lo")).contains(selected_tab)) {
      user = null;
      account();
    } else if (new ArrayList<>(List.of("8", "e")).contains(selected_tab)) {
      Console.clear();
    } else {
      menu();
    }
  }

  public static void start() {
    Console.clear();

    if (current_size.equals("small")) {
      String[] q = {
        language.get("doesnotsupport"),
        language.get("entertocontinue")
      };
      Console.ask(q);
      menu();
    } else if (current_size.equals("big")) {
      
      Sudoku sudoku = new Sudoku();

      int[][] right_matrix = new int[sudoku.matrix.length][sudoku.matrix[0].length];
      for (int i = 0; i < sudoku.matrix.length; i++) {
        System.arraycopy(sudoku.matrix[i], 0, right_matrix[i], 0, sudoku.matrix[i].length);
      }
      int diffrence = max - min;
      int number_of_gaps = (int)(Math.random()*diffrence) + min;
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          for (int k = 0; k < number_of_gaps; k++) {
            int random_x = (int)(Math.random()*3);
            int random_y = (int)(Math.random()*3);
            while(sudoku.matrix[i*3+random_x][j*3+random_y] == 0) {
              random_x = (int)(Math.random()*3);
              random_y = (int)(Math.random()*3);
            }
            sudoku.matrix[i*3+random_x][j*3+random_y] = 0;
          }
        }
      }

      start_play_time = System.currentTimeMillis();

      play(sudoku, right_matrix);
    }
  }

  public static void play(Sudoku sudoku, int[][] right_matrix) {
    System.out.println(" << " + language.get("titlestart"));
    sudoku.printGrid();
    
    String[] q = {
      language.get("enterijnplay"),
      language.get("exitplay")
    };

    String[] input_line = Console.ask(q).split(" ");

    if (input_line.length == 1) {
      if (input_line[0].equals("menu")) {
        menu();
      }
    } else if (input_line.length == 3) {
      int i = Integer.parseInt(input_line[0]);
      int j = Integer.parseInt(input_line[1]);
      int n = Integer.parseInt(input_line[2]);

      sudoku.matrix[j-1][i-1] = n;
      
      boolean is_right = true;

      for (int ii = 0; ii < sudoku.matrix.length; ii++) {
        for (int jj = 0; jj < sudoku.matrix[ii].length; jj++) {
          if (sudoku.matrix[ii][jj] != right_matrix[ii][jj]) {
            is_right = false;
            break;
          }
        }
      }

      if (is_right) {
        end_play_time = System.currentTimeMillis();
        long max_time = 810000000;
        double multiplier = 1 - (Math.pow(end_play_time - start_play_time, 1.5)/max_time);
        if (multiplier < 0.01) multiplier = 0.01;
        int win_score = (int)(difficulty_config.getInteger(current_difficulty+"-"+current_size+"-score") * multiplier);

        String[] qw = {
          language.get("youwin") + " " + win_score + " scores!",
          language.get("entertocontinue")
        };
    
        Console.ask(qw);
        
        user.score += win_score;

        user_data = new ConfigManager("userdata/" + Cypher.SHA256(user.username + "username") + ".cfg");
        user_data.write(Cypher.SHA256(user.public_username + "score"), Cypher.AESEncrypt("" + user.score, user.public_username + "score"));

        menu();
        return;
      }

      Console.clear();
      play(sudoku, right_matrix);
    }

    

    
  }

  public static int find(int[] array, int target) {
    for (int i = 0; i < array.length; i++) {
      if (array[i] == target) {
        return i;
      }
    }
    return -1;
  }

  public static void tutorial() {
    Console.clear();

    String[] v = {
      language.get("sixtutorial"),
      language.get("ninetutorial"),
      language.get("back")
    };

    String selected_tutorial = Console.ask(
      language.get("titletutorial"), v);

    if (new ArrayList<>(List.of("1", "s")).contains(selected_tutorial)) {
      Console.clear();
      String[] q = {
        language.get("titletutorial"),
        language.get("0sixtutorial"),
        language.get("1sixtutorial"),
        language.get("2sixtutorial"),
        language.get("3sixtutorial"),
        language.get("4sixtutorial"),
        language.get("5sixtutorial"),
        language.get("entertocontinue")
      };
      Console.ask(q);
    } else if (new ArrayList<>(List.of("2", "n")).contains(selected_tutorial)) {
      Console.clear();
      String[] q = {
        language.get("titletutorial"),
        language.get("0ninetutorial"),
        language.get("1ninetutorial"),
        language.get("2ninetutorial"),
        language.get("3ninetutorial"),
        language.get("4ninetutorial"),
        language.get("5ninetutorial"),
        language.get("entertocontinue")
      };
      Console.ask(q);
    } else if (new ArrayList<>(List.of("3", "b")).contains(selected_tutorial)) {
      menu();
      return;
    } else {
      tutorial();
    }

    Console.clear();
    String[] q = {
      language.get("titletutorial"),
      language.get("0tipstutorial"),
      language.get("1tipstutorial"),
      language.get("2tipstutorial"),
      language.get("3tipstutorial"),
      language.get("entertocontinue")
    };
    Console.ask(q);

    menu();
  }

  public static void statistics() {
    Console.clear();

    String[] q = {
      language.get("titlestatistics"),
      language.get("scorestatistics") + ": " + user.score,
    };

    String[] v = {
      language.get("back")
    };

    String selected_tab = Console.ask(
      q, v);

    if (new ArrayList<>(List.of("1", "b")).contains(selected_tab)) {
      menu();
    } else {
      statistics();
    }
  }

  public static void leaderboard() {
    Console.clear();

    Map<String, Integer> scores = new HashMap<>();

    String directory_path = "userdata/";
    File directory = new File(directory_path);

    File[] files = directory.listFiles((_, name) -> name.endsWith(".cfg"));

    if (files != null) {
      for (File file : files) {
        ConfigManager user_data_leaderboard = new ConfigManager("userdata/"+file.getName());
        String public_username = user_data_leaderboard.get("publicusername");
        String score_hashed = user_data_leaderboard.get(Cypher.SHA256(public_username + "score"));
        int score = Integer.parseInt(Cypher.AESDecrypt(score_hashed, public_username + "score"));
        // System.out.println(public_username + " " + score);
        scores.put(public_username, score);
      }
    }

    String[] q = {
      language.get("titleleaderboard")
    };

    int place = 1;

    while (!scores.isEmpty()) {
      int highest_value = Integer.MIN_VALUE;
      String highest_value_key = "";
      for (String key: scores.keySet()) {
        if (scores.get(key) > highest_value) {
          highest_value = scores.get(key);
          highest_value_key = key;
        }
      }

      String[] new_q = new String[q.length + 1];
      System.arraycopy(q, 0, new_q, 0, q.length);
      new_q[new_q.length - 1] = "#" + place + " - " + highest_value_key + " - " + scores.get(highest_value_key);
      place+=1;
      q = new_q;

      scores.remove(highest_value_key);
    }


    
    String[] v = {
      language.get("back")
    };

    String selected_tab = Console.ask(
      q, v);

    if (new ArrayList<>(List.of("1", "b")).contains(selected_tab)) {
      menu();
    } else {
      leaderboard();
    }
  }

  public static void difficulty() {
    Console.clear();

    String[] v = {
      language.get("sizedifficulty") + ": " + current_size,
      language.get("difficultydifficulty") + ": " + current_difficulty,
      language.get("back"),
    };

    String selected_tab = Console.ask(
      language.get("titledifficulty"), v);

    if (new ArrayList<>(List.of("1", "s")).contains(selected_tab)) {
      Console.clear();

      String[] vs = {
        language.get("smallsize"),
        language.get("bigsize")
      };
  
      String selected_size = Console.ask(language.get("selectsize"), vs);

      if (new ArrayList<>(List.of("1", "s")).contains(selected_size)) {
        current_size = "small";
      } else if (new ArrayList<>(List.of("2", "b")).contains(selected_size)) {
        current_size = "big";
      }

      default_config.write("size", current_size);

      difficulty();

    } else if (new ArrayList<>(List.of("2", "d")).contains(selected_tab)) {
      Console.clear();

      String[] vd = {
        language.get("easydifficulty"),
        language.get("normaldifficulty"),
        language.get("harddifficulty"),
        language.get("extremedifficulty")
      };
  
      String selected_difficulty = Console.ask(language.get("selectdifficulty"), vd);

      if (new ArrayList<>(List.of("1", "e")).contains(selected_difficulty)) {
        current_difficulty = "easy";
      } else if (new ArrayList<>(List.of("2", "n")).contains(selected_difficulty)) {
        current_difficulty = "normal";
      } else if (new ArrayList<>(List.of("3", "h")).contains(selected_difficulty)) {
        current_difficulty = "hard";
      } else if (new ArrayList<>(List.of("4", "ex")).contains(selected_difficulty)) {
        current_difficulty = "extreme";
      }

      max = difficulty_config.getInteger(current_difficulty+"-"+current_size+"-max");
      min = difficulty_config.getInteger(current_difficulty+"-"+current_size+"-min");

      default_config.write("difficulty", current_difficulty);

      difficulty();

    } else if (new ArrayList<>(List.of("3", "b")).contains(selected_tab)) {
      menu();
    } else {
      difficulty();
    }
  }


  public static void selectLanguageAtStart() {
    
    Console.clear();
    
    String default_language_code = default_config.get("defaultlanguage");
    ConfigManager default_language = new ConfigManager("language/" + default_language_code + ".cfg"); // default language config

    String[] v = {
      default_language.get("enlanguage"),
      default_language.get("kzlanguage")
    };

    String selected_language = Console.ask(
      default_language.get("selectlanguage"), v);

    if (new ArrayList<>(List.of("1", "en")).contains(selected_language)) {
      current_language = "en";
    } else if (new ArrayList<>(List.of("2", "kz")).contains(selected_language)) {
      current_language = "kz";
    } else {
      selectLanguageAtStart();
    }

    default_config.write("defaultlanguage", current_language);
  }  
  
  public static void changeLanguage() {
    
    Console.clear();

    String[] v = {
      language.get("enlanguage"),
      language.get("kzlanguage"),
      language.get("back")
    };

    String selected_language = Console.ask(
      language.get("selectlanguage"), v);

    if (new ArrayList<>(List.of("1", "en")).contains(selected_language)) {
      current_language = "en";
    } else if (new ArrayList<>(List.of("2", "kz")).contains(selected_language)) {
      current_language = "kz";
    } else if (new ArrayList<>(List.of("3", "e")).contains(selected_language)) {
      menu();
      return;
    } else {
      changeLanguage();
    }

    default_config.write("defaultlanguage", current_language);

    menu();
  }
}