from view.gui import MainFrame
from app.ctrl import MngApplication
import sys
from tkinter import Tk

print("Demarrage de l'application...")

app = MngApplication()
print("Verification de la connexion...")
valid = app.verifyConnection()

if not valid:
	print("La connexion a la BD est invalide! Est-elle active?")
	sys.exit()

print("Mise en place et depart du GUI")
root = Tk()
frame = MainFrame(root, app)
root.mainloop()  
