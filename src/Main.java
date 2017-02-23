import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
  public static void main(String[] args) throws Exception {
    String location = "";
    List<String> data = Files.readAllLines(Paths.get(location));
    String current;
    for(int i = 0 ; i < data.size(); i++) {
      current = data.get(i);
      System.out.println(current);
    }


  }
}
