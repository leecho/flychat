package com.honvay.flyai.document.web.controller;

import com.honvay.flyai.framework.web.ResponsePayload;
import com.honvay.flyai.document.application.DocumentApplicationService;
import com.honvay.flyai.document.domain.model.Document;
import com.honvay.flyai.document.domain.model.RelevantSegment;
import com.honvay.flyai.document.web.converter.DocumentAssembler;
import com.honvay.flyai.document.web.model.dto.DocumentDto;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@ResponsePayload
@RequestMapping("/document")
public class DocumentController {

    private final DocumentApplicationService documentApplicationService;
    private final DocumentAssembler documentAssembler;

    public DocumentController(DocumentApplicationService documentApplicationService,
                              DocumentAssembler documentAssembler) {
        this.documentApplicationService = documentApplicationService;
        this.documentAssembler = documentAssembler;
    }

    @PostMapping
    public void create(@RequestBody DocumentDto documentDto){
        this.documentApplicationService.create(documentAssembler.assemble(documentDto),new File(documentDto.getFilePath()));
    }

    @PutMapping
    public void update(@RequestBody DocumentDto documentDto){
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        // TODO: 2023/7/15 删除需要再完善
        this.documentApplicationService.delete(Document.of(id));
    }


    @GetMapping("/relevant")
    public List<RelevantSegment> findRelevant(@RequestParam String source,
                                              @RequestParam List<Long> documentIds,
                                              @RequestParam(defaultValue = "0.8f") double similarity,
                                              @RequestParam(defaultValue = "5") int relevantSize){
        return this.documentApplicationService.findRelevantSegment(source, documentIds,similarity,relevantSize);
    }
}
