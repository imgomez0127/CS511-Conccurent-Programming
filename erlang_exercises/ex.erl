-module(ex).
-compile(export_all).
-record(person,{name,age}).

fact(0) ->
    1;
fact(N) when N>0 ->
    N*fact(N-1);
fact(N) when N<0 ->
    -1.

len([]) ->
    0;
len([_|T]) ->
    1+len(T).


mem(_X,[]) ->
    false;
mem(X,[H|_T]) when X =:= H ->
    true;
mem(X,[_|T]) ->
    mem(X,T).

sublist([],_) ->
    true;
sublist([H1|T1],LST2) ->
    mem(H1,LST2) and sublist(T1,LST2).

p() -> #person{name="Tom",age=5}.

mkLeaf(N) ->
    {node,N,{empty},{empty}}.
aTree() -> 
    {node,7,mkLeaf(2),{node,9,mkLeaf(8),{empty}}}.

sizeT({empty})->
    0;
sizeT({node,_D,lt,rt})->
    1+ sizeT(lt) + sizeT(rt). 

sumT({empty})->
    0;
sumT({node,D,lt,rt})->
    D+sumT(lt)+sumT(rt).

mapTree(_F,{empty})->
    {empty};
mapTree(F,{node,D,LT,RT}) ->
    {node,F(D),mapTree(F,LT),mapTree(F,RT)}.

foldT(_F,A,{empty}) ->
    A;
foldT(F,A,{node,D,LT,RT}) ->
    F(D,foldT(F,A,LT),foldT(F,A,RT)).

test() ->
    foldT(fun(X,AL,AR) -> [X|AL++AR] end, [] , aTree()).

test2() ->
    foldT(fun(X,AL,AR) -> AL++[X]++AR end, [] , aTree()).

mirror(T) ->
    foldT(fun(X,AL,AR) -> {node,X,AR,AL} end, {empty},T).

mapT2(F,T)->
    foldT(fun(X,AL,AR) -> {node,F(X),AL,AR} end, {empty}, T).

-record(inh,{name,children}).

theRegistry() ->
    M = maps:new(),
    M1 = maps:put(1,#inh{name="Tom",children=[5,6]},M),
    M2 = maps:put(2,#inh{name="Sue",children=[5]},M1),
    M3 = maps:put(3,#inh{name="Anne",children=[6]},M2),
    M4 = maps:put(4,#inh{name="Jill",children=[]},M3), 
    M5 = maps:put(5,#inh{name="Robert",children=[]},M4),
    maps:put(6,#inh{name="Susan",children=[]},M5).

nameOf(ID,TownR) ->
    (maps:get(ID,TownR))#inh.name.

nameOfChildren(ID,TownR) ->
    lists:map(
        fun(ChildID) -> (maps:get(ChildID,TownR))#inh.name end, 
        (maps:get(ID,TownR))#inh.children).

descendentsOf(ID,TownR) ->
    All_Descendents = [],
    lists:foldr(fun(Descendents) ->
        Next_To_Traverse = queue:new(),
        map(
            fun(Descendent) -> 
                Parent = queue:get(Next_To_Traverse),
                Next_To_Traverse = queue:drop(Next_To_Traverse),
                All_Descendents = All_Descendents ++ (maps:get(ID,TownR))#inh.children,
                Next_To_Traverse = queue:join(
                    Next_To_Traverse,
                    queue:from_list(((maps:get(ID,TownR))#inh.children)))
            end,
            Descendents
        ),
    All_Descendents.
