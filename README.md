# kotlin-exe-jar
Template project container for kotlin scripts

## Result Example:

--------------------------------Schema--------------------------------
DataFrame with 4 observations
first_name    [Str]  Franz, Horst, Javier, Javier
last_name     [Str]  Smith, Keanes, gomez, Toro
age           [Dbl]  23, 12, 35, 20
weight        [Dbl]  88, 70, 80, 90
Test          [Int]  0, 0, 0, 0
full_name     [Str]  Franz Smith, Horst Keanes, Javier gomez, Javier Toro
W_A_relation  [Dbl]  3.826, 5.833, 2.286, 4.5
---------------------------------------------Data Frame---------------------------------------------
A DataFrame: 4 x 7
    first_name   last_name   age   weight   Test      full_name   W_A_relation
1        Franz       Smith    23       88      0    Franz Smith          3.826
2        Horst      Keanes    12       70      0   Horst Keanes          5.833
3       Javier       gomez    35       80      0   Javier gomez          2.286
4       Javier        Toro    20       90      0    Javier Toro            4.5

--------------------Summarize--------------------
A DataFrame: 3 x 2
    first_name   n
1        Franz   1
2        Horst   1
3       Javier   2
