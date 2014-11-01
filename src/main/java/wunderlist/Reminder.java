package wunderlist;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A reminder
 * 
 * @author Paul
 * 
 */
@XmlRootElement
public class Reminder {
	int id = 0;
	int task_id = 0;
	String date = null;
	String owner = "null";

	public Reminder() {

	};

	public Reminder(int id, int task_id, String date, String owner) {
		super();
		this.id = id;
		this.task_id = task_id;
		this.date = date;
		this.owner = owner;
	}

	public Reminder(int task_id, String date, String owner) {
		super();
		this.task_id = task_id;
		this.date = date;
		this.owner = owner;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}
