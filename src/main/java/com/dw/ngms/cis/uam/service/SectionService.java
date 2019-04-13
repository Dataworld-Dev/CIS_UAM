package com.dw.ngms.cis.uam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.Section;
import com.dw.ngms.cis.uam.repository.SectionRepository;

@Service
public class SectionService {
    @Autowired
    private SectionRepository sectionRepository;

    public List<Section> getAllSections() {
        return this.sectionRepository.findAll();
    }

    public Section addSection(Section Section) {
        return this.sectionRepository.save(Section);
    }

}
