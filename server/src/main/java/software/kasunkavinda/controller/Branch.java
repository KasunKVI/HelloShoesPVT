package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.kasunkavinda.dto.BranchDTO;
import software.kasunkavinda.service.BranchService;

import java.util.List;

@RestController
//@RequestMapping("api/v1/branch")
@RequiredArgsConstructor
public class Branch {

    private final BranchService branchService;

    @GetMapping("/health")
    public String healthTest() {
        return "Branch Health Good";
    }


//    @GetMapping
//    public List<BranchDTO> getAllBranches(){
//        return branchService.getAllBranches();
//    }

}
