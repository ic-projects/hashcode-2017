import java.util.HashMap;

public class DataCentre {
  public HashMap<Integer, Video> videos = new HashMap<>();

  public DataCentre() {

  }

  public Video (Integer n) {
    return videos.get(n);
  }
}
