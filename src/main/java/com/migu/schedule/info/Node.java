package com.migu.schedule.info;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务节点类
 */
public class Node {
    private int nodeId;

    private List<Task> taskLists = new ArrayList<Task>();

    public void removeTask(int taskId) {
        for(int i=0; i<taskLists.size(); i++) {
            if(taskLists.get(i).getTaskId() == taskId) {
                taskLists.remove(i);
            }
        }
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public List<Task> getTaskLists() {
        return taskLists;
    }

    public void setTaskLists(List<Task> taskLists) {
        this.taskLists = taskLists;
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeId=" + nodeId +
                ", taskLists=" + taskLists +
                '}';
    }
}
