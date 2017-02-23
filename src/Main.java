import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;

public class Main {

    public static ArrayList<Video> videos = new ArrayList<>();
    public static ArrayList<Video> endpoints  = new ArrayList<>();
    public static ArrayList<CacheServer> cacheServers  = new ArrayList<>();

  public static void main(String[] args) throws Exception {
      String location = "";
      List<String> data = Files.readAllLines(Paths.get(location));
      String[] firstLine = data.get(0).split(" ");
      int noOfVideos = Integer.parseInt(firstLine[0]);
      int noOfEndPoints = Integer.parseInt(firstLine[1]);
      int noOfRequestDesc = Integer.parseInt(firstLine[2]);
      int noOfCache = Integer.parseInt(firstLine[3]);
      int cacheSize = Integer.parseInt(firstLine[4]);

      for(int i = 0; i < noOfCache ;i++) {
          cacheServers.add(new CacheServer(cacheSize));
      }

      String[] secondLine = data.get(1).split(" ");
      for(int i = 0 ; i < noOfVideos; i++) {
          int size = Integer.parseInt(secondLine[i]);
          Video v = new Video(size);
          videos.add(v);
      }

      int offset = 0;
      for(int i = 0 ; i < noOfEndPoints; i++) {
          String[] ep = data.get(2+ i + offset).split(" ");
          int datacenterLatency =  Integer.parseInt(ep[0]);
          int caches =  Integer.parseInt(ep[1]);
          HashMap<Integer, Integer> latencies = new HashMap<>();

          for(int i2 = 0 ;i2 < caches; i2++) {
              String[] laten = data.get(2 + i + offset + i2 + 1).split(" ");
              latencies.put(Integer.parseInt(laten[0]), Integer.parseInt(laten[1]));
          }
          offset = caches;
          Endpoint endpoint = new Endpoint(latencies);
          endpoints.add(endpoint);
      }



  }

}
