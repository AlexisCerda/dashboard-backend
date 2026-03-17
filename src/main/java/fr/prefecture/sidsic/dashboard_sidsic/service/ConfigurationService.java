package fr.prefecture.sidsic.dashboard_sidsic.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.prefecture.sidsic.dashboard_sidsic.dto.ConfigurationDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Configuration;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Groupe;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.repository.ConfigurationRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.GroupeRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreRepository;
import jakarta.transaction.Transactional;

@Service
public class ConfigurationService {
  private final ConfigurationRepository configurationRepository;
  private final MembreRepository membreRepository;
  private final GroupeRepository groupeRepository;

  public ConfigurationService(ConfigurationRepository configurationRepository, MembreRepository membreRepository,
      GroupeRepository groupeRepository) {
    this.configurationRepository = configurationRepository;
    this.membreRepository = membreRepository;
    this.groupeRepository = groupeRepository;
  }

  private ConfigurationDTO convertToDTO(Configuration configuration) {
    ConfigurationDTO dto = new ConfigurationDTO();
    dto.setId(configuration.getId());
    dto.setIdMembre(configuration.getMembre() != null ? configuration.getMembre().getId() : null);
    dto.setIdGroupe(configuration.getGroupe() != null ? configuration.getGroupe().getId() : null);
    dto.setNom(configuration.getNom());
    dto.setTaches(configuration.getTaches());
    dto.setNotes(configuration.getNotes());
    dto.setAchats(configuration.getAchats());
    dto.setPrets(configuration.getPrets());
    dto.setMouvements(configuration.getMouvements());
    return dto;
  }

  public List<ConfigurationDTO> getAllConfigurationsByGroupe(Long idGroupe) {
    groupeRepository.findById(idGroupe)
        .orElseThrow(() -> new RuntimeException("Groupe not found"));
    return configurationRepository.findByGroupeId(idGroupe).stream().map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public List<ConfigurationDTO> getAllConfigurationsByMembre(Long idMembre) {
    membreRepository.findById(idMembre)
        .orElseThrow(() -> new RuntimeException("Membre not found"));
    return configurationRepository.findByMembreId(idMembre).stream().map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public ConfigurationDTO getAllConfigurationsByGroupeAndMembre(Long idGroupe, Long idMembre) {
    groupeRepository.findById(idGroupe)
        .orElseThrow(() -> new RuntimeException("Groupe not found"));
    membreRepository.findById(idMembre)
        .orElseThrow(() -> new RuntimeException("Membre not found"));
    Configuration configuration = configurationRepository.findByMembreIdAndGroupeId(idMembre, idGroupe)
        .orElseThrow(() -> new RuntimeException("Configuration not found for this member in this group"));
    return convertToDTO(configuration);
  }

  public ConfigurationDTO getConfigurationById(Long idConfiguration) {
    Configuration configuration = configurationRepository.findById(idConfiguration)
        .orElseThrow(() -> new RuntimeException("Configuration not found"));
    return convertToDTO(configuration);
  }

  @Transactional
  public ConfigurationDTO createConfiguration(Long idMembre, Long idGroupe) {
    Membre membre = membreRepository.findById(idMembre)
        .orElseThrow(() -> new RuntimeException("Membre not found"));
    Groupe groupe = groupeRepository.findById(idGroupe)
        .orElseThrow(() -> new RuntimeException("Groupe not found"));

    boolean existe = configurationRepository.findByMembreIdAndGroupeId(idMembre, idGroupe).isPresent();
    if (existe) {
      throw new RuntimeException("Une configuration existe déjà pour ce membre dans ce groupe");
    }

    Configuration configuration = new Configuration();
    configuration.setMembre(membre);
    configuration.setGroupe(groupe);

    return convertToDTO(configurationRepository.save(configuration));
  }

  @Transactional
  public ConfigurationDTO updateConfiguration(Long idConfiguration, ConfigurationDTO configurationDTO) {
    Configuration configuration = configurationRepository.findById(idConfiguration)
        .orElseThrow(() -> new RuntimeException("Configuration not found"));

    configuration.setNom(configurationDTO.getNom());
    configuration.setTaches(configurationDTO.getTaches());
    configuration.setNotes(configurationDTO.getNotes());
    configuration.setAchats(configurationDTO.getAchats());
    configuration.setPrets(configurationDTO.getPrets());
    configuration.setMouvements(configurationDTO.getMouvements());

    return convertToDTO(configurationRepository.save(configuration));
  }

  @Transactional
  public void deleteConfiguration(Long idConfiguration) {
    Configuration configuration = configurationRepository.findById(idConfiguration)
        .orElseThrow(() -> new RuntimeException("Configuration not found"));
    configurationRepository.delete(configuration);
  }

}
