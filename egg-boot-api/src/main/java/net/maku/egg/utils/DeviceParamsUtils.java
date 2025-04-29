package net.maku.egg.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeviceParamsUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T decodeString(String encode, Class<T> type) throws JsonProcessingException {
        String jsonString = UrlUtil.getURLDecoderString(encode);
        //去除/n/t
        jsonString = jsonString.replaceAll("\\\\n|\\\\t|=", "");
        //去除头尾引号
        jsonString = jsonString.replaceAll("^\"|\"$", "");
        //去除多余斜杠
        jsonString = jsonString.replaceAll("\\\\", "");
        return objectMapper.readValue(jsonString, type);
    }
}