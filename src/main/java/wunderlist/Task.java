package wunderlist;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A task in Wunderlist
 * 
 * @author Paul
 * 
 */
@XmlRootElement
public class Task implements TaskList{
	private int id = 0;
	private String title = "null";
	@XmlElement(name = "due_date")
	private String dueDate = "null";
	private int list_id = 0;

	public Task() {

	}

	public Task(int id, String title, String dueDate, int list_id) {
		super();
		this.id = id;
		this.title = title;
		this.dueDate = dueDate;
		this.list_id = list_id;
	}
	
	public Task(String title, String dueDate, int list_id) {
		super();
		this.title = title;
		this.dueDate = dueDate;
		this.list_id = list_id;
	}
	
	public Task(int id, String title, int list_id) {
		super();
		this.id = id;
		this.title = title;
		this.list_id = list_id;
	}

	public Task(String title, int list_id) {
		this.title = title;
		this.list_id = list_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public int getList_id() {
		return list_id;
	}

	public void setList_id(int list_id) {
		this.list_id = list_id;
	}

}