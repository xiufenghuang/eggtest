package net.maku.egg.service;

import net.maku.egg.entity.EggFileBinEntity;
import net.maku.egg.query.EggFileBinQuery;
import net.maku.egg.vo.EggFileBinVO;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.BaseService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * bin文件版本控制
 *
 * @author 孙超 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface EggFileBinService extends BaseService<EggFileBinEntity> {

    PageResult<EggFileBinVO> page(EggFileBinQuery query);

    EggFileBinVO get(Long id);


    void save(EggFileBinVO vo);

    void update(EggFileBinVO vo);

    void delete(List<Long> idList);

    EggFileBinEntity binaryFile(int type);

    ResponseEntity<Resource> downloadFileInChunks(int type, EggFileBinEntity entity, String rangeHeader);

}