package net.maku.egg.vo;

import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fhs.core.trans.vo.TransPojo;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import java.util.Date;

/**
 * 电子价签表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class EggPriceTagInfoExcelVO implements TransPojo {
	@ExcelIgnore
	private Long id;

	@ExcelProperty("电子价签名称")
	private String name;

	@ExcelProperty("电子价签图片路径")
	private String imageUrl;

}