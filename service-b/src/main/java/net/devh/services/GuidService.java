package net.devh.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface GuidService {
    UUID guid = UUID.randomUUID();

    String getGuid();

}
