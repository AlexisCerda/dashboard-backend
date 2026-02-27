package fr.prefecture.sidsic.dashboard_sidsic.service;

import org.springframework.stereotype.Service;

import fr.prefecture.sidsic.dashboard_sidsic.repository.GroupeRepository;

@Service
public class GroupeService {
    private final GroupeRepository  groupeRepository;

    public GroupeService(GroupeRepository GroupeRepository) {
        this.groupeRepository = GroupeRepository;
    }

}
