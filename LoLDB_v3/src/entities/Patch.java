package entities;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Patch
 *
 */
@Entity
@Table(name="Patch")
@NamedQueries(
{
	@NamedQuery(name = "getAllPatches", query = "SELECT p FROM Patch p ORDER BY p.version DESC"),
})
public class Patch implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	private static final long serialVersionUID = 1L;

	@Column(name="p_version")
	private String version;
	
	@Override
	public String toString()
	{
		return version;
	}

	@Column(name="p_datum")
	private Date date;
	
	
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	public String getVersion()
	{
		return version;
	}
	public void setVersion(String version)
	{
		this.version = version;
	}
	public Patch() {
		super();
	}   
	public int getID() {
		return this.ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}
   
}
