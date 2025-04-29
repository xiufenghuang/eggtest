package net.maku.egg.vo;

import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fhs.core.trans.vo.TransPojo;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;

/**
 * 设备电子价签关系表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class EggDevicePriceTagRelationExcelVO implements TransPojo {
	@ExcelProperty("关系ID")
	private Long id;

	@ExcelProperty("设备SN编号")
	private String sn;

	@ExcelProperty("电子价签ID")
	private Long priceTagId;

}