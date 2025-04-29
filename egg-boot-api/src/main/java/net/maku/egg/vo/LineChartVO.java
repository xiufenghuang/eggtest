package net.maku.egg.vo;

import lombok.Data;

import java.util.List;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-10-12 14:52
 **/
@Data
public class LineChartVO {
    private List<Integer> x;
    private List<Double> y;
}
