# AutomatonTheory
Automaton Theory: Kotlin programming language using Swing for the graphical user interface

This project is about all the Computer Theory of Automatons
Includes algorithms for solving automatons issues
Graphical user interface for modelink automatons

# Solving
Deterministic Finite Automaton (DFA) also known as Finite State Machine(FSA)
Non Deterministic Finite Automaton (NFA)
Non Deterministic Finite Automaton Epsilon (NFAe)
Push Down Automaton (PDA)
Turing Machine

# Converting
From NFA to DFA
From NFAe to DFA
From DFA to Regular Expression(Regex)
From Context Free Grammar(CFG or CFL) to PDA
Minimizing DFA
Regular Expression to NFAe

# Operations
Union
Intersection
Substraction
Complement

# Usage
When converting from regular expression to nfae it is necessary to append a dot(.) for every union
for instance:
the regex "(0+1)*0" , when inputed should be "(0+1)*.0"

When inserting turing machine transitions the mask is (currentSymbol, newSymbol, Movement)
Something like "0,0,L"  or "1,1,R"

Movements are L for left, R for right 
Whenever you want to represent blank as a symbol use capital case 'B'

# License 
Copyright (c) 2016 Victor Ramirez

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
associated documentation files (the "Software"), to deal in the Software without restriction, including 
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the 
following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions 
of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
IN THE SOFTWARE.
