package com.example.project.pharmacy.service;

import com.example.project.pharmacy.cache.PharmacyRedisTemplateService;
import com.example.project.pharmacy.dto.PharmacyDto;
import com.example.project.pharmacy.entity.Pharmacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacySearchService {

    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    public List<PharmacyDto> searchPharmacyDtoList() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // redis
        List<PharmacyDto> pharmacyDtoList = pharmacyRedisTemplateService.findAll();
        if(!pharmacyDtoList.isEmpty()) {
            stopWatch.stop();
            log.info("redis findAll success!");
            log.info("redis read time:{}",stopWatch.getTotalTimeMillis());
            return pharmacyDtoList;
        }
        stopWatch.stop();

        // db
        stopWatch = new StopWatch();
        stopWatch.start();
        List<Pharmacy> list = pharmacyRepositoryService.findAll();
        if(!list.isEmpty()){
            stopWatch.stop();
            log.info("db findAll success!");
            log.info("db read time:{}",stopWatch.getTotalTimeMillis());
        }
        if(stopWatch.isRunning()){stopWatch.stop();}
        return list
                .stream()
                .map(this::convertToPharmacy)
                .collect(Collectors.toList());
    }

    public List<PharmacyDto> searchPharmacyDtoList(String depthName) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // redis
        List<PharmacyDto> pharmacyDtoList = pharmacyRedisTemplateService.findByDepth(depthName);
        if(!pharmacyDtoList.isEmpty()) {
            stopWatch.stop();
            log.info("redis findAll success!");
            log.info("redis read time:{}",stopWatch.getTotalTimeMillis());
            return pharmacyDtoList;
        }
        stopWatch.stop();

        // db
        stopWatch = new StopWatch();
        stopWatch.start();
        List<Pharmacy> list = pharmacyRepositoryService.findAll();
        if(!list.isEmpty()){
            stopWatch.stop();
            log.info("db findAll success!");
            log.info("db read time:{}",stopWatch.getTotalTimeMillis());
        }
        if(stopWatch.isRunning()){stopWatch.stop();}
        return list
                .stream()
                .map(this::convertToPharmacy)
                .collect(Collectors.toList());
    }

    private PharmacyDto convertToPharmacy(Pharmacy pharmacy) {
        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .pharmacyName(pharmacy.getPharmacyName())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .build();
    }
}
