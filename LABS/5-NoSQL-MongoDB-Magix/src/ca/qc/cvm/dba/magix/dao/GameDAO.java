package ca.qc.cvm.dba.magix.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import ca.qc.cvm.dba.magix.entity.Card;

import static java.lang.Math.round;

public class GameDAO {
	/**
	 * Méthode retournant le nombre de parties jouées
	 * @return nombre
	 */
	public static long getGameCount() {
		MongoDatabase connection = DBConnection.getConnection();
		MongoCollection<Document> collection = connection.getCollection("parties");
		long count = 0;

		try {
			count = collection.countDocuments();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}
	
	/**
	 * Permet d'obtenir des informations sur les dernières parties jouées
	 * 
	 * @param numberOfResults Nombre de parties à retourner
	 * @return Une liste contenant les informations des dernières parties jouées
	 */
	public static List<String> getLatestGamesResults(int numberOfResults) {
		MongoDatabase connection = DBConnection.getConnection();
		MongoCollection<Document> collection = connection.getCollection("parties");
		final List<String> results = new ArrayList<String>();
		Document orderBy = new Document("time", -1);
		FindIterable<Document> iterator = collection.find().sort(orderBy).limit(numberOfResults);

		try {
			MongoCursor<Document> cursor = iterator.cursor();

			while (cursor.hasNext()) {
				Document doc = cursor.next();

				// Temps
				long time = doc.getLong("time");
				Date date = new Date(time);
				String formattedDate = date.toString();

				// Gagnant
				int gagnant = doc.getInteger("gagnant");
				String formattedGagnant = "";
				if (gagnant == 1) {
					formattedGagnant = "Joueur";
				}
				else if (gagnant == 2) {
					formattedGagnant = "IA";
				}
				else {
					formattedGagnant = "--";
				}

				// Rounds
				int rounds = doc.getInteger("totalRounds");

				// Insert
				String result = String.format("%-20s\tGagné par : %-10s\t\t%-2s tours", formattedDate, formattedGagnant, rounds);
				results.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}
	
	/**
	 * Permet de savoir, pour le joueur, le nombre de parties 
	 * qui ont été gagnées avec cette carte dans leur sélection.
	 * 
	 * Cette méthode ne doit pas vérifier pour l'IA, seulement le vrai joueur
	 * 
	 * @param c Carte à vérifier
	 * @return Le nombre de parties
	 */
	private static long getNumberOfWonGames(Card c) {
		MongoDatabase connection = DBConnection.getConnection();
		MongoCollection<Document> collection = connection.getCollection("parties");
		long count = 0;

		try {
			String nomCarte = c.getName();
			Document where = new Document("listePlayer", nomCarte);
			where.append("gagnant", 1);
			count = collection.countDocuments(where);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}
	
	/**
	 * Nombre de tours moyen avant que la partie se complète.
	 * 
	 * @return la moyenne de rondes par partie
	 */
	public static double getAverageRounds() {
		MongoDatabase connection = DBConnection.getConnection();
		MongoCollection<Document> collection = connection.getCollection("parties");
		double avg = 0;

		try {
			Document group = new Document(
				"$group", new Document("_id", null).append(
						"total", new Document("$avg",  "$totalRounds")
				)
			);

			List<Document> liste = new ArrayList<Document>();
			liste.add(group);

			AggregateIterable<Document> iterable = collection.aggregate(liste);
			MongoCursor<Document> cursor = iterable.cursor();

			if (cursor.hasNext()) {
				avg = cursor.next().getDouble("total");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return avg;
	}
	
	/**
	 * Cette méthode est appelée lorsqu'une partie se termine.
	 * 
	 * @param playerCards Les cartes choisies par l'usager
	 * @param aiCards Les cartes choisies par l'IA (adversaire)
	 * @param playerLife La vie restante du joueur à la fin de la partie
	 * @param aiLife La vie restante de l'IA à la fin de la partie
	 * @param totalRounds Le nombre de tours joués avant la fin de la partie
	 */
	public static void logGame(List<Card> playerCards, List<Card> aiCards, int playerLife, int aiLife, int totalRounds) {
		try {
			MongoDatabase connection = DBConnection.getConnection();
			MongoCollection<Document> collection = connection.getCollection("parties");
			int resultat = 0;

			List<String> nomPlayer = new ArrayList<String>();
			for(int i = 0; i < playerCards.size(); i++) {
				nomPlayer.add(playerCards.get(i).getName());
			}
			List<String> nomAi = new ArrayList<String>();
			for(int i = 0; i < aiCards.size(); i++){
				nomAi.add(aiCards.get(i).getName());
			}

			if (playerLife > aiLife) {
				resultat = 1;
			}
			else if (playerLife < aiLife) {
				resultat = 2;
			}
			else {
				resultat = 0;
			}

			Document doc = new Document();
			doc.append("time", System.currentTimeMillis());
			doc.append("playerLife", playerLife);
			doc.append("aiLife", aiLife);
			doc.append("totalRounds", totalRounds);
			doc.append("listePlayer", nomPlayer);
			doc.append("listeAI", nomAi);
			doc.append("gagnant", resultat);

			collection.insertOne(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet de savoir pour chaque carte combien de fois elle furent
	 * présente lorsque l'usager a gagné.
	 * 
	 * @param collection de cartes du jeu
	 * @return Une liste d'objets, où chaque object est similaire à : {"Wolf", 3},
	 * ce qui signifie que la carte "Wolf" a été présente 3 fois dans les parties gagnées du joueur
	 */
	public static List<Object[]> getCardRankings(List<Card> collection) {
		List<Object[]> rankings = new ArrayList<Object[]>();
		
		for (Card c : collection) {
			rankings.add(new Object[]{c.getName(), getNumberOfWonGames(c)});
		}
		
		return sortCardsByRanking(rankings);
	}	
	
	/**
	 * Méthode permettant de trier les objets en ordre décroissant
	 * 
	 * Exemple de liste valide:
	 * rankings.get(0) contient : Object[]{"Wolf", 4}
	 * rankings.get(1) contient : Object[]{"Dragon", 2}
	 * rankings.get(2) contient : Object[]{"Swift", 5}
	 * 
	 * Ceci retournera
	 * rankings.get(0) contient : Object[]{"Swift", 5}
	 * rankings.get(1) contient : Object[]{"Wolf", 4}
	 * rankings.get(2) contient : Object[]{"Dragon", 2}
	 * 
	 * @param rankings à trier
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<Object[]> sortCardsByRanking(List<Object[]> rankings) {
		Collections.sort(rankings, new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				int result = 0;
				
				if ((long)(((Object[])o1)[1]) < (long)(((Object[])o2)[1])) {
					result = 1;
				}
				else if ((long)(((Object[])o1)[1]) > (long)(((Object[])o2)[1])) {
					result = -1;
				}
				return result;
			}
			
		});
		
		return rankings;
	}
}
