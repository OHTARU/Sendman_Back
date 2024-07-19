package sendman.backend.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import sendman.backend.account.domain.Account;
import sendman.backend.account.dto.AccountinfoResponseDTO;
import sendman.backend.account.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountinfoResponseDTO getUser(User user){
        if (accountRepository.findByEmail(user.getUsername()).isPresent()){
            Account account = accountRepository.findByEmail(user.getUsername()).get();
            return AccountinfoResponseDTO.builder()
                    .email(account.getEmail())
                    .name(account.getName()).build();
        }else {
            throw new RuntimeException("회원 정보 없음");
        }
    }
    public Account findByAccount(User user){
        if (accountRepository.findByEmail(user.getUsername()).isPresent())
            return accountRepository.findByEmail(user.getUsername()).get();
        else throw new RuntimeException("회원 정보 없음");
    }
}
