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

### If/Else
Conditional operator: variable (optional), followed by two methods, followed by raysShrug, an arbitrary number of parameters, and a raysFox
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

### Grouping
raysC => Functions like a (. Allows you to easily stack evaluations. raysFox functions as the ).
Example:
```
raysC rays2 rays2 raysB rays2 raysB rays3 raysB rays3 raysB raysFox raysOne raysQ =>
print(2 * 2 * 2 * 3 * 3) => print(72) => "H"
```

### Reserved Words
All of rays emotes are reserved for future use.