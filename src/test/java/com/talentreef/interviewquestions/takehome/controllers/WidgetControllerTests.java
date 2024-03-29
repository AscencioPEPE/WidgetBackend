package com.talentreef.interviewquestions.takehome.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentreef.interviewquestions.takehome.dto.WidgetDTO;
import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.services.WidgetService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WidgetControllerTests {

    final private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private WidgetService widgetService;

    @InjectMocks
    private WidgetController widgetController;

    private ArgumentCaptor<Widget> widgetCaptor = ArgumentCaptor.forClass(Widget.class);

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(widgetController).build();
    }

	@Test
	public void when_getAllWidgets_expect_allWidgets() throws Exception {
		WidgetDTO widgetDTO = new WidgetDTO();
		widgetDTO.setName("Widget von Hammersmark");
		widgetDTO.setDescription("A widget description");
		widgetDTO.setPrice(new BigDecimal("10.00"));
		List<WidgetDTO> allWidgetsDTO = List.of(widgetDTO);

		when(widgetService.getAllWidgets()).thenReturn(allWidgetsDTO);

		MvcResult result = mockMvc.perform(get("/v1/widgets")).andExpect(status().isOk()).andDo(print()).andReturn();

		List<WidgetDTO> parsedResult = objectMapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<WidgetDTO>>() {
				});

		assertThat(parsedResult).usingRecursiveComparison().isEqualTo(allWidgetsDTO);
	}

	@Test
    public void when_createWidget_expect_CreatedResponse() {
        WidgetDTO createdWidgetDTO = new WidgetDTO();
        createdWidgetDTO.setName("Test Widget");
        createdWidgetDTO.setDescription("Test Widget Description");
        createdWidgetDTO.setPrice(new BigDecimal("10.99"));

        Widget newWidget = new Widget();
        newWidget.setName("Test Widget");
        newWidget.setDescription("Test Widget Description");
        newWidget.setPrice(new BigDecimal("10.99"));

        when(widgetService.createWidget(any(Widget.class))).thenReturn(createdWidgetDTO);

        ResponseEntity<WidgetDTO> response = widgetController.createWidget(newWidget);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(createdWidgetDTO);

        verify(widgetService, times(1)).createWidget(widgetCaptor.capture());
        Widget capturedWidget = widgetCaptor.getValue();
        assertThat(capturedWidget.getName()).isEqualTo("Test Widget");
        assertThat(capturedWidget.getDescription()).isEqualTo("Test Widget Description");
        assertThat(capturedWidget.getPrice()).isEqualByComparingTo(new BigDecimal("10.99"));
    }

    @Test
    public void when_createDuplicateWidget_expect_BadRequestResponse() {
        Widget existingWidget = new Widget();
        existingWidget.setName("Duplicate Widget Name");

        Widget newWidget = new Widget();
        newWidget.setName("Duplicate Widget Name");
        newWidget.setDescription("Duplicate Widget Description");
        newWidget.setPrice(new BigDecimal("20.99"));

        when(widgetService.createWidget(any(Widget.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Widget with name 'Duplicate Widget Name' already exists"));

        assertThatThrownBy(() -> widgetController.createWidget(newWidget))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Widget with name 'Duplicate Widget Name' already exists")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);

        verify(widgetService, times(1)).createWidget(newWidget);
    }

	@Test
	public void whenGetWidgetByName_expect_widgetDetails() throws Exception {
		String widgetName = "Existing Widget";
		WidgetDTO existingWidgetDTO = new WidgetDTO("Existing Widget", "An existing widget description",
				new BigDecimal("29.99"));
		when(widgetService.getWidgetByName(widgetName)).thenReturn(existingWidgetDTO);

		mockMvc.perform(get("/v1/widgets/{name}", widgetName)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void whenUpdateWidget_expect_widgetUpdated() throws Exception {
		String widgetName = "Existing Widget";
		WidgetDTO updatedWidgetDTO = new WidgetDTO("Existing Widget", "An updated widget description",
				new BigDecimal("39.99"));
		when(widgetService.updateWidget(eq(widgetName), any())).thenReturn(updatedWidgetDTO);

		String widgetDtoJson = objectMapper.writeValueAsString(updatedWidgetDTO);

		mockMvc.perform(
				put("/v1/widgets/{name}", widgetName).contentType(MediaType.APPLICATION_JSON).content(widgetDtoJson))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void whenDeleteWidget_expect_noContent() throws Exception {
		String widgetName = "WidgetToDelete";
		doNothing().when(widgetService).deleteWidget(widgetName);

		mockMvc.perform(delete("/v1/widgets/{name}", widgetName)).andExpect(status().isNoContent()).andDo(print());
	}
}
