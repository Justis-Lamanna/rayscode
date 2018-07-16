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

Method declarations start with raysH, followed by the name, and arguments. Ends with raysZ and the return values.
Example:
```
raysH raysMethod raysOne raysTwo
raysZ raysOne raysTwo raysP raysFox
=>
function method(one, two){
    return one + two;
}
```

Multiple returns are permissible. Separate by raysFox. Order of return is preserved.
Example:
```
raysH raysMethod raysOne raysTwo
raysZ raysOne raysTwo raysP raysFox raysTwo raysOne raysI raysFox
=>
function method(one, two){
    return one + two, two - one;
}
```

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

### Infinity
rays3c marks a special character, called the infinity character. This could be considered analogous to a null in a
standard programming language, although takes on additional roles in rayscode. In the case of I/O, this marks that
all characters should be read from input, or the whole stack should be read to output.

```
rays3c raysT
=>
//retrieves every character typed, and places on the stack
```

### If/Else
Conditional operator: variable, followed by two methods, followed by raysShrug, an arbitrary number of parameters, and a raysFox
Variable evaluates. If <= 0, calls the first method. If > 0, calls the second method.
Example:
```
raysTest raysNegative raysPositive raysShrug raysThree raysTwo raysOne raysFox
=>
if(test <= 0){
    negative(one, two, three);
} else {
    positive(one, two, three);
}
```

### Grouping
~~raysC => Functions like a (. Allows you to easily stack evaluations. raysFox functions as the ).
Example:~~ 

Grouping has been deprecated, as it is unnecessary in a stack-based language.

### Swapping
raysShock - Swaps some number of elements in the stack. Pass infinite to swap the entire stack.
Example:
```
rays2 rays2 rays3 rays3 rays3c raysShock
=>
//rays3 rays3 rays2 rays2
```

### Duplicating
raysC - Duplicates some number of elements in the stack. Pass infinite to duplicate the entire stack.
Example:
```
rays2 rays3 rays3c raysC
=>
//rays3 rays2
```

### Reserved Words
All fox emotes are reserved for future use. All non-fox emotes are reserved for potential future use.
rays2 - Literal integer 2
rays3 - Literal integer 3
rays3c - Literal integer infinity
raysA - Integer division
raysB - Multiplication
raysC
raysD
raysE
raysFox - Semicolon, and other marker
raysH - Marks the beginning of method declaration
raysI - Subtraction
raysL
raysLick
raysLove
raysLurk
raysP - Addition
raysQ - Print output
raysShock - Swap
raysShrug - Conditional operator
raysShy
raysT - Accept input
raysThump
raysZ - Marks the end of method declaration