package net.maku.system.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-13 14:46
 **/
@Data
@Accessors(chain = true)
public class FileVO {
    private String name;
    private String url;
}
