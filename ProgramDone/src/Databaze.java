import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Databaze {
	
	private Map<String,Kniha>  prvkyDatabaze;
	private Map<String, List<Kniha>> autorKnihy;
	private Map<String, List<Kniha>> zanrKnihy;
	private Map<Integer, List<Kniha>> rokKnihy;
	
	public Databaze() {
		prvkyDatabaze = new HashMap<>();
		autorKnihy = new HashMap<>();
		zanrKnihy = new HashMap<>();
		rokKnihy = new HashMap<>();
	}
	
	public boolean setKniha(String nazev, String autor, String typ, int rok, String spec, String vypujceni) {
		Kniha newKniha = new Kniha(nazev, autor, typ, rok, spec, vypujceni);
		if(prvkyDatabaze.put(nazev, newKniha)==null) {
			addToMapList(autorKnihy, autor, newKniha);
			addToMapList(zanrKnihy, spec, newKniha);
			addToMapList(rokKnihy, rok, newKniha);
			return true;
		} 
		else return false;
	}
	
	public Kniha getKniha(String nazev) {
		return prvkyDatabaze.get(nazev);
	}
	
	public List<Kniha> getDila(String autor) {
		return autorKnihy.getOrDefault(autor, new ArrayList<>());
	}
	
	public List<Kniha> getDila_podleZanru(String zanr){
		return zanrKnihy.getOrDefault(zanr, new ArrayList<>());
	}
	
	public void vypisDatabaze() {
		
		Set<String> seznamNazvu = new TreeSet<>(prvkyDatabaze.keySet());
		for(String nazev : seznamNazvu) {
			Kniha kniha = prvkyDatabaze.get(nazev);
			System.out.println("Nazev: " + kniha.getNazev() + " Autor: " + kniha.getAutor() + " Spec: " + kniha.getSpec() +" Rok vydani: " + kniha.getRok_Vydani() + " Stav dostupnosti: " + kniha.getVypujceni());
			System.out.println();
		}
	}
	

	
	public static boolean platnyZanr(String zanr) {
        switch (zanr) {
            case "detektivni":
            case "sci-fi":
            case "fantasy":
            case "hororovy":
            case "dobrodruzny":
                return true;
            default:
                return false;
        }
    }
	
	public static boolean platnyRocnik(String rocnik) {
        switch (rocnik) {
            case "1.rocnik":
            case "2.rocnik":
            case "3.rocnik":
            case "4.rocnik":
            case "5.rocnik":
            case "6.rocnik":
            case "7.rocnik":
            case "8.rocnik":
            case "9.rocnik":
                return true;
            default:
                return false;
        }
    }
	
	public boolean vymazKnihu(String nazev) {
		if(prvkyDatabaze.remove(nazev)!=null) {
			return true;
		} else return false;
	}
	
	public boolean zmenaDostupnosti(String nazev) {
		
		Kniha book = prvkyDatabaze.get(nazev);
		
		if(book != null) {	
			book.setVypujceni("vypujcena".equalsIgnoreCase(book.getVypujceni()) ? "dostupna" : "vypujcena");
			return true;
		}else return false;
	}
	
	public void vypisDatabazePodleTypu() {
		for(Map.Entry<String, Kniha> entry: prvkyDatabaze.entrySet()) {
			String nazev = entry.getKey();
			Kniha kniha = entry.getValue();
			String typ = kniha.getTyp();
			
			System.out.println("Nazev: " + nazev + ", Typ: " + typ);
		}
	}
	
	public void ulozitDoSouboru(String nazev) {
		String slozka = nazev + ".txt";
		Kniha kniha = prvkyDatabaze.get(nazev);
		
		try
		{
			String nazev_knihy = nazev.toLowerCase();
			FileWriter fw = new FileWriter(slozka);
			BufferedWriter out = new BufferedWriter(fw);
			
			out.write(kniha.getNazev() + ";" + kniha.getAutor() + ";" + kniha.getTyp() + ";" + kniha.getRok_Vydani() + ";" + kniha.getSpec() + ";" + kniha.getVypujceni());
			
			out.close();
			fw.close();
		}
		catch (IOException e) 
		{
			System.out.println("Soubor se nepodarilo otevrit nebo vytvorit.");
		}

	
	}
	
	public void nacistZeSouboru(String nazev) {
        String slozka = nazev + ".txt";
        
        try (BufferedReader br = new BufferedReader(new FileReader(slozka))) {
            String line = br.readLine();
            if (line != null) {
                
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    String nazev_knihy = parts[0].trim();
                    String autor = parts[1].trim();
                    String typ = parts[2].trim();
                    int rok_vydani = Integer.parseInt(parts[3].trim());
                    String spec = parts[4].trim();
                    String vypujceni = parts[5].trim();

                    
                    System.out.println("Nazev: " + nazev_knihy);
                    System.out.println("Autor: " + autor);
                    System.out.println("Typ: " + typ);
                    System.out.println("Rok vydani: " + rok_vydani);
                    System.out.println("Spec: " + spec);
                    System.out.println("Vypujceni: " + vypujceni);
                } else {
                    System.out.println("Invalid format in the text file.");
                }
            } else {
                System.out.println("No data found in the text file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	
	
	private <T> void addToMapList(Map<T, List<Kniha>> map, T key, Kniha book) {
		map.computeIfAbsent(key, k -> new ArrayList<>()).add(book);
	}
	
	
	
	
	private static volatile Connection conn;
	
	public boolean pripojit()
	{
		conn = null; 
		
		try {conn = DriverManager.getConnection("jdbc:sqlite:tabulka.db");}
		catch (SQLException e)
		{
			
			return false;
		}
		return true;
	}
	
	public void odpojit()
	{
		if (conn != null)
		{
			try {conn.close();}
			catch (SQLException e) {System.out.println(e.getMessage());}
		}
	}
	
	public boolean vytvorit_tabulku() 
	{
	    if (conn == null) return false;
	    String sql = "CREATE TABLE IF NOT EXISTS tabulka ("
	            + "nazev varchar(255) NOT NULL,"
	            + "autor varchar(255) NOT NULL,"
	            + "typ varchar(255) NOT NULL,"
	            + "rok_vydani int NOT NULL,"
	            + "spec varchar(255) NOT NULL,"
	            + "vypujceni varchar(255) NOT NULL);";
	    try {
	        Statement stmt = conn.createStatement();
	        stmt.execute(sql);
	      
	        return true;
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	    return false;
	}
	
	
	
	public void ulozit_do_tabulky() {
	    	
		
	    
	    try (Connection connection = DriverManager.getConnection("jdbc:sqlite:tabulka.db")) {
            String sql = "DELETE FROM tabulka";

            try (Statement statement = connection.createStatement()) {
                
                int rowsDeleted = statement.executeUpdate(sql);

                
                if (rowsDeleted > 0) {
                    System.out.println("Celkovy pocet knih v databazy: " + prvkyDatabaze.size());
                    
                } else {
                    System.out.println("Tabulka je prazdna.");
                    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
           
        }
	        
	    	setKniha("Book1", "Author1", "Type1", 2022, "Spec1", "dostupna");
	    	setKniha("Book2", "Author2", "Type2", 2023, "Spec2", "dostupna");
	        
	    	
	        
	       
	        
	        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:tabulka.db")) {
	            String sql = "INSERT INTO tabulka (nazev, autor, typ, rok_vydani, spec, vypujceni) VALUES (?, ?, ?, ?, ?, ?)";

	           
	            try (PreparedStatement statement = connection.prepareStatement(sql)) {
	                
	                for (Kniha kniha : prvkyDatabaze.values()) {
	                    
	                    statement.setString(1, kniha.getNazev());
	                    statement.setString(2, kniha.getAutor());
	                    statement.setString(3, kniha.getTyp());
	                    statement.setInt(4, kniha.getRok_Vydani());
	                    statement.setString(5, kniha.getSpec());
	                    statement.setString(6, kniha.getVypujceni());

	                    
	                    statement.executeUpdate();
	                }

	                System.out.println("Data byla uspesne ulozena do databaze.");
	            }catch (SQLException e) {
	            	e.printStackTrace();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}

	
	
	public void nacistZTabulky() {
	    try (Connection connection = DriverManager.getConnection("jdbc:sqlite:tabulka.db")) {
	        String sql = "SELECT nazev, autor, typ, rok_vydani, spec, vypujceni FROM tabulka";

	        try (PreparedStatement statement = connection.prepareStatement(sql);
	             ResultSet resultSet = statement.executeQuery()) {

	            if (!resultSet.next()) {
	                System.out.println("Nebyly nalezeny žádné záznamy.");
	            } else {
	                do {
	                    String nazev = resultSet.getString("nazev");	               
	                    String autor = resultSet.getString("autor");
	                    System.out.println("Nacteny nazev z databaze: " + autor);
	                    String typ = resultSet.getString("typ");
	                    int rok_vydani = resultSet.getInt("rok_vydani");
	                    String spec = resultSet.getString("spec");
	                    String vypujceni = resultSet.getString("vypujceni");

	                    setKniha(nazev,autor, typ, rok_vydani, spec, vypujceni);
	                    
	                } while (resultSet.next());
	            }
	        }catch(SQLException e) {
	        	e.printStackTrace();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        
	    }
	}
	
}
