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

### Size
rays3c marks the size character, which is the length of the stack at the time of evaluation.

```
rays3 rays2 rays2 rays2 rays2 rays3c
=>
//3, 2, 2, 2, 2, 5
```

### If/Else
Conditional operator: variable, followed by raysShrug, followed by an expression, a raysFox, another expression, 
and another raysfox. Variable evaluates. If <= 0, or stack is empty, the first expression evaluates, otherwise, the second does.
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

### Assignment
raysLove - Binds a value to a variable.
```
rays3 rays2 raysI raysOne raysLove
=>
raysOne = 3 - 2;
```

### Swapping
raysShock - Swaps the last two elements in the stack
Example:
```
rays2 rays3 rays3 rays2 raysShock
=>
//rays2 rays3 rays2 rays3
```

### Duplicating
raysE - Duplicates the last element in the stack
Example:
```

rays2 rays3 raysE
=>
//rays2 rays3 rays3
```

### Pop 
raysD - Consumes one element from the stack.
Example:
```
rays2 rays3 rays2 raysD
=>
rays2 rays3
```

### Roll
raysThump - Moves the top-most element to the bottom of the stack.
Example:
```
rays2 rays2 rays3 rays3 raysThump
=>
roll(1) //rays3 rays2 rays2 rays3
```

### Reserved Words
All fox emotes are reserved for future use. All non-fox emotes are reserved for potential future use.

rays2 - Literal integer 2  
rays3 - Literal integer 3  
rays3c - Stack size
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
raysLove - Assignment  
raysLurk - Marks end of loop  
raysP - Addition  
raysQ - Print output  
raysShock - Swap  
raysShrug - Conditional operator  
raysShy  
raysT - Accept input  
raysThump - Roll
raysZ - Marks the end of method declaration