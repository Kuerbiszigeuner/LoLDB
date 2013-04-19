package entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Champ_PatchNotes
 *
 */
@Entity
@Table(name="PatchNotes")
public class Champ_PatchNotes implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name="champ_fs")
	private Champs champ_patch_fs;
	
	@Column(name="patch_notes")
	private String patch_notes;
	
	
	public Champs getChamp_patch_fs()
	{
		return champ_patch_fs;
	}
	public void setChamp_patch_fs(Champs champ_patch_fs)
	{
		this.champ_patch_fs = champ_patch_fs;
	}
	public String getPatch_notes()
	{
		return patch_notes;
	}
	public void setPatch_notes(String patch_notes)
	{
		this.patch_notes = patch_notes;
	}
	public Champ_PatchNotes() {
		super();
	}   
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
   
}
