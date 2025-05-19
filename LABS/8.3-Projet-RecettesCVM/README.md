# Projet RecettesCVM

## 1. Membres de l’équipe
- Alexia

## 2. Schéma de persistance

### 2.1 Collections MongoDB
- **recipes**  
  Document `Recipe` contenant :
  - `_id` (ObjectId)  
  - `name` (String)  
  - `portion` (Integer)  
  - `prepTime` (Integer)  
  - `cookTime` (Integer)  
  - `ingredients` (Array de `{ name: String, quantity: String }`)  
  - `steps` (Array de String)  
  - `photoKey` (String) – clé BerkeleyDB pour l’image  

## 3. Indexation MongoDB

| Collection | Champ              | Ordre     | Unique |
|------------|--------------------|-----------|--------|
| recipes    | name               | ASC (1)   | Oui    |
| recipes    | ingredients.name   | ASC (1)   | Non    |
| recipes    | photoKey           | ASC (1)   | Non    |
| recipes    | prepTime           | ASC (1)   | Non    |
| recipes    | cookTime           | ASC (1)   | Non    |
