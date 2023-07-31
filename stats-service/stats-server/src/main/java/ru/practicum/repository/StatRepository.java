package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.Hits;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hits, Long> {
    @Query(value = "select new ru.practicum.dto.StatsDto(hit.app, hit.uri, count(hit.ip)) " +
            "from Hits hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "group by hit.app, hit.uri " +
            "order by count(hit.ip) desc ")
    List<StatsDto> getStatsHitWithoutURI(LocalDateTime start,
                                         LocalDateTime end);

    @Query(value = "select new ru.practicum.dto.StatsDto(hit.app, hit.uri, count(distinct hit.ip)) " +
            "from Hits hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "and hit.uri in (?3) " +
            "group by hit.app, hit.uri " +
            "order by count(distinct hit.ip) desc ")
    List<StatsDto> getStatsHitUnique(LocalDateTime start,
                                     LocalDateTime end, List<String> uri);

    @Query(value = "select new ru.practicum.dto.StatsDto(hit.app, hit.uri, count(hit.ip)) " +
            "from Hits hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "and hit.uri in (?3) " +
            "group by hit.app, hit.uri " +
            "order by count(hit.ip) desc ")
    List<StatsDto> getStatsHitNotUnique(LocalDateTime start,
                                        LocalDateTime end, List<String> uri);
}
