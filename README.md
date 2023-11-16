# Chess ♟️

Chess - это шахматное приложение для Android, написанное на Kotlin. Оно предлагает возможность играть локально (два игрока на одном устройстве) или против алгоритма. 

## Начальный экран 📱

<img src="https://github.com/quqveik1/ChessGPT/assets/64206443/8e5485ca-b3ad-41ff-8174-1ea87c475206" width="100">

## Архитектура приложения 🏗️

Приложение основывается на принципе Single Activity. Игровой экран полноценно сохраняется при вызове `onSaveInstanceState` и полностью реализует принцип отделения модели от представления. 

- [`ChessBoard`](https://github.com/quqveik1/ChessGPT/blob/main/app/src/main/java/com/kurlic/chessgpt/chess/ChessBoard.kt) - модель шахматной доски, в этом классе описана вся логика шахмат. Все игровые фрагменты наследуются от главного игрового фрагмента и могут немного менять поведение.
- [`ChessView`](https://github.com/quqveik1/ChessGPT/blob/main/app/src/main/java/com/kurlic/chessgpt/chess/ChessView.kt) - представление шахматной доски на экране

<img src="https://github.com/quqveik1/ChessGPT/assets/64206443/ae385c42-74cf-4a7f-a5f5-02e1b44bb6b5" width="100">

## Создание игры 🎮

Для создания игры показывается AlertDialog.

<img src="https://github.com/quqveik1/ChessGPT/assets/64206443/efa6bdbb-a2aa-4e4a-af50-63ebff3a6d05" width="100">

## Локальные игры 💾

Локальные игры сохраняются при помощи библиотеки Room и отображаются при помощи RecyclerView.
Игры храняться, как классы [`ChessBoard`](https://github.com/quqveik1/ChessGPT/blob/main/app/src/main/java/com/kurlic/chessgpt/chess/ChessBoard.kt) в JSON формате

<img src="https://github.com/quqveik1/ChessGPT/assets/64206443/7742255d-8dec-4c74-8a61-e2fea6a0a3c3" width="100">

## Шахматный алгоритм 🧠

Здесь используется довольно упрощенный MinMax алгоритм, который выбирает ход на основе перебора всех ходов и оценивает, сколько очков он может набрать. Также алгоритм оценивает, что он может потерять после хода соперника в ответ на его ход.

[Подробнее об алгоритме](https://github.com/quqveik1/ChessGPT/tree/main/app/src/main/java/com/kurlic/chessgpt/ai)

## Другие особенности
- Поддержка английского и русского 🌏
- Поддержка тёмной 🌙 и светлой ☀️ темы


## Используемые библиотеки 📚
- GSON
- Retrofit
- OkHttp
- Room

------
- [EN](https://github.com/quqveik1/ChessGPT/blob/main/README_EN.md)
- [DE](https://github.com/quqveik1/ChessGPT/blob/main/README_DE.md)
