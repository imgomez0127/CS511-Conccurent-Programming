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
    
client_helper(S,Tries) ->
    Ref = make_ref(),
    S!{self(),Ref, rand:uniform(20)},
    receive
        {S, Ref, gotIt} -> io:format("Client ~p guessed in ~w attempts ~n",[self(),Tries]);
        {S, Ref, notIt} -> client_helper(S,Tries+1)
    end.

client(S) ->
    Ref = make_ref(),
    S!{self(),Ref,start},
    receive 
        {S,Ref,New_S} -> client_helper(New_S,0);
        _ -> error
    end.
