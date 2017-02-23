import java.util.Map;

public class Endpoint {
  Map<Integer, Integer> servers;
  Integer latency;
  List<Request> requests;
  Endpoint (Map<Integer, Integer> serverslatency, Integer latency, List<Request> requests) {
    servers = serverslatency;
    this.latency = latency;
    this.requests = requests;
  }

  
}
