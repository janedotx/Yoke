package com.system2override.yoke.integrations;

import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.models.LocalTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Helpers {
    // newTasks should be the result of querying the task API using updatedMin
    public static boolean completedNewTasks(List<LocalTask> newTasks) {
        return false;
    }

    public static List<String> getNewCompleted(List<LocalTask> incompletes, List<LocalTask> newCompletes) {
        List<String> incompleteIDStrings =  getTodoAppIDStringsFromTasks(incompletes);
        List<String> newCompleteIDStrings = getTodoAppIDStringsFromTasks(newCompletes);

        return null;
    }
 //    /*
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
