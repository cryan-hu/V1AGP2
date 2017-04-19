package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import model.persoon.Student;
import model.presentie.Afwezigheid;

import server.Conversation;
import server.Handler;

public class AfwezigheidsController implements Handler {
	private PrIS informatieSysteem;

/**
	 * Deze methode haalt eerst de opgestuurde JSON-data op. Daarna worden
	 * de benodigde gegevens uit het domeinmodel gehaald. Deze gegevens worden
	 * dan weer omgezet naar JSON en teruggestuurd naar de Polymer-GUI!
	 * 
	 * @param conversation - alle informatie over het request
	 */
	
	public AfwezigheidsController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}
	private void opslaan(Conversation conversation) throws ParseException, IOException {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();		
    JsonArray lineItems = lJsonObjectIn.getJsonArray("afwezigheden");
  	JsonObjectBuilder lJsonObjectTerug = Json.createObjectBuilder();
    String useCase = lJsonObjectIn.getString("useCase");
    String useCaseVoor = useCase;
    String berichtTerug = null;
		if(useCase.equals("afwezigheidVerwijderen")){
			useCaseVoor = "afwezigMelden";
		}
		if(useCase.equals("beterMelden")){
			useCaseVoor = "beterMelden";
		}
    String username = lJsonObjectIn.getString("username");
    DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");     
  	ArrayList<Afwezigheid> afwezigheden = getAfwezigheden();
  	
  	

    ArrayList<String> lessenAS = new ArrayList<String>();    
    for (Object o : lineItems) { 
    	lessenAS.add(o.toString());
    }
    System.out.println(useCase + " voor " + username);
    
    for(String s : lessenAS){
    	String[] element = s.split("\",\"");
    	String lesDatum = element[0].replace("[","").replace("\"", "");
    	String lesBeginDatumTijdString = lesDatum+" "+element[2];
			String lesEindDatumTijdString = lesDatum+" "+element[3];	
			String docent = element[6].replace("]","").replace("\"", "");

			
			Date lesBeginTijd = format1.parse(lesBeginDatumTijdString);
			Date lesEindTijd = format1.parse(lesEindDatumTijdString);
			
    	Afwezigheid afwezigheidStudent = new Afwezigheid(useCaseVoor, lesBeginTijd, lesEindTijd, username, element[1], element[4],element[5], docent);
    	
    	if(useCase.equals("ziekMelden") || useCase.equals("afwezigMelden")){	//ziekMelden en afwezigMelden useCase
    		if(!afwezigheden.contains(afwezigheidStudent)){
    			afwezigheden.add(afwezigheidStudent);
    			berichtTerug = "Afwezigheid/ziekmelding succesvol toegevoegd!";
    		}
    		else{
    			berichtTerug = "Er bestaat al eenzelfde afwezigheid/ziekmelding!";
    		}
    	}	else if(useCase.equals("afwezigheidVerwijderen")){					//afwezigheidVerwijderen useCase
    		if(afwezigheden.contains(afwezigheidStudent)){
    			berichtTerug = "Afwezigheid succesvol verwijdert!";
    			afwezigheden.remove(afwezigheidStudent);
    		} else {
    			berichtTerug = "Geen afwezigheid om te verwijderen!";
    		}
    	}
    	
    
    	
    	
    }    
  
    if(useCase.equals("beterMelden")){                                    //beterMelden useCase
      Iterator<Afwezigheid> it = afwezigheden.iterator();
      while (it.hasNext()) {
      		Afwezigheid i = it.next();
          if (i.getUseCase().equals("ziekMelden") && i.getUsername().equals(username)) {
             it.remove();
          }
      }
      berichtTerug = "Je bent nu beter gemeld, alle ziekmeldingen verwijdert!";
    }

    try {
  		writeData(afwezigheden);
  	} catch (IOException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}
    
    lJsonObjectTerug.add("terugString", berichtTerug);
    String terug = lJsonObjectTerug.build().toString();		
    conversation.sendJSONMessage(terug);
  	



		
	}
	
	private ArrayList<Afwezigheid> getAfwezigheden() throws ParseException, IOException {
		FileReader fr = new FileReader("CSV/afwezigheden.csv");
		BufferedReader br = new BufferedReader(fr);
  	ArrayList<Afwezigheid> afwezigBestaand = new ArrayList<Afwezigheid>();
  	String regel = br.readLine();
		while(regel != null){
			String[] values = regel.split(",");
			Date lesBeginTijd = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK).parse(values[1]);
			Date lesEindTijd = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK).parse(values[2]);
			Afwezigheid b = new Afwezigheid(values[0],lesBeginTijd,lesEindTijd,values[3],values[4],values[5],values[6],values[7]);
			afwezigBestaand.add(b);
			regel = br.readLine();
		}
		br.close();
		return afwezigBestaand;
	}
	private void writeData(ArrayList<Afwezigheid> afwezigheden) throws IOException, ParseException{		
		
		FileWriter fw = new FileWriter("CSV/afwezigheden.csv");
		PrintWriter pw = new PrintWriter(fw);
		for(Afwezigheid a : afwezigheden){
			pw.println(a.toString());
		}
		pw.close();		
	}


	private void ophalen(Conversation conversation) throws ParseException, IOException {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		
		ArrayList<Afwezigheid> afwezigheden = getAfwezigheden();
		JsonArrayBuilder lJsonArrayZiektes = Json.createArrayBuilder();
		JsonArrayBuilder lJsonArrayAfwezigheden = Json.createArrayBuilder();
		JsonObjectBuilder lTotaal =	Json.createObjectBuilder(); 
		
		
		Collections.sort(afwezigheden, new Comparator<Afwezigheid>() {
	    public int compare(Afwezigheid m1, Afwezigheid m2) {
	        return m1.getBeginTijd().compareTo(m2.getEindTijd());
	    }
		});
		
		for(Afwezigheid a : afwezigheden){
			if(a.getUsername().equals(lGebruikersnaam)){
				String datum = new SimpleDateFormat("dd-MM-yyyy").format(a.getBeginTijd());
				String startTijd = new SimpleDateFormat("HH:mm").format(a.getBeginTijd());
				String eindTijd = new SimpleDateFormat("HH:mm").format(a.getEindTijd());	
  			if(a.getUseCase().equals("ziekMelden")){
  			JsonObjectBuilder lJob =	Json.createObjectBuilder(); 
    			lJob
    			.add("datum", datum)
    			.add("startTijd", startTijd)
    			.add("eindTijd", eindTijd)
    			.add("vak", a.getVak())
    			.add("klas", a.getKlas())
    			.add("lokaal", a.getLokaal())
    			.add("docent", a.getDocent());
    			lJsonArrayZiektes.add(lJob);
  			}
  			if(a.getUseCase().equals("afwezigMelden")){
  			JsonObjectBuilder lJob =	Json.createObjectBuilder(); 
    			lJob
    			.add("datum", datum)
    			.add("startTijd", startTijd)
    			.add("eindTijd", eindTijd)
    			.add("vak", a.getVak())
    			.add("klas", a.getKlas())
    			.add("lokaal", a.getLokaal())
    			.add("docent", a.getDocent());
    			lJsonArrayAfwezigheden.add(lJob);
  			}
			}
		}
  	
  	
  	
		lTotaal.add("ziektes", lJsonArrayZiektes);
		lTotaal.add("afwezigheden", lJsonArrayAfwezigheden);
		
   	//nothing to return use only errorcode to signal: ready!
  	String lJsonOutStr = lTotaal.build().toString();
 		conversation.sendJSONMessage(lJsonOutStr);					// terug naar de Polymer-GUI!
	}

	@Override
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/student/useCase/ophalen")) {
			try {
				opslaan(conversation);
			} catch (ParseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				ophalen(conversation);
			} catch (ParseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}