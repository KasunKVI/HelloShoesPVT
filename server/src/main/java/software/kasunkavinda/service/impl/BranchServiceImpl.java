package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.BranchRepo;
import software.kasunkavinda.dto.BranchDTO;
import software.kasunkavinda.entity.BranchEntity;
import software.kasunkavinda.service.BranchService;
import software.kasunkavinda.util.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BranchServiceImpl implements BranchService {

    private final BranchRepo branchRepo;
    private final Mapping mapper;

    @Override
    public List<String> getAllBranchNames() {
        return branchRepo.findAll().stream()
                .map(BranchEntity::getName)
                .collect(Collectors.toList());
    }
}
