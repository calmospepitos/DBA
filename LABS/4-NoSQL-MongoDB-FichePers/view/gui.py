import tkinter as tk
import sys
from tkinter import Tk, BOTH, Text, TOP, BOTH, X, N, LEFT
from app.ctrl import MngApplication

class MainFrame(tk.Frame):
	def __init__(self, parent, app):
		tk.Frame.__init__(self, parent)
		self.parent = parent
		self.app = app;
		self.initUI()

	def initUI(self):
		self.parent.geometry("500x500+200+200")

		self.parent.title("GFP - Gestion de fiches de personnes")
		self.pack(fill=BOTH, expand=True)

		self.parent.protocol("WM_DELETE_WINDOW", self.quit)

		self.bgImage = tk.PhotoImage(file='assets/images/background.gif')
		background=tk.Label(self.parent,image = self.bgImage)
		background.place(x=0, y=0, relwidth=1, relheight=1, anchor="nw")
		background.pack(side = "top", fill = "both", expand = "yes")

		self.exitButton = tk.Button(self.parent,text="Rechercher",command=self.showSearch, width=10, height=1)
		self.exitButton.place(x=200, y=20)

		self.exitButton = tk.Button(self.parent,text="Ajouter",command=self.showAdd, width=10, height=1)
		self.exitButton.place(x=300, y=20)

		self.exitButton = tk.Button(self.parent,text="Quitter",command=self.quit, width=10, height=1)
		self.exitButton.place(x=400, y=20)

		#############################################################
		# Création des données de la page d'accueil
		#------------------------------------------------------------
		self.mainText = tk.Label(self.parent, text="--", bg="white")
		self.mainText.place(x=10, y=470)

		#############################################################
		# Création du Frame ajouter
		#------------------------------------------------------------
		self.frameAdd = tk.Frame(self.parent, width=480, height=420)

		lbl = tk.Label(self.frameAdd, text="Prénom : ")
		lbl.place(x=10, y=10)

		lbl = tk.Label(self.frameAdd, text="Nom : ")
		lbl.place(x=10, y=40)

		lbl = tk.Label(self.frameAdd, text="Age : ")
		lbl.place(x=10, y=70)

		lbl = tk.Label(self.frameAdd, text="Emplois antérieurs : ")
		lbl.place(x=10, y=110)

		lbl = tk.Label(self.frameAdd, text="(1 emploi par ligne)")
		lbl.place(x=10, y=130)

		btn = tk.Button(self.frameAdd,text="Sauvegarder",command=self.processSave, width=10, height=1)
		btn.place(x=380, y=380)

		self.addDeleteBtn = tk.Button(self.frameAdd,text="Supprimer",command=self.processDelete, width=10, height=1)
		self.addDeleteBtn.place(x=10, y=380)

		self.addFirstName = tk.Entry(self.frameAdd, width=30)
		self.addFirstName.place(x=150, y=10)

		self.addLastName = tk.Entry(self.frameAdd, width=30)
		self.addLastName.place(x=150, y=40)

		self.addAge = tk.Entry(self.frameAdd, width=30)
		self.addAge.place(x=150, y=70)

		self.addJobs = tk.Text(self.frameAdd, width=30, height=4)
		self.addJobs.place(x=150, y=110)

		#############################################################
		# Création du Frame rechercher
		#------------------------------------------------------------
		self.frameSearch = tk.Frame(self.parent, width=480, height=420)
		self.frameSearch.resultLabels = []

		lbl = tk.Label(self.frameSearch, text="Age min. : ")
		lbl.place(x=10, y=10)

		self.searchAgeMin = tk.Entry(self.frameSearch, width=30)
		self.searchAgeMin.place(x=150, y=10)

		btn = tk.Button(self.frameSearch,text="Chercher",command=self.processSearch, width=10, height=1)
		btn.place(x=200, y=50)

		self.refreshFooter()

	def refreshFooter(self):
		self.mainText["text"] = str(self.app.getDatabaseSize()) + " fiches présentes"

	def quit(self):
		self.app.quiting()
		sys.exit()

	def hideElements(self):
		self.mainText.place_forget()
		self.frameAdd.place_forget()
		self.frameSearch.place_forget()

	def showMain(self):
		self.hideElements()
		self.mainText.place(x=10, y=10)

	def showAdd(self, person = None):
		self.addFirstName.delete(0, 'end')
		self.addLastName.delete(0, 'end')
		self.addAge.delete(0, 'end')
		self.addJobs.delete('1.0', 'end')

		self.hideElements()
		self.frameAdd.place(x=10, y=60)

		self.currentPerson = person

		if person is not None:
			self.addFirstName.insert(0,person["firstName"])
			self.addLastName.insert(0,person["lastName"])
			self.addAge.insert(0,person["age"])

			jobs = ""

			for job in person["jobs"]:
				jobs = jobs + job + "\n"

			self.addJobs.insert('1.0', jobs)
			self.addDeleteBtn.place(x=10,y=380)
		else:
			self.addDeleteBtn.place_forget()

	def processDelete(self):
		self.app.deletePerson(self.currentPerson["_id"])
		self.currentPerson = None
		self.refreshFooter()
		self.showAdd()

	def processSave(self):
		jobsInput = self.addJobs.get("1.0",'end')
		previousJobs = jobsInput.split("\n")
		previousJobs = [x for x in previousJobs if x != '']

		personId = None

		if self.currentPerson is not None:
			personId = self.currentPerson["_id"]

		if len(self.addAge.get()) > 0 and len(self.addFirstName.get()) > 0 and len(self.addLastName.get()) > 0:
			self.app.save(self.addFirstName.get(),
						 self.addLastName.get(),
						 int(self.addAge.get()),
						 previousJobs, personId)

			self.refreshFooter()
			self.showAdd()

	def showSearch(self):
		self.hideElements()
		self.frameSearch.place(x=10, y=60)

		self.searchAgeMin.delete(0, 'end')

		self.deletePreviousSearch()

	def processSearch(self):
		self.deletePreviousSearch()
		results = self.app.search(self.searchAgeMin.get(), 10)
		y = 120

		for result in results:
			label = tk.Label(self.frameSearch, text="- " + result["firstName"] + " " + result["lastName"] + " (" + str(result["age"]) + " ans)")
			label.place(x=10, y=y)
			label.person = result
			label.bind("<Button-1>", self.personClicked)

			self.frameSearch.resultLabels.append(label)
			y = y + 25

	def deletePreviousSearch(self):
		for label in self.frameSearch.resultLabels:
			label.place_forget()

		self.frameSearch.resultLabels = []

	def personClicked(self, event):
		self.hideElements()
		self.showAdd(event.widget.person)