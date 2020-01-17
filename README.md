# Trains et circuits

Projet d'exercice de l'unité éducative Programmation Concurrente à
IMT Atlantique.

Mathieu CAROFF
Sébastien NAL

## Exercice 1 - Le comportement d'un train

### Q1.1

La position de chaque train est indiquée par une classe `Position`.

Le déplacement d'un train correspond à l'occupation de différentes positions
au cours du temps. Il s'agira donc de construire une nouvelle position pour
chaque train, à chaque fois qu'un train se déplace.

Une manière de modéliser la situation est de faire des Trains des acteurs,
et de leur associer une Thread à chacun.

Chaque train est alors responsable de son déplacement, c'est à dire,
responsable de créer une nouvelle instance de train, occupant la
nouvelle position, et de mettre à jour la liste des trains.

Les classes `Station`, `Section`, `Element` et `Railway` n'ont donc aucune
responsabilité dans le déplacement des trains.

### Q1.2

On ajoute `implements Runnable` et `public void run()` à la classe `Train`.
