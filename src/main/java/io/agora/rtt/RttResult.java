package io.agora.rtt;

public enum RttResult {
    // Internal return codes to intimate success or failure
    SUCCESS(0, "Success"),
    FAILED_TO_FETCH_BUILDER_TOKEN(1, "Failed to fetch builderToken"),
    FAILED_TO_START_TASK(2, "Failed to start RTT task "),
    FAILED_TO_QUERY_TASK(3, "Failed to query RTT task"),
    FAILED_TO_STOP_TASK(4, "Failed to stop RTT task");
  
    private final int code;
    private final String description;
  
    private RttResult(int code, String description) {
      this.code = code;
      this.description = description;
    }
  
    public String getDescription() {
       return description;
    }
  
    public int getCode() {
       return code;
    }
  
    @Override
    public String toString() {
      return code + ": " + description;
    }
  }