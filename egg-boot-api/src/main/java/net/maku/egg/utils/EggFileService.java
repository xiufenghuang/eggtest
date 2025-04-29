//package net.maku.egg.utils;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//@Service
//public class EggFileService {
//    @Autowired
//    private RedisTemplate<String, byte[]> redisTemplate;
//
//    public void storeFileChunks(String fileUrl, int chunkSize) throws IOException {
//        URL url = new URL(fileUrl);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//
//        try (InputStream inputStream = connection.getInputStream()) {
//            byte[] buffer = new byte[chunkSize];
//            int bytesRead;
//            int chunkNumber = 0;
//
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                String redisKey = "file:" + fileUrl.hashCode() + ":chunk:" + chunkNumber;
//                byte[] chunkData = new byte[bytesRead];
//                System.arraycopy(buffer, 0, chunkData, 0, bytesRead);
//                redisTemplate.opsForValue().set(redisKey, chunkData);
//                chunkNumber++;
//            }
//        }
//    }
//}