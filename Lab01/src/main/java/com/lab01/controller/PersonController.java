package com.lab01.controller;

import com.lab01.forms.PersonForm;
import com.lab01.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping
public class PersonController {
    private static List<Person> people = new ArrayList<Person>();
    static {
        people.add(new Person("Vasya Pupkin ", 25));
        people.add(new Person("Vitaliy Tsal", 30));
    }

    @Value("${welcome.message}")
    private String message;
    @Value("${error.message}")
    private String errorMessage;
    @GetMapping(value = {"/", "/index"})
    public ModelAndView index(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        model.addAttribute("message", message);
        log.info("/index was called");
        return modelAndView;
    }

    @GetMapping(value = {"/allpeople"})
    public ModelAndView personList(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("peoplelist");
        model.addAttribute("people", people);
        log.info("/allpeople was called");
        return modelAndView;
    }

    @GetMapping(value = {"/addperson"})
    public ModelAndView showAddPersonPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("addperson");
        PersonForm personForm = new PersonForm();
        model.addAttribute("personform", personForm);
        log.info("/addperson was called");
        return modelAndView;
    }

    @PostMapping(value = {"/addperson"})
    public ModelAndView savePerson(Model model, //
                                   @ModelAttribute("personform") PersonForm personForm) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("peoplelist");
        String name = personForm.getName();
        int age = personForm.getAge();
        if (name != null && name.length() > 0 && age > 0) {
            Person newPerson = new Person(name, age);
            people.add(newPerson);
            model.addAttribute("people", people);
            return modelAndView;
        }
        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("addperson");
        return modelAndView;
    }

    @GetMapping(value = {"/delperson"})
    public ModelAndView showDelPage(Model model){
        ModelAndView modelAndView = new ModelAndView("delperson");
        model.addAttribute("people", people);
        log.info("/delperson was called");
        return modelAndView;
    }
    @PostMapping(value = {"/delperson"})
    public ModelAndView deletePerson(Model model, @ModelAttribute("PersonNames") String personNames){
        ModelAndView modelAndView = new ModelAndView("peoplelist");
        people.removeAll((people.stream().filter(t-> t.getName().equalsIgnoreCase(personNames))).collect(Collectors.toList()));
        model.addAttribute("people", people);
        return modelAndView;
    }
    @GetMapping(value = {"/editperson"})
    public ModelAndView showEditPage(Model model){
        ModelAndView modelAndView = new ModelAndView("editperson");
        model.addAttribute("people", people);
        log.info("/editperson was called");
        return modelAndView;
    }
    @PostMapping(value = {"/editperson"})
    public ModelAndView editPerson(Model model, @ModelAttribute("PersonNames") String personNames,@ModelAttribute("Name")String name ,@ModelAttribute("Age")int age){
        ModelAndView modelAndView = new ModelAndView("peoplelist");
        if(age > 0)
            people.stream().filter(t-> t.getName().equals(personNames)).findFirst().get().setAge(age);
        if(name != null && name.length() >0)
            people.stream().filter(t-> t.getName().equals(personNames)).findFirst().get().setName(name);
        model.addAttribute("people", people);
        return modelAndView;
    }
}
