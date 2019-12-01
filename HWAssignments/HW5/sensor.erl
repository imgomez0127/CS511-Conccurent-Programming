-module(sensor).
-compile(export_all).
-author("Ian Gomez").

produce_reading(Watcher_PID,ID) ->
    Reading = rand:uniform(11),
    if Reading == 11 ->
        exit(anomalous_reading);
    true ->
        Watcher_PID ! {ID,Reading},
        Sleep_time = rand:uniform(10000),
        timer:sleep(Sleep_time),
        produce_reading(Watcher_PID,ID)
    end.

