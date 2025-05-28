package by.grgu.accountservice.controller;

import by.grgu.accountservice.database.entity.Account;
import by.grgu.accountservice.database.repository.AccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/user-accounts")
public class UserAccountController {
    private final AccountRepository accountRepository;

    public UserAccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping("/updateField")
    public ResponseEntity<Map<String, String>> updateAccount(@RequestBody Map<String, String> updatedData) {
        String oldUsername = updatedData.get("oldUsername");
        String newUsername = updatedData.getOrDefault("username", oldUsername);
        String newFirstname = updatedData.getOrDefault("firstname", "");
        String newLastname = updatedData.getOrDefault("lastname", "");
        String newEmail = updatedData.getOrDefault("email", "");

        Account account = accountRepository.findByUsername(oldUsername)
                .orElseThrow(() -> new RuntimeException("Аккаунт не найден: " + oldUsername));


        if (!oldUsername.equals(newUsername)) {
            account.setUsername(newUsername);
        }
        if (!newFirstname.isEmpty()) {
            account.setFirstname(newFirstname);
        }
        if (!newLastname.isEmpty()) {
            account.setLastname(newLastname);
        }
        if (!newEmail.isEmpty()) {
            account.setEmail(newEmail);
        }

        accountRepository.save(account);
        accountRepository.flush();

        return ResponseEntity.ok(Map.of("status", "success", "message", "Аккаунт обновлён"));
    }
}
