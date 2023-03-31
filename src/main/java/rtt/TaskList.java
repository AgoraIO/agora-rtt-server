package rtt;

import java.util.HashMap;

public class TaskList {

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
