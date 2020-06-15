package com.learnreactivespring.controller;

import com.learnreactivespring.domain.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping("/client/retrieve")
    public Flux<Item> retrieveAllItems(){
        return webClient.get().uri("/v1/items")
                .retrieve()
                .bodyToFlux(Item.class)
                .log("Items retrieve")
                ;
    }

    @GetMapping("/client/exchange")
    public Flux<Item> exchangeAllItems(){
        return webClient.get().uri("/v1/items")
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Item.class))
                .log("Items retrieve")
                ;
    }

    @GetMapping("/client/retrieve/{id}")
    public Mono<Item> retrieveItem(@PathVariable String id){
        return webClient.get().uri("/v1/items/{id}", id)
                .retrieve()
                .bodyToMono(Item.class)
                ;
    }


    @GetMapping("/client/exchange/{id}")
    public Mono<Item> exchangeItem(@PathVariable String id){
        return webClient.get().uri("/v1/items/{id}", id)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
                ;
    }

    @PostMapping("/client/post/item/retrieve")
    public Mono<Item> postItemUsingRetrieve(){
        Item item = new Item(null, "Bose HeadPhones", 50.99);
        return webClient.post().uri("/v1/items/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                ;
    }

    @PostMapping("/client/post/item/exchange")
    public Mono<Item> postItemUsingExchange(){
        Item item = new Item(null, "Bose HeadPhones", 50.99);
        return webClient.post().uri("/v1/items/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
                ;
    }


}
