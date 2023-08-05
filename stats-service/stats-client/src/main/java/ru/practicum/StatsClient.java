package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.HitsDto;
import ru.practicum.dto.StatsDto;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsClient {

    private final RestTemplate rest;

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
