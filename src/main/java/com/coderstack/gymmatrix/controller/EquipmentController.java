package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.exceptions.ResourceNotFoundException;
import com.coderstack.gymmatrix.models.Equipment;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.repository.EquipmentRepository;
import com.coderstack.gymmatrix.repository.GymRepository;
import com.coderstack.gymmatrix.service.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EquipmentController {

    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private S3UploadService s3UploadService;

    @PostMapping(value = "/gym/{gym_id}/admin/{admin_id}/equipment", consumes = "multipart/form-data")
    public ResponseEntity<?> addEquipment(
            @PathVariable int gym_id,
            @RequestParam("equipmentName") String equipmentName,
            @RequestParam("remarks") String remarks,
            @RequestParam("file") MultipartFile file) throws IOException {

        Gym gym = gymRepository.findById(gym_id)
                .orElseThrow(() -> new ResourceNotFoundException("Gym not found"));

        String savedPath = saveImage(file);

        Equipment newEquipment = new Equipment();
        newEquipment.setGym(gym);
        newEquipment.setEquipmentName(equipmentName);
        newEquipment.setRemarks(remarks);
        newEquipment.setEquipmentPhotoLink(savedPath);

        s3UploadService.uploadFile(savedPath, file.getBytes());
        equipmentRepository.save(newEquipment);

        return ResponseEntity.ok(Map.of("message", "Equipment saved successfully"));
    }


    @GetMapping("/gym/{gym_id}/{role}/{role_id}/equipment")
    public List<Equipment> getEquipments(@PathVariable int gym_id) {
        Gym gym = gymRepository.findById(gym_id)
                .orElseThrow(() -> new ResourceNotFoundException("gym not found"));

        List<Equipment> equipments = equipmentRepository.findByGym(gym);

        equipments.forEach(equipment -> {
            if (equipment.getEquipmentPhotoLink() != null && !equipment.getEquipmentPhotoLink().isEmpty()) {
                String key = equipment.getEquipmentPhotoLink();
                URL fileUrl = s3UploadService.generatePresignedUrl(key, Duration.ofMinutes(15));
                equipment.setEquipmentPhotoLink(fileUrl.toString());
            }
        });

        return equipments;
    }


    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); // avoid collision
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

}
