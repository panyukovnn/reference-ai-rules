package ru.panyukovnn.testcontainers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.panyukovnn.testcontainers.model.LinkInfo;

import java.util.UUID;

public interface LinkInfoRepository extends JpaRepository<LinkInfo, UUID> {

}
