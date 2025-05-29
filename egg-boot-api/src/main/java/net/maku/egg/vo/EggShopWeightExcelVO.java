package net.maku.egg.vo;

import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fhs.core.trans.vo.TransPojo;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import java.util.Date;

/**
 * 店铺设备重量记录
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class EggShopWeightExcelVO implements TransPojo {
	@ExcelProperty("记录id")
	private Long id;

	@ExcelProperty("店铺id")
	private Long shopId;

	@ExcelProperty("店铺设备重量")
	private Double weight;

}