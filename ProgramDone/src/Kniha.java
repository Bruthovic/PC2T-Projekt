import java.util.List;

public class Kniha {
	
	private String nazev;
	private String autor;
	private String typ;
	private int rok_vydani;
	private String spec;
	private String vypujceni;
	
	public Kniha(String nazev, String autor, String typ, int rok_vydani, String spec, String vypujceni) {
		this.nazev = nazev;
		this.autor = autor;
		this.typ = typ;
		this.rok_vydani = rok_vydani;
		this.spec = spec;
		this.vypujceni = vypujceni;
	}
	
	public String getNazev() {
		return nazev;
	}
	
	public String getAutor() {
		return autor;
	}
	
	public String getTyp() {
		return typ;
	}
	
	public int getRok_Vydani() {
		return rok_vydani;
	}
	
	public String getSpec() {
		return spec;
	}
	
	public String getVypujceni() {
		return vypujceni;
	}
	
	public void setVypujceni(String vypujceni) {
		this.vypujceni = vypujceni;
	}
	
	public void setAutor(String autor) {
		this.autor = autor;
	}
	
	public void setRok(int rok_vydani) {
		this.rok_vydani = rok_vydani;
	}
}
