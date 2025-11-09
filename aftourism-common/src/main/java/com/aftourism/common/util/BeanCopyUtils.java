package com.aftourism.common.util;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Bean 拷贝工具类，简化对象属性转换。
 */
public final class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    /**
     * 拷贝单个对象。
     */
    public static <S, T> T copy(S source, Supplier<T> targetSupplier) {
        if (source == null) {
            return null;
        }
        T target = targetSupplier.get();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 拷贝列表对象。
     */
    public static <S, T> List<T> copyList(List<S> sourceList, Supplier<T> targetSupplier) {
        if (sourceList == null) {
            return List.of();
        }
        return sourceList.stream()
                .map(item -> copy(item, targetSupplier))
                .collect(Collectors.toList());
    }
}
