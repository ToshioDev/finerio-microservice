package me.alexisdev.fineriomicroservice.controllers;

import me.alexisdev.fineriomicroservice.manager.RestClientManager;
import me.alexisdev.fineriomicroservice.repositories.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
public class TestController {

    private RestClientManager restClient = new RestClientManager();

    @Autowired
    MovementRepository movementRepository;

    @GetMapping("/runTest/{username}/{password}")
    public Object test(@PathVariable String username,@PathVariable String password) throws IOException {

        restClient.setUsername(username);
        restClient.setPassword(password);
        restClient.updateToken();
        if (movementRepository != null){
            System.out.println("Exist Repo");
            restClient.setMovementRepository(movementRepository);
            if (restClient.loadMovements()){
                System.out.println("Movemenets Loaded Succesfully :D");
            }else {
                System.out.println("Reset List with Movements");
            }
        }

        return restClient.getMovementRepository().findAll();
    }
}
