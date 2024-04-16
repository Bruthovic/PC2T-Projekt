import java.io.*;
import java.sql.*;
import java.util.*;

public class Databaza {
	
    private static Map<String, Kniha> prvkyDatabazy;
    private static Connection conn;
    
    public static boolean pripojit() {
        conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:kniznica.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Wtf");
            return false;
        }
        return true;
    }

    public static void odpojit() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static boolean vytvorit_tabulku() {
        if (conn == null) return false;
        try {
        	Statement stmt = conn.createStatement();
        	 String sql = "CREATE TABLE IF NOT EXISTS kniznica (nazov TEXT, autori TEXT, rok INTEGER, vypozicanie TEXT, zaner TEXT)";
        	 stmt.execute(sql);
            
        	 String sqlSecondTable = "CREATE TABLE IF NOT EXISTS kniznicaUcebnic (nazov TEXT, autori TEXT, rok INTEGER, vypozicanie TEXT, rocnik TEXT)";
        	 stmt.execute(sqlSecondTable);
            
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static ArrayList<String> nacitat_tabulku() {
        ArrayList<String> myList = new ArrayList<>();
        Map<String, List<String>> bookMap = new HashMap<>();
        String sql = "SELECT nazov, autori, rok, vypozicanie, zaner FROM kniznica"; 
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String nazov = rs.getString("nazov");
                String autori = rs.getString("autori");
                int rok = rs.getInt("rok");
                String vypozicanie = rs.getString("vypozicanie");
                String zaner = rs.getString("zaner");

                if (bookMap.containsKey(nazov)) {
                 
                    List<String> authors = bookMap.get(nazov);
                    authors.addAll(Arrays.asList(autori.split(",")));
                } else {
                  
                    bookMap.put(nazov, Arrays.asList(autori.split(",")));
                }
               
                String autoriString = String.join(", ", bookMap.get(nazov));
                String bookInfo = String.format("Nazov: %s, Autori: %s, Rok: %d, Vypozicanie: %s, Zaner: %s",
                                                nazov, autoriString, rok, vypozicanie, zaner);
                myList.add(bookInfo);
            }
            
        } catch (SQLException e) {
            System.out.println("Error reading books: " + e.getMessage());
        }
        
        return myList;
    }
    
    public static ArrayList<String> nacitat_tabulku2() {
        ArrayList<String> myList2 = new ArrayList<>();
        Map<String, List<String>> bookMap2 = new HashMap<>();
        String sql = "SELECT nazov, autori, rok, vypozicanie, rocnik FROM kniznicaUcebnic"; 
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String nazov = rs.getString("nazov");
                String autori = rs.getString("autori");
                int rok = rs.getInt("rok");
                String vypozicanie = rs.getString("vypozicanie");
                String rocnik = rs.getString("rocnik");

                if (bookMap2.containsKey(nazov)) {
                    
                    List<String> authors = bookMap2.get(nazov);
                    authors.addAll(Arrays.asList(autori.split(",")));
                } else {
                    
                    bookMap2.put(nazov, Arrays.asList(autori.split(",")));
                }
               
                String autoriString = String.join(", ", bookMap2.get(nazov));
                String bookInfo = String.format("Nazov: %s, Autori: %s, Rok: %d, Vypozicanie: %s, Rocnik: %s", nazov, autoriString, rok, vypozicanie, rocnik);
                myList2.add(bookInfo);
            }
            
        } catch (SQLException e) {
            System.out.println("Error reading books: " + e.getMessage());
        }        
        return myList2;
    }
    
    public static void vypis_knih(ArrayList<String> myList) {
        System.out.println("Vypise vsechny knihy");
        
        for (String book : myList) {
            System.out.println(book);                      
        }
        
        int size = myList.size();
        
        for (int i = 0; i < size; i++) {
            String bookInfo = myList.get(i);
            String[] parts = bookInfo.split(", ");
            String nazov = parts[0].substring(parts[0].indexOf(":") + 1).trim(); 
            List<String> autori = new ArrayList<>();
            
            for (int j = 1; j < 5; j++) {
                String part = parts[j].trim(); 
                
                
                boolean isNumber = false;
                for (char c : part.toCharArray()) {
                    if (Character.isDigit(c)) {
                        isNumber = true;
                        break;
                    }
                }
               if(isNumber) {
            	   break;
               }                                	   
            	   autori.addAll(Arrays.asList(part.substring(part.indexOf(":") + 1).trim().split(", ")));               
            }
            int rok = Integer.parseInt(parts[parts.length - 3].substring(parts[parts.length - 3].indexOf(":") + 1).trim()); 
            String vypozicanie = parts[parts.length - 2].substring(parts[parts.length - 2].indexOf(":") + 1).trim(); 
            String zaner = parts[parts.length - 1].substring(parts[parts.length - 1].indexOf(":") + 1).trim();

            prvkyDatabazy.put(nazov, new Román(nazov, autori, rok, vypozicanie, zaner));
        }


     
    }
    
    public static void vypis_knih2(ArrayList<String> myList2) {
        System.out.println("Vypise vsechny knihy");
        for (String book : myList2) {
            System.out.println(book);           
        }
        int size = myList2.size();
        
        for (int i = 0; i < size; i++) {
            String bookInfo = myList2.get(i);
            String[] parts = bookInfo.split(", ");

            String nazov = parts[0].substring(parts[0].indexOf(":") + 1).trim(); 
            List<String> autori = new ArrayList<>();
            for (int j = 1; j < 5; j++) {
                String part = parts[j].trim(); 
                boolean isNumber = false;
                for (char c : part.toCharArray()) {
                    if (Character.isDigit(c)) {
                        isNumber = true;
                        break;
                    }
                }
               if(isNumber) {
            	   break;
               }                          	   
            	   autori.addAll(Arrays.asList(part.substring(part.indexOf(":") + 1).trim().split(", ")));
               
            }

            int rok = Integer.parseInt(parts[parts.length - 3].substring(parts[parts.length - 3].indexOf(":") + 1).trim()); 
            String vypozicanie = parts[parts.length - 2].substring(parts[parts.length - 2].indexOf(":") + 1).trim(); 
            String rocnik = parts[parts.length - 1].substring(parts[parts.length - 1].indexOf(":") + 1).trim(); 

            prvkyDatabazy.put(nazov, new Učebnica(nazov, autori, rok, vypozicanie, rocnik));
        }


     
    }
    
    public Databaza() 
    {
        prvkyDatabazy = new HashMap<>();
    }
    public boolean setRomán(String názov, List<String> autori, int rok, String vypozicanie, String žáner) 
    {
        if (prvkyDatabazy.put(názov, new Román(názov, autori, rok, vypozicanie, žáner)) == null) {
        	
                String nazov = názov;
                String zaner = žáner;

                String sql = "INSERT INTO kniznica(nazov, autori, rok, vypozicanie, zaner) VALUES(?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, nazov);
                    pstmt.setString(2, String.join(",", autori));
                    pstmt.setInt(3, rok);
                    pstmt.setString(4, vypozicanie);
                    pstmt.setString(5, zaner);
                    pstmt.executeUpdate();
                    System.out.println("Book added successfully.");
                } catch (SQLException e) {
                    System.out.println("Error adding book: " + e.getMessage());
                }
           
        	return true;
        }
        else
            return false;
    }
    
    public boolean setUcebnica(String názov, List<String> autori, int rok, String vypozicanie, String ročnik) 
    {
        if (prvkyDatabazy.put(názov, new Učebnica(názov, autori, rok, vypozicanie, ročnik)) == null) {
        	String nazov = názov;
            String rocnik = ročnik;

            String sql = "INSERT INTO kniznica(nazov, autori, rok, vypozicanie, zaner) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nazov);
                pstmt.setString(2, String.join(",", autori));
                pstmt.setInt(3, rok);
                pstmt.setString(4, vypozicanie);
                pstmt.setString(5, rocnik);
                pstmt.executeUpdate();
                System.out.println("Book added successfully.");
            } catch (SQLException e) {
                System.out.println("Error adding book: " + e.getMessage());
            }
            return true;
        }
       
        else
            return false;
    }
    
    public boolean upravaKnihy(String názov, List<String> novíAutori, int novýRok, String novéVypozicanie) 
    {
        Kniha kniha = prvkyDatabazy.get(názov);
        if (kniha != null) 
        {
            kniha.setAutori(novíAutori);
            kniha.setRok(novýRok);
            kniha.setVypozicanie(novéVypozicanie);
            return true;
        } else 
        {
    		System.out.println("Kniha s názvom "+názov+" neexistuje.");
            return false;
        }
    }
    
    public boolean odstranenieKnihy(String nazovKnihy) 
    {
    	Kniha kniha = prvkyDatabazy.get(nazovKnihy);
        if (kniha != null) 
        {
        	prvkyDatabazy.remove(nazovKnihy);
			System.out.println("Kniha "+nazovKnihy+" bola odstránená.");
        	return true;
        }
        	else
    		System.out.println("Kniha s názvom "+nazovKnihy+" neexistuje.");
        	return false;
    }
    
    public boolean zmenaDostupnosti(String nazovKnihy, String Vypozicanie) 
    {
        Kniha kniha = prvkyDatabazy.get(nazovKnihy);
        if (kniha != null) 
        {
            kniha.setVypozicanie(Vypozicanie);
            return true;
        } 
        else
    		System.out.println("Kniha s názvom "+nazovKnihy+" neexistuje.");
            return false;
    }
    
    public void vypisDatabazy() 
    {
        Map<String, Kniha> sortedMap = new TreeMap<>(prvkyDatabazy);

        for (Map.Entry<String, Kniha> záznam : sortedMap.entrySet()) {
            String názov = záznam.getKey();
            Kniha kniha = záznam.getValue();
            System.out.println("Názov: " + kniha.getNázov());
            System.out.println("Autor/autori: " + kniha.getAutor());
            if (kniha instanceof Román) {
                System.out.println("Žáner: " + ((Román) kniha).getŽáner());
            } else if (kniha instanceof Učebnica) {
                System.out.println("Odporúčaný ročník: " + ((Učebnica) kniha).getRočnik());
            }
            System.out.println("Rok vydania: " + kniha.getRok());
            System.out.println("Dostupnosť: " + kniha.getVypozicanie());
            System.out.println();
        }
    }
    
    public void vypisKnihy(String nazov)
    {
        Kniha kniha = prvkyDatabazy.get(nazov);
        if (kniha != null) 
        {
            System.out.println("Názov: " + kniha.getNázov());
            System.out.println("Autor/autori: " + kniha.getAutor());
            if (kniha instanceof Román) 
            {
                System.out.println("Žáner: " + ((Román) kniha).getŽáner());
            } 
            else if (kniha instanceof Učebnica) 
            {
                System.out.println("Odporúčaný ročník: " + ((Učebnica) kniha).getRočnik());
            }
            System.out.println("Rok vydania: " + kniha.getRok());
            System.out.println("Dostupnosť: " + kniha.getVypozicanie());
        } 
        else 
        {
            System.out.println("Kniha s názvom " + nazov + " neexistuje.");
        }
    }
    
    public void vypisKnihAutora(String autor) 
    {
        List<Kniha> knihyAutora = new ArrayList<>();
        for (Kniha kniha : prvkyDatabazy.values()) 
        {
            List<String> autoriKnihy = kniha.getAutor();
            if (autoriKnihy.contains(autor)) 
            {
                knihyAutora.add(kniha);
            }
        }
        knihyAutora.sort(Comparator.comparingInt(Kniha::getRok));
        if (!knihyAutora.isEmpty()) 
        {
            System.out.println("Knihy autora " + autor + " v chronologickom poradí:");
            for (Kniha kniha : knihyAutora) 
            {
                System.out.println("Názov: " + kniha.getNázov());
                System.out.println("Rok vydania: " + kniha.getRok());
                System.out.println();
            }
        }
        else 
        {
            System.out.println("Autor " + autor + " nemá žiadne knihy v databázi.");
        }
    }
    
    public void vypisZanru(String Zaner) 
    {
        List<Kniha> knihyPodlaZanru = new ArrayList<>();
        for (Kniha kniha : prvkyDatabazy.values()) {
            if (((Román) kniha).getŽáner().equals(Zaner))
            {
                knihyPodlaZanru.add(kniha);
            }
        }
        if (!knihyPodlaZanru.isEmpty()) 
        {
            System.out.println("Zoznam kníh žánru" + Zaner + ":");
            for (Kniha kniha : knihyPodlaZanru) 
            {
                System.out.print(kniha.getNázov()+" ");
                /*System.out.println("Autor/autori: " + kniha.getAutor());
                System.out.println("Rok vydání: " + kniha.getRok());
                System.out.println("Dostupnosť: " + kniha.getVypozicanie());
                System.out.println();*/
            }
        } 
        else 
        {
            System.out.println("Databáza neobsahuje žiadne knihy tohto žánru.");
        }
    }
    
    public void vypisDostupnosti()
    {
    List<Kniha> knihyPodlaDostupnosti = new ArrayList<>();
	    for (Kniha kniha : prvkyDatabazy.values()) 
	    {
	        if ((kniha.getVypozicanie().equals("Vypozicana")));
	        {
	        	knihyPodlaDostupnosti.add(kniha);
	        }
	    }
	    if (!knihyPodlaDostupnosti.isEmpty())
        {
            System.out.println("Zoznam vypožičaných knih: ");
            for (Kniha kniha : knihyPodlaDostupnosti) 
            {
                System.out.println("Názov: "+kniha.getNázov());
                if (kniha instanceof Román) 
                {
                    System.out.println("Typ: Román");
                } 
                else if (kniha instanceof Učebnica) 
                {
                    System.out.println("Typ: Učebnica");
                }
                System.out.println();
            }
        }
    }
    
    public boolean ulozKnihu(String nazov)
	{
    	 Kniha kniha = prvkyDatabazy.get(nazov);
		try 
		{
				FileWriter fw = new FileWriter("Kniha.txt");
				BufferedWriter out = new BufferedWriter(fw);
				 if (kniha != null) 
				 {
		                out.write(kniha.getNázov() + "\n");
		                out.write(String.join(", ", kniha.getAutor()) + "\n");
		                if (kniha instanceof Román) 
		                {
		                    out.write("R: "+((Román) kniha).getŽáner() + "\n");
		                } 
		                else  if (kniha instanceof Učebnica) 
		                {
		                    out.write("U: "+((Učebnica) kniha).getRočnik() + "\n");
		                }
		                out.write(kniha.getRok() + "\n");
		                if(kniha.getVypozicanie().equals("Vypozicana"))
		                {
		                	out.write("N\n");
		                }
		                else
		                {
		                	out.write("Y\n");
		                }
		         } 
				 else 
				 {
		                out.write("null\n");
		         }
				out.close();
				fw.close();
		}
		catch (IOException e) 
		{
			System.out.println("Soubor sa nepodarilo vytvoriť");
			return false;
		}
		return true;
	}
    
    public boolean nacitajKnihu()
    {
        try 
        {
            FileReader fr = new FileReader("Kniha.txt");
            BufferedReader in = new BufferedReader(fr);
            String nazov = in.readLine();
            List<String> autori = new ArrayList<>();
            String[] autoriArray = in.readLine().split(", ");
            for (String autor : autoriArray) 
            {
                autori.add(autor);
            }
            String ZanerRocnik = in.readLine();
            int rok = Integer.parseInt(in.readLine());
            String vypozicanie = in.readLine();

            Kniha kniha;
            if (ZanerRocnik.startsWith("R")) 
            {
                String zaner = ZanerRocnik.substring(3);
                kniha = new Román(nazov, autori, rok, vypozicanie, zaner);
            } 
            else 
            {
                String rocnik = ZanerRocnik.substring(3);
                kniha = new Učebnica(nazov, autori, rok, vypozicanie, rocnik);
            }
            prvkyDatabazy.put(nazov, kniha);
            in.close();
            fr.close();
            System.out.println("Knihu sa podarilo načítať");
            System.out.println("Názov: " + kniha.getNázov());
            System.out.println("Autor/autori: " + kniha.getAutor());
            if (kniha instanceof Román) 
            {
                System.out.println("Žáner: " + ((Román) kniha).getŽáner());
            } 
            else if (kniha instanceof Učebnica) 
            {
                System.out.println("Odporúčaný ročník: " + ((Učebnica) kniha).getRočnik());
            }
            System.out.println("Rok vydania: " + kniha.getRok());
            System.out.println("Dostupnosť: " + kniha.getVypozicanie());
            System.out.println();
        } 
        catch (IOException e) 
        {
            System.out.println("Chyba pri čítaní zo suboru.");
            return false;
        }
        return true;
    }
}
