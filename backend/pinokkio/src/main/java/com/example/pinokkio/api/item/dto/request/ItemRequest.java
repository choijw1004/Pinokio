package com.example.pinokkio.api.item.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
public class ItemRequest {

    @NotEmpty(message = "posId는 필수 값입니다.")
    private String posId;
    @NotEmpty(message = "categoryId는 필수 값입니다.")
    private String categoryId;
    @Positive(message = "amount는 양수여야 합니다.")
    private int price;
    @Positive(message = "amount는 양수여야 합니다.")
    private int amount;
    @NotEmpty(message = "name은 필수 값입니다.")
    private String name;
    @NotEmpty(message = "detail은 필수 값입니다.")
    private String detail;

    private MultipartFile file;

}
