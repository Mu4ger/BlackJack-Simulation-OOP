## Blackjack Game (OOP CSC 232)
Munkhdelger Zolbayar

## Rules
The blackjack game simulation will allow a user to play a card game against a computer dealer. 
The game will simulate the typical flow of a blackjack game, including player actions such as hitting, and standing, as well as dealer actions such as dealing cards and determining the winner. 
The scope of the project will include implementing the core rules of blackjack, managing player bets and balances, and providing a user-friendly interface for interaction.

## Credit
This Project was made as an assignment for an Object Oriented Programming Class.
The usage of AI was apparent in this project with the help of the simple class structure and finding small syntax and style errors.
The Java library was used to find methods and aid in the process of writing the code.

## Design / Implementation
Every card is represented by an instance of the 'Card' class, which is then organized within a 'Deck'. 
The contents of a 'Deck' can be visually presented through a 'DeckPanel', a customized extension of Swing's 'JPanel'.
Within the game, there are three main instances of 'Deck' utilized: one to manage the dealer's initially shuffled deck, and two others to handle the cards belonging to the player and the dealer. 
These cards are dynamically moved between the dealer's deck and the player or dealer card groups whenever a player requests a hit or when the dealer draws a card.

