# Rayscode
A project to create a programming language based on the raysfire emotes on Twitch. All commands are one of
the raysfire emotes, outside of optional support for variables/methods, which are still raysfire-like.

##Goals
1. Draft basic documentation on code functionality [done]
a. Add additional functionality. Possible end goal is to create functionality for each emote.
2. Create an interpreter that correctly parses the rayscode language [ongoing]
3. Create a Discord bot which executes rayscode on-the-fly

##Rayscode Standard
(I am by no means an expert on drafting this sort of legislation. I literally wrote this over the span
of a few hours one night)

1. Arbitrary memory must be readable/writeable
All variables and methods must be prefixed with "rays" (Technically this is wimp mode I guess?)
rays2 and rays3 make the only two literal numbers. Methods and combinations are called via postfix notation (so stack based)
Standard operators:
Addition: raysP
Subtraction: raysI
Multiplication: raysB
Division: raysA
Complete a line happens ends with a raysFox (The semicolon, so to speak)

Example:
raysFour rays2 rays2 raysP raysFox => var four = 2 + 2.

Method declarations start with raysH, followed by the name, and arguments. Ends with raysZ and the return values

Example:
raysH raysMethod raysOne raysTwo
raysZ raysOne raysTwo raysP raysFox
=>
function method(one, two){
    return one + two;
}

Multiple returns are permissible. Separate by raysFox. Order of return is preserved.

Example:
raysH raysMethod raysOne raysTwo
raysZ raysOne raysTwo raysP raysFox raysTwo raysOne raysI raysFox
=>
function method(one, two){
    return one + two, two - one;
}

2. If/else constructs must exist
Conditional operator: variable (optional), followed by two methods, followed by raysShrug, an arbitrary number of parameters, and a raysFox
- Variable evaluates. If <= 0, calls the first method. If > 0, calls the second method

3. (Optional) I/O
raysT: Accepts input. Puts the specified number of characters entered onto the stack
Example:
raysThree raysT
=>
//retrieves three characters, and puts them in the stack

raysQ: Writes to console. Writes the specified number of characters on the stack as characters
Example:
raysThree raysQ
=>
//prints three chars at the top of the stack

4. Grouping
raysC => Functions like a (. Allows you to easily stack evaluations. raysFox functions as the ).

raysC rays2 rays2 raysB rays2 raysB rays3 raysB rays3 raysB raysFox raysOne raysQ =>
print(2 * 2 * 2 * 3 * 3) => print(72) => "H"

Example:
raysTest raysNegative raysPositive raysShrug raysThree raysTwo raysOne raysFox
=>
if(test <= 0){
    negative(one, two, three);
} else {
    positive(one, two, three);
}

Reserved words: All of rays emotes are reserved for future use