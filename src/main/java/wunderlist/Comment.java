package wunderlist;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A comment related to a task
 * 
 * @author Paul
 *
 */
@XmlRootElement
public class Comment {
	@XmlElement(name = "channel_id")
	int task_id = 0;
	String channel_type = "tasks";
	int id = 0;
	String text = "null";

	public Comment() {
		super();
	}

	public Comment(int id, int task_id, String text) {
		super();
		this.task_id = task_id;
		this.id = id;
		this.text = text;
	}

	public Comment(int task_id, String text) {
		super();
		this.task_id = task_id;
		this.text = text;
	}

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public String getChannel_type() {
		return channel_type;
	}

	public void setChannel_type(String channel_type) {
		this.channel_type = channel_type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
