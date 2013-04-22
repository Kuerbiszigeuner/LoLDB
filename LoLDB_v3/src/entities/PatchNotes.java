package entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: PatchNotes
 *
 */
@Entity
@Table(name="PatchNotes")
public class PatchNotes implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="patch_fs")
	private Patch patch;
	
	@ManyToOne
	@JoinColumn(name="champ_fs")
	private Champs champ;
	
	@Column(name="text")
	private String text;

	
	
	public Patch getPatch()
	{
		return patch;
	}
	public void setPatch(Patch patch)
	{
		this.patch = patch;
	}
	public Champs getChamp()
	{
		return champ;
	}
	public void setChamp(Champs champ)
	{
		this.champ = champ;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public PatchNotes() {
		super();
	}   
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
   
}
