package com.talentreef.interviewquestions.takehome.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.talentreef.interviewquestions.takehome.dto.WidgetDTO;
import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.respositories.WidgetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
public class WidgetServiceTests {

	@Mock
	private WidgetRepository widgetRepository;

	@InjectMocks
	private WidgetService widgetService;

	@Captor
	private ArgumentCaptor<Widget> widgetCaptor;

	@Test
	public void when_getAllWidgets_expect_DTOList() {
		Widget existingWidget = new Widget();
		existingWidget.setId(1L);
		existingWidget.setName("existingWidget");
		existingWidget.setDescription("existingWidget");
		existingWidget.setPrice(new BigDecimal("10.99"));
		List<Widget> widgetList = List.of(existingWidget);
		when(widgetRepository.findAll()).thenReturn(widgetList);
		WidgetDTO expectedWidgetDTO = new WidgetDTO(existingWidget);
		List<WidgetDTO> expectedWidgetDTOList = List.of(expectedWidgetDTO);
		List<WidgetDTO> actualWidgetDTOList = widgetService.getAllWidgets();
		assertThat(actualWidgetDTOList).usingRecursiveComparison().isEqualTo(expectedWidgetDTOList);
	}

	@Test
	public void when_createWidget_expect_widgetSaved() {
		Widget newWidget = new Widget();
		newWidget.setName("Widget Name");
		newWidget.setDescription("Widget Description");
		newWidget.setPrice(new BigDecimal("10.99"));

		when(widgetRepository.save(any(Widget.class))).thenReturn(newWidget);

		WidgetDTO resultDTO = widgetService.createWidget(newWidget);

		verify(widgetRepository).save(widgetCaptor.capture());
		Widget capturedWidget = widgetCaptor.getValue();

		assertThat(capturedWidget).usingRecursiveComparison().isEqualTo(newWidget);

		WidgetDTO expectedDTO = new WidgetDTO();
		expectedDTO.setName(capturedWidget.getName());
		expectedDTO.setDescription(capturedWidget.getDescription());
		expectedDTO.setPrice(capturedWidget.getPrice());

		assertThat(resultDTO).usingRecursiveComparison().isEqualTo(expectedDTO);
	}

	@Test
	public void when_createDuplicateWidget_expect_ResponseStatusException() {
		Widget existingWidget = new Widget();
		existingWidget.setName("Duplicate Widget Name");

		when(widgetRepository.findById(anyString())).thenReturn(Optional.of(existingWidget));

		Widget newWidget = new Widget();
		newWidget.setName("Duplicate Widget Name");
		newWidget.setDescription("Duplicate Widget Description");
		newWidget.setPrice(new BigDecimal("20.99"));

		assertThatThrownBy(() -> widgetService.createWidget(newWidget)).isInstanceOf(ResponseStatusException.class)
				.hasMessageContaining("Widget with name 'Duplicate Widget Name' already exists")
				.hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);

		verify(widgetRepository, never()).save(any(Widget.class));
	}

	@Test
	public void when_updateWidget_expect_widgetUpdated() {
		String widgetName = "existingWidget";
		Widget existingWidget = new Widget();
		existingWidget.setId(1L);
		existingWidget.setName(widgetName);
		existingWidget.setDescription("Any description");
		existingWidget.setPrice(new BigDecimal("10.99"));

		WidgetDTO updateDTO = new WidgetDTO();
		updateDTO.setDescription("Updated Description");
		updateDTO.setPrice(new BigDecimal("99.99"));

		when(widgetRepository.findById(widgetName)).thenReturn(Optional.of(existingWidget));

		when(widgetRepository.save(any(Widget.class))).thenAnswer(invocation -> invocation.getArgument(0));

		WidgetDTO resultDTO = widgetService.updateWidget(widgetName, updateDTO);

		verify(widgetRepository).save(widgetCaptor.capture());
		Widget capturedWidget = widgetCaptor.getValue();

		assertThat(capturedWidget.getName()).isEqualTo(widgetName);
		assertThat(capturedWidget.getDescription()).isEqualTo(updateDTO.getDescription());
		assertThat(capturedWidget.getPrice()).isEqualTo(updateDTO.getPrice());

		assertThat(resultDTO.getName()).isEqualTo(widgetName);
		assertThat(resultDTO.getDescription()).isEqualTo(updateDTO.getDescription());
		assertThat(resultDTO.getPrice()).isEqualTo(updateDTO.getPrice());
	}

	@Test
	public void when_getWidgetByName_expect_correctWidgetDTO() {
		String widgetName = "TestWidget";
		Widget widget = new Widget();
		widget.setId(2L);
		widget.setName(widgetName);
		widget.setDescription("A widget for testing");
		widget.setPrice(new BigDecimal("20.00"));

		when(widgetRepository.findById(widgetName)).thenReturn(Optional.of(widget));

		WidgetDTO resultDTO = widgetService.getWidgetByName(widgetName);

		assertThat(resultDTO).usingRecursiveComparison().isEqualTo(new WidgetDTO(widget));
	}

	@Test
	public void when_deleteWidget_expect_widgetRepositoryDeleteCalled() {
		String widgetName = "WidgetToDelete";
		Widget widgetToDelete = new Widget();
		widgetToDelete.setId(3L);
		widgetToDelete.setName(widgetName);
		widgetToDelete.setDescription("A widget to be deleted");
		widgetToDelete.setPrice(new BigDecimal("15.99"));

		when(widgetRepository.findById(widgetName)).thenReturn(Optional.of(widgetToDelete));
		doNothing().when(widgetRepository).deleteById(widgetName);

		widgetService.deleteWidget(widgetName);

		verify(widgetRepository).deleteById(widgetName);
	}

}
