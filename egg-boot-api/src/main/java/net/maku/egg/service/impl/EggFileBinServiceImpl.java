package net.maku.egg.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.maku.egg.convert.EggFileBinConvert;
import net.maku.egg.dao.EggFileBinDao;
import net.maku.egg.entity.EggFileBinEntity;
import net.maku.egg.query.EggFileBinQuery;
import net.maku.egg.service.EggFileBinService;
import net.maku.egg.vo.EggFileBinVO;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.impl.BaseServiceImpl;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * bin文件版本控制
 *
 * @author 孙超 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
@AllArgsConstructor
public class EggFileBinServiceImpl extends BaseServiceImpl<EggFileBinDao, EggFileBinEntity> implements EggFileBinService {

    @Override
    public PageResult<EggFileBinVO> page(EggFileBinQuery query) {
        IPage<EggFileBinEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(EggFileBinConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }


    private LambdaQueryWrapper<EggFileBinEntity> getWrapper(EggFileBinQuery query){
        LambdaQueryWrapper<EggFileBinEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(ObjectUtil.isNotEmpty(query.getId()), EggFileBinEntity::getId, query.getId());
        wrapper.like(ObjectUtil.isNotEmpty(query.getFileType()), EggFileBinEntity::getFileType, query.getFileType());
        wrapper.like(ObjectUtil.isNotEmpty(query.getFileVersion()), EggFileBinEntity::getFileVersion, query.getFileVersion());

        return wrapper;
    }


    @Override
    public EggFileBinVO get(Long id) {
        EggFileBinEntity entity = baseMapper.selectById(id);
        EggFileBinVO vo = EggFileBinConvert.INSTANCE.convert(entity);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(EggFileBinVO vo) {
        EggFileBinEntity entity = EggFileBinConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EggFileBinVO vo) {
        EggFileBinEntity entity = EggFileBinConvert.INSTANCE.convert(vo);

        updateById(entity);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);

    }

    @Override
    public EggFileBinEntity binaryFile(int type) {
        // 创建 LambdaQueryWrapper
        LambdaQueryWrapper<EggFileBinEntity> queryWrapper = new LambdaQueryWrapper<>();
        // 设置查询条件
        queryWrapper.eq(EggFileBinEntity::getFileType, type); // 等于 type
        queryWrapper.orderByDesc(EggFileBinEntity::getCreateTime); // 按照 created_at 降序排序
        queryWrapper.last("LIMIT 1"); // 只取一条数据

        // 查询最新的数据
        EggFileBinEntity latestData = this.getOne(queryWrapper);

        if (latestData != null) {
            // 处理获取到的数据
            return latestData;
        } else {
            // 没有找到数据
            return null;
        }
    }

    @Override
    public ResponseEntity<Resource> downloadFileInChunks(int type, EggFileBinEntity entity, String rangeHeader) {
        String url = entity.getFileUrl();

        if (url.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("无法访问文件 URL，状态码：" + connection.getResponseCode());
            }

            long fileSize = entity.getFileSize();
            if (fileSize == -1) {
                throw new IOException("无法获取文件大小，可能是文件 URL 无效或服务器不支持 Content-Length");
            }

            long rangeStart = 0, rangeEnd = fileSize - 1;

            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                String[] ranges = rangeHeader.replace("bytes=", "").split("-");
                rangeStart = Long.parseLong(ranges[0]);
                if (ranges.length > 1) {
                    if(Long.parseLong(ranges[1]) <= rangeEnd) {
                        rangeEnd = Long.parseLong(ranges[1]);
                    }
                }
            }

            if (rangeStart > rangeEnd || rangeEnd >= fileSize) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
            }

            long chunkSize = rangeEnd - rangeStart + 1;

            InputStream inputStream = new URL(url).openStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            long skipped = 0;

            while (skipped < rangeStart) {
                long result = inputStream.skip(rangeStart - skipped);
                if (result == 0) {
                    throw new IOException("Failed to skip bytes");
                }
                skipped += result;
            }

            int bytesRead;
            long bytesReadTotal = 0;
            while (bytesReadTotal < chunkSize && (bytesRead = inputStream.read(buffer)) != -1) {
                if (bytesReadTotal + bytesRead > chunkSize) {
                    bytesRead = (int) (chunkSize - bytesReadTotal);
                }
                baos.write(buffer, 0, bytesRead);
                bytesReadTotal += bytesRead;
            }

            inputStream.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize);
            headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
            headers.setContentLength(chunkSize);

            ByteArrayResource byteArrayResource = new ByteArrayResource(baos.toByteArray());
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .body(byteArrayResource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}