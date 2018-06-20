package com.migu.schedule;


import com.migu.schedule.constants.ReturnCodeKeys;
import com.migu.schedule.info.Node;
import com.migu.schedule.info.Task;
import com.migu.schedule.info.TaskInfo;
import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.List;

/*
*类名和方法不能修改
 */
public class Schedule {

    private List<Node> nodeList = new ArrayList<Node>();
    private List<Task> suspendTask = new ArrayList<Task>();

    public int init() {
        nodeList.clear();
        suspendTask.clear();
        return ReturnCodeKeys.E001;
    }


    public int registerNode(int nodeId) {
        if(nodeId <= 0) {
            //服务节点编号小于等于0
            return ReturnCodeKeys.E004;
        }
        for(Node node : nodeList) {
            //服务节点已注册
            if(nodeId == node.getNodeId()) {
                return ReturnCodeKeys.E005;
            }
        }

        Node toAddedNode = new Node();
        toAddedNode.setNodeId(nodeId);
        nodeList.add(toAddedNode);

        return ReturnCodeKeys.E003;
    }

    public int unregisterNode(int nodeId) {
        if(nodeId <= 0) {
            //服务节点编号小于等于0
            return ReturnCodeKeys.E004;
        }

        for(Node node : nodeList) {
            if(nodeId == node.getNodeId()) {
                suspendTask.addAll(node.getTaskLists());
                for(int i=0; i<nodeList.size(); i++) {
                    if(nodeList.get(i).getNodeId() == nodeId) {
                        nodeList.remove(i);
                    }
                }
                return ReturnCodeKeys.E006;
            }
        }
        //服务节点不存在
        return ReturnCodeKeys.E007;
    }


    public int addTask(int taskId, int consumption) {
        if(taskId <= 0) {
            return ReturnCodeKeys.E009;
        }

        //遍历挂起任务
        for(Task task : suspendTask) {
            if(task.getTaskId() == taskId) {
                //任务已添加
                return ReturnCodeKeys.E010;
            }
        }
        //遍历正在运行任务
        for(Node node : nodeList) {
            List<Task> task = node.getTaskLists();
            for(Task existsTask : task) {
                if(existsTask.getTaskId() == taskId) {
                    //任务已添加
                    return ReturnCodeKeys.E010;
                }
            }
        }

        Task task = new Task();
        task.setTaskId(taskId);
        task.setConsumption(consumption);
        suspendTask.add(task);
        return ReturnCodeKeys.E008;
    }


    public int deleteTask(int taskId) {
        if(taskId <= 0) {
            //任务编号非法
            return ReturnCodeKeys.E009;
        }
        //遍历挂起任务
        for(int i=0; i<suspendTask.size(); i++) {
            if(suspendTask.get(i).getTaskId() == taskId) {
                suspendTask.remove(i);
                return ReturnCodeKeys.E011;
            }
        }
        //遍历正在运行任务
        for(int i=0; i<nodeList.size(); i++) {
            List<Task> task = nodeList.get(i).getTaskLists();
            for(Task existsTask : task) {
                if(existsTask.getTaskId() == taskId) {
                    //任务已添加
                    nodeList.get(i).removeTask(taskId);
                    return ReturnCodeKeys.E011;
                }
            }
        }
        //任务不存在
        return ReturnCodeKeys.E012;
    }


    public int scheduleTask(int threshold) {
        // TODO 方法未实现
        return ReturnCodeKeys.E000;
    }


    public int queryTaskStatus(List<TaskInfo> tasks) {
        if(tasks == null) {
            //参数列表非法
            return ReturnCodeKeys.E016;
        }
        TaskInfo taskInfo = new TaskInfo();
        for(Task task : suspendTask) {
            taskInfo = new TaskInfo();
            taskInfo.setTaskId(task.getTaskId());
            taskInfo.setNodeId(-1);
            tasks.add(taskInfo);
        }
        for(Node node : nodeList) {
            for(Task task : node.getTaskLists()) {
                taskInfo = new TaskInfo();
                taskInfo.setTaskId(task.getTaskId());
                taskInfo.setNodeId(node.getNodeId());
                tasks.add(taskInfo);
            }
        }

        for(int i=0; i<tasks.size(); i++){
            for(int j=1; j<tasks.size()-i; j++){
                if(tasks.get(j-1).getTaskId() > tasks.get(j).getTaskId()){
                    TaskInfo info = new TaskInfo();
                    info = tasks.get(j-1);
                    tasks.set(j-1,tasks.get(j));
                    tasks.set(j,info);
                }
            }
        }
        return ReturnCodeKeys.E015;
    }

    public static void main(String[] args) {
        Schedule s = new Schedule();
        s.addTask(1,20);
        s.addTask(5,25);
        s.addTask(3,30);
        s.addTask(8,30);
        s.addTask(6,30);
        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
        s.queryTaskStatus(taskInfos);
        System.out.println(s.suspendTask);
        System.out.println(taskInfos);
    }
}
