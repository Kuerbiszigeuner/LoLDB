package entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Tagging
 *
 */
@Entity
@Table(name="tagging")
@NamedQueries({
	@NamedQuery(name="getAllTag", query = "SELECT t FROM Tagging t ORDER BY t.champ.name, t.typ.Bezeichnung "),
	@NamedQuery(name="firstTag", query = "SELECT t FROM Tagging t WHERE t.id = (SELECT MIN(ta.id) FROM Tagging ta)"),
	@NamedQuery(name="getTaggingFromChamp", query="SELECT ta FROM Tagging ta WHERE ta.champ = :p"),
})
public class Tagging implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="champ_fs")
	private Champs champ;
	
	@ManyToOne
	@JoinColumn(name="typ_fs")
	private Typ typ;
	
	public Champs getChamp()
	{
		return champ;
	}
	public void setChamp(Champs champ)
	{
		this.champ = champ;
	}
	public Typ getTyp()
	{
		return typ;
	}
	public void setTyp(Typ typ)
	{
		this.typ = typ;
	}
	
	
	public Tagging() 
	{
		super();
	}   
	public int getId() 
	{
		return this.id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}
   
}
