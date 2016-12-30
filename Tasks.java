package ua.sumdu.j2se.bohdanbutko.tasks;

import javafx.collections.transformation.SortedList;

import java.util.*;

/**
 * Created by Bogdan Butko on 11/18/2016.
 */
public class Tasks {

    /**
     * @return массив задач из списка, время оповещения которых
     * находится между from(исключительно) и to(включительно)
     * @param tasks список задач
     * @param start начало оповещения
     * @param end конец оповоещения
     */
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end) throws InstantiationException, IllegalAccessException {

        TaskList taskList = (TaskList) tasks.getClass().newInstance();

        //System.out.println(taskList.getClass().getName());

        for(Task t : tasks){
            if((t.nextTimeAfter(start).after(start) && t.nextTimeAfter(start) != null)
                    && (t.nextTimeAfter(start).before(end))){
                taskList.add(t);
            }
        }

        return taskList;
    }


    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end) throws Exception{

        SortedMap<Date, Set<Task>> sortedMap = new TreeMap<Date, Set<Task>>();

        for(Task t: tasks){

            if (sortedMap.get(t.nextTimeAfter(start)) != null){

                Set<Task> setTask = sortedMap.get(t.nextTimeAfter(start));
                setTask.add(t);
                sortedMap.put(t.nextTimeAfter(start), setTask);
            }
            else{

                Set<Task> setTask = new HashSet<Task>();
                setTask.add(t);
                sortedMap.put(t.nextTimeAfter(start), setTask);
            }
        }

        return sortedMap;
    }
}
