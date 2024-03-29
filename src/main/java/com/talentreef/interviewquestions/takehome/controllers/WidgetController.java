package com.talentreef.interviewquestions.takehome.controllers;

import com.talentreef.interviewquestions.takehome.dto.WidgetDTO;
import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.services.WidgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/v1/widgets", produces = MediaType.APPLICATION_JSON_VALUE)
public class WidgetController {

	private final WidgetService widgetService;

	public WidgetController(WidgetService widgetService) {
		Assert.notNull(widgetService, "widgetService must not be null");
		this.widgetService = widgetService;
	}

	@GetMapping
    @Operation(summary = "Get all widgets", responses = {
            @ApiResponse(description = "Successful Retrieval", responseCode = "200", content = @Content(schema = @Schema(implementation = WidgetDTO.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500")
    })
    public ResponseEntity<List<WidgetDTO>> getAllWidgets() {
        return ResponseEntity.ok(widgetService.getAllWidgets());
    }

    @PostMapping
    @Operation(summary = "Create a new widget", responses = {
            @ApiResponse(description = "Widget Created", responseCode = "201"),
            @ApiResponse(description = "Bad Request", responseCode = "400"),
            @ApiResponse(description = "Internal Server Error", responseCode = "500")
    })
    public ResponseEntity<WidgetDTO> createWidget(@RequestBody Widget widget) {
        WidgetDTO createdWidget = widgetService.createWidget(widget);
        return new ResponseEntity<>(createdWidget, HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Get a widget by name", responses = {
            @ApiResponse(description = "Successful Retrieval", responseCode = "200", content = @Content(schema = @Schema(implementation = WidgetDTO.class))),
            @ApiResponse(description = "Not Found", responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", responseCode = "500")
    })
    public ResponseEntity<WidgetDTO> getWidgetByName(@Parameter(description = "Name of the widget to be obtained") @PathVariable String name) {
        WidgetDTO widgetDTO = widgetService.getWidgetByName(name);
        return ResponseEntity.ok(widgetDTO);
    }

    @PutMapping("/{name}")
    @Operation(summary = "Update a widget", responses = {
            @ApiResponse(description = "Successful Update", responseCode = "200", content = @Content(schema = @Schema(implementation = WidgetDTO.class))),
            @ApiResponse(description = "Not Found", responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", responseCode = "500")
    })
    public ResponseEntity<WidgetDTO> updateWidget(@Parameter(description = "Name of the widget to be updated") @PathVariable String name, @RequestBody WidgetDTO widgetDTO) {
        WidgetDTO updatedWidget = widgetService.updateWidget(name, widgetDTO);
        return ResponseEntity.ok(updatedWidget);
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Delete a widget", responses = {
            @ApiResponse(description = "Successful Deletion", responseCode = "204"),
            @ApiResponse(description = "Not Found", responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", responseCode = "500")
    })
    public ResponseEntity<Void> deleteWidget(@Parameter(description = "Name of the widget to be deleted") @PathVariable String name) {
        widgetService.deleteWidget(name);
        return ResponseEntity.noContent().build();
    }

}
