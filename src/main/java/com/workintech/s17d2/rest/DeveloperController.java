package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Experience;
import jakarta.annotation.PostConstruct;
import com.workintech.s17d2.model.Developer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.workintech.s17d2.tax.Taxable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/developers")
public class DeveloperController {

    private Taxable developerTax;

    public Map<Integer, Developer> developers = new HashMap<>();

    @PostConstruct
    public void init(){
        developers.put(1,new Developer(1,"Özgür", 5000.0, Experience.JUNIOR));
        developers.put(2,new Developer(2,"Meriç", 45000.0,Experience.MID));
        developers.put(3,new Developer(3,"Fuat", 60000.0, Experience.SENIOR));
    }

    @Autowired
    public DeveloperController(Taxable developerTax){
        this.developerTax = developerTax;
    }

    @GetMapping
    public List<Developer> getDevelopers(){
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer getDeveloper(@PathVariable int id){
        return developers.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer postDeveloper(@RequestBody Developer developer){
        developers.put(developer.getId(), developer);
        if(developer.getExperience() == (Experience.JUNIOR)){
            developer.setSalary(developerTax.getSimpleTaxRate());
        } else if(developer.getExperience() == (Experience.MID)){
            developer.setSalary(developerTax.getMiddleTaxRate());
        } else {
            developer.setSalary(developerTax.getUpperTaxRate());
        }
        return developers.get(developer.getId());
    }

    @PutMapping("/{id}")
    public Developer putDeveloper(@PathVariable int id, @RequestBody Developer developer){
        developers.replace(id,developers.get(id), developer);
        return developer;
    }

    @DeleteMapping("/{id}")
    public Developer deleteDeveloper(@PathVariable int id){
        return developers.remove(id);
    }
}
