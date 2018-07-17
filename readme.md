# Rayscode
A project to create a programming language based on the [raysfire emotes](https://twitchemotes.com/channels/23196698) on Twitch. 
All commands are one of the raysfire emotes, outside of optional support for variables/methods, which are still raysfire-like.

## Goals
1. Draft basic documentation on code functionality.
2. Add additional functionality. Possible end goal is to create functionality for each emote.
3. Create an interpreter that correctly parses the rayscode language.
4. Create a Discord bot which executes rayscode on-the-fly.

## Rayscode Standard
(I am by no means an expert on drafting this sort of legislation. I literally wrote this over the span
of a few hours one night). Rayscode is not intended to be mind-bashingly difficult to write, at least, not to the
caliber of other languages such as Pikachu and Brainfuck. Several commands are created to make your life a little
simpler.

### raysFox
In rayscode, the raysFox marks the end of certain snippets. It basically combines the semicolon and the right
parenthesis all in one symbol.

### Basic Functions
All variables and methods must be prefixed with "rays".
rays2 and rays3 make the only two literal numbers. Methods and combinations are called via postfix notation.
Standard operators:
* Addition: raysP
* Subtraction: raysI
* Multiplication: raysB
* Division: raysA

Example:
```
raysFour rays2 rays2 raysP raysFox => var four = 2 + 2.
```

Method declarations start with raysH, followed by the name, and number of arguments. Ends with raysZ.

Methods and variables are helpful functions for you, the programmer, as are the presence of multiplication and division
operators. If you want, see if you can strip down your code to use only valid raysfire emotes!

### I/O
raysT: Accepts input. Puts the specified number of characters entered onto the stack.
Example:
```
raysThree raysT
=>
//retrieves three characters, and puts them in the stack
```

raysQ: Writes to console. Writes the specified number of characters on the stack as characters.
Example:
```
raysThree raysQ
=>
//prints three chars at the top of the stack
```

Note: Reading followed by writing will *swap* the string! 

### Infinity
rays3c marks a special character, called the infinity character. This could be considered analogous to a null in a
standard programming language, although takes on additional roles in rayscode. In the case of I/O, this marks that
all characters should be read from input, or the whole stack should be read to output.

```
rays3c raysT
=>
//retrieves every character typed, and places on the stack
```

### If/Else (NE)
Conditional operator: variable, followed by raysShrug, followed by an expression, a raysFox, another expression, 
and another raysfox. Variable evaluates. If <= 0, the first expression evaluates, otherwise, the second does.
Example:
```
rays3 rays3 raysI raysShrug rays3 rays3 raysI raysFox rays3 rays2 raysI raysFox
=>
if((3-3) <= 0){
    3 - 3;
} else {
    3 - 2;
}
```

### Looping (NE)
raysC - Marks the loop point.
raysLurk - Go to the loop point.
Example:
```
rays3 raysC raysShrug rays3 rays2 raysI raysFox rays3 rays2 raysI raysI raysD raysFox
=>
var = 3;
loop:
if(var <= 0){
    1;
else {
    var = var - 1;
    goto loop
}
```

### Swapping (NE)
raysShock - Swaps some number of elements in the stack. Pass infinite to swap the entire stack.
Example:
```
rays2 rays2 rays3 rays3 rays3c raysShock
=>
//rays3 rays3 rays2 rays2
```

### Duplicating (NE)
raysE - Duplicates some number of elements in the stack. Pass infinite to duplicate the entire stack.
Example:
```
rays2 rays3 rays3c raysC
=>
//rays3 rays2
```

### Pop (NE)
raysD - Consumes a member of the stack.
Example:
```
rays2 rays3 rays2 raysD
=>
rays2 rays3
```

### Roll (NE)
raysThump - Rolls the stack some number of times.
Example:
```
rays2 rays2 rays3 rays3 rays3 rays2 raysI raysThump
=>
roll(1) //rays3 rays2 rays2 rays3
```

### Reserved Words
All fox emotes are reserved for future use. All non-fox emotes are reserved for potential future use.

rays2 - Literal integer 2  
rays3 - Literal integer 3  
rays3c - Literal integer infinity  
raysA - Integer division  
raysB - Multiplication  
raysC - Marks start of loop  
raysD - Pop  
raysE - Duplicate stack  
raysFox - Semicolon, and other marker  
raysH - Marks the beginning of method declaration  
raysI - Subtraction  
raysL  
raysLick  
raysLove  
raysLurk - Marks end of loop  
raysP - Addition  
raysQ - Print output  
raysShock - Swap  
raysShrug - Conditional operator  
raysShy  
raysT - Accept input  
raysThump - Roll
raysZ - Marks the end of method declaration