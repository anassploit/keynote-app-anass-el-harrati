package net.anassploit.keynoteservice.DTO;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KeynoteDTO {
    String id;
    String nom;
    String prenom;
    String email;
    String fonction;
}
