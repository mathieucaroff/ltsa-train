# Trains et circuits

Projet d'exercice de l'unité éducative programmation concurrente à
IMT Atlantique.

- Mathieu CAROFF
- Sébastien NAL

## Ordre des vérrous

Afin d'éviter des situations d'interblocages résultant de la prises multiple de
vérrous, ce programme suit de manière consitente l'ordre de prise suivant:

- 1. Les vérrous sur les arcs
- 2. Les vérrous sur les éléments
- S'il est nécessaire de prendre les vérrous sur deux éléments distincts,
  commencer par celui de plus petit indice (le plus "à gauche").

La méthode `move()` de Train contient une exception à la règle d'ordre de prise
des verrous sur les éléments. Cette exception ne cause pas de problème de
deadlock, comme le commentaire placé dans le code l'explique.

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

On rajoute une nouvelle variable `count` dans `Element` qui permet de savoir combien de
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

### Q3.1

On ajoute une varialbe `direction` à Railway qui indique la direction
d'utilisation du rail entres les gares. Cette variable vaut `null` lorsque
qu'aucun train ne parcours le rail.
On ajoute une variable `count` à Railway qui indique le nombre de trains
hors d'une gare.

La variable permettant d'exprimer la condition est `direction` dans `Railway`.

### Q3.2

La condition de suretée est :

Pour tout les trains présents sur le rail, la direction du train doit être
égale à celle du rail.

Formellement la condition est :

```
for train in allTrain:
    if train.getPos().getElement().isStation() == false:
        if train.getPos().getDirection() != railway.direction:
            return false
return true
```

La variable `count` devra aussi être tenue à jour.

### Q3.3

La classe réponsable de cette variable critique est Railway. De même pour la
variable `count`.

### Q3.4

L'action critiques suceptible de modifier la variable est l'action `move`
dans la classe `Train`.

### Q4.1

La classe Railway permet déjà d'ajouter autant de gares que souhaitées.

Voici la trace d'un interblocage avec trois trains et une gare intermédiaire
pouvant accueillir deux trains.
Le train 1 et 2 sont dans la gare intermédiaire C, souhaitant aller en gare D;
le train 3 est en CD et se déplace vers la gare A.

```
Train[1] is on GareC going from left to right
Train[3] is on GareD going from left to right
Train[2] is on BC going from left to right
Train[3] is on GareD going from right to left
Train[2] is on GareC going from left to right
Direction was : from left to right, setting direction null
Direction was : null, setting direction from right to left
Train[3] is on CD going from right to left
no room left in the next position: Train[3] is on CD going from right to left
no room left in the next position: Train[3] is on CD going from right to left
no room left in the next position: Train[3] is on CD going from right to left
```

### Q4.2

On ajoute une classe `Arc` responsable des sections entre deux gares et de la direction
entre deux gares, ainsi que tenant compte du nombre de trains circulant sur ces sections.

Une fois cette modification faite, le problème persiste comme attendu.

Avec trois trains et une gare intermédiare C ne pouvant en acceuillir que 2, le
train 1 ne peut pas rentrer en gare C lorsque les deux autres trains souhaitent
sortir dans la direction opposée à celle du train A:

```
Train[2] is on GareC going from right to left
CD change direction from right to left to direction null
ABC change direction null to direction from left to right
Train[1] is on AB going from left to right
CD change direction null to direction from right to left
Train[3] is on CD going from right to left
Train[3] is on GareC going from right to left
CD change direction from right to left to direction null
Train[1] is on BC going from left to right
no room left in the next position: Train[1] is on BC going from left to right
no room left in the next position: Train[1] is on BC going from left to right
```

Pour résoudre ce problème d'interblocage, on modifie le rôle de la variable
`count` pour la gare intermédiaire: elle compte les trains présents et les
trains en déplacement vers la gare.

On propose alors la condition suivant:

```
if intermediaryStation.count > intermediaryStation.size:
    return false
return true
```
