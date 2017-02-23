import java.util.ArrayList;

public class Video {
  public int size;
  public ArrayList<CacheServer> cacheServers = new ArrayList<>();

  public Video(int size) {
    this.size = size;
  }
}
