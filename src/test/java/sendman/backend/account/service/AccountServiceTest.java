package sendman.backend.account.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import sendman.backend.account.domain.Account;
import sendman.backend.account.dto.AccountinfoResponseDTO;
import sendman.backend.account.dto.GoogleUserinfoResponseDTO;
import sendman.backend.account.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;

    private User user;
    private Account account;
    @BeforeEach
    void setUp() {
        account = Account.from(new GoogleUserinfoResponseDTO(
                "1234","test@example.com","test"));
        accountRepository.save(account);
    }
    @Test
    @DisplayName("AccountSerivce GetUser Test")
    void getUser() {
        //given
        AccountinfoResponseDTO given = new AccountinfoResponseDTO("test@example.com","test");
        user = new User("test@example.com","",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        //when
        AccountinfoResponseDTO response = accountService.getUser(user);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(given.email(), response.email());
        Assertions.assertEquals(given.name(), response.name());
        verify(accountRepository, times(1)).findByEmail(user.getUsername());
    }

    @Test
    void findByAccount() {
        //given
        AccountinfoResponseDTO given = new AccountinfoResponseDTO("test@example.com","test");
        user = new User("test@example.com","",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));
        //when
        Account response = accountService.findByAccount(user);
        //then
        Assertions.assertNotNull(response);
        verify(accountRepository, times(1)).findByEmail(user.getUsername());
    }
}