package com.example

import * from whiley.lang.*

void ::main(System.Console sys):
    sys.out.println("Hello World")

int ::getOne() ensures $ == 1:
    return 2

