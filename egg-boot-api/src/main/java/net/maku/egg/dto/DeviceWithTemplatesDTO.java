package net.maku.egg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.maku.egg.entity.EggDeviceEntity;
import net.maku.egg.entity.EggTemplateEntity;

import java.util.List;

/**
 * 功能：
 * 作者：程序员青戈
 * 日期：2025/4/23 15:50
 */
@Data
@AllArgsConstructor
public class DeviceWithTemplatesDTO {
    private EggDeviceEntity device;
    private List<EggTemplateEntity> templates;
    private String shopName;
    private List<String> templateNames;

}