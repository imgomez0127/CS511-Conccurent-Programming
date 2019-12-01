-module(main).
-compile(export_all).
-author("Ian Gomez").

setup_loop(N,_Num_Watchers) when  N == 0->
    ok;
setup_loop(N,Num_Watchers) when Num_Watchers > 0->
    if not(N rem 10 == 0) ->
        IDs = [I || I <- lists:seq(N - N rem 10,N-1)],
        spawn(watcher,watcher,[IDs]),
        setup_loop(N- N rem 10, Num_Watchers - 1);
    true ->
        IDs = [I || I <- lists:seq(N-10,N-1)],
        spawn(watcher,watcher,[IDs]),
        setup_loop(N-10,Num_Watchers-1)
    end.
    
start() ->
    {ok , [ N ]} = io:fread("enter  number  of sensors > ", "~d") ,
    if N =< 1 ->
        io:fwrite("setup: range  must be at  least  2~n", []);
    true  ->
        Num_watchers = 1 + (N div  10),
        setup_loop(N, Num_watchers)
    end.
