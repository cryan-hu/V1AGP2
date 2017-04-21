package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import model.persoon.Student;
import model.presentie.Afwezigheid;
import model.presentie.Presentie;
import model.presentie.Presentie;
import model.vakGegevens.Les;
import server.Conversation;
import server.Handler;

public class AbsentieController implements Handler {
	private PrIS informatieSysteem;

	/**
	 * Deze methode haalt eerst de opgestuurde JSON-data op. Daarna worden de
	 * benodigde gegevens uit het domeinmodel gehaald. Deze gegevens worden dan
	 * weer omgezet naar JSON en teruggestuurd naar de Polymer-GUI!
	 * 
	 * @param conversation
	 *          - alle informatie over het request
	 */

	public AbsentieController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	private void opslaan(Conversation conversation) throws ParseException, IOException {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		JsonArray studenten = lJsonObjectIn.getJsonArray("studenten");
		JsonArray les = lJsonObjectIn.getJsonArray("les");
		String username = lJsonObjectIn.getString("username");
		JsonObjectBuilder lJsonObjectTerug = Json.createObjectBuilder();

		DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");

		ArrayList<String> lessenAS = new ArrayList<String>();
		for (Object o : les) {
			lessenAS.add(o.toString().replace("\"", ""));
		}
		ArrayList<String> studentenAS = new ArrayList<String>();
		for (Object o : studenten) {
			studentenAS.add(o.toString());
		}

		String lesDatum = lessenAS.get(0);
		String lesBeginDatumTijdString = lesDatum + " " + lessenAS.get(1);
		String lesEindDatumTijdString = lesDatum + " " + lessenAS.get(2);
		Date lesBeginTijd = format1.parse(lesBeginDatumTijdString);
		Date lesEindTijd = format1.parse(lesEindDatumTijdString);
		ArrayList<Presentie> presenties = informatieSysteem.getPresenties();
		for (int i = 0; i < studenten.size(); i++) {
			JsonObject lGroepMember_jsonObj = studenten.getJsonObject(i);

				int lStudentNummer = lGroepMember_jsonObj.getInt("id");
				Student s = informatieSysteem.getStudent(lStudentNummer);
				String studentuser = s.getGebruikersnaam();
				Presentie p = new Presentie(studentuser, lesBeginTijd, lesEindTijd, lessenAS.get(4), lessenAS.get(3),
						lessenAS.get(5), lessenAS.get(6));
				Boolean aangeklikt = false;
				try{
					aangeklikt = lGroepMember_jsonObj.getBoolean("sameGroup");

				} catch(Exception e){
				}
			if (aangeklikt) {
				if(!presenties.contains(p)){
					presenties.add(p);
				}
			}
			else{
				if(presenties.contains(p)){
					System.out.println(!presenties.contains(p));
					presenties.remove(p);
		      }
			}
		}

		
		
		
		writeData(presenties);

		String berichtTerug = "Absentie Studenten Opgeslagen";
		lJsonObjectTerug.add("terugString", berichtTerug);
		String terug = lJsonObjectTerug.build().toString();
		conversation.sendJSONMessage(terug);

	}



	private void writeData(ArrayList<Presentie> presenties) throws IOException, ParseException {
			FileWriter fw = new FileWriter("CSV/absenties.csv");
			PrintWriter pw = new PrintWriter(fw);
			for (Presentie a : presenties) {
				pw.println(a.toString());
			}
			pw.close();
	}

	private void ophalen(Conversation conversation) throws ParseException, IOException {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String usernameStudent = lJsonObjectIn.getString("usernameStudent");

		ArrayList<Presentie> presenties = informatieSysteem.getPresenties();
		ArrayList<Presentie> presentiesStudent = new ArrayList<Presentie>();
		JsonArrayBuilder lJsonArrayPresenties = Json.createArrayBuilder();
		JsonObjectBuilder lTotaal = Json.createObjectBuilder();
		
		Collections.sort(presentiesStudent, new Comparator<Presentie>() {
			public int compare(Presentie m1, Presentie m2) {
				return m1.getStartDatum().compareTo(m2.getEindDatum());
			}
		});
		
		for(Presentie q : presenties){
			System.out.println(q.getUsername() + "  " +  usernameStudent);
			System.out.println(q.getUsername().equals(usernameStudent));
			if(q.getUsername().equals(usernameStudent)){
				String datum = new SimpleDateFormat("dd-MM-yyyy").format(q.getEindDatum());
				String startTijd = new SimpleDateFormat("HH:mm").format(q.getStartDatum());
				String eindTijd = new SimpleDateFormat("HH:mm").format(q.getEindDatum());	
  			JsonObjectBuilder lJob =	Json.createObjectBuilder(); 
    			lJob
    			.add("datum", datum)
    			.add("startTijd", startTijd)
    			.add("eindTijd", eindTijd)
    			.add("vak", q.getVak())
    			.add("klas", q.getKlas())
    			.add("lokaal", q.getLokaal())
    			.add("docent", q.getDocent());
    			lJsonArrayPresenties.add(lJob);
			}
		}


		lTotaal.add("presenties", lJsonArrayPresenties);

		// nothing to return use only errorcode to signal: ready!
		String lJsonOutStr = lTotaal.build().toString();
		conversation.sendJSONMessage(lJsonOutStr); // terug naar de Polymer-GUI!
	}
	
	private void getAbsentie(Conversation conversation) throws ParseException, IOException {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String usernameStudent = lJsonObjectIn.getString("usernameStudent");

		ArrayList<Presentie> presenties = informatieSysteem.getPresenties();
		ArrayList<Presentie> presentiesStudent = new ArrayList<Presentie>();
		JsonArrayBuilder lJsonArrayPresenties = Json.createArrayBuilder();
		JsonObjectBuilder lTotaal = Json.createObjectBuilder();
		
		Collections.sort(presentiesStudent, new Comparator<Presentie>() {
			public int compare(Presentie m1, Presentie m2) {
				return m1.getStartDatum().compareTo(m2.getEindDatum());
			}
		});
		
		for(Presentie q : presenties){
			System.out.println(q.getUsername() + "  " +  usernameStudent);
			System.out.println(q.getUsername().equals(usernameStudent));
			if(q.getUsername().equals(usernameStudent)){
				String datum = new SimpleDateFormat("dd-MM-yyyy").format(q.getEindDatum());
				String startTijd = new SimpleDateFormat("HH:mm").format(q.getStartDatum());
				String eindTijd = new SimpleDateFormat("HH:mm").format(q.getEindDatum());	
  			JsonObjectBuilder lJob =	Json.createObjectBuilder(); 
    			lJob
    			.add("datum", datum)
    			.add("startTijd", startTijd)
    			.add("eindTijd", eindTijd)
    			.add("vak", q.getVak())
    			.add("klas", q.getKlas())
    			.add("lokaal", q.getLokaal())
    			.add("docent", q.getDocent());
    			lJsonArrayPresenties.add(lJob);
			}
		}


		lTotaal.add("presenties", lJsonArrayPresenties);

		// nothing to return use only errorcode to signal: ready!
		String lJsonOutStr = lTotaal.build().toString();
		conversation.sendJSONMessage(lJsonOutStr); // terug naar de Polymer-GUI!
	}

	@Override
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/docent/absentie/opslaan")) {
			try {
				opslaan(conversation);
			} catch (ParseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(conversation.getRequestedURI().startsWith("/absentie/ophalen")) {
			try {
				getAbsentie(conversation);
			} catch (ParseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				ophalen(conversation);
			} catch (ParseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}