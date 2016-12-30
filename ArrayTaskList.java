package ua.sumdu.j2se.bohdanbutko.tasks;

import java.util.Iterator;

/**
* Класс хранит список задач
*/
public class ArrayTaskList extends TaskList implements Cloneable{

	private final static int SIZE = 10;
	private Task array[];

	public ArrayTaskList(){

		array = new Task[SIZE];
		numberOfLists++;
	}

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
		else
		{
			if(array.length < (count + 1)){
				Task tempArray[] = new Task[array.length * 2];

				for(int i = 0; i < count; i++){
					tempArray[i] = array[i];
				}
				array = tempArray;
			}
			array[count++] = task;
		}
	}

	/**
	* Удаление задачи
	*
	* @param task задача
	* @return подтверждение удаления
	*/
	@Override
	public boolean remove(Task task){

		if(task == null){
			throw new NullPointerException();
		}
		else{
			for(int i = 0; i < count; i++){
				if(task.equals(array[i])){
					while(i < (count - 1)){
						array[i] = array[++i];
					}
					array[count -1] = null;
					count--;
				}
			}
			return true;
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
			return array[index];
		}
	}

	/*
	* @return массив задач из списка, время оповещения которых
	* находится между from(исключительно) и to(включительно)
	* @param from начало оповещения
	* @param to конец оповоещения
	*/
	/*
	public TaskList incoming(int from, int to){

		TaskList arrayIncoming = new ArrayTaskList();

		for(int i = 0; i < count; i++){

			if((array[i].nextTimeAfter(from) > from && (array[i].nextTimeAfter(from) != -1)) 
				&& (array[i].nextTimeAfter(from)) <= to){
				arrayIncoming.add(array[i]);
			}
		}
		return arrayIncoming;
	}
	*/

	@Override
	public boolean equals(Object object) {

		if((object != null) && (object instanceof ArrayTaskList)){

			if(this.size() != ((ArrayTaskList) object).size()){
				return false;
			}

			Iterator<Task> i = this.iterator();

			for(Task t: (ArrayTaskList)object){
				if(!t.equals(i.next()))
					return false;
			}
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {

		int result = 0;
		for(Task t: this){
			result += t.hashCode();
		}

		return result;
	}

	/**
	 * @return копию списка зачач
	 */
	@Override
	public ArrayTaskList clone() throws CloneNotSupportedException{

		ArrayTaskList tempList = (ArrayTaskList)super.clone();

		for(int i = 0; i < tempList.size(); i++){
			tempList.array[i] = (tempList.array[i].clone());
		}

		return tempList;
	}

	public Iterator<Task> iterator() {

		return new Iterator<Task>() {

			int temp = 0;

			public boolean hasNext() {
				return (temp < size());
			}

			public Task next() {
				if(hasNext())
					return array[temp++];
				else
					throw new IllegalArgumentException();
			}

			public void remove(){
				throw new UnsupportedOperationException();
			}
		};
	}
}