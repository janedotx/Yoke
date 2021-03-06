package com.system2override.hobbes.integrations;

import com.system2override.hobbes.Models.RoomModels.LocalTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskHelpers {
    public static List<LocalTask> selectCompletedTasks(List<LocalTask> tasks) {
        List<LocalTask> completedTasks = new ArrayList<>();
        for (LocalTask task: tasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            }
        }
        return completedTasks;
    }

    public static List<String> getTodoAppIDStringsFromTasks(List<LocalTask> tasks) {
        List<String> ids = new ArrayList<>();
        for (LocalTask task: tasks) {
            ids.add(task.getTodoAppIdString());
        }
        return ids;
    }

    public static List<String> selectLocalTaskIDStringsInCommon(List<String> a, List<String> b) {
        Set setA = new HashSet(a);
        Set setB = new HashSet(b);
        setA.retainAll(setB);

        return new ArrayList(setA);
    }
//    */

}
