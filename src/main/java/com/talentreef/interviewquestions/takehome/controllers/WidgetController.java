package com.talentreef.interviewquestions.takehome.controllers;

import com.talentreef.interviewquestions.takehome.dto.WidgetDTO;
import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.services.WidgetService;
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
import org.springframework.web.server.ResponseStatusException;

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
	public ResponseEntity<List<WidgetDTO>> getAllWidgets() {
		return ResponseEntity.ok(widgetService.getAllWidgets());
	}

	@PostMapping
	public ResponseEntity<WidgetDTO> createWidget(@RequestBody Widget widget) {
		try {
			WidgetDTO createdWidget = widgetService.createWidget(widget);
			return new ResponseEntity<>(createdWidget, HttpStatus.CREATED);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create widget", e);
		}
	}

	@GetMapping("/{name}")
	public ResponseEntity<WidgetDTO> getWidgetByName(@PathVariable String name) {
		WidgetDTO widgetDTO = widgetService.getWidgetByName(name);
		return ResponseEntity.ok(widgetDTO);
	}

	@PutMapping("/{name}")
	public ResponseEntity<WidgetDTO> updateWidget(@PathVariable String name, @RequestBody WidgetDTO widgetDTO) {
		WidgetDTO updatedWidget = widgetService.updateWidget(name, widgetDTO);
		return ResponseEntity.ok(updatedWidget);
	}

	@DeleteMapping("/{name}")
	public ResponseEntity<Void> deleteWidget(@PathVariable String name) {
		widgetService.deleteWidget(name);
		return ResponseEntity.noContent().build();
	}

}
