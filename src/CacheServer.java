import java.util.ArrayList;
import java.util.HashMap;

public class CacheServer {
  public int id;
  public HashMap<Integer, Integer> latencyToEndpoint = new HashMap<>();
  public ArrayList<Video> videos = new ArrayList<>();
  public HashMap<Video, Integer> videoValues = new HashMap<>();
  public int maxCapacity;

  public CacheServer(int cacheSize) {
    maxCapacity = cacheSize;
  }

  public void addVideo(Video video) {
    videos.add(video);
    video.cacheServers.add(this);
  }
}
