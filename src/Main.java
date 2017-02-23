import java.awt.datatransfer.StringSelection;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static ArrayList<Video> videos = new ArrayList<>();
    public static ArrayList<Endpoint> endpoints  = new ArrayList<>();
    public static ArrayList<CacheServer> cacheServers  = new ArrayList<>();

  public static void main(String[] args) throws Exception {
      String location = "/data/sample.in";
      String pre = Paths.get(".").toAbsolutePath().normalize().toString();
      List<String> data = Files.readAllLines(Paths.get(pre+location));
      for(int i = 0; i < data.size(); i++) {
          //System.out.println(data.get(i));
      }
      String[] firstLine = data.get(0).split(" ");
      int noOfVideos = Integer.parseInt(firstLine[0]);
      int noOfEndPoints = Integer.parseInt(firstLine[1]);
      int noOfRequestDesc = Integer.parseInt(firstLine[2]);
      int noOfCache = Integer.parseInt(firstLine[3]);
      int cacheSize = Integer.parseInt(firstLine[4]);

      System.out.println(noOfVideos+","+noOfEndPoints+","+noOfRequestDesc
      +","+noOfCache+","+cacheSize);
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
      int lastOffset = 0;
      for(int i = 0 ; i < noOfEndPoints; i++) {

          String[] ep = data.get(2+ i + offset).split(" ");
          System.out.println("endpoint: "+ String.join(",",ep));
          int datacenterLatency =  Integer.parseInt(ep[0]);
          int caches =  Integer.parseInt(ep[1]);
          Map<Integer, Integer> latencies = new HashMap<>();

          for(int i2 = 0 ;i2 < caches; i2++) {

              String[] laten = data.get(2 + i + offset + i2 + 1).split(" ");
              System.out.println("latencies: "+ String.join(",",laten));
              latencies.put(Integer.parseInt(laten[0]), Integer.parseInt(laten[1]));
              lastOffset = 2 + i + offset + i2 + 1;
          }
          offset = caches;
          Endpoint endpoint = new Endpoint(latencies, datacenterLatency);
          endpoints.add(endpoint);
      }

      for(int i = 0 ; i < noOfRequestDesc; i++) {
          String[] ep = data.get(2 + i + lastOffset).split(" ");
          System.out.println("req: "+ String.join(",",ep));
          int vid =  Integer.parseInt(ep[0]);
          int end =  Integer.parseInt(ep[1]);
          int latency =  Integer.parseInt(ep[2]);
          endpoints.get(end).addRequest(videos.get(vid),latency);
      }


  }

}
