import java.util.ArrayList;

public class Video {
  public int size;
  public int id;
  public ArrayList<CacheServer> cacheServers = new ArrayList<>();

  public Video(int size, int id) {
    this.size = size;
    this.id = id;
  }
}
