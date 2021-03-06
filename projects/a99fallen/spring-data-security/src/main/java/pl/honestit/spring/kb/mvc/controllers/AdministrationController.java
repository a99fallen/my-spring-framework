package pl.honestit.spring.kb.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.honestit.spring.kb.core.services.KnowledgeSourceService;
import pl.honestit.spring.kb.core.services.SkillService;
import pl.honestit.spring.kb.core.services.UserService;
import pl.honestit.spring.kb.dto.AddKnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.KnowledgeSourceDTO;
import pl.honestit.spring.kb.dto.LoggedUserDTO;
import pl.honestit.spring.kb.dto.SkillDTO;
import pl.honestit.spring.kb.utils.TestDataGenerator;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdministrationController {

    private UserService userService;
    private SkillService skillService;
    private KnowledgeSourceService knowledgeSourceService;

    @Autowired
    public AdministrationController(UserService userService, SkillService skillService, KnowledgeSourceService knowledgeSourceService) {
        this.userService = userService;
        this.skillService = skillService;
        this.knowledgeSourceService = knowledgeSourceService;
    }

    @GetMapping
    public String prepareAdminPanel(Model model, @SessionAttribute(required = false) LoggedUserDTO user) {
        if (user == null) {
            TestDataGenerator.getLoggedUserDTO("admin");
        }
        AddKnowledgeSourceDTO newSource = new AddKnowledgeSourceDTO();
        model.addAttribute("newSource", newSource);

        List<KnowledgeSourceDTO> allSources = knowledgeSourceService.getAllSources();
        model.addAttribute("sources", allSources);

        List<SkillDTO> allSkills = skillService.getAllSkills();
        model.addAttribute("availableSkills", allSkills);

        model.addAttribute("user", user);
        return "admin";
    }

    @PostMapping("/sources/add")
    private String addNewKnowledgeSource(AddKnowledgeSourceDTO newKnowledgeSource) {
        knowledgeSourceService.addNewSource(newKnowledgeSource);
        return "redirect:/admin";
    }

    @PostMapping("/sources/delete")
    private String deleteSource(Long sourceId) {
        knowledgeSourceService.deleteSource(sourceId);
        return "redirect:/admin";
    }

    @PostMapping("/sources/activate")
    public String activateSource(Long sourceId) {
        knowledgeSourceService.activateSource(sourceId);
        return "redirect:/admin";
    }
}