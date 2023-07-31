package ru.practicum;

import ru.practicum.dto.HitsDto;
import ru.practicum.model.Hits;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitsMapper {
    public static Hits makeHits(HitsDto hitsDto) {
        Hits hits = new Hits();
        hits.setApp(hitsDto.getApp());
        hits.setUri(hitsDto.getUri());
        hits.setIp(hitsDto.getIp());
        hits.setTimestamp(LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(hitsDto.getTimestamp())));
        return hits;
    }

    public static HitsDto makeHitsDto(Hits hits) {
        HitsDto hitsDto = new HitsDto();
        hitsDto.setApp(hits.getApp());
        hitsDto.setUri(hits.getUri());
        hitsDto.setIp(hits.getIp());
        hitsDto.setTimestamp(hits.getTimestamp().toString());
        return hitsDto;
    }
}
