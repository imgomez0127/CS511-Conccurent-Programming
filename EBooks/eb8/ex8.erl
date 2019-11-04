-module(ex8).
-compile(export_all).
servlet(From,N) ->
    receive
        {From, Ref, N} -> From ! {self(),Ref, gotIt};
        {From, Ref, _} -> From ! {self(),Ref,notIt},servlet(From,N)
    end.

server() ->
    receive
        {From, Ref, start} ->
            S = spawn(?MODULE,servlet,[From,rand:uniform(20)]),
            From ! {self(),Ref,S},
            server()
    end.

start() ->
    spawn(fun server/0).
    
client_helper(S) ->
    Ref = make_ref(),
    S!{self(),Ref, rand:uniform(20)},
    receive
        {S, Ref, gotIt} -> gotIt;
        {S, Ref, notIt} -> client_helper(S)
    end.

client(S) ->
    Ref = make_ref(),
    S!{self(),Ref,start},
    receive 
        {S,Ref,New_S} -> client_helper(New_S);
        _ -> error
    end.
