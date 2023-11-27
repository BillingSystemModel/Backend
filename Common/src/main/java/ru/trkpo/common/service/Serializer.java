package ru.trkpo.common.service;

import org.springframework.stereotype.Service;

@Service
public interface Serializer<T> {
    String serialize(T item);
}
