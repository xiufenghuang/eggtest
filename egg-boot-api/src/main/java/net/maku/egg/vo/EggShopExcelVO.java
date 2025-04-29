package net.maku.egg.vo;

import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fhs.core.trans.vo.TransPojo;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import java.util.Date;

/**
 * 店铺表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class EggShopExcelVO implements TransPojo {
	@ExcelIgnore
	private Long id;

	@ExcelProperty("机构ID")
	private Long orgId;

	@ExcelProperty("店铺名称")
	private String name;

	@ExcelProperty("店铺地址")
	private String address;

}