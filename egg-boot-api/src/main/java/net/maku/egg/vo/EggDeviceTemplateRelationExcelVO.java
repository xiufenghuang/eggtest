package net.maku.egg.vo;

import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fhs.core.trans.vo.TransPojo;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import java.util.Date;

/**
 * 设备模版关联表
 *
 * @author xiufenguhuang babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class EggDeviceTemplateRelationExcelVO implements TransPojo {
	@ExcelIgnore
	private Long id;

	@ExcelProperty("设备ID")
	private Integer deviceId;

	@ExcelProperty("模板ID")
	private Integer templateId;

}