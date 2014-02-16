package com.example

//import * from whiley.lang.*

method main(System.Console sys):
    sys.out.println("Hello World")

function getOne() => int i
ensures i == 1:
    return 1

