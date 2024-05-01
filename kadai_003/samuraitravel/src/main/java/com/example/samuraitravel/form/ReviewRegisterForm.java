package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRegisterForm {
	@NotNull
    private Integer houseid;    

	@NotNull
    private Integer userid;    

    @NotNull
    private Integer rate;  
    
    @NotBlank(message = "コメントを入力してください。")
    private String comment;
}
