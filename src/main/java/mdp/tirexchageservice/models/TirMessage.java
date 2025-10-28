package mdp.tirexchageservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tir_message")
public class TirMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String messageType;
    private String guaranteeNumber;
    private String iruReference;
    private String customsIndex;
    private String status;
    @Lob
    private String payload;
    private LocalDateTime createdAt;
}

