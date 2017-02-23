import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
  public static void main(String[] args) throws Exception {
      String location = "";
      List<String> data = Files.readAllLines(Paths.get(location));
      String[] firstLine = data.get(0).split(" ");
      int noOfVideos = Integer.parseInt(firstLine[0]);
      int noOfEndPoints = Integer.parseInt(firstLine[1]);
      int noOfRequestDesc = Integer.parseInt(firstLine[2]);
      int noOfCache = Integer.parseInt(firstLine[3]);
      int cacheSize = Integer.parseInt(firstLine[4]);


  }

}
