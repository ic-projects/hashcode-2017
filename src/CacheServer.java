import java.util.ArrayList;
import java.util.HashMap;

public class CacheServer {
  public HashMap<Integer, Integer> latencyToEndpoint = new HashMap<>();
  public ArrayList<Video> videos = new ArrayList<>();
  public int maxCapacity;

  public CacheServer(int cacheSize) {
    maxCapacity = cacheSize;
  }
}
