package orgProgramNewVersion;

import java.sql.*;
import java.util.*;


public class Main 
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		Databaza DatabazaKnih=new Databaza();
		int vyber, typ, index, rok;
		String názov, Vypozicanie, Spec;
		List<String> autori;
		boolean run=true;
		
		DatabazaKnih.pripojit();
       		DatabazaKnih.vytvorit_tabulku();
        	DatabazaKnih.nacitat_tabulku();
        	DatabazaKnih.nacitat_tabulku2();
		
		while(run)
		{
			System.out.println("Zvoľte požadovaný úkon:");
			System.out.println("1 --- Pridanie novej knihy");
			System.out.println("2 --- Úprava knihy");
			System.out.println("3 --- Zmazanie knihy");
			System.out.println("4 --- Úprava dostupnosti");
			System.out.println("5 --- Výpis všetkých kníh v databáze");
			System.out.println("6 --- Výpis informácií o konkrétnej knihe");
			System.out.println("7 --- Výpis knih od konkrétneho autora knihe");
			System.out.println("8 --- Výpis knih podla zadaneho žánru");
			System.out.println("9 --- Výpis všetkých vypožičaných kníh podla typu");
			System.out.println("10 --- Ulož knihu do suboru");
			System.out.println("11 --- Načítaj knihu zo suboru");
			System.out.println("12 --- Ulož aktualnu databazu do SQL databazy");
			System.out.println("13 --- Ukončenie programu a uloženei do SQL databazy");
			System.out.println("14 --- Nacteni");

			vyber = sc.nextInt();
			switch (vyber)
			{
			case 1:
				System.out.println("Vyberte typ knihy (1--> Román; 2-->Učebnica)");
				typ = sc.nextInt();
				if (typ ==1)
				{
					System.out.println("Zadajte názov románu: ");
					názov=sc.next();
					System.out.println("Zadajte meno autora/autorov (oddelte čiarkami): ");
					sc.nextLine();
					autori = Arrays.asList(sc.nextLine().split(","));
					System.out.println("Zadajte žáner románu: ");
					Spec=sc.next();
					System.out.println("Zadajte rok vydania knihy: ");
					rok=sc.nextInt();
					System.out.println("Zvoľte stav dostupnosti (Y/N): ");
					Vypozicanie=sc.next();
					if (!DatabazaKnih.setRomán(názov,autori,rok,Vypozicanie,Spec))
						System.out.println("Kniha v databáze už existuje");
					break;
				}
				else
				{
					System.out.println("Zadajte názov učebnice: ");
					názov=sc.next();
					System.out.println("Zadajte meno autora/autorov (oddelte čiarkami: ");
					sc.nextLine();
					autori = Arrays.asList(sc.nextLine().split(","));
					System.out.println("Zadajte vhodný ročník: ");
					Spec=sc.next();
					System.out.println("Zadajte rok vydania knihy: ");
					rok=sc.nextInt();
					System.out.println("Zvoľte stav dostupnosti (Y/N): ");
					Vypozicanie=sc.next();
					if (!DatabazaKnih.setUcebnica(názov,autori,rok,Vypozicanie,Spec))
						System.out.println("Kniha v databáze už existuje");
					break;
				}
			case 2:
				System.out.println("Zadajte názov knihy, ktorú chcete upraviť");
				názov=sc.next();
				System.out.println("Zadajte nové meno autora/autorov (oddelte čiarkami): ");
				sc.nextLine();
				autori = Arrays.asList(sc.nextLine().split(","));
				System.out.println("Zadajte nový rok vydania knihy: ");
				rok=sc.nextInt();
				System.out.println("Zvoľte nový stav dostupnosti: ");
				Vypozicanie=sc.next();
				DatabazaKnih.upravaKnihy(názov,autori,rok,Vypozicanie);
				break;
			case 3:
				System.out.println("Zadajte názov knihy, ktorú chcete odstraniť");
				názov=sc.next();
				DatabazaKnih.odstranenieKnihy(názov);
				break;
			case 4:
				System.out.println("Zadajte názov knihy, ktorej chcete zmeniť dostupnosť");
				názov=sc.next();
				System.out.println("Zmeňte dostupnosť (Y-Vrátená, N-Vypožičaná");
				Vypozicanie=sc.next();
				DatabazaKnih.zmenaDostupnosti(názov, Vypozicanie);
			case 5:
				DatabazaKnih.vypisDatabazy();
				break;
			case 6:
				System.out.println("Zadajte názov knihy, pre vypísanie informácií");
				názov=sc.next();
				DatabazaKnih.vypisKnihy(názov);
				break;
			case 7:
				System.out.println("Zadajte meno autora, pre vypísanie informácií");
				názov=sc.next();
				DatabazaKnih.vypisKnihAutora(názov);
				break;
			case 8:
				System.out.println("Zadajte žáner kníh, ktoré chcete vypísať");
				názov=sc.next();
				DatabazaKnih.vypisZanru(názov);
				break;
			case 9:
				DatabazaKnih.vypisDostupnosti();
				break;
			case 10:
				System.out.println("Zadajte názov knihy, ktorú chcete uložiť do súboru");
				názov=sc.next();
				DatabazaKnih.ulozKnihu(názov);
				break;
			case 11:
				DatabazaKnih.nacitajKnihu();
			case 12:
				//Vloženie do databazy ----->Poupravuj, čo sa ti nepáči, nezdá, pridaj ošetrenie voči chybám ;* (mehehehe)
				break;
			case 13:
				System.out.println("Dovidenia.");
				run=false;
				break;
			case 14:
				DatabazaKnih.vypis_knih(DatabazaKnih.nacitat_tabulku());
				DatabazaKnih.vypis_knih2(DatabazaKnih.nacitat_tabulku2());
				break;
				}
			}
		DatabazaKnih.odpojit();
					
		}
			
}
