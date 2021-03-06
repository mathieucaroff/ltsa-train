# Trains et circuits

Projet d'exercice de l'unité éducative programmation concurrente à
IMT Atlantique.

- Mathieu CAROFF
- Sébastien NAL

- [Trains et circuits](#trains-et-circuits)
  - [Compilation et exécution](#compilation-et-ex%c3%a9cution)
  - [Git](#git)
  - [Exercices](#exercices)
  - [Ordre des verrous](#ordre-des-verrous)
  - [Exercice 1 - Le comportement d'un train](#exercice-1---le-comportement-dun-train)
    - [Q1.1](#q11)
    - [Q1.2](#q12)
    - [Q1.3](#q13)
  - [Exercice 2 - Plusieurs trains sur la ligne](#exercice-2---plusieurs-trains-sur-la-ligne)
    - [Q2.1](#q21)
    - [Q2.2](#q22)
    - [Q2.3](#q23)
    - [Q2.4](#q24)
    - [Q2.5](#q25)
    - [Q2.6](#q26)
    - [Q2.8](#q28)
  - [Exercice 3 - Éviter les interblocages](#exercice-3---%c3%89viter-les-interblocages)
    - [Q3.1](#q31)
    - [Q3.2](#q32)
    - [Q3.3](#q33)
    - [Q3.4](#q34)
  - [Exercice 4 - Gare intermédiaire](#exercice-4---gare-interm%c3%a9diaire)
    - [Q4.1](#q41)
    - [Q4.2](#q42)
    - [Q4.3](#q43)

## Compilation et exécution

Compilez et exécutez le projet avec les commandes suivantes:

```
cd src
javac train/Main.java
java train/Main
```

## Git

Le projet est public sur Github: https://github.com/mathieucaroff/ltsa-train

## Exercices

À chaque fois que nous avons résolu un exercice, nous avons marqué le
dernier commit d'un tag. Les commentaires ont été ajouté après la résolution
du dernier exercice.

## Ordre des verrous

Afin d'éviter des situations d'interblocages résultant de la prise multiple de
verrous, ce programme suit de manière consistante l'ordre de prise suivant:

- 1. Les verrous sur les arcs
- 2. Les verrous sur les éléments
- S'il est nécessaire de prendre les verrous sur deux éléments distincts,
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

On choisit de retirer le mot clé final du champs `Position pos` de la
classe train. On choisit donc que chaque train soit responsable de tenir
à jour sa position.

Les classes `Station`, `Section`, `Element` et `Railway` n'ont donc aucune
responsabilité dans le déplacement des trains, bien que les éléments de ligne 
soient utilisés dans des positions.

### Q1.2

On ajoute à Train une méthode `move` qui modifie la position du train.

On ajoute une méthode `getNextPosition()` à Railway. Cette méthode accepte une
position et donne la prochain position à occuper.

On ajoute un attribut `railway` à Train qui réfère au rail sur lequel le train
se trouve. Cet attribut est passé dans le constructeur.

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

## Exercice 2 - Plusieurs trains sur la ligne

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
trains sont présents dans l'élément. On généralise `size` à `Element`, une section ayant
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

## Exercice 3 - Éviter les interblocages

### Q3.1

On ajoute une variable `direction` à Railway qui indique la direction
d'utilisation du rail entres les gares. Cette variable vaut `null` lorsque
qu'aucun train ne parcourt le rail.
On ajoute une variable `count` à Railway qui indique le nombre de trains
hors d'une gare.

La variable permettant d'exprimer la condition est `direction` dans `Railway`.

### Q3.2

La condition de sûreté est :

Pour tous les trains présents sur le rail, la direction du train doit être
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

La classe reponsable de cette variable critique est Railway. De même pour la
variable `count`.

### Q3.4

L'action critiques susceptible de modifier la variable est l'action `move`
dans la classe `Train`.

## Exercice 4 - Gare intermédiaire

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

Avec trois trains et une gare intermédiaire C ne pouvant en accueillir que 2, le
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

### Q4.3

Nous avons ajouté à l'invariant la condition énoncée à la question 4.2.
