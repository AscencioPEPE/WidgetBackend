package com.talentreef.interviewquestions.takehome.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.talentreef.interviewquestions.takehome.models.Widget;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonTypeName("Widget")
@AllArgsConstructor
@NoArgsConstructor
public class WidgetDTO {
    private String name;
    private String description;
    private BigDecimal price;
    
	public WidgetDTO(Widget widget) {
        this.name = widget.getName();
        this.description = widget.getDescription();
        this.price = widget.getPrice();
    }
	
}
