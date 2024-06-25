package com.example.ProJectLP.controller;

import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    //vinyl 검색
    @RequestMapping(value = "/search/vinyl", method = RequestMethod.GET)
    public ResponseDto<?> searchVinyl(@RequestParam String keyword,
                                      @RequestParam("pageNum") int page,
                                      @RequestParam("pageLimit") int limit) {

        page = page -1;
        return searchService.searchVinyl(keyword, page, limit);
    }
}
