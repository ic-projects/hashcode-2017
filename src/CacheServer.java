import java.util.ArrayList;
import java.util.HashMap;

public class CacheServer {
  public int id;
  public HashMap<Endpoint, Integer> endpointToLatency = new HashMap<>();
  public ArrayList<Video> videos = new ArrayList<>();
  public HashMap<Video, Integer> videoValues = new HashMap<>();
  public int maxCapacity;

  public CacheServer(int cacheSize) {
    maxCapacity = cacheSize;
  }
}
