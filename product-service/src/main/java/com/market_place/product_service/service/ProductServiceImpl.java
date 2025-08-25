package com.market_place.product_service.service;

import com.market_place.product_service.dto.*;
import com.market_place.product_service.exception.ProductNotFound;
import com.market_place.product_service.mapper.ProductMapper;
import com.market_place.product_service.model.Product;
import com.market_place.product_service.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    /**
     * Effettua la creazione del prodotto
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottiene l'id dell'utente dall'autenticazione </li>
     *     <li> Crea il prodotto dai dati ottenuti nel DTO</li>
     *     <li> Salva il prodotto nel db e crea il DTO di risposta</li>
     * </ul>
     * @param request DTO per validare i dati inseriti del prodotto
     * @return un DTO che mappa l'oggetto dal database con i dati necessari
     * @throws IllegalArgumentException se i dati del DTO non sono validi
     */
    @Override
    public ProductResponseDto create(ProductRequestDto request) {
        UUID userId = getUserId();
        log.info("[CREATE] Creazione prodotto");

        Product newProduct = Product.builder()
                .creatorId(userId)
                .name(request.getName())
                .description(request.getDescription())
                .image(request.getImage())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .category(request.getCategory())
                .build();

        Product savedProduct = productRepository.save(newProduct);
        log.info("[CREATE] Creazione prodotto {} con nome: {} andata a buon fine", savedProduct.getId(), savedProduct.getName());

        return mapper.fromProductToDto(savedProduct);
    }

    /**
     * Ottiene tutti i prodotti
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottiene l'id dell'utente dall'autenticazione </li>
     *     <li> Ottiene tutti i prodotti dal db e li converte in un DTO</li>
     * </ul>
     * @return un DTO che mappa l'oggetto dal database con i dati necessari
     */
    @Override
    public List<ProductResponseDto> getProducts() {
        UUID userId = getUserId();
        log.info("[GET PRODUCTS] Visualizzazione dei prodotti da parte di {}", userId);

        return productRepository.findAll().stream()
                .map(mapper::fromProductToDto)
                .collect(Collectors.toList());
    }

    /**
     * Aggiorna la quantità del prodotto
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottiene l'id dell'utente dall'autenticazione </li>
     *     <li> In base al metodo (ADD, DECREMENT) eseguo una query per aggiornare la quantità </li>
     *     <li> Se le righe aggiornate sono == 0, nessuna entità è stata aggiornata </li>
     *     <li> Ottengo il prodotto aggiornato dal database e lo converto in un DTO </li>
     * </ul>
     * @param request DTO con id del prodotto, la quantità e il tipo di update (ADD, DECREMENT)
     * @return un DTO che mappa l'oggetto dal database
     * @throws InternalServerErrorException se il prodotto non viene trovato,
     * se l'id dell'utente non corrisponde al creatore del prodotto o la quantità è negativa
     */
    @Override
    @Transactional
    public ProductResponseDto updateQuantity(QuantityUpdateDto request) {
        UUID userId = getUserId();
        log.info("[UPDATE] Utente {} modifica della quantità per il prodotto {}",userId, request.getProductId());

        int rows;

        if (request.getType().equals(UpdateType.ADD)){
            rows = productRepository.incrementQuantity(request.getProductId(), request.getQuantity(), userId);
        } else {
            rows = productRepository.decrementQuantity(request.getProductId(), request.getQuantity(), userId);
        }
        if (rows == 0) {
            log.warn("[UPDATE] Errore con update del prodotto {}", request.getProductId());
            throw new InternalServerErrorException("Errore interno");
        }
        Product updatedProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFound("Prodotto non trovato dopo aggiornamento"));

        log.info("[UPDATE] Modifica del prodotto {} andata a buon fine, quantità attuale: {}",
                updatedProduct.getId(), updatedProduct.getQuantity());

        return mapper.fromProductToDto(updatedProduct);
    }

    /**
     * Elimina un progetto
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottiene l'id dell'utente dall'autenticazione </li>
     *     <li> Verifico che l'utente sia un ADMIN </li>
     *     <li> Se l'utente è un ADMIN può eliminare il prodotto </li>
     *     <li> Se non lo è, l'utente deve aver creato il prodotto, altrimenti non lo può eliminare </li>
     *     <li> Se le righe aggiornate sono == 0, nessuna entità è stata aggiornata </li>
     * </ul>
     * @param id del prodotto da eliminare
     * @throws InternalServerErrorException se il prodotto non esiste o l'id dell'user che l'ha creato non corrisponde
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = (UUID) auth.getPrincipal();

        log.info("[DELETE] User {} sta cercando di eliminare il prodotto {}", userId, id);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        int rows;
        if (isAdmin) {
            rows = productRepository.deleteByIdCustom(id);
        } else {
            rows = productRepository.deleteByIdAndCreatorId(id, userId);
        }
        if (rows == 0){
            log.warn("[DELETE] Prodotto con id {} non trovato o errore interno", id);
            throw new InternalServerErrorException("Errore interno");
        }
    }

    /**
     * Ottiene i prodotti che l'utente ha creato
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottengo i prodotti dove il creatorId = userId </li>
     *     <li> Ritorno un DTO con i dati necessari del prodotto</li>
     * </ul>
     * @return un DTO che mappa l'oggetto dal database
     */
    @Override
    public List<ProductResponseDto> getUserProducts() {
        log.info("[GET USER PRODUCTS] Visualizzazione dei prodotti dell'utente {}", getUserId());

        return productRepository.findByCreatorId(getUserId()).stream()
                .map(mapper::fromProductToDto)
                .collect(Collectors.toList());
    }

    /**
     * Acquisto del prodotto
     *
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottiene l'id dell'utente dall'autenticazione </li>
     *     <li> Ottengo il prodotto dal db con il parametro id, e la quantità >= di quella nella requestDto </li>
     *     <li> Eseguo una query per sottrarre la quantità richiesta dall'utente dal prodotto salvato </li>
     *     <li> Se le righe aggiornate sono == 0, nessuna entità è stata aggiornata </li>
     *     <li> Creo un DTO di risposta con i dati necessari del prodotto ottenuto </li>
     *     <li> Setto la quantità del prodotto nel DTO = quantità richiesta dell'utente </li>
     * </ul>
     * @param productId id del prodotto
     * @param requestDto DTO contenete la quantità richiesta dall'utente
     * @throws ProductNotFound se il prodotto non viene trovato o la quantità non è disponibile
     * @return un DTO che mappa l'oggetto dal database
     */
    @Override
    @Transactional
    public ProductResponseDto purchaseProduct(Long productId, PurchaseRequestDto requestDto) {
        UUID userId = getUserId();
        log.info("[PURCHASE] Acquisto del prodotto {}, quantita di {} da utente {}",
                productId, requestDto.getQuantity(), userId);

        Product existProduct = productRepository.findAvaibleProduct(productId, requestDto.getQuantity())
                .orElseThrow(() -> new ProductNotFound("Prodotto non trovato o quantità non disponibile"));

        int rows = productRepository.decrementProductQuantity(existProduct.getId(), requestDto.getQuantity());
        if (rows == 0){
            log.warn("[UPDATE] Prodotto con id {} non trovato o errore interno", existProduct.getId());
            throw new ProductNotFound("Elemento non esistente");
        }

        ProductResponseDto response = mapper.fromProductToDto(existProduct);
        response.setQuantity(requestDto.getQuantity());

        log.info("{}", response);
        return response;
    }

    private static UUID getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) auth.getPrincipal();
    }
}
