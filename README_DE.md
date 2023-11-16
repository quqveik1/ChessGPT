# Schach ♟️

Schach ist eine Schachanwendung für Android, geschrieben in Kotlin. Sie bietet die Möglichkeit, lokal zu spielen (zwei Spieler auf einem Gerät) oder gegen einen Algorithmus.

## Startbildschirm 📱

<img src="https://github.com/quqveik1/ChessGPT/assets/64206443/8e5485ca-b3ad-41ff-8174-1ea87c475206" width="100">

## App-Architektur 🏗️

Die Anwendung basiert auf dem Prinzip der Einzelnen Aktivität. Der Spielbildschirm wird vollständig gespeichert, wenn `onSaveInstanceState` aufgerufen wird und implementiert vollständig die Trennung von Modell und Ansicht.

- [`ChessBoard`](https://github.com/quqveik1/ChessGPT/blob/main/app/src/main/java/com/kurlic/chessgpt/chess/ChessBoard.kt) - das Modell des Schachbretts; diese Klasse beschreibt die gesamte Schachlogik. Alle Spiel-Fragmente erben vom Hauptspiel-Fragment und können das Verhalten leicht ändern.
- [`ChessView`](https://github.com/quqveik1/ChessGPT/blob/main/app/src/main/java/com/kurlic/chessgpt/chess/ChessView.kt) - die Darstellung des Schachbretts auf dem Bildschirm

<img src="https://github.com/quqveik1/ChessGPT/assets/64206443/ae385c42-74cf-4a7f-a5f5-02e1b44bb6b5" width="100">

## Spiel erstellen 🎮

Ein AlertDialog wird angezeigt, um ein Spiel zu erstellen.

<img src="https://github.com/quqveik1/ChessGPT/assets/64206443/efa6bdbb-a2aa-4e4a-af50-63ebff3a6d05" width="100">

## Lokale Spiele 💾

Lokale Spiele werden mit der Room-Bibliothek gespeichert und mit RecyclerView angezeigt. Spiele werden als [`ChessBoard`](https://github.com/quqveik1/ChessGPT/blob/main/app/src/main/java/com/kurlic/chessgpt/chess/ChessBoard.kt) Klassen im JSON-Format gespeichert.

<img src="https://github.com/quqveik1/ChessGPT/assets/64206443/7742255d-8dec-4c74-8a61-e2fea6a0a3c3" width="100">

## Schach-Algorithmus 🧠

Hier wird ein ziemlich vereinfachter MinMax-Algorithmus verwendet, der einen Zug auf der Grundlage der Aufzählung aller Züge auswählt und schätzt, wie viele Punkte er erzielen kann. Der Algorithmus schätzt auch, was er nach dem Gegenzug des Gegners verlieren könnte.

[Mehr zum Algorithmus](https://github.com/quqveik1/ChessGPT/tree/main/app/src/main/java/com/kurlic/chessgpt/ai)

## Weitere Funktionen
- Unterstützt Englisch und Russisch 🌏
- Unterstützt Dunkles 🌙 und Helles ☀️ Thema

## Verwendete Bibliotheken 📚
- GSON
- Retrofit
- OkHttp
- Room
