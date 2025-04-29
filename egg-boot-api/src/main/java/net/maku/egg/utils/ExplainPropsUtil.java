package net.maku.egg.utils;

import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Optional;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-18 14:24
 **/
public class ExplainPropsUtil {

    //只会处理dataList第一个元素内容
    public static String getPropsLabel(List<String> dataList, String unexpectedValue) {
        return Optional.ofNullable(dataList)
                .flatMap(list -> list.isEmpty() ? Optional.empty() : Optional.of(list.get(0)))
                .filter(label -> StrUtil.isNotEmpty(label.trim()))
                .orElse(unexpectedValue);
    }
}
