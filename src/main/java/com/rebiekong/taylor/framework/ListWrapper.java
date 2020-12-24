package com.rebiekong.taylor.framework;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ListWrapper
 *
 * @author rebie
 * @since 2020/12/24.
 */
public class ListWrapper<T> {

    private final List<T> data;

    public ListWrapper(List<T> data) {
        this.data = data;
    }

    public static <T> ListWrapper<T> wrapper(List<T> data) {
        return new ListWrapper<>(data);
    }

    // TODO this is not a deep clone
    public List<T> getData() {
        return new ArrayList<>(data);
    }

    public <KEY> ListWrapper<T> merge(ListWrapper<T> subList, Function<T, KEY> keyFunc) {
        return merge(subList.data, keyFunc);
    }

    public <R> ListWrapper<R> map(Function<T, R> mapper) {
        return new ListWrapper<>(this.getData().stream().map(mapper).collect(Collectors.toList()));
    }

    public <R> ListWrapper<R> flatMap(Function<T, List<R>> mapper) {
        return new ListWrapper<>(this.getData().stream().flatMap(mapper.andThen(Collection::stream)).collect(Collectors.toList()));
    }

    public void print() {
        this.getData().forEach(System.out::println);
    }

    public ListWrapper<T> distinct() {
        return ListWrapper.wrapper(this.getData().stream().distinct().collect(Collectors.toList()));
    }

    public <KEY> ListWrapper<T> distinct(Function<T, KEY> keyFunc) {
        return ListWrapper.wrapper(this.getData().stream().collect(Collectors.groupingBy(keyFunc)).values().stream().map(a -> a.get(0)).collect(Collectors.toList()));
    }

    public <KEY> ListWrapper<T> merge(List<T> subList, Function<T, KEY> keyFunc) {
        List<T> res = getData();
        Set<KEY> mainMap = data.stream().map(keyFunc).collect(Collectors.toSet());
        Map<KEY, T> subMap = subList.stream().collect(Collectors.toMap(keyFunc, a -> a));
        subMap.keySet().forEach(key -> {
            if (!mainMap.contains(key)) {
                res.add(subMap.get(key));
            }
        });
        return new ListWrapper<>(res);
    }
}
