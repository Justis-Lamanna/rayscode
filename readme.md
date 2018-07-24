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

### Basic Functions
All variables must be prefixed with "rays".
rays2 and rays3 make the only two literal numbers. Methods and combinations are called via postfix notation.
Standard operators:
* Addition: raysP
* Subtraction: raysI
* Multiplication: raysB
* Division: raysA

Example:
```
rays2 rays2 raysP => 2 + 2;
```

### Methods
All methods must be prefixed with "rays". Method declaration must occur before their use.
Declaration is as follows:
1. raysH marks the beginning of method declaration.
2. The name of the method.
3. Zero or more raysShy mark the number of parameters. 
4. The code functionality. 
5. raysZ Marks the end of method declaration.

Inside the method itself, a clean stack is provided, with the specified number of parameters. Any variables
declared outside the function are usable inside the function, but any variables declared inside the function
are forgotten when the function is exited. 

```
raysH raysSquare raysShy raysE raysB raysZ
=>
function square(param){
    return param * param;
}
```

Methods and variables are helpful functions for you, the programmer, as are the presence of multiplication and division
operators. If you want, see if you can strip down your code to use only valid raysfire emotes!

### I/O
raysLick: Accepts input. Puts the characters entered onto the stack.
Example:
```
raysLick
=>
//retrieves entered characters, and puts them in the stack
```

raysQ: Writes one letter to STD:OUT. The character is consumed.
Example:
```
raysThree raysQ
=>
//prints ASCII char 3, which isn't visible.
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
Conditional operator: variable, followed by raysShrug, followed by an expression, a raysT, another expression, 
and a raysFox. Variable evaluates. If <= 0, or stack is empty, the first expression evaluates, otherwise, the second does.
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

### Looping
raysC - Marks the loop point.
raysLurk - Go to the loop point.
Example:
```
rays3 raysC raysShrug rays3 rays2 raysI raysT rays3 rays2 raysI raysI raysD raysFox
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
raysFox - End of if statement
raysH - Marks the beginning of method declaration  
raysI - Subtraction  
raysL  
raysLick - Accepts input
raysLove - Assignment  
raysLurk - Marks end of loop  
raysP - Addition  
raysQ - Print output  
raysShock - Swap  
raysShrug - Conditional operator  
raysShy - Indicates "one" parameter in method declaration.
raysT - "Else" operator
raysThump - Roll
raysZ - Marks the end of method declaration