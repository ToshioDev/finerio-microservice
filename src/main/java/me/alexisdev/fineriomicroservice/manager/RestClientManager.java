package me.alexisdev.fineriomicroservice.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import me.alexisdev.fineriomicroservice.models.Movement;
import me.alexisdev.fineriomicroservice.models.ResponseToken;
import me.alexisdev.fineriomicroservice.repositories.MovementRepository;
import me.alexisdev.fineriomicroservice.utils.Utils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@Getter
@Setter
public class RestClientManager {

    private String username;
    private String password;
    private static final String AUTHENTICATION_URL = "https://api.finerio.mx/api/login";
    private String token;

    private String userId;

    private MovementRepository movementRepository;

    private int offset = 0;


    public HttpStatus updateToken() throws JsonProcessingException {
        RestTemplate template = new RestTemplate();
        String userJson = String.format("{\"username\":\"%s\",\"password\":\"%s\"}",username,password);

        try{
            ResponseEntity<String> responseEntity = template.postForEntity(AUTHENTICATION_URL,userJson,String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK){
                ObjectMapper objectMapper = new ObjectMapper();
                ResponseToken response = objectMapper.readValue(responseEntity.getBody(),ResponseToken.class);
                token = response.getAccess_token();
                System.out.println("############> " + response);
                return HttpStatus.OK;
            }
        }catch (Exception e){
            System.out.println("Login Request not finished");
        }

        return HttpStatus.UNAUTHORIZED;
    }

    public String getUserId() throws JsonProcessingException {
        if (getToken() != null){
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.add("Authorization", "Bearer " + getToken());
            HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange("https://api.finerio.mx/api/me", HttpMethod.GET,entity,String.class);

            System.out.println("User ID: " + Utils.keyToValueEntity("id",response));
            System.out.println("Result - status ("+ response.getStatusCode() + ") has body: " + response.hasBody());
            System.out.println("############> " + response);
            this.userId = Utils.keyToValueEntity("id",response);
        }
        return userId;
    }


    public boolean loadMovements() throws JsonProcessingException {
        HttpStatus hasToken = updateToken();
        System.out.println("Token: "+ getToken());
        if (hasToken.equals(HttpStatus.OK)){
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.add("Authorization", "Bearer " + getToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", httpHeaders);

            ResponseEntity<String> response = restTemplate.exchange(String.format("https://api.finerio.mx/api/users/%s/movements?deep=false&offset=%s&max=35&includeCharges=true&includeDeposits=true&includeDuplicates=false",getUserId(),offset), HttpMethod.GET,entity,String.class);

            List<Movement> data = Utils.responseToMovements(response);
            movementRepository.saveAll(data);
            if (data.size() > 0){
                System.out.println("Offset: " + offset);
                System.out.println("Result - status ("+ response.getStatusCode() + ") has body: " + response.hasBody());
                System.out.println("############> " + response.getBody());

                offset += 10;

                return true;
            }else {
                offset = 0;
            }
        }
        return false;
    }
}
