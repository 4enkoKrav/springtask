package by.krav.springtask.controller;

import by.krav.springtask.model.CompanyIT;
import by.krav.springtask.form.CompanyForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Controller
public class CompanyController {

    private static List<CompanyIT> companies = new ArrayList<>();
    static {
        companies.add(new CompanyIT("Epam", "EpamManager"));
        companies.add(new CompanyIT("ItechArt", "ItechManager"));
        companies.add(new CompanyIT("IBA", "IBAManager"));
    }
    private static int ind = 0;

    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;


    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public ModelAndView index(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        model.addAttribute("message", message);
        log.info("Success. Index was called");
        return modelAndView;
    }


    @RequestMapping(value = {"/allcompanies"}, method = RequestMethod.GET)
    public ModelAndView personList(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("companylist");
        model.addAttribute("companies", companies);
        return modelAndView;
    }


                        /////    ADD COMPANY    /////
    @RequestMapping(value = {"/addcompany"}, method = RequestMethod.GET)
    public ModelAndView showAddPersonPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("addcompany");
        CompanyForm companyForm = new CompanyForm();
        model.addAttribute("companyform", companyForm);
        log.info("Adding Company");
        return modelAndView;
    }

    @RequestMapping(value = {"/addcompany"}, method = RequestMethod.POST)
    public ModelAndView ActionAddPersonPage(Model model, //
                          @ModelAttribute("companyform") CompanyForm companyForm){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("companylist");
        String name = companyForm.getName();
        String manager = companyForm.getManager();

        if(name != null && name.length()>0 && manager != null && manager.length()>0){
            CompanyIT companyIT = new CompanyIT(name, manager);
            companies.add(companyIT);
            model.addAttribute("companies", companies);
            return modelAndView;
        }
        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("addcompany");
        log.info("Adding Company");
        return modelAndView;
    }


                        ////    DELETE COMPANY    ////

    @GetMapping("/delete/{name}")
    public String DeleteProduct(@PathVariable(value = "name") String name){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("companylist");
        CompanyIT companyITdelete = companies.
                stream()
                .filter(x -> x.getName().equals(name)).findFirst().orElse(null);

        companies.remove(companyITdelete);
        log.info("Removing Company");
        return "redirect:/allcompanies";
    }


                        ////    UPDATE COMPANY    ////

    @GetMapping("{updatecompany}")
    public ModelAndView UpdateGetProduct(Model model, @ModelAttribute("productUpdate") CompanyIT companyIT){
        ModelAndView modelAndView = new ModelAndView("updatecompany");
        CompanyForm companyForm = new CompanyForm();
        companyForm.setName(companyForm.getName());
        companyForm.setManager(companyForm.getManager());
        ind  = companies.indexOf(companyIT);
        model.addAttribute("companyform", companyForm);
        log.info("Updating Company");
        return  modelAndView;
    }

    @PostMapping("{updatecompany}")
    public ModelAndView UpdatePostProduct(Model model, @ModelAttribute("productUpdate") CompanyIT companyIT){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("companylist");
        String name = companyIT.getName();
        String manager = companyIT.getManager();
        if(name != null && name.length()>0 && manager != null && manager.length()>0){
            companies.get(ind).setName(name);
            companies.get(ind).setManager(manager);
            model.addAttribute("companies", companies);
            return  modelAndView;
        }
        model.addAttribute("companiesList", companies);
        log.info("Updating Company");
        return modelAndView;
    }

}


