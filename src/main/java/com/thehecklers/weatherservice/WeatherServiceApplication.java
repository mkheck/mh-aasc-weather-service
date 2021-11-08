package com.thehecklers.weatherservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@SpringBootApplication
public class WeatherServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherServiceApplication.class, args);
	}

}

@RestController
class AirportController {
	@Value("${avwx-token:NoValidTokenRetrieved}")
	private String token;
	private final WebClient client = WebClient.create("https://avwx.rest/api/");

	@GetMapping("/metar/{id}")
	public final Mono<METAR> retrieveMETAR(@PathVariable String id) {
		System.out.println(">>> retrieveMETAR, ID: " + id);

		return client.get()
				.uri("metar/" + id + "?token=" + token)
				.retrieve()
				.bodyToMono(METAR.class);
	}

	@GetMapping("/taf/{id}")
	public final Mono<TAF> retrieveTAF(@PathVariable String id) {
		System.out.println(">>> retrieveTAF, ID: " + id);

		return client.get()
				.uri("taf/" + id + "?token=" + token)
				.retrieve()
				.bodyToMono(TAF.class);
	}
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class METAR {
	private String raw;
	private Time time;
	private String flight_rules;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class TAF {
	private Time start_time;
	private Time end_time;
	private Iterable<Forecast> forecast;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Time {
	private ZonedDateTime dt;
	private String repr;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class Forecast {
	private String raw;
	private Time start_time;
	private Time end_time;
	private String flight_rules;
	private Visibility visibility;
	private WindDirection wind_direction;
	private WindSpeed wind_speed;
	private Iterable<String> other;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class Visibility {
	private String repr;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class WindDirection {
	private String repr;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class WindSpeed {
	private String repr;
}