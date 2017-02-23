
import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static ArrayList<Video> videos = new ArrayList<>();
    public static ArrayList<Endpoint> endpoints  = new ArrayList<>();
    public static ArrayList<CacheServer> cacheServers  = new ArrayList<>();

  public static void main(String[] args) throws Exception {
      //String location = "/data/me_at_the_zoo.in";
      //String location = "/data/sample.in";
      String location = "/data/videos_worth_spreading.in";
      //String location = "/data/kittens.in";
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
          cacheServers.add(new CacheServer(cacheSize, i));
      }

      String[] secondLine = data.get(1).split(" ");
      for(int i = 0 ; i < noOfVideos; i++) {
          int size = Integer.parseInt(secondLine[i]);
          Video v = new Video(size, i);
          videos.add(v);
      }

      int offset = 0;
      int lastOffset = 0;
      for(int i = 0 ; i < noOfEndPoints; i++) {

          String[] ep = data.get(2+ i + offset).split(" ");
          System.out.println(i+"/"+noOfEndPoints+"  endpoint: "+ String.join(",",ep));
          int datacenterLatency =  Integer.parseInt(ep[0]);
          int caches =  Integer.parseInt(ep[1]);
          Map<Integer, Integer> latencies = new HashMap<>();
          lastOffset = 2 + i + offset;
          Endpoint endpoint = new Endpoint(datacenterLatency);
          for(int i2 = 0 ;i2 < caches; i2++) {
              String[] laten = data.get(2 + i + offset + i2 + 1).split(" ");
              System.out.println("latencies: "+ String.join(",",laten));
              latencies.put(Integer.parseInt(laten[0]), Integer.parseInt(laten[1]));
              lastOffset = 2 + i + offset + i2 + 1;
              cacheServers.get(Integer.parseInt(laten[0])).endpointToLatency.put(endpoint,Integer.parseInt(laten[1]));
          }

          offset += caches;
          endpoint.servers = latencies;

          endpoints.add(endpoint);
      }

      for(int i = 0 ; i < noOfRequestDesc; i++) {
          String[] ep = data.get(1 + i + lastOffset).split(" ");
          System.out.println("req: "+ String.join(",",ep));
          int vid =  Integer.parseInt(ep[0]);
          int end =  Integer.parseInt(ep[1]);
          int latency =  Integer.parseInt(ep[2]);
          endpoints.get(end).addRequest(videos.get(vid),latency);
      }

      setCacheVideoValues();

        for(CacheServer c: cacheServers) {
            System.out.println( Arrays.toString(c.videoValues.entrySet().toArray()));
        }

  }

    public static void setCacheVideoValues() {
        int processed = 0;
        int numCacheServers = cacheServers.size();
        for (CacheServer cacheServer : cacheServers) {
            for (Video video : videos) {
                //System.out.println(video.size + " @ "+cacheServer.maxCapacity);
                if (video.size > cacheServer.maxCapacity) {
                    break;
                }
                    int value = 0;
                    for (Endpoint endpoint : endpoints) {
                        if (cacheServer.endpointToLatency.containsKey(endpoint) && endpoint.requests.get(video) != null) {
                            value += endpoint.requests.get(video) * (endpoint.latency
                                    - cacheServer.endpointToLatency.get(endpoint));
                        }
                    }
                    if (value > 0) {
                        //System.out.println("size"+video.size+" added"+value);
                        cacheServer.videoValues.put(video, value);
                    }

            }
            processed += 1;
            System.out.println("Processed " + processed + " of " + numCacheServers + " cache servers.");
        }
    }

    public String getOutputString() {
      String outputString = "";
      int serverNo = 0;
      for(CacheServer server: cacheServers) {
          if (server.videos.size() != 0) {
              serverNo++;
              outputString += "/n";
              outputString += server.id;
              for(Video video : server.videos ) {
                  outputString += " ";
                  outputString += video.id;
              }


          }
      }
      outputString = serverNo + outputString;
      return outputString;
    }

}
