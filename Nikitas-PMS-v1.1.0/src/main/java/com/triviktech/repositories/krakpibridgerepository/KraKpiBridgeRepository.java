package com.triviktech.repositories.krakpibridgerepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.triviktech.entities.kra.KRA;
import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.entities.krakpibridge.KraKpiBridge;

import jakarta.transaction.Transactional;

public interface KraKpiBridgeRepository extends JpaRepository<KraKpiBridge, Long> {
     boolean existsByKraAndKraKpi(KRA kra, KraKpi kraKpi);

     List<KraKpiBridge> findAllByKraKpi(KraKpi kraKpi);

     @Modifying
     @Transactional
     @Query("DELETE FROM KraKpiBridge b WHERE b.kraKpi = :kraKpi")
     void deleteByKraKpi(@Param("kraKpi") KraKpi kraKpi);

}
