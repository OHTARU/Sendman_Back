package sendman.backend.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sendman.backend.account.domain.Account;
import sendman.backend.account.dto.AccountinfoResponseDTO;
import sendman.backend.account.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountinfoResponseDTO getUser(org.springframework.security.core.userdetails.User userdto){
        if (accountRepository.findByEmail(userdto.getUsername()).isPresent()){
            Account user = accountRepository.findByEmail(userdto.getUsername()).get();
            return AccountinfoResponseDTO.builder()
                    .email(user.getEmail())
                    .name(user.getName()).build();
        }else {
            throw new RuntimeException("회원 정보 없음");
        }
    }
}
