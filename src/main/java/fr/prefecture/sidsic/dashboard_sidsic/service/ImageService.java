package fr.prefecture.sidsic.dashboard_sidsic.service;

import fr.prefecture.sidsic.dashboard_sidsic.entity.Image;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
	private final ImageRepository imageRepository;

	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	public List<Image> getImagesByMembreId(Long membreId) {
		return imageRepository.findByMembreId(membreId);
	}

	public Optional<Image> getImageById(Long id) {
		return imageRepository.findById(id);
	}

	public Image saveImage(Image image) {
		return imageRepository.save(image);
	}

	public void deleteImage(Long id) {
		imageRepository.deleteById(id);
	}
}
