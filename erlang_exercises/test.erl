-module(test).
-compile(export_all).

list([])     -> [];
list([Elem]) -> [Elem];
list(List)   -> list(List, length(List), []).

list([], 0, Result) ->
    Result;
list(List, Len, Result) ->
    {Elem, Rest} = nth_rest(random:uniform(Len), List),
    list(Rest, Len - 1, [Elem|Result]).

nth_rest(N, List) -> nth_rest(N, List, []).

nth_rest(1, [E|List], Prefix) -> {E, Prefix ++ List};
nth_rest(N, [E|List], Prefix) -> nth_rest(N - 1, List, [E|Prefix]).

is_sorted([_H]) ->
    true;
is_sorted([H|T]) ->
    [H1|_T1] = T,
    (H =< H1) and is_sorted(T).
print_list_helper([H])->
    io:fwrite("~tc~n",[H]),
    io:fwrite("]");
print_list_helper([H|T])->
    io:fwrite("~tc~n",[H]),
    io:fwrite(","),
    print_list_helper(T).
print_list(Lst) ->
    io:fwrite("["),
    print_list_helper(Lst).
bogosort(Lst) ->
    io:fwrite(Lst),
    case is_sorted(Lst) of 
        true -> Lst;
        false -> bogosort(Lst)
    end.
