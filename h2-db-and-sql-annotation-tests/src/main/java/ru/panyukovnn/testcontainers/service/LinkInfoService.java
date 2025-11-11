package ru.panyukovnn.testcontainers.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.panyukovnn.testcontainers.dto.FilterLinkInfoRequest;
import ru.panyukovnn.testcontainers.dto.LinkInfoResponse;
import ru.panyukovnn.testcontainers.dto.PageableRequest;
import ru.panyukovnn.testcontainers.mapper.LinkInfoMapper;
import ru.panyukovnn.testcontainers.repository.LinkInfoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkInfoService {

    private final LinkInfoMapper linkInfoMapper;
    private final LinkInfoRepository linkInfoRepository;

    public List<LinkInfoResponse> findByFilter(FilterLinkInfoRequest filterRequest) {
        PageableRequest page = filterRequest.getPage();

        Pageable pageable = mapPageable(page);

        return linkInfoRepository.findByFilter(
                filterRequest.getLinkPart(),
                filterRequest.getEndTimeFrom(),
                filterRequest.getEndTimeTo(),
                filterRequest.getDescriptionPart(),
                filterRequest.getActive(),
                pageable
            ).stream()
            .map(linkInfoMapper::toResponse)
            .toList();
    }

    protected Pageable mapPageable(PageableRequest page) {
        List<Sort.Order> sorts = page.getSorts().stream()
            .map(sort -> new Sort.Order(
                Sort.Direction.valueOf(sort.getDirection()),
                sort.getField()
            ))
            .toList();

        return PageRequest.of(page.getNumber() - 1, page.getSize(), Sort.by(sorts));
    }
}
