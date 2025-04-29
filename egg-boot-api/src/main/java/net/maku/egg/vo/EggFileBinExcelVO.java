package net.maku.egg.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import lombok.Data;

import java.util.Date;

/**
 * bin文件版本控制
 *
 * @author 孙超 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class EggFileBinExcelVO implements TransPojo {
	@ExcelProperty("bin文件id")
	private Long id;

	@ExcelProperty("文件URL")
	private String fileUrl;

	@ExcelProperty("文件大小")
	private Long fileSize;

	@ExcelProperty("文件类型")
	private String fileTypeLabel;

	@ExcelIgnore
	@Trans(type = TransType.DICTIONARY, key = "file_bin_type", ref = "fileTypeLabel")
	private Integer fileType;

	@ExcelProperty("文件版本")
	private String fileVersion;

	@ExcelProperty("创建时间")
	private Date createTime;

}