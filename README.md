# Trains et circuits

## TODO

supprimer isStation (+ wait dans Element) (si nécessaire)

Projet d'exercice de l'unité éducative programmation concurrente à
IMT Atlantique.

Mathieu CAROFF
Sébastien NAL

## Exercice 1 - Le comportement d'un train

### Q1.1

La position de chaque train est indiquée par une classe `Position`.

Le déplacement d'un train correspond à l'occupation de différentes positions
au cours du temps. Il s'agira donc de construire une nouvelle position à
chaque fois qu'un train se déplace.

On choisi de retirer le mot clé final du champs `Position pos` de la
classe train. On choisi donc que chaque train soit responsable de tenir
à jour sa position.

Les classes `Station`, `Section`, `Element` et `Railway` n'ont donc aucune
responsabilité dans le déplacement des trains, que les éléments de ligne soit
utilisés dans des positions.

### Q1.2

On ajoute à Train une méthode `move` qui modifie la position du train.

On ajoute une méthode `getNextPosition()` à Railway. Cette méthode accepte une
position et donne la prochain position à occuper.

On ajoute un attribut `railway` à Train qui réfère au rail sur lequel le train
se trouve. Cette attribut est passé dans le constructeur.

On ajoute une méthode `isStation` à la classe abstraite `Element`. Cette
méthode renvoie le booléen `true` pour `Station` et `false` pour
`Section`.

### Q1.3

On modifie le code.

Voici une trace:

```
Train[1] is on AB going from left to right
Train[1] is on BC going from left to right
Train[1] is on CD going from left to right
Train[1] is on GareD going from left to right
Train[1] is on GareD going from right to left
Train[1] is on CD going from right to left
Train[1] is on BC going from right to left
Train[1] is on AB going from right to left
Train[1] is on GareA going from right to left
Train[1] is on GareA going from left to right
Train[1] is on AB going from left to right
Train[1] is on BC going from left to right
Train[1] is on CD going from left to right
```

### Q2.1

Une manière de modéliser la situation est de faire des Trains des acteurs,
et de leur associer une Thread à chacun.

On ajoute `implements Runnable` et `public void run()` à la classe `Train`.

On ajoute les instances de Thread au `Main`.

```
Train[1] is on AB going from right to left
Train[2] is on BC going from right to left
Train[3] is on GareA going from left to right
Train[1] is on GareA going from right to left
Train[3] is on AB going from left to right
Train[2] is on AB going from right to left
```

### Q2.2

On rajoute une nouvelle variable count dans `Element` qui permet de savoir combien de
trains sont présent dans l'élément. On généralise `size` à `Element`, une section ayant
une taille de 1.
Les variables utilisées pour exprimer l'invariant de sûreté sont : `count` et `size` dans `Element`.

### Q2.3

L'invariant de sûreté peut donc s'exprimer ainsi :

```
for element in allElement
    if element.getCount() > element.getSize()
        return false
return true

```

### Q2.4

Le déplacement d'un train (méthode `move`) est une action critique,
c'est la seule dans la version actuelle du projet.

### Q2.5

Cette méthode doit être ajoutée dans la classe `Train`.

### Q2.6

Il faut ajouter les méthodes `incrementCount()`, `decrementCount()` et `hasRoom()`
dans la classe `Element`.

### Q2.8

Lors de la simulation avec 3 trains nous avons pu mettre en évidence
un cas d'interblocage des trains, en voici la trace :

```
no room left in the next position: Train[1] is on CD going from right to left
no room left in the next position: Train[3] is on BC going from left to right
no room left in the next position: Train[2] is on GareD going from right to left
```

Il est donc nécessaire d'améliorer l'invariant de sûreté.

