import java.util.HashMap;
import java.util.Map;

public class Endpoint {
  Map<Integer, Integer> servers;
  Integer latency;
  HashMap<Video, Integer> requests
  Endpoint (Map<Integer, Integer> serverslatency, Integer latency) {
    servers = serverslatency;
    this.latency = latency;
  }

  public void addRequest(Video video, Integer latency) {
    requests.put(video, latency);
  }


}
