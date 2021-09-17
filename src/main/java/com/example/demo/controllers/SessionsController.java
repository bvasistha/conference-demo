package com.example.demo.controllers;


import com.example.demo.models.Session;
import com.example.demo.repositories.SessionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {
    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping
    public List<Session> list(){
        return sessionRepository.findAll();
    }
    @GetMapping
    @RequestMapping("{id}") // this id will be passed on as the parameter on the get() in the line to follow...
    public Session get (@PathVariable Long id){
        return sessionRepository.getById(id);
    }
    @PostMapping // http verbiage of post request
    //@ResponseStatus(HttpStatus.CREATED)
    // Difference between REST Controller and Spring MVC
    // by default REST Controller returns 200 as response status for all the calls even though @POSTMapping annotation
    // is added
    // Adding the @ResponseStatus will help us map the 201 in the hpp world.
    public Session create (@RequestBody final Session session){
        return sessionRepository.saveAndFlush(session);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id){
        //Also need to check for children records before deleting.
        sessionRepository.deleteById(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Session update (@PathVariable Long id, @RequestBody Session session){
        //@RequestBody gets the
        //because this is a PUT, we expect all attributes to be passed in. A PATCH would only need attributes
        // that need to be updated
        //TODO: Add annotation that all attributes are passed in, otherwise return a 400 bad payload
        Session existingSession = sessionRepository.getById(id);
        BeanUtils.copyProperties(session, existingSession, "session_id");
        // updating the existingSession by the session that was passed on the parameter via @RequestBody
        // ignoreProperties will ignore to update the session id which is the primary key
        return sessionRepository.saveAndFlush(existingSession);
    }
}
