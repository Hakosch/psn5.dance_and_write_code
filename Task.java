package ua.sumdu.j2se.bohdanbutko.tasks;

import java.io.Serializable;
import java.util.Date;

/**
* Класс, который описывает тип данных "задача", который
* содержит информацию о сути задачи, её статусе, времени
* оповещения, интервале времени, через который нужно повторить оповещение о ней.
*/
public class Task implements Cloneable, Serializable{
    
    // заголовок задачи
    private String title;
    
    // статус задачи(активна/неактивна)
    private boolean active;
    
    // время оповещения о задаче (для единоразовой задачи)
    //private int time;
    private Date time;

    // время начала оповещения
    //private int start;
    private Date start;

    // время окончания оповещения
    //private int end;
    private Date end;

    // интервал времени, через который необходимо повторить оповещение о задаче
    private Date interval;
      
    
    /** 
    * конструктор для единоразовой и многоразовой задач
    *
    * @param title заголовок задачи
    * @param time время оповещения о задаче
    */
    public Task(String title, Date time){

        this.setTime(time);
        this.setTitle(title);
        this.setActive(false);
    }

    /** 
    * конструктор для повторяющейся задачи 
    *
    * @param title заголовок задачи
    * @param start время начала оповещения о задаче
    * @param end время окончания оповещения о задаче
    * @param interval интервал времени через который необходимо повторить оповещение о задаче
    */
    public Task(String title, Date start, Date end, Date interval){

        this.setTitle(title);
        this.setTime(start, end, interval);
        this.setActive(false);
    }

    /**
    * Получение заголовка задачи
    *
    * @return заголовок задачи
    */
    public String getTitle(){

        return title;
    }
    
    /**
    * Установка заголовка задачи 
    *
    * @param title заголовок
    */
    public void setTitle(String title) throws IllegalArgumentException{

        if((title == null) || (title.equals("")))
            throw new IllegalArgumentException();
        else
            this.title = title;
    }
    
    /**
    * Проверка статуса задачи
    *
    * @return статус задачи
    */
    public boolean isActive(){
        
        if(active)
            return true;
        else
            return false;
    }
    
    /**
    * Установка статуса задачи
    *
    * @param active статус задачи
    */
    public void setActive(boolean active){
        
        this.active = active;
    }
    
    /**
    * Задание времени оповещения о задаче
    * для единоразовой задачи
    *
    * @param time время оповещения о задаче
    */

    public void setTime(Date time) throws NullPointerException{
        
        if(time == null){
        	throw new NullPointerException();
        }
        else{
        	this.time = time;
        	this.start = time;
        	this.end = time;
        	this.interval = new Date(0);
        }
    }
    
    /**
    * Задание времени оповещения о задаче
    * для повторяющейся задачи
    *
    * @param start время начала оповещения о задаче
    * @param end время прекращения оповещения о задаче
    * @param interval интервал времени, через который необходимо повторить оповещение о задаче
    */
    public void setTime(Date start, Date end, Date interval) throws NullPointerException{

        if((start == null) || (end == null) || (interval == null) || (start.after(end))){
        	throw new NullPointerException();
        }
        else{
        	this.start = start;
        	this.end = end;
        	this.interval = interval;
        }
    }

    /**
    * @return время начала оповещения (для повторяющейся задачи) или время единственного оповещения (для единоразовой задачи)
    */
    public Date getTime(){

        if(isRepeated()){
            return start;
        }
        else{
            return time;
        }
    }

    /**
    * @return время начала оповещения (для повторяющейся задачи) или время единственного оповещения (для единоразовой задачи)
    */
    public Date getStartTime(){

        if(isRepeated()){
            return start;
        }
        else{
            return time;
        }
    }

    /**
    * @return время окончания оповещения (для повторяющейся задачи) или время единственного оповещения (для единоразовой задачи)
    */
    public Date getEndTime(){

        if(isRepeated()){
            return end;
        }
        else{
            return time;
        }
    }

    /**
    * @return интервал времени, через который необходимо повторить оповещение о задаче (для повторяющейся задачи) или 0 (для единоразовой задачи)
    */
    public Date getRepeatInterval(){

        return interval;
    }

    /**
    * @return информацию о том, повторяется ли задача
    */
    public boolean isRepeated(){

        if(interval.equals(new Date(0)))
            return false;
        else
            return true;
    }

    /**
     * @return описание данной задачи
     */
    @Override
    public String toString(){

        if(isActive()){
            if(isRepeated()){
                return "Task \"" + title + "\" from " + start + " to " + end + " every " + interval.getTime()/1000 + " seconds";
            }
            else{
                return "Task \"" + title + "\" at " + time;
            }
        }
        else{
            return "Task \"" +  title  + "\" is inactive";
        }
    }

    /**
    * @param current время после которого происходит проверка
    *
    * @return время следующего оповещения
    */
    public Date nextTimeAfter(Date current) throws NullPointerException{

        Date nextTimeValue = null;

        if(current == null){
            throw new NullPointerException();
        }
        else{
            if(isActive()){
                if(isRepeated()) {
                    if (current.before(this.start)) {
                        nextTimeValue = this.start;
                        return nextTimeValue;
                    } else {
                        for (Date startDate = this.start; startDate.before(this.end) || startDate.getTime() == end.getTime();startDate.setTime(startDate.getTime() + this.interval.getTime())) {
                            if (startDate.after(current)) {
                                nextTimeValue = startDate;
                                break;
                            }
                        }
                        return nextTimeValue;
                    }
                }
                else{
                    if(this.time.after(current)){
                        return this.time;
                    }
                    else{
                        return nextTimeValue;
                    }
                }
            }
            else{
                return nextTimeValue;
            }
        }
    }

    @Override
    public boolean equals(Object object){
        Task obj = (Task)object;

        if(this == obj
                ||
                (this.getTitle().equals(obj.getTitle())
                        && this.getStartTime().equals(obj.getStartTime())
                        && this.getEndTime().equals(obj.getEndTime())
                        && this.getRepeatInterval().equals(obj.getRepeatInterval())
                ))
            return true;
        else
            return false;
    }

    @Override
    public int hashCode(){
        return (int)this.start.getTime() + (int)this.end.getTime() + (int)this.interval.getTime() + this.title.hashCode();
    }

    @Override
    public Task clone() throws CloneNotSupportedException{
        return (Task)super.clone();
    }
}
