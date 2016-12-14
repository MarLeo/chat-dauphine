# Projet Java EE - Chat Dauphine WebSocket Application - M2 MIAGE IF - Paris Dauphine

## Installation des dépendances

* Installer [Git](https://git-scm.com/downloads)
* Installer [Node.js](https://nodejs.org)
* Installer le [JDK Java](http://www.oracle.com/technetwork/java/javase/downloads)

### Installer Bower

Mac/Linux

```sh
$ sudo npm install -g bower
```

Windows

```sh
C:\>npm install -g bower
```

## Récupérer le projet depuis Github

* [Créer un compte GitHub](https://github.com/join) (si c'est pas déjà fait)

```sh
$ git clone https://github.com/francoisburdy/avalanche.git
$ cd chat-dauphine
$ bower install
```

## Lancer l'application
```sh
mvn spring-boot:run
```

## Rappel utilisation git

* Pull (à faire le plus souvent possible, pour éviter les conflicts)

```sh
# Dans le répertoire racine du projet chat-dauphine/
$ git pull
```

* Voir ses modifications : 

```sh
# Dans le répertoire racine du projet chat-dauphine/

# Voir tous les fichiers modifiés
$ git status

# Voir le détails des modifications
$ git diff
```

* Commit & push

```sh
# Dans le répertoire racine du projet chat-dauphine/

# Ajoute tous les fichiers modifiés au commit
$ git add -A

# Commit avec message
$ git commit -m "Message de commit"

# Push
$ git push origin master

```
