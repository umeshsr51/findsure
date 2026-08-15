package com.findsure.util;

import com.findsure.repository.ItemRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class QrTokenGenerator {

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int TOKEN_LENGTH = 7;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ItemRepository itemRepository;

    public QrTokenGenerator(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public String generateUnique() {
        String token;
        do {
            token = "FS-" + randomSuffix();
        } while (itemRepository.existsByQrToken(token));
        return token;
    }

    private String randomSuffix() {
        StringBuilder sb = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
