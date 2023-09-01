# Chess ♟️

Chess is a chess application for Android, written in Kotlin. It offers the ability to play locally (two players on one device) or against an algorithm.

## Home Screen 📱

<img src="https://github.com/quqveik1/ChessGPT/assets/64206443/8e5485ca-b3ad-41ff-8174-1ea87c475206" width="100">

## App Architecture 🏗️

The application is based on the Single Activity principle. The game screen is fully preserved when calling `onSaveInstanceState` and fully implements the separation of model and view.

- [`ChessBoard`](https://github.com/quqveik1/ChessGPT/blob/main/app/src/main/java/com/kurlic/chessgpt/chess/ChessBoard.kt) - the model of the chessboard; this class describes all the chess logic. All game fragments inherit from the main game fragment and may slightly modify behavior.
- [`ChessView`](https://github.com/quqveik1/ChessGPT/blob/main/app/src/main/java/com/kurlic/chessgpt/chess/ChessView.kt) - the display of the chessboard on the screen

<img src="https://github.com/quqveik1/ChessGPT/assets/64206443/ae385c42-74cf-4a7f-a5f5-02e1b44bb6b5" width="100">

## Game Creation 🎮

An AlertDialog is shown to create a game.

<img src="https://github.com/quqveik1/ChessGPT/assets/64206443/efa6bdbb-a2aa-4e4a-af50-63ebff3a6d05" width="100">

## Local Games 💾

Local games are saved using the Room library and displayed using RecyclerView. Games are stored as [`ChessBoard`](https://github.com/quqveik1/ChessGPT/blob/main/app/src/main/java/com/kurlic/chessgpt/chess/ChessBoard.kt) classes in JSON format.

<img src="https://github.com/quqveik1/ChessGPT/assets/64206443/7742255d-8dec-4c74-8a61-e2fea6a0a3c3" width="100">

## Chess Algorithm 🧠

A fairly simplified MinMax algorithm is used here, which selects a move based on enumerating all moves and estimating how many points it can score. The algorithm also estimates what it may lose after the opponent's counter-move.

[More on the algorithm](https://github.com/quqveik1/ChessGPT/tree/main/app/src/main/java/com/kurlic/chessgpt/ai)

## Other Features
- Supports English and Russian 🌏
- Supports Dark 🌙 and Light ☀️ themes

## Libraries Used 📚
- GSON
- Retrofit
- OkHttp
- Room 
