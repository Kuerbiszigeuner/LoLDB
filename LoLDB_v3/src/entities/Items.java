package entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Items
 *
 */
@Entity
@Table(name="items")
public class Items implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private static final long serialVersionUID = 1L;
	
	@Column(name="i_name")
	private String i_name;

	public String getI_name()
	{
		return i_name;
	}
	public void setI_name(String i_name)
	{
		this.i_name = i_name;
	}
	public Items() {
		super();
	}   
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
   
}
