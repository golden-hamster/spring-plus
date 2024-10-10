package org.example.expert.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.image.exceptoin.ImageException;
import org.example.expert.domain.image.ImageFile;
import org.example.expert.domain.image.dto.response.ImageResponse;
import org.example.expert.domain.image.infrastructure.ImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageUtil imageUtil;

    public ImageResponse save(final MultipartFile image) {
        validateSizeOfImage(image);
        final ImageFile imageFile = new ImageFile(image);
        final String imageUrl = uploadImage(imageFile);
        return new ImageResponse(imageUrl);
    }

    private String uploadImage(final ImageFile imageFile) {
        try {
            return imageUtil.uploadImage(imageFile);
        } catch (final ImageException e) {
            throw e;
        }
    }

    private void validateSizeOfImage(final MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new ImageException("이미지가 존재하지 않습니다.");
        }
    }
}
