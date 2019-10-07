-module(ex).
-compile(export_all).

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

