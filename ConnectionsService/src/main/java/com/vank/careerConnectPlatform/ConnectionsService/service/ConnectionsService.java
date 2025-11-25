package com.vank.careerConnectPlatform.ConnectionsService.service;

import com.vank.careerConnectPlatform.ConnectionsService.entity.Person;
import com.vank.careerConnectPlatform.ConnectionsService.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsService {
    private final PersonRepository personRepository;

    public List<Person> getfFirstDegreeConnectionsOfUser(Long userId){
        log.info("Getting first degree connections of user with ID: {}", userId);

        return personRepository.getFirstDegreeConnections(userId);
    }
}
