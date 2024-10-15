package com.example.project.pharmacy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepthDto {
    String name;
    Set<Long> pharmacyIds;

    void addPharmacyIds(Long pharmacyId){ pharmacyIds.add(pharmacyId); }
    void addAllPharmacyIds(Set<Long> pharmacyIds){ this.pharmacyIds.addAll(pharmacyIds); }
}
