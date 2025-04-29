package net.maku.egg.vo;

import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fhs.core.trans.vo.TransPojo;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import java.util.Date;

/**
 * 设备表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class EggDeviceExcelVO implements TransPojo {
	@ExcelIgnore
	private Long id;

	@ExcelProperty("店铺ID")
	private Long shopId;

	@ExcelProperty("设备SN编号")
	private String sn;

	@ExcelProperty("设备名称")
	private String name;

	@ExcelProperty("设备状态")
	private String statusLabel;

	@ExcelIgnore
	@Trans(type = TransType.DICTIONARY, key = "device_status", ref = "statusLabel")
	private Integer status;

	@ExcelProperty("设备类型")
	private Integer type;

	@ExcelProperty("上一次重量")
	private Double previousWeight;

	@ExcelProperty("当前重量")
	private Double currentWeight;

}