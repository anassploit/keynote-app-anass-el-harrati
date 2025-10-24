package net.anassploit.conferencesevice.feign;

import net.anassploit.conferencesevice.DTO.KeynoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "keynote-service", url = "http://localhost:8888/keynote-service/api/keynotes")
public interface KeynoteClient {

    @GetMapping("/{id}")
    KeynoteDTO getKeynoteById(@PathVariable("id") Long id);

    @GetMapping
    List<KeynoteDTO> getAllKeynotes();

}

