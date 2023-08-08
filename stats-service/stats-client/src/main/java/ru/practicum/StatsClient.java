package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.HitsDto;
import ru.practicum.dto.StatsDto;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StatsClient {

    private final RestTemplate rest;

    @Value("${stats-server.url}")
    private String serverUrl;

    public void saveInfo(HitsDto hitDto) {
        rest.postForLocation(serverUrl.concat("/hit"), hitDto);
    }

    public List<StatsDto> getStatistics(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique);

        StatsDto[] statistics = rest.getForObject(
                serverUrl.concat("/stats?start={start}&end={end}&uris={uris}&unique={unique}"),
                StatsDto[].class,
                parameters);

        if (statistics == null) {
            return List.of();
        }
        return List.of(statistics);
    }
}
