package ua.sumdu.j2se.bohdanbutko.tasks;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

/**
* Класс списка задач, хранит и описывает список объектов Task
*/

public abstract class TaskList implements Cloneable, Iterable<Task>, Serializable{

	// Количество задач
	protected static int numberOfLists = 0;
	protected int count = 0;

	/**
	* Метод для добавления не уникальных задач. 
	* Возможность добавления пустых задач запрещена
	*
	* @param task не уникальная задача
	*/
	public abstract void add(Task task) throws NullPointerException;

	/**
	* Метод для удаления всех задач равных входной.
	* Возможность удаления пустой задачи запрещена
	*
	* @param task задача
	* @return подтверждение удаления
	*/
	public abstract boolean remove(Task task) throws NullPointerException;

	/**
	* @return количество задач в текущем списке
	*/
	public int size(){
		return count;
	}

	/**
	* Метод принимает на вход индекс и возвращает задачу под заданым индексом
	* 
	* @param index номер задачи
	* @return задача под заданым номером
	*/
	public abstract Task getTask(int index);

	@Override
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(this.getClass().getName());
		stringBuilder.append("[");

		for(Task t: this){
			stringBuilder.append(t.getTitle());
			stringBuilder.append(",");
		}

		stringBuilder.append("]");

		return stringBuilder.toString();
	}

	@Override
	public abstract boolean equals(Object object);

	@Override
	public abstract int hashCode();

	@Override
	public TaskList clone() throws CloneNotSupportedException{

		TaskList taskList = (TaskList)super.clone();

		return taskList;
	}
}