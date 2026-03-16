package fr.prefecture.sidsic.dashboard_sidsic.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MembreDTOLastCO {
  private LocalDate lastco;
  private Long id;
}
