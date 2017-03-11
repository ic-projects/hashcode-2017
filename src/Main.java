
import java.lang.reflect.Array;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

      //System.out.println(noOfVideos+","+noOfEndPoints+","+noOfRequestDesc
      //+","+noOfCache+","+cacheSize);
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
            //System.out.println( Arrays.toString(c.videoValues.entrySet().toArray()));
            int[] w = new int[c.videoValues.size()+1];
            int[] v = new int[c.videoValues.size()+1];
            int n = c.videoValues.size();
            int W = c.maxCapacity;
            int[][] m = new int[n+1][W];
            ArrayList<Video>[][] videoSolution = new  ArrayList[n+1][W];
            ArrayList<Video> possibleVideos = new ArrayList();
            for(Video vv: c.videoValues.keySet()) {
                possibleVideos.add(vv);
            }
            int ii = 0 ;
            for(Video vid: c.videoValues.keySet()) {
                w[ii+1] = vid.size;
                v[ii+1] = c.videoValues.get(vid);
                ii++;
            }

            c.videos = solve(w, v, W, n, new ArrayList(c.videoValues.keySet()));

            /*
            for (int j = 0; j < W; j++) {
                m[0][j] = 0;
                videoSolution[0][j] = new ArrayList<>();
            }

            for (int i = 1; i <= n; i++) {
                for (int j = 0; j < W; j++) {
                    if (w[i-1] > j) {
                        m[i][j] = m[i - 1][j];
                        ArrayList<Video> newV = new ArrayList();
                        newV.addAll(videoSolution[i - 1][j]);
                        videoSolution[i][j] = newV;
                    } else {
                        m[i][j] = Math.max(m[i - 1][j], m[i - 1][j - w[i-1]] + v[i-1]);
                        ArrayList<Video> newV = new ArrayList();
                        newV.addAll(videoSolution[i - 1][j]);
                        videoSolution[i][j] = newV;
                        if (m[i - 1][j] < m[i - 1][j - w[i-1]] + v[i-1]) {
                            videoSolution[i][j].add(possibleVideos.get(i-1));
                        }
                    }
                }
            }

                for (int i = 0; i < n; i++) {
                    System.out.println(Arrays.toString(m[i]));
                }
*/
           // c.videos = videoSolution[n-1][W-1];


        }

      System.out.println("solution: ");
        int maxsize = 0;
      for(CacheServer c: cacheServers) {
          int maxsize2 = 0;
          System.out.println("id:" + c.id+" max:"+c.maxCapacity);
          for(Video v: c.videos) {
              System.out.println(v.id+"   size:"+v.size);
              maxsize2+=v.size;
          }
          maxsize=Math.max(maxsize2,maxsize);
          System.out.println("=============");
      }
      System.out.println("maxsize" + maxsize);


      System.out.println(getOutputString());
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

    public static String getOutputString() {
      String outputString = "";
      int serverNo = 0;
      for(CacheServer server: cacheServers) {
          if (server.videos.size() != 0) {
              serverNo++;
              outputString += "\n";
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

    public static ArrayList<Video> solve(int[] wt, int[] val, int W, int N,ArrayList<Video> vid)
    {
        ArrayList<Video> toreturn = new ArrayList<>();
        int NEGATIVE_INFINITY = Integer.MIN_VALUE;
        int[][] m = new int[N + 1][W + 1];
        int[][] sol = new int[N + 1][W + 1];

        for (int i = 1; i <= N; i++)
        {
            for (int j = 0; j <= W; j++)
            {
                int m1 = m[i - 1][j];
                int m2 = NEGATIVE_INFINITY;
                if (j >= wt[i])
                    m2 = m[i - 1][j - wt[i]] + val[i];
                /** select max of m1, m2 **/
                m[i][j] = Math.max(m1, m2);
                sol[i][j] = m2 > m1 ? 1 : 0;
            }
        }
        /** make list of what all items to finally select **/
        int[] selected = new int[N + 1];
        for (int n = N, w = W; n > 0; n--)
        {
            if (sol[n][w] != 0)
            {
                selected[n] = 1;
                w = w - wt[n];
            }
            else
                selected[n] = 0;
        }
        /** Print finally selected items **/
        //System.out.println("\nItems selected : ");
        for (int i = 1; i < N + 1; i++)
            if (selected[i] == 1)
                toreturn.add(vid.get(i-1));
        //System.out.println();
        return toreturn;
    }

}
