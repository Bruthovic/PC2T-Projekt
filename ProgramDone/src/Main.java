import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int vyber, rok, moznost = 0;
		boolean run = true;
		String nazev, autor, typ, spec, vypujceni, zmena;
		Kniha info;
		
		
		Databaze DatabazeKnih = new Databaze();
		DatabazeKnih.pripojit();
		DatabazeKnih.vytvorit_tabulku();
		DatabazeKnih.nacistZTabulky();
		
		while(run) {
			System.out.println("-------------- Menu --------------");
			System.out.println("1 --- Pridani nove knihy");
			System.out.println("2 --- Uprava knihy");
			System.out.println("3 --- Smazani knihy");
			System.out.println("4 --- Stav dostupnosti");
			System.out.println("5 --- Vypis vsech knih z databaze");
			System.out.println("6 --- Vypis informaci o konkretni knize");
			System.out.println("7 --- Vypis knih od konkretniho autora knihy");
			System.out.println("8 --- Vypis knih podle zadaneho zanru nebo rocniku");
			System.out.println("9 --- Vypis vsech vypujcenych knih podle typu");
			System.out.println("10 --- Uloz knihu do souboru");
			System.out.println("11 --- Nacti knihu ze souboru");
			System.out.println("12 --- Ukonceni programu a ulozeni do SQL databazy");
			System.out.println("Zadejte volbu: ");
			Scanner scanner = new Scanner(System.in);
			boolean validInput = false;
			
			
			do {
	            try {
	                moznost = scanner.nextInt();
	                if((moznost > 0) && (moznost < 13)) {
	                	 validInput = true;
	                }
	                else System.out.println("Neplatny vstup. Zadejte znovu pozadovanou volbu: ");
	                
	            } catch (InputMismatchException e) {
	                
	                System.out.println("Neplatny vstup. Zadejte znovu pozadovanou volbu: ");
	                scanner.nextLine(); 
	            }
	        } while (!validInput);
			
			validInput = false;
			
			switch(moznost) {
				case 1:
					do {
					    try {
					        System.out.println("Vyberte typ knihy (1--> Roman; 2--> Ucebnica)");
					        moznost = sc.nextInt();
					    } catch (InputMismatchException e) {
					        System.out.println("Neplatny vstup.");
					        sc.next();
					        moznost = 0; 
					    }
					} while (moznost < 1 || moznost > 2);
					if(moznost == 1) {
						
						sc.nextLine();
						System.out.println("Zadajte nazev romanu: ");
						System.out.println();
						nazev=sc.nextLine();
						
						System.out.println("Zadajte jmeno autora/autoru (oddelte carkami): ");
						autor = sc.nextLine();
						scanner.nextLine();
						System.out.println();
						typ = "roman";
						System.out.println("Zadajte zanr romanu [detektivni/sci-fi/fantasy/hororovy/dobrodruzny]: ");
				        do {
				            spec = scanner.next().toLowerCase();
				            if (!DatabazeKnih.platnyZanr(spec)) {
				                System.out.println("Neplatn    nr. Zadajte znovu: ");
				            }
				        } while (!DatabazeKnih.platnyZanr(spec));
						System.out.println("Zadajte rok vydani knihy: ");
						do {
							rok = sc.nextInt();
							if(rok>2024) System.out.println("Nezadal jsi platny rok. Zadej ho znovu: ");
						}while(rok>2024);
						
						vypujceni = "dostupna";
						if (!DatabazeKnih.setKniha(nazev,autor, typ,rok,spec,vypujceni))
							System.out.println("Kniha v databaze uz existuje");
					}
					else {
						System.out.println("Zadajte nazev Ucebnice: ");
						nazev=sc.next();
						System.out.println("Zadajte jmeno autora/autoru (oddelte carkami): ");
						autor = sc.next();
						typ = "ucebnice";
						System.out.println("Zadajte pro jaky rocnik je kniha vhodna [format 1-9: napr. -> 1.rocnik]: ");
						do {
				            spec = scanner.next().toLowerCase();
				            if (!DatabazeKnih.platnyRocnik(spec)) {
				                System.out.println("Neplatn  rocnik. Zadajte znovu: ");
				            }
				        } while (!DatabazeKnih.platnyRocnik(spec));
						System.out.println("Zadajte rok vydani knihy: ");
						do {
							rok = sc.nextInt();
							if(rok>2024) System.out.println("Nezadal jsi platny rok. Zadej ho znovu: ");
						}while(rok>2024);
						
						vypujceni = "dostupna";
						if (!DatabazeKnih.setKniha(nazev,autor, typ,rok,spec,vypujceni))
							System.out.println("Kniha v databaze uz existuje");
					}
					break;
				case 2:
					System.out.println("Napis nazev knihy, kterou chcete upravit: ");
					nazev = sc.next();
					info = null;
					zmena = "";
					info = DatabazeKnih.getKniha(nazev);
					if(info != null) {
						System.out.println("Zadejte nove jmeno autora/autoru (oddelte carkami): ");
						autor = sc.next();
						System.out.println("Zadejte novy rok vydani knihy: ");
						do {
							rok = sc.nextInt();
							if(rok>2024) System.out.println("Nezadal jsi platny rok. Zadej ho znovu: ");
						}while(rok>2024);
						System.out.println("Kniha je " + info.getVypujceni() + ". Chcete zmenit jeji dostupnost? [Y/N]");
						zmena = sc.next();
						if(zmena.equals("Y")) {
							if(DatabazeKnih.zmenaDostupnosti(nazev)) {
								System.out.println("Dostupnost u knihy byla uspesne zmenena.");
							}else System.out.println("Neco se nepovedlo.");
						}
						else System.out.println("Dostupnost knihy byla zachovana.");
						info.setAutor(autor);
						info.setRok(rok);
					}
					else System.out.println("Kniha neni v databazi. Navrat do menu.");
					break;
				case 3:
					System.out.println("Zadejte nazev knihy, kterou chcete odstranit: ");
					nazev = sc.next();
					if(DatabazeKnih.vymazKnihu(nazev)) {
						System.out.println("Kniha byla uspesne odstranena.");
					}
					else System.out.println("Kniha neni v databazi. Navrat do menu.");
					break;
				case 4:
					System.out.println("Zadejte nazev knihy, u ktere chcete zmenit dostupnost: ");
					nazev = sc.next();
					info = null;
					zmena = "";
					info = DatabazeKnih.getKniha(nazev);
					if (info != null) {
					    System.out.println("Kniha je " + info.getVypujceni() + ". Chcete zmenit jeji dostupnost? [Y/N]");
						zmena = sc.next();
						if(zmena.equals("Y")) {
							if(DatabazeKnih.zmenaDostupnosti(nazev)) {
								System.out.println("Dostupnost u knihy byla uspesne zmenena.");
							}else System.out.println("Kniha v databaze neexistuje.");
						}
						else if(zmena.equals("N")) System.out.println("Dostupnost knihy se nezmenila.");
					    
					} else {
					    // Handle the case where the info object is null
					    System.out.println("Kniha s n zvem '" + nazev + "' nebyla nalezena v datab zi.");
					}
					break;
				case 5:
					System.out.println("Vypis knih: "); 
					DatabazeKnih.vypisDatabaze();
					break;
				case 6:
					System.out.println("Zadejte nazev knihy: "); 
					nazev=sc.next();
					info = null;
					info = DatabazeKnih.getKniha(nazev);
					if(info != null) {
						System.out.println("Autor: " + info.getAutor());
						System.out.println("Zaradeni: " + info.getSpec());
						System.out.println("Rok vydani: " + info.getRok_Vydani());
						System.out.println("Stav vypujceni: " + info.getVypujceni());
					} else System.out.println("Kniha neexistuje.");
					break;
				case 7:
					
					System.out.println("Zadejte jmeno autora: ");
					autor=sc.next();
				
					List<Kniha> booksByAuthor = DatabazeKnih.getDila(autor);

					if (booksByAuthor.isEmpty()) {
					    System.out.println("Nenasli jsme zadne knihy u tohoto autora: " + autor);
					} else {
					    System.out.println("Books by author " + autor + ":");
					    for (Kniha book : booksByAuthor) {
					        System.out.println("Nazev: " + book.getNazev() + ", Autor: " + book.getAutor() + ", Spec: " + book.getSpec() + ", Rok vydani: " + book.getRok_Vydani());
					    }
					}
					
					break;
				case 8:
					System.out.println("Zadejte zanr nebo rocnik: ");
					do {
			            spec = scanner.next().toLowerCase();
			            if ((!DatabazeKnih.platnyZanr(spec)) && (!DatabazeKnih.platnyRocnik(spec))) {
			                System.out.println("Neplatn    nr nebo rocnik. Zadajte znovu: ");
			            }
			        } while ((!DatabazeKnih.platnyZanr(spec)) && (!DatabazeKnih.platnyRocnik(spec)));
					
					List<Kniha> knihyPodleZanru = DatabazeKnih.getDila_podleZanru(spec); 
					
					if(knihyPodleZanru.isEmpty()) {
						System.out.println("Nenasli jsme zadnou knihu v tomto zanru.");
					}else {
						System.out.println("Knihy tohoto zanru: ");
						for(Kniha knihy : knihyPodleZanru) {
							System.out.println(knihy.getNazev());
						}
					}
					break;
				case 9:
					System.out.println("Vypis knih podle Romanu/Ucebnice: ");
					DatabazeKnih.vypisDatabazePodleTypu();
					break;
				case 10:
					info=null;
					System.out.println("Zadej nazev knihy: ");
					nazev = sc.next();
					info = DatabazeKnih.getKniha(nazev);
					if(info != null) {
						DatabazeKnih.ulozitDoSouboru(nazev);
					}else System.out.println("Knihu nemuzeme ulozit do souboru, protoze neexistuje.");
					break;
				case 11:
					info=null;
					System.out.println("Zadej nazev knihy, ze ktere chcete vypsat udaje: ");
					nazev = sc.next();
					info = DatabazeKnih.getKniha(nazev);
					if(info != null) {
						DatabazeKnih.nacistZeSouboru(nazev);
					}else System.out.println("Kniha neexistuje");
					break;
				case 12: 	
					System.out.println("Ulozeni databaze: ");
					DatabazeKnih.ulozit_do_tabulky();
					System.exit(0);				
			}
		}
		DatabazeKnih.odpojit();
	}

}
