package com.example.datarelojservice.repository;

import com.example.datarelojservice.entity.DatarelojEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatarelojRepository extends JpaRepository<DatarelojEntity, Long> {
}
