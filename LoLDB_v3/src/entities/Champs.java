package entities;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Champs
 *
 */
@Entity
@Table(name="champ")
@NamedQueries(
{
	@NamedQuery(name = "getAllChamps", query = "SELECT c FROM Champs c ORDER BY c.name"),
	@NamedQuery(name = "getFirstChamp", query = "SELECT c FROM Champs c WHERE c.name = (SELECT MIN(ca.name) FROM Champs ca)"),
	@NamedQuery(name = "getParamChamp", query = "SELECT c.id  FROM Champs c WHERE c.name = :p"),
	@NamedQuery(name = "getChamp", query = "SELECT c FROM Champs c WHERE c.name = :p")
	
})
public class Champs implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private static final long serialVersionUID = 1L;
	
	@Column(name="c_name")
	private String name;
	
	
	@Column(name="c_icon")
	private String path;
	
	
	

	public String getPath()
	{
		return path;
	}
	public void setPath(String path)
	{
		this.path = path;
	}
	public Champs() {
		super();
	}   
	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return name;
	}
   
	public int toID()
	{
		return id;
	}
}
