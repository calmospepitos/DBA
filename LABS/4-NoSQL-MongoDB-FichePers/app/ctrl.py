from dao.mongo import PersonDAO, Connection

class MngApplication:
	def __init__(self):
		pass

	def verifyConnection(self):
		return Connection.verifyConnection()

	def quiting(self):
		Connection.closeConnection()

	def save(self, firstName, lastName, age, previousJobs, id = None):
		PersonDAO.savePerson(firstName, lastName, age, previousJobs, id)

	def deletePerson(self, id):
		PersonDAO.deletePerson(id)

	def search(self, ageMin, limit):
		return PersonDAO.search(ageMin, limit)

	def getDatabaseSize(self):
		return PersonDAO.getSize()