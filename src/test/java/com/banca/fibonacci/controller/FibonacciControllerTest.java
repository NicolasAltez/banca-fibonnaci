package com.banca.fibonacci.controller;

import com.banca.fibonacci.config.security.filters.ApiKeyFilter;
import com.banca.fibonacci.controller.advice.GenericControllerAdvice;
import com.banca.fibonacci.models.dtos.FibonacciResultDTO;
import com.banca.fibonacci.service.FibonacciResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FibonacciController.class)
class FibonacciControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private FibonacciResultService fibonacciService;

    @MockBean
    private ApiKeyFilter apiKeyFilter;

    @BeforeEach
    void setup() {
        apiKeyFilter = new ApiKeyFilter("api-key-test", "api-header-test");

        mockMvc = MockMvcBuilders.standaloneSetup(new FibonacciController(fibonacciService))
                .addFilters(apiKeyFilter)
                .setControllerAdvice(new GenericControllerAdvice())
                .build();
    }

    @Nested
    class UnauthorizedTests {

        @Nested
        class WithoutHeaderTests {

            @Test
            void testGetFibonacci_ValidPosition_WithoutHeader() throws Exception {
                ResultActions respuesta = mockMvc.perform(get("/api/fibonacci/5")
                                .contentType(MediaType.APPLICATION_JSON));

                respuesta.andExpect(status().isUnauthorized());

                verify(fibonacciService, never()).getFibonacci(anyInt());
            }

            @Test
            void testGetLeastRequestedResultsTop10_WithoutHeader() throws Exception {
                mockMvc.perform(get("/api/fibonacci/least-requested/top10")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());

                verify(fibonacciService, never()).getLeastRequestedResultsTop10();
            }

            @Test
            void testGetMostRequestedResultsTop10_WithoutHeader() throws Exception {
                mockMvc.perform(get("/api/fibonacci/most-requested/top10")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());

                verify(fibonacciService, never()).getMostRequestedResultsTop10();
            }
        }

        @Nested
        class WithInvalidHeaderTests {

            @Test
            void testGetFibonacci_ValidPosition_WithInvalidHeader() throws Exception {
                mockMvc.perform(get("/api/fibonacci/5")
                                .header("x-api-key", "invalid-api-key")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());

                verify(fibonacciService, never()).getFibonacci(anyInt());
            }

            @Test
            void testGetLeastRequestedResultsTop10_WithInvalidHeader() throws Exception {
                mockMvc.perform(get("/api/fibonacci/least-requested/top10")
                                .header("x-api-key", "invalid-api-key")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());

                verify(fibonacciService, never()).getLeastRequestedResultsTop10();
            }

            @Test
            void testGetMostRequestedResultsTop10_WithInvalidHeader() throws Exception {
                mockMvc.perform(get("/api/fibonacci/most-requested/top10")
                                .header("x-api-key", "invalid-api-key")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    class AuthorizedTests{

        @Test
        void testGetFibonacci_PositionWith_Character() throws Exception {

            mockMvc.perform(get("/api/fibonacci/5a")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("api-header-test", "api-key-test"))
                    .andExpect(status().isBadRequest());

        }

   /*     @Test
        void testGetFibonacci_PositionWith_Negative() throws Exception {

            mockMvc.perform(get("/api/fibonacci/-35")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("api-header-test", "api-key-test"))
                    .andExpect(status().isBadRequest());

        }
*/
        @Test
        void testGetFibonacci_ValidPosition() throws Exception {
            when(fibonacciService.getFibonacci(5)).thenReturn(5L);

            mockMvc.perform(get("/api/fibonacci/5")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("api-header-test", "api-key-test"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("5"));

            verify(fibonacciService, times(1)).getFibonacci(5);
        }


        @Test
        void testGetLeastRequestedResultsTop10() throws Exception {
            List<FibonacciResultDTO> results = List.of(
                    new FibonacciResultDTO( 0, 0, 1),
                    new FibonacciResultDTO( 1, 0, 2)
            );
            when(fibonacciService.getLeastRequestedResultsTop10()).thenReturn(results);

            mockMvc.perform(get("/api/fibonacci/least-requested/top10")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("api-header-test", "api-key-test"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].position", is(0)))
                    .andExpect(jsonPath("$[1].position", is(1)));

            verify(fibonacciService).getLeastRequestedResultsTop10();
        }

        @Test
        void testGetLeastRequestedResultsTop10_EmptyList() throws Exception {
            when(fibonacciService.getLeastRequestedResultsTop10()).thenReturn(List.of());

            mockMvc.perform(get("/api/fibonacci/least-requested/top10")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("api-header-test", "api-key-test"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(fibonacciService, times(1)).getLeastRequestedResultsTop10();
        }


        @Test
        void testGetMostRequestedResultsTop10() throws Exception {
            List<FibonacciResultDTO> results = List.of(
                    new FibonacciResultDTO( 0, 0, 10),
                    new FibonacciResultDTO( 1, 1, 20)
            );
            when(fibonacciService.getMostRequestedResultsTop10()).thenReturn(results);

            mockMvc.perform(get("/api/fibonacci/most-requested/top10")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("api-header-test", "api-key-test"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].position", is(0)))
                    .andExpect(jsonPath("$[1].position", is(1)));

            verify(fibonacciService).getMostRequestedResultsTop10();
        }

        @Test
        void testGetMostRequestedResultsTop10_EmptyList() throws Exception {
            when(fibonacciService.getMostRequestedResultsTop10()).thenReturn(List.of());

            mockMvc.perform(get("/api/fibonacci/most-requested/top10")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("api-header-test", "api-key-test"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(fibonacciService, times(1)).getMostRequestedResultsTop10();
        }
    }

   /* @Nested
    class RateLimiterTests{
        @Test
        void testRateLimiter_TooManyRequests() throws Exception {
            // Realizar el número de solicitudes permitido antes de alcanzar el límite
            for (int i = 0; i < 5; i++) { // Suponiendo un limited de 5 solicitudes
                mockMvc.perform(get("/api/fibonacci/5")
                                .header("x-api-key", "api-key-test")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            }

            // Realizar una solicitud adicional que debería exceder el límite de tasa
            mockMvc.perform(get("/api/fibonacci/5")
                            .header("x-api-key", "api-key-test")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isTooManyRequests()) // Espera un estado HTTP 429
                    .andExpect(jsonPath("$.message", containsString("Too Many Requests")));
        }
    }*/

}