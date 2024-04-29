package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewEditForm {
	@NotNull
    private Integer id;    

	@NotNull
    private Integer houseid;    

	@NotNull
    private Integer userid;    

	@NotNull
    private Integer rate;  
    
    @NotBlank(message = "コメントを入力してください。")
    private String comment;
}
