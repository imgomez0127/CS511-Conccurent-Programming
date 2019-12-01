-module(watcher).
-compile(export_all).
-author("Ian Gomez").
find_id(_PID,[]) ->
  error;
find_id(_PID,[{ID,_PID}|_T]) ->
     ID;
find_id(PID,[_H|T]) ->
    find_id(PID,T).

watcher_helper(Sensors_list) ->
    receive
        {ID,Reading} -> io:format("Sensor: ~w, Reading: ~w~n",[ID,Reading]);
        {_,_,_,PID,Reason} -> 
                            ID = find_id(PID,Sensors_list),
                             io:format("Sensor ~w crashed, Reason: ~w~n",[ID,Reason]),
                             SL1 = lists:keydelete(PID,1,Sensors_list),
                             {New_PID,_} = spawn_monitor(sensor,produce_reading,[self(),ID]),
                             SL2 = [{ID,New_PID}|SL1],
                             io:format("Restarted ~w New List ~w~n",[ID,SL2])
    end,
    watcher_helper(Sensors_list).

watcher(Sensors_IDs) ->
    Sensors_list = lists:map(fun (ID) -> 
        {PID,_} = spawn_monitor(sensor,produce_reading,[self(),ID]),
        {ID,PID}
    end,
    Sensors_IDs),
%    Sensors_list = [{ID,PID} || ID <- Sensors_IDs, {PID,_Ref} <- spawn_monitor(sensor,produce_reading,[self(),ID])],
    io:format("~w~n",[Sensors_list]),
    watcher_helper(Sensors_list).
