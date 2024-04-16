// co to udela
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Databaza {
    private Map<String, Kniha> prvkyDatabazy;

    public Databaza() 
    {
        prvkyDatabazy = new HashMap<>();
    }
    public boolean setRomán(String názov, List<String> autori, int rok, String vypozicanie, String žáner) 
    {
        if (prvkyDatabazy.put(názov, new Román(názov, autori, rok, vypozicanie, žáner)) == null)
            return true;
        else
            return false;
    }
    
    public boolean setUcebnica(String názov, List<String> autori, int rok, String vypozicanie, String ročnik) 
    {
        if (prvkyDatabazy.put(názov, new Učebnica(názov, autori, rok, vypozicanie, ročnik)) == null)
            return true;
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
