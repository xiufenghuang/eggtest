package net.maku.egg.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class EggGateWayDevice {
    @NotBlank(message = "设备SN不能为空")
    private String sn;

    @NotEmpty(message = "设备重量数据不能为空")
    private List<DeviceWeight> devices;

    @Data
    public static class DeviceWeight {
        private Double previousWeight;
        private Double currentWeight;
    }
} 