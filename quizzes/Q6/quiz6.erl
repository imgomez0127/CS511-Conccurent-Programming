%"I pledge my honor that I have abided by the Stevens honor system" - igomez1 gung
-module(quiz6).
-compile(export_all).

dryCleaner(Clean,Dirty) ->
    receive
        {dropOffOverall} -> dryCleaner(Clean,Dirty+1);
        {From,Ref,dryCleanItem} when Dirty > 0-> 
            From ! {self(),Ref,ok},
            dryCleaner(Clean+1,Dirty-1);
        {From,Ref,pickUpOverall} when Clean > 0 -> 
            From ! {self(),Ref,ok},
            dryCleaner(Clean-1,Dirty)
    end.

employee(DC) ->
    DC ! {dropOffOverall},
    Ref = make_ref(),
    DC ! {self(),Ref,pickUpOverall},
    receive
        {_From,Ref,ok} -> done
    end.

dryCleanMachine(DC) ->
    Ref = make_ref(),
    DC ! {self(),Ref,dryCleanItem},
    receive
        {DC,Ref,ok} -> 
            DC ! {self(),Ref,ok}, 
            timer:sleep(1000), 
            dryCleanMachine(DC)
    end.

start(E,M)->
    DC=spawn(?MODULE ,dryCleaner ,[0,0]),
    [spawn(?MODULE ,employee ,[DC]) || _ <- lists:seq(1,E)],
    [spawn(?MODULE ,dryCleanMachine ,[DC]) || _ <- lists:seq(1,M)].
