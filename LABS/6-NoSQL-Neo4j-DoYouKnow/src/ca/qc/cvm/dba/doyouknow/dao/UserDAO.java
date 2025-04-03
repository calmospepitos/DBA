package ca.qc.cvm.dba.doyouknow.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.StatementResult;
import org.neo4j.driver.types.Node;

public class UserDAO {
	private Session session;

	public UserDAO() {
		session = DBConnection.getConnection();

		StatementResult result = session.run("MATCH (n) RETURN COUNT(n) as nb");
		boolean mustCreate = result.next().get("nb").asInt() == 0;
 		if (mustCreate) { 		
 			Node fred = creerNoeud("Frédéric");
 			Node eric = creerNoeud("Éric");
 			Node paul = creerNoeud("Paul");
 			Node pierrePaul = creerNoeud("Pierre-Paul");
 			Node jeanChristophe = creerNoeud("Jean-Christophe");
 			Node marcel = creerNoeud("Marcel");
 			Node marcAndre = creerNoeud("Marc-André");
 			Node richard = creerNoeud("Richard");
 			Node alain = creerNoeud("Alain");
 			Node michelle = creerNoeud("Michelle");
 			Node jeanMarc = creerNoeud("Jean-Marc");
 			Node suzanne = creerNoeud("Suzanne");
 			
 			creerRelationConnaissance(fred, eric);
 			creerRelationConnaissance(eric,fred);
 			creerRelationConnaissance(eric,paul);
 			creerRelationConnaissance(paul,fred);
 			creerRelationConnaissance(paul,marcel);
 			creerRelationConnaissance(eric,jeanChristophe);
 			creerRelationConnaissance(eric,richard);
 			creerRelationConnaissance(jeanMarc,richard);
 			creerRelationConnaissance(jeanMarc,marcel);
 			creerRelationConnaissance(suzanne,marcel);
 			creerRelationConnaissance(michelle,marcel);
 			creerRelationConnaissance(marcAndre,eric);
 			creerRelationConnaissance(marcAndre,paul);
 			creerRelationConnaissance(fred,marcel);
 			creerRelationConnaissance(marcel,eric);
 			creerRelationConnaissance(eric,marcel);
 			
 			creerRelationPlusVieux(fred, marcAndre);
 			creerRelationPlusVieux(eric, fred);
 			creerRelationPlusVieux(marcel, eric);
 			creerRelationPlusVieux(marcel, paul);
 			creerRelationPlusVieux(paul, pierrePaul);
 			creerRelationPlusVieux(paul, jeanChristophe);
 			creerRelationPlusVieux(fred, richard);
 			creerRelationPlusVieux(fred, alain);
 			creerRelationPlusVieux(alain, michelle);
 			creerRelationPlusVieux(alain, jeanMarc);
 			creerRelationPlusVieux(alain, suzanne);
 		}
	}
	
	public Node creerNoeud(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		session.run("CREATE (a:Person {name: $name})", params);		
		return session.run("MATCH (a:Person) WHERE a.name = $name RETURN a", params).next().get("a").asNode();
	}
	
	public void creerRelationConnaissance(Node node1, Node node2) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p1", node1.id());
		params.put("p2", node2.id());
		session.run("MATCH (a:Person),(b:Person) WHERE id(a) = $p1 AND id(b) = $p2 CREATE (a)-[r:KNOWS]->(b)", params);
	}
	
	public void creerRelationPlusVieux(Node node1, Node node2) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p1", node1.id());
		params.put("p2", node2.id());
		session.run("MATCH (a:Person),(b:Person) WHERE id(a) = $p1 AND id(b) = $p2 CREATE (a)-[r:OLDER_THAN]->(b)", params);
	}
	
	/**
	 * Exécute une requête Cypher.
	 * 
	 * @param requete. Le RETURN de la requête doit retourner Name. Exemple : ... return n.name
	 */
	private List<String> executerRequeteCypher(String requete) {
		return this.executerRequeteCypher(requete, null);
	}
			
	private List<String> executerRequeteCypher(String requete, String nom) {
		List<String> result = new ArrayList<String>();
		Map<String, Object> params = new HashMap<String, Object>();
		
		if (nom != null) {
			params.put("p1", nom);
		}
		
		StatementResult queryResult = session.run(requete, params);
		
		while (queryResult.hasNext()) {
			Record record = queryResult.next();
			result.add(record.get(0).asString());
		}
		
		return result;
	}

	/**
	 * Chercher la liste de tous les usagers
	 * 
	 * @return La liste complète!
	 */
	public List<String> findAllUsers() {
		List<String> result = new ArrayList<String>();
		
		return result;
	}
	
	/**
	 * Pour l'usager, retourner toutes les personnes qu'il connait
	 * 
	 * @param user : nom de l'usager
	 * @return La liste de personnes qu'il connait
	 */
	public List<String> getDirectConnectionsOf(String user) {
		List<String> result = new ArrayList<String>();
		
		return result;
	}
	
	/**
	 * Retourner la liste des personnes qui sont connues de 2 personnes ou plus
	 * 
	 * @return liste des personnes populaires
	 */
	public List<String> getPopularUsers() {
		List<String> result = new ArrayList<String>();
		
		return result;
	}
	
	/**
	 * Si deux contacts directs d'une personne connaissent la même personne,
	 * mais que celui-ci ne la connait pas... pourquoi ne pas lui proposer?
	 * (i.e. Les amis de mes amis sont mes amis)
	 * 
	 * @param nom de la personne
	 * 
	 * @return liste de propositions
	 */
	public List<String> proposeConnection(String user) {
		List<String> result = new ArrayList<String>();
		
		return result;
	}
	
	/**
	 * Certaines personnes ne sont connues de personnes... qui?
	 * 
	 * @return les personnes qui sont méconnues
	 */
	public List<String> checkUnconnected() {
		List<String> result = new ArrayList<String>();
		
		return result;
	}
	
	/**
	 * Quelle est la personne la plus âgée?
	 * 
	 * @return nom
	 */
	public List<String> getOldest() {
		List<String> result = new ArrayList<String>();
		
		return result;
	}
}
