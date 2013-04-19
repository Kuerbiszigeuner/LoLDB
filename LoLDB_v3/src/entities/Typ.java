package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: Typ
 *
 */
@Entity
@Table(name="typ")
@NamedQueries({
	 @NamedQuery(name="allTyp", query = "SELECT t FROM Typ t ORDER BY t.Bezeichnung"),
	 @NamedQuery(name="firstTyp", query = "SELECT t FROM Typ t WHERE t.Bezeichnung = (SELECT MIN(ta.Bezeichnung) FROM Typ ta)"),
	 @NamedQuery(name="getTagForChamp", query="SELECT ta.typ FROM Tagging ta WHERE ta.champ = :p"),
	 @NamedQuery(name = "getParamTyp", query = "SELECT t.id  FROM Typ t WHERE t.Bezeichnung = :p"),
	 @NamedQuery(name = "getTyp", query = "SELECT t FROM Typ t WHERE t.Bezeichnung = :p")
	
	 //SELECT *
	 //FROM Typ
	 //WHERE Typ.ID IN
	 //(SELECT Typ_fs FROM tagging)
})
public class Typ implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	
	private int id;
	private static final long serialVersionUID = 1L;
	
	@Column(name="t_name")
	private String Bezeichnung;

	@Override
	public String toString() {
		return Bezeichnung;
	}
	public String getBezeichnung() {
		return Bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		Bezeichnung = bezeichnung;
	}
	public Typ() {
		super();
	}   
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
   
}
