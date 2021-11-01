package de.kyleonaut.coinsystem.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 01.11.2021
 */
@Getter
@RequiredArgsConstructor
public class User {
    private final String name;
    private final UUID uuid;
    private final long amount;
}
