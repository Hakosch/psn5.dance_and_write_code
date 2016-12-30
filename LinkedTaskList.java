package ua.sumdu.j2se.bohdanbutko.tasks;

import java.util.Iterator;

public class LinkedTaskList extends TaskList implements Cloneable{

	public LinkedTaskList(){
		numberOfLists++;
	}

	public Iterator<Task> iterator() {

		return new Iterator<Task>() {

			MyList temp = head;

			public boolean hasNext() {
				if(temp == null)
					return false;
				else
					return true;
			}

			public Task next() {
				if(hasNext()){
					Task tempTask = temp.task;
					temp = temp.next;
					return tempTask;
				}
				else
					throw new IllegalArgumentException();
			}

			public void remove(){
				throw new UnsupportedOperationException();
			}
		};
	}

	private class MyList{

        MyList next;
		Task task;

		private MyList(){
		next = null;
		task = null;
		}
	}

	private MyList head;
	private MyList tail;

	/**
	* Добавление задачи
	*
	* @param task задача
	*/
	@Override
	public void add(Task task){

        if(task == null){
			throw new NullPointerException();
		}
		else{
			MyList element = new MyList();
			element.task = task;

			if(tail == null){
				head = element;
				tail = element;
				head.next = tail;
			}
			else{
				tail.next = element;
				tail = element;
			}

			count++;
		}
	}

	/**
	* Удаление задачи
	*
	* @param task задача
	*/
	@Override
	public boolean remove(Task task){

        if(task == null){
			throw new NullPointerException();
		}
		else{
			if(head == null){
				return false;
			}

			MyList temp = head;

			if(temp.task.equals(task)){
				head = temp.next;
				count--;
				return true;
			}

			while(temp.next != null){
				if(temp.next.task.equals(task))
				{
					if(temp.next.equals(tail)){
						tail = temp;
						tail.next = null;
						count--;
						return true;
					}
					else{
						temp.next = temp.next.next;
						count--;
						return true;
					}
				}

				temp = temp.next;
			}

			return false;
		}
	}

	/**
	* @param index номер задачи
	* @return задача по заданому индексу
	*/ 
	@Override
	public Task getTask(int index){

        if((index < 0) || (index >= size())){
			throw new IllegalArgumentException();
		}
		else{
			MyList temp = head;

			for(int i = 0; i < index; i++){
				temp = temp.next;
			}

			return temp.task;
		}
	}

	/**
	* @return массив задач из списка, время оповещения которых
	* находится между from(исключительно) и to(включительно)
	* @param from начало оповещения
	* @param to конец оповоещения
	*/
	/*
	public TaskList incoming(int from, int to){

        TaskList arrayIncoming = new LinkedTaskList();
		MyList currentTask = head;

		for(int i = 0; i < count; i++){
			if((currentTask.task.nextTimeAfter(from) > from 
				&& currentTask.task.nextTimeAfter(from) != -1) 
				&& (currentTask.task.nextTimeAfter(from) <= to)){

				arrayIncoming.add(currentTask.task);
			}

			currentTask = currentTask.next;
		}

		return arrayIncoming;
	}
	*/

    @Override
    public boolean equals(Object object) {

        if((object != null) && (object instanceof LinkedTaskList)){

			if(this.size() != ((LinkedTaskList) object).size()){
				return false;
			}

            Iterator<Task> i = this.iterator();

            for(Task t: (LinkedTaskList)object){
                if(!t.equals(i.next()))
                    return false;
            }

            return true;
        }
        else
            return false;
    }

    @Override
    public int hashCode() {

        int result = 0;
        for(Task t: this)
            result += t.hashCode();

        return result;
    }

    /**
     * @return копию списка задач
     */
    @Override
    public LinkedTaskList clone() throws CloneNotSupportedException{

        LinkedTaskList tempList = (LinkedTaskList)super.clone();

        return tempList;
    }
}