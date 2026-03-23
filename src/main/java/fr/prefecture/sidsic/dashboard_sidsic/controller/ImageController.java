package fr.prefecture.sidsic.dashboard_sidsic.controller;

import fr.prefecture.sidsic.dashboard_sidsic.dto.ImageDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Image;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.service.ImageService;
import fr.prefecture.sidsic.dashboard_sidsic.service.MembreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ImageController {
  private final ImageService imageService;
  private final MembreService membreService;

  public ImageController(ImageService imageService, MembreService membreService) {
    this.imageService = imageService;
    this.membreService = membreService;
  }

  private ImageDTO convertToDTO(Image image) {
    ImageDTO dto = new ImageDTO();
    dto.setId(image.getId());
    dto.setNom(image.getNom());
    dto.setPath(image.getPath());
    return dto;
  }

  private List<ImageDTO> convertToDTOList(List<Image> images) {
    List<ImageDTO> dtos = new ArrayList<>();
    for (Image image : images) {
      dtos.add(convertToDTO(image));
    }
    return dtos;
  }

  @GetMapping("/membres/{id}/images")
  public ResponseEntity<?> getImagesByMembre(@PathVariable("id") Long idMembre) {
    Optional<Membre> membreOpt = membreService.getMembreById(idMembre);
    if (membreOpt.isPresent()) {
      List<Image> images = membreOpt.get().getImages();
      return ResponseEntity.ok(convertToDTOList(images));
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le membre n'existe pas");
    }
  }

  @PostMapping(value = "/membres/{idMembre}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createImageByMembre(
      @PathVariable Long idMembre,
      @RequestParam("file") MultipartFile file) {
    try {
      if (file.isEmpty()) {
        return ResponseEntity.badRequest().body("Aucun fichier reçu");
      }
      String projectRoot = System.getProperty("user.dir");
      String uploadDir = projectRoot + File.separator + "uploads";
      File uploadFolder = new File(uploadDir);

      if (!uploadFolder.exists()) {
        uploadFolder.mkdirs();
      }

      String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
      String finalFileName = System.currentTimeMillis() + "_" + originalFilename;

      String filePath = uploadDir + File.separator + finalFileName;
      File dest = new File(filePath);

      file.transferTo(dest);

      Membre membre = membreService.GetMembre(membreService.getMembreById(idMembre));
      Image image = new Image();
      image.setMembre(membre);
      image.setNom(originalFilename);
      image.setPath("uploads/" + finalFileName);

      Image saved = imageService.saveImage(image);
      return ResponseEntity.ok(convertToDTO(saved));
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erreur lors de l'enregistrement du fichier : " + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @PutMapping("/membres/{idMembre}/images/{idImage}")
  public ResponseEntity<?> updateImageByMembre(@PathVariable Long idMembre, @PathVariable Long idImage,
      @RequestBody ImageDTO imageDTO) {
    try {
      Membre membre = membreService.GetMembre(membreService.getMembreById(idMembre));
      Image image = imageService.getImageById(idImage)
          .orElseThrow(() -> new RuntimeException("Image not found"));
      if (!membre.getImages().contains(image)) {
        throw new RuntimeException("L'image n'appartient pas au membre");
      }
      image.setNom(imageDTO.getNom());
      image.setPath(imageDTO.getPath());
      return ResponseEntity.ok(convertToDTO(imageService.saveImage(image)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @DeleteMapping("/images/{id}")
  public ResponseEntity<String> deleteImage(@PathVariable Long id) {
    try {
      Optional<Image> imageOpt = imageService.getImageById(id);

      if (imageOpt.isPresent()) {
        Image image = imageOpt.get();
        String cheminEnBase = image.getPath();

        imageService.deleteImage(id);
        if (cheminEnBase != null && !cheminEnBase.startsWith("http")) {
          String projectRoot = System.getProperty("user.dir");
          Path filePath = Paths.get(projectRoot, cheminEnBase.replace("/", File.separator));

          try {
            Files.deleteIfExists(filePath);
          } catch (IOException e) {
            System.err.println("Le fichier physique n'a pas pu être supprimé : " + e.getMessage());
          }
        }

        return ResponseEntity.status(HttpStatus.OK).body("Image et fichier correctement supprimés !");
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'image n'existe pas dans la BD.");
      }

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
