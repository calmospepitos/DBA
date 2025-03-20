from pymongo import MongoClient
import datetime

class Connection:
	client = None

	def verifyConnection():
		if Connection.client is None:
			try:
				Connection.client = MongoClient('localhost', 27017)
				db = Connection.client.list_database_names()
			except Exception as e:
				Connection.client = None
			finally:
				pass

		return Connection.client is not None

	def closeConnection():
		Connection.client.close()


class PersonDAO:

	# Fonction permettant de trouver le nombre de personnes dans la BD
	# Doit retourner un int/nombre
	def getSize():
		nombre: int = Connection.client.clients.personnes.count_documents({})
		return nombre

	# Suppression d'une personne
	# Aucune valeur à retourner
	def deletePerson(id):
		Connection.client.clients.personnes.delete_one({"_id" : id})

	# Sauvegarder d'une personne
	# Si "id is None", alors insertion, sinon c'est une mise à jour
	# Aucune valeur à retourner
	def savePerson(firstName, lastName, age, previousJobs, id = None):
		doc = {
				"firstName": firstName,
				"lastName": lastName,
				"age": age,
				"jobs": previousJobs
			}
		
		if id is None:
			Connection.client.clients.personnes.insert_one(doc)
		else:
			Connection.client.clients.personnes.replace_one({"_id" : id}, doc)

	# Recherche dans la BD. Le paramètre Age est optionnel.
	# Doit retourner un tableau de personnes(documents MongoDB).
	def search(age, limit):
		# age est un string, vous devez donc le transformer en int comme suit : int(age)
		results = []
		query = {}
		query["age"] = int(age)

		if len(age) > 0:
			cursor = Connection.client.clients.personnes.find(query).limit(limit)
			for document in cursor:
				results.append(document)

		return results
