package com.triviktech.entities.krakpibridge;

import com.triviktech.entities.kra.KRA;
import com.triviktech.entities.krakpi.KraKpi;

import jakarta.persistence.*;

@Entity
@Table(name = "kra_kpi_bridge")
public class KraKpiBridge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kra_id", nullable = false)
    private KRA kra;

    @ManyToOne
    @JoinColumn(name = "kra_kpi_id", nullable = false)
    private KraKpi kraKpi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KRA getKra() {
        return kra;
    }

    public void setKra(KRA kra) {
        this.kra = kra;
    }

    public KraKpi getKraKpi() {
        return kraKpi;
    }

    public void setKraKpi(KraKpi kraKpi) {
        this.kraKpi = kraKpi;
    }
}
