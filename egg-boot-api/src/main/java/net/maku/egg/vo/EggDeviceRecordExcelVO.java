package net.maku.egg.vo;

import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fhs.core.trans.vo.TransPojo;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import java.util.Date;

/**
 * 设备记录表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class EggDeviceRecordExcelVO implements TransPojo {
	@ExcelIgnore
	private Long id;

	@ExcelProperty("设备SN编号")
	private String sn;

	@ExcelProperty("售卖重量")
	private Double soldWeight;

	@ExcelProperty("创建时间")
	private Date createTime;

}