package io.agora.rtt;

import java.util.HashMap;

public class TaskList {
    // A class to store and retrieve RTT tasks started using the server
    private HashMap<String, RttTask> tasks = new HashMap<>();

    public void addTask(RttTask task) {
        tasks.put(task.channelName, task);
    }

    public void removeTask(String channelName) {
        tasks.remove(channelName);
    }

    public RttTask getTask(String channelName) {
        return tasks.get(channelName);
    }

}
