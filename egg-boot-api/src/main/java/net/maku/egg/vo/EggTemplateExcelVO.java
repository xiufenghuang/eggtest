package net.maku.egg.vo;

import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fhs.core.trans.vo.TransPojo;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;

/**
 * 蛋品模板
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class EggTemplateExcelVO implements TransPojo {
	@ExcelIgnore
	private Integer id;

	@ExcelProperty("模板名称")
	private String name;

	@ExcelProperty("模板JSON参数")
	private String options;

}