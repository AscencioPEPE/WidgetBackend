package com.talentreef.interviewquestions.takehome.services;

import com.talentreef.interviewquestions.takehome.dto.WidgetDTO;
import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.respositories.WidgetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
public class WidgetService {

	private final WidgetRepository widgetRepository;

	@Autowired
	private WidgetService(WidgetRepository widgetRepository) {
		Assert.notNull(widgetRepository, "widgetRepository must not be null");
		this.widgetRepository = widgetRepository;
	}

	public List<WidgetDTO> getAllWidgets() {
		return widgetRepository.findAll().stream().map(widget -> new WidgetDTO(widget)).collect(Collectors.toList());
	}

	public WidgetDTO createWidget(Widget widget) {
        if (widgetRepository.findById(widget.getName()).isPresent()) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Widget with name '" + widget.getName() + "' already exists");
        }
		Widget newWidget = new Widget();
		newWidget.setName(widget.getName());
		newWidget.setDescription(widget.getDescription());
		newWidget.setPrice(widget.getPrice());
		return convertToDTO(widgetRepository.save(newWidget));
	}

	public WidgetDTO getWidgetByName(String name) {
		return widgetRepository.findById(name).map(this::convertToDTO)
				.orElseThrow(() -> new EntityNotFoundException("Widget not found with name: " + name));
	}

	public WidgetDTO updateWidget(String name, WidgetDTO widgetDTO) {
		Widget widget = widgetRepository.findById(name)
				.orElseThrow(() -> new EntityNotFoundException("Widget not found with name: " + name));

		widget.setDescription(widgetDTO.getDescription());
		widget.setPrice(widgetDTO.getPrice());
		return convertToDTO(widgetRepository.save(widget));
	}

	public void deleteWidget(String name) {
		Widget widget = widgetRepository.findById(name)
				.orElseThrow(() -> new EntityNotFoundException("Widget not found with name: " + name));
		widgetRepository.deleteById(widget.getName());
	}

	private WidgetDTO convertToDTO(Widget widget) {
		return new WidgetDTO(widget);
	}

}
