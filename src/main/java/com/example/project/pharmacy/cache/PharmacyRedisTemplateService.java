package com.example.project.pharmacy.cache;

import com.example.project.pharmacy.dto.DepthDto;
import com.example.project.pharmacy.dto.PharmacyDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRedisTemplateService {

    private static final String CACHE_KEY = "PHARMACY";
    private static final String DEPTH_KEY = "PHARMACY_DEPTH";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(PharmacyDto pharmacyDto) {
        if (Objects.isNull(pharmacyDto) || Objects.isNull(pharmacyDto.getId())) {
            log.error("Required Values must not be null");
            return;
        }

        try {
            hashOperations.put(CACHE_KEY,
                    pharmacyDto.getId().toString(),
                    compress(serializePharmacyDto(pharmacyDto)).toString());
//                    serializePharmacyDto(pharmacyDto));
//            log.info("byte : {}",serializePharmacyDto(pharmacyDto).getBytes().length);
            log.info("byte : {}",compress(serializePharmacyDto(pharmacyDto)).length);
            redisTemplate.expire(CACHE_KEY,1, TimeUnit.DAYS);
            log.info("[PharmacyRedisTemplateService save success] id: {}", pharmacyDto.getId());
        } catch (IOException e) {
            log.error("[PharmacyRedisTemplateService save error] {}", e.getMessage());
        }
    }

    public List<PharmacyDto> findByPharmacyIds(Set<Long> pharmacyIds){
        List<PharmacyDto> list = new ArrayList<>();
        try {
            for (Long pharmacyId : pharmacyIds) {
                String value = decompress(hashOperations.entries(CACHE_KEY).get(pharmacyId.toString()).getBytes());
//                String value = hashOperations.entries(CACHE_KEY).get(pharmacyId.toString());
                list.add(deserializePharmacyDto(value));
            }
            return list;
        }
        catch (JsonProcessingException e) {
            log.error("[PharmacyRedisTemplateService findAll error]: {}", e.getMessage());
            return Collections.emptyList();
        } catch (IOException e) {
            log.error("decompress error");
            throw new RuntimeException(e);
        }
    }

    public List<PharmacyDto> findAll() {
        try {
            List<PharmacyDto> list = new ArrayList<>();
            for (String value : hashOperations.entries(CACHE_KEY).values()) {
                PharmacyDto pharmacyDto = deserializePharmacyDto(decompress(value.getBytes()));
//                PharmacyDto pharmacyDto = deserializePharmacyDto(value);
                list.add(pharmacyDto);
            }
            return list;

        } catch (JsonProcessingException e) {
            log.error("[PharmacyRedisTemplateService findAll error]: {}", e.getMessage());
            return Collections.emptyList();
        } catch (IOException e) {
            log.error("decompress error");
            throw new RuntimeException(e);
        }
    }

    public List<PharmacyDto> findByDepth(String depthName) {
        try {
            DepthDto depthDto = deserializeDepthDto(decompress(hashOperations.entries(DEPTH_KEY).get(depthName).getBytes()));
//            DepthDto depthDto = deserializeDepthDto(hashOperations.entries(DEPTH_KEY).get(depthName));
            return findByPharmacyIds(depthDto.getPharmacyIds());

        }catch (JsonProcessingException e){
            log.error("[PharmacyRedisTemplateService find error]: {}",e.getMessage());
            return Collections.emptyList();
        } catch (IOException e) {
            log.error("decompress error");
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {
        hashOperations.delete(CACHE_KEY, String.valueOf(id));
        log.info("[PharmacyRedisTemplateService delete]: {}", id);
    }

    private String serializePharmacyDto(PharmacyDto pharmacyDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(pharmacyDto);
    }

    private PharmacyDto deserializePharmacyDto(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, PharmacyDto.class);
    }

    private String serializeDepthDto(DepthDto depthDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(depthDto);
    }

    private DepthDto deserializeDepthDto(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, DepthDto.class);
    }

    public byte[] compress(final String str) throws IOException {
        if ((str == null) || (str.length() == 0)) {
            return null;
        }

        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(str.getBytes("UTF-8"));
        gzip.flush();
        gzip.close();
        return obj.toByteArray();
    }

    public String decompress(final byte[] compressed) throws IOException {
        final StringBuilder outStr = new StringBuilder();
        if ((compressed == null) || (compressed.length == 0)) {
            return "";
        }
        if (isCompressed(compressed)) {
            final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                outStr.append(line);
            }
        } else {
            outStr.append(compressed);
        }
        return outStr.toString();
    }

    public static boolean isCompressed(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }
}
