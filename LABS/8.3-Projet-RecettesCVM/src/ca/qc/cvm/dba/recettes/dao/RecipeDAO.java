package ca.qc.cvm.dba.recettes.dao;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.cvm.dba.recettes.entity.Recipe;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import org.bson.types.ObjectId;
import org.bson.Document;
import com.mongodb.client.result.UpdateResult;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import ca.qc.cvm.dba.recettes.entity.Ingredient;
import ca.qc.cvm.dba.recettes.entity.Recipe;

public class RecipeDAO {

	/**
	 * Méthode permettant de sauvegarder une recette
	 * 
	 * Notes importantes:
	 * - Si le champ "id" n'est pas null, alors c'est une mise à jour, autrement c'est une insertion
	 * - Le nom de la recette doit être unique
	 * - Regarder comment est fait la classe Recette et Ingredient pour avoir une idée des données à sauvegarder
	 *
	 * @param recipe recette à sauvegarder
	 * @return true si succès, false sinon
	 */
	public static boolean save(Recipe recipe) {
		boolean success = false;

		try {
			MongoDatabase database = MongoConnection.getConnection();
			MongoCollection<Document> collection = database.getCollection("recipes");

			Document existingRecipe = collection.find(Filters.eq("name", recipe.getName())).first();

			Document doc = new Document()
					.append("name", recipe.getName())
					.append("portion", recipe.getPortion())
					.append("prepTime", recipe.getPrepTime())
					.append("cookTime", recipe.getCookTime());

			List<Document> ingredientDocs = new ArrayList<>();
			if (recipe.getIngredients() != null) {
				for (Ingredient ing : recipe.getIngredients()) {
					Document ingDoc = new Document()
							.append("name", ing.getName())
							.append("quantity", ing.getQuantity());
					ingredientDocs.add(ingDoc);
				}
			}
			doc.append("ingredients", ingredientDocs);

			if (recipe.getSteps() != null) {
				doc.append("steps", recipe.getSteps());
			}

			if (recipe.getImageData() != null) {
				String photoKey = UUID.randomUUID().toString();
				Database berkeleyDb = BerkeleyConnection.getConnection();
				DatabaseEntry keyEntry = new DatabaseEntry(photoKey.getBytes(StandardCharsets.UTF_8));
				DatabaseEntry valueEntry = new DatabaseEntry(recipe.getImageData());

				berkeleyDb.put(null, keyEntry, valueEntry);
				doc.append("photoKey", photoKey);
				recipe.setImageData(null);
			}

			if (existingRecipe != null) {
				String oldPhotoKey = existingRecipe.getString("photoKey");
				if (oldPhotoKey != null) {
					Database berkeleyDb = BerkeleyConnection.getConnection();
					DatabaseEntry oldKeyEntry = new DatabaseEntry(oldPhotoKey.getBytes(StandardCharsets.UTF_8));
					berkeleyDb.delete(null, oldKeyEntry);
				}

				if (recipe.getImageData() != null) {
					String newPhotoKey = UUID.randomUUID().toString();
					Database berkeleyDb = BerkeleyConnection.getConnection();
					DatabaseEntry newKeyEntry = new DatabaseEntry(newPhotoKey.getBytes(StandardCharsets.UTF_8));
					DatabaseEntry newValueEntry = new DatabaseEntry(recipe.getImageData());

					berkeleyDb.put(null, newKeyEntry, newValueEntry);
					doc.append("photoKey", newPhotoKey);
					recipe.setImageData(null);
				}

				UpdateResult result = collection.updateOne(
						Filters.eq("_id", existingRecipe.getObjectId("_id")),
						new Document("$set", doc)
				);
				success = (result.getModifiedCount() > 0);
			} else {
				collection.insertOne(doc);
				ObjectId generatedId = doc.getObjectId("_id");
				recipe.setId(generatedId.toHexString());
				success = true;
			}

		} catch (MongoWriteException mwe) {
			mwe.printStackTrace();
			success = false;
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}

		return success;
	}

	/**
	 * Méthode permettant de retourner la liste des recettes de la base de données.
	 * 
	 * Notes importantes:
	 * - N'oubliez pas de limiter les résultats en fonction du paramètre limit
	 * - La liste doit être triées en ordre croissant, selon le nom des recettes
	 * - Le champ filtre doit permettre de filtrer selon le préfixe du nom (insensible à la casse)
	 * - N'oubliez pas de mettre l'ID dans la recette
	 * - Il pourrait ne pas y avoir de filtre (champ filtre vide)
	 * 	 * 
	 * @param filter champ filtre, peut être vide ou null
	 * @param limit permet de restreindre les résultats
	 * @return la liste des recettes, selon le filtre si nécessaire
	 */
	public static List<Recipe> getRecipeList(String filter, int limit) {
		List<Recipe> recipeList = new ArrayList<>();

		try {
			MongoDatabase database = MongoConnection.getConnection();
			MongoCollection<Document> collection = database.getCollection("recipes");

			List<Document> documents;
			if (filter != null && !filter.isEmpty()) {
				documents = collection.find(Filters.regex("name", "^" + filter, "i"))
						.sort(new Document("name", 1))
						.limit(limit)
						.into(new ArrayList<>());
			} else {
				documents = collection.find()
						.sort(new Document("name", 1))
						.limit(limit)
						.into(new ArrayList<>());
			}

			for (Document doc : documents) {
				Recipe recipe = new Recipe();
				recipe.setId(doc.getObjectId("_id").toHexString());
				recipe.setName(doc.getString("name"));
				recipe.setPortion(doc.getInteger("portion", 0));
				recipe.setPrepTime(doc.getInteger("prepTime", 0));
				recipe.setCookTime(doc.getInteger("cookTime", 0));
				recipe.setSteps(doc.getList("steps", String.class));

				List<Document> ingredientDocs = doc.getList("ingredients", Document.class);
				List<Ingredient> ingredients = new ArrayList<>();
				if (ingredientDocs != null) {
					for (Document ingDoc : ingredientDocs) {
						ingredients.add(new Ingredient(ingDoc.getString("quantity"), ingDoc.getString("name")));
					}
				}
				recipe.setIngredients(ingredients);

				String photoKey = doc.getString("photoKey");
				if (photoKey != null) {
					Database berkeleyDb = BerkeleyConnection.getConnection();
					DatabaseEntry keyEntry = new DatabaseEntry(photoKey.getBytes(StandardCharsets.UTF_8));
					DatabaseEntry valueEntry = new DatabaseEntry();
					if (berkeleyDb.get(null, keyEntry, valueEntry, null) == com.sleepycat.je.OperationStatus.SUCCESS) {
						recipe.setImageData(valueEntry.getData());
					}
				}

				recipeList.add(recipe);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return recipeList;
	}

	/**
	 * Suppression d'une recette
	 * 
	 * @param recipe
	 * @return true si succès, false sinon
	 */
	public static boolean delete(Recipe recipe) {
		boolean success = false;

		try {
			MongoDatabase database = MongoConnection.getConnection();
			MongoCollection<Document> collection = database.getCollection("recipes");

			Document recipeDoc = collection.find(Filters.eq("_id", new ObjectId(recipe.getId()))).first();
			if (recipeDoc != null) {
				String photoKey = recipeDoc.getString("photoKey");
				if (photoKey != null) {
					Database berkeleyDb = BerkeleyConnection.getConnection();
					DatabaseEntry keyEntry = new DatabaseEntry(photoKey.getBytes(StandardCharsets.UTF_8));
					berkeleyDb.delete(null, keyEntry);
				}

				collection.deleteOne(Filters.eq("_id", new ObjectId(recipe.getId())));
				success = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}
	
	/**
	 * Suppression totale de toutes les données du système!
	 *
	 * @return true si succès, false sinon
	 */
	public static boolean deleteAll() {
		boolean success = false;

		try {
			MongoDatabase database = MongoConnection.getConnection();
			MongoCollection<Document> collection = database.getCollection("recipes");
			collection.deleteMany(new Document());

			Database berkeleyDb = BerkeleyConnection.getConnection();
			var environment = berkeleyDb.getEnvironment();
			var txn = environment.beginTransaction(null, null);

			try (var cursor = berkeleyDb.openCursor(txn, null)) {
				DatabaseEntry keyEntry = new DatabaseEntry();
				DatabaseEntry valueEntry = new DatabaseEntry();
				while (cursor.getNext(keyEntry, valueEntry, null) ==
						com.sleepycat.je.OperationStatus.SUCCESS) {
					cursor.delete(); // Delete the current entry
				}
			}

			txn.commit();

			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * Permet de retourner le nombre d'ingrédients en moyenne dans une recette
	 * 
	 * @return le nombre moyen d'ingrédients
	 */
	public static double getAverageNumberOfIngredients() {
		double num = 0;

		try {
			MongoDatabase database = MongoConnection.getConnection();
			MongoCollection<Document> collection = database.getCollection("recipes");

			long totalRecipes = collection.countDocuments();
			if (totalRecipes == 0) {
				return 0;
			}

			long totalIngredients = 0;
			for (Document doc : collection.find()) {
				List<Document> ingredients = doc.getList("ingredients", Document.class);
				if (ingredients != null) {
					totalIngredients += ingredients.size();
				}
			}

			num = (double) totalIngredients / totalRecipes;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return num;
	}

	/**
	 * Permet d'obtenir la recette ayant le plus d'ingrédients
	 * (s'il y a deux recettes ayant le nombre maximum d'ingrédients, retourner la première)
	 *
	 * @return la recette ayant le plus d'ingrédients
	 */
	public static Recipe getMostIngredientsRecipe() {
		Recipe r = null;

		try {
			MongoDatabase database = MongoConnection.getConnection();
			MongoCollection<Document> collection = database.getCollection("recipes");

			List<Document> documents = collection.find().into(new ArrayList<>());
			int maxIngredients = 0;

			for (Document doc : documents) {
				List<Document> ingredients = doc.getList("ingredients", Document.class);
				int ingredientCount = (ingredients != null) ? ingredients.size() : 0;

				if (ingredientCount > maxIngredients) {
					maxIngredients = ingredientCount;

					r = new Recipe();
					r.setId(doc.getObjectId("_id").toHexString());
					r.setName(doc.getString("name"));
					r.setPortion(doc.getInteger("portion", 0));
					r.setPrepTime(doc.getInteger("prepTime", 0));
					r.setCookTime(doc.getInteger("cookTime", 0));
					r.setSteps(doc.getList("steps", String.class));

					List<Ingredient> ingredientList = new ArrayList<>();
					if (ingredients != null) {
						for (Document ingDoc : ingredients) {
							ingredientList.add(new Ingredient(ingDoc.getString("quantity"), ingDoc.getString("name")));
						}
					}
					r.setIngredients(ingredientList);

					String photoKey = doc.getString("photoKey");
					if (photoKey != null) {
						Database berkeleyDb = BerkeleyConnection.getConnection();
						DatabaseEntry keyEntry = new DatabaseEntry(photoKey.getBytes(StandardCharsets.UTF_8));
						DatabaseEntry valueEntry = new DatabaseEntry();
						if (berkeleyDb.get(null, keyEntry, valueEntry, null) == com.sleepycat.je.OperationStatus.SUCCESS) {
							r.setImageData(valueEntry.getData());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}
	
	/**
	 * Permet d'obtenir le temps de la recette la plus longue à faire.
	 * 
	 * La recette la plus longue est calculée selon son temps de cuisson plus son temps de préparation
	 * 
	 * @return le temps maximal
	 */
	public static long getMaxRecipeTime() {
		long num = 0;

		try {
			MongoDatabase database = MongoConnection.getConnection();
			MongoCollection<Document> collection = database.getCollection("recipes");

			for (Document doc : collection.find()) {
				int prepTime = doc.getInteger("prepTime", 0);
				int cookTime = doc.getInteger("cookTime", 0);
				long totalTime = prepTime + cookTime;

				if (totalTime > num) {
					num = totalTime;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return num;
	}
	
	/**
	 * Permet d'obtenir le nombre de photos dans la base de données BerkeleyDB
	 * 
	 * @return nombre de photos dans BerkeleyDB
	 */
	public static long getPhotoCount() {
		long num = 0;

		try {
			Database berkeleyDb = BerkeleyConnection.getConnection();
			var environment = berkeleyDb.getEnvironment();
			var txn = environment.beginTransaction(null, null);

			try (var cursor = berkeleyDb.openCursor(txn, null)) {
				DatabaseEntry keyEntry = new DatabaseEntry();
				DatabaseEntry valueEntry = new DatabaseEntry();

				while (cursor.getNext(keyEntry, valueEntry, null) == com.sleepycat.je.OperationStatus.SUCCESS) {
					num++;
				}
			}

			txn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return num;
	}

	/**
	 * Permet d'obtenir le nombre de recettes dans votre base de données
	 * 
	 * @return nombre de recettes
	 */
	public static long getRecipeCount() {
		long num = 0;

		try {
			MongoDatabase database = MongoConnection.getConnection();
			MongoCollection<Document> collection = database.getCollection("recipes");
			num = collection.countDocuments();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return num;
	}
	
	/**
	 * Permet d'obtenir la dernière recette ajoutée dans le système
	 * 
	 * @return la dernière recette
	 */
	public static Recipe getLastAddedRecipe() {
		Recipe recipe = null;

		try {
			MongoDatabase database = MongoConnection.getConnection();
			MongoCollection<Document> collection = database.getCollection("recipes");

			Document lastRecipeDoc = collection.find()
					.sort(new Document("_id", -1))
					.limit(1)
					.first();

			if (lastRecipeDoc != null) {
				recipe = new Recipe();
				recipe.setId(lastRecipeDoc.getObjectId("_id").toHexString());
				recipe.setName(lastRecipeDoc.getString("name"));
				recipe.setPortion(lastRecipeDoc.getInteger("portion", 0));
				recipe.setPrepTime(lastRecipeDoc.getInteger("prepTime", 0));
				recipe.setCookTime(lastRecipeDoc.getInteger("cookTime", 0));
				recipe.setSteps(lastRecipeDoc.getList("steps", String.class));

				List<Document> ingredientDocs = lastRecipeDoc.getList("ingredients", Document.class);
				List<Ingredient> ingredients = new ArrayList<>();
				if (ingredientDocs != null) {
					for (Document ingDoc : ingredientDocs) {
						ingredients.add(new Ingredient(ingDoc.getString("quantity"), ingDoc.getString("name")));
					}
				}
				recipe.setIngredients(ingredients);

				String photoKey = lastRecipeDoc.getString("photoKey");
				if (photoKey != null) {
					Database berkeleyDb = BerkeleyConnection.getConnection();
					DatabaseEntry keyEntry = new DatabaseEntry(photoKey.getBytes(StandardCharsets.UTF_8));
					DatabaseEntry valueEntry = new DatabaseEntry();
					if (berkeleyDb.get(null, keyEntry, valueEntry, null) == com.sleepycat.je.OperationStatus.SUCCESS) {
						recipe.setImageData(valueEntry.getData());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return recipe;
	}
	
	/**
	 * Cette fonctionnalité permet de générer une recette en se basant sur celles existantes
	 * dans le système. Voici l'algorithme générale à utiliser :
	 * 
	 * 1- Allez chercher tous les ingrédients dans votre base de données
	 * 2- Construisez une liste aléatoire d'ingrédients selon les ingrédients obtenus à l'étape précédente
	 * 3- Créez une liste aléatoire de quelques étapes basée sur une liste prédéfinie(ex : "Mélangez tous les ingrédients", "cuire au four 20 minutes", etc)
	 * 4- Faites un temps de cuisson, de préparation et de nombre de portions aléatoires
	 * 5- Copiez une image d'une autre recette
	 * 6- Construisez un nom en utilisant cette logique :
	 *    - un préfixe aléatoire parmi une liste prédéfinie (ex: ["Giblotte à", "Mélangé de", "Crastillon de"]
	 *    - un suffixe basé sur un des ingrédients de la recette (ex: "farine").
	 *    - Résultat fictif : Giblotte à farine
	 * 
	 * Laissez l'ID de le recette vide, et ne l'ajoutez pas dans la base de données.
	 * 
	 * @return une recette générée
	 */
	public static Recipe generateRandomRecipe() {
		Recipe r = new Recipe();
		
		return r;
	}
	
	/**
	 * Permet d'obtenir une proposition d'ingrédient à ajouter à la recette en cours de modification
	 * 
	 * - L'idée est de comparer la recette en cours avec une autre recette existante en fonction de leurs ingrédients.
	 * - Si une recette possède au moins 2 ingrédients identiques avec la recette en cours de modification,
	 *   alors retourner un autre ingrédient de cette recette
	 * - La recette en cours ne doit pas déjà avoir cet ingrédient
	 * - Cette fonction est appelée lors de la MODIFICATION d'une recette existante, pas lors de la création d'une nouvelle recette
	 * 
	 * - Exemple :
	 *     Recette 1 - Pesto : Basilic + parmesan + huile d'olive
	 *     Recette 2 - Salade tomates au basilic : Tomates + Basilic + huile d'olive
	 *     
	 *     Le système pourrait proposer à Recette 2 d'y ajouter du parmesan, puisque la recette de Pesto et de salade
	 *     possèdent 2 mêmes ingrédients (basilic et huile d'olive)
	 * 
	 * @param recipeId id de la recette
	 * @return une proposition d'un ingrédient à ajouter à la recette
	 */
	public static String getProposedIngredient(String recipeId) {
		String proposedIngredient = "--";
		
		return proposedIngredient;
	}
}
